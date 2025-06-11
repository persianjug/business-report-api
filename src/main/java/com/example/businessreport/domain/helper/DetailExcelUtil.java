package com.example.businessreport.domain.helper;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
// import org.hibernate.validator.internal.util.logging.Log_.logger;

import com.example.businessreport.domain.dto.DetailFull;
import com.example.businessreport.domain.model.WorkDetail;

public class DetailExcelUtil {

  // 定数定義
  public static final String SHEET_NAME = "業務報告書";
  public static final String FONT_NAME = "Meiryo UI";
  public static final short FONT_HEIGHT = 11;
  public static final short HEADER1_FONT_HEIGHT = 16;
  public static final String DATE_FORMAT_PATTERN = "yyyy/mm/dd";
  public static final String PERIOD_DATE_FORMAT_PATTERN = "yyyy年MM月dd日";

  // セルスタイル名の定数
  public static final String STYLE_GENERAL = "general";
  public static final String STYLE_STRING_WRAP = "stringWrap";
  public static final String STYLE_DATETIME = "datetime";
  public static final String STYLE_HEADER1 = "header1";
  public static final String STYLE_HEADER2 = "header2";
  public static final String STYLE_HEADER3 = "header3";

  // ヘッダー項目
  private record HeaderItem(int rowNum, String text, String styleKey) {
  }

  // データ項目
  private record DataItem(int rowNum, int colNum, Function<DetailFull, ?> valueSupplier, String styleKey) {
  }

  /**
   * ワークブックオブジェクトの生成
   * 
   * @return ワークブックオブジェクト
   */
  public static Workbook createDetailWorkbook() {
    return new XSSFWorkbook();
  }

  /**
   * ワークシートオブジェクトの生成
   * 
   * @param workbook  ワークブックオブジェクト
   * @param sheetName シート名
   * @return ワークシートオブジェクト
   */
  public static Sheet createDetailSheet(Workbook workbook, String sheetName) {
    return workbook.createSheet(sheetName);
  }

  /**
   * セルのスタイルを取得する
   * 
   * @param workbook ワークブックオブジェクト
   * @return セルのスタイル情報(HashMap)
   */
  public static final Map<String, CellStyle> createCellStyles(Workbook workbook) {
    // マップ
    Map<String, CellStyle> cellStyles = new HashMap<>();

    // 標準のスタイル (general)
    CellStyle styleGeneral = createBaseStyle(workbook, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
    styleGeneral.setWrapText(true);
    styleGeneral.setFont(createFont(workbook, FONT_HEIGHT, IndexedColors.BLACK.getIndex()));
    applyBorders(styleGeneral, BorderStyle.THIN, IndexedColors.GREY_25_PERCENT.getIndex());
    cellStyles.put(STYLE_GENERAL, styleGeneral);

    // 改行が入った文字列用のスタイル (stringWrap)
    // general とほとんど同じ、ベーススタイルを再利用しても良いですが、
    // ここでは明示的に同じ設定を記述しています。
    CellStyle styleStringWrap = createBaseStyle(workbook, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
    styleStringWrap.setWrapText(true);
    styleStringWrap.setFont(createFont(workbook, FONT_HEIGHT, IndexedColors.BLACK.getIndex()));
    applyBorders(styleStringWrap, BorderStyle.THIN, IndexedColors.GREY_25_PERCENT.getIndex());
    cellStyles.put(STYLE_STRING_WRAP, styleStringWrap);

    // 日時表示用のスタイル (datetime)
    DataFormat format = workbook.createDataFormat();
    CellStyle styleDatetime = createBaseStyle(workbook, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
    styleDatetime.setDataFormat(format.getFormat(DATE_FORMAT_PATTERN)); // DATE_FORMAT_PATTERN 定数を使用
    styleDatetime.setFont(createFont(workbook, FONT_HEIGHT, IndexedColors.BLACK.getIndex()));
    applyBorders(styleDatetime, BorderStyle.THIN, IndexedColors.GREY_25_PERCENT.getIndex());
    cellStyles.put(STYLE_DATETIME, styleDatetime);

    // ヘッダー1のスタイル (header1)
    CellStyle styleHeader1 = createBaseStyle(workbook, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
    styleHeader1.setFont(createFont(workbook, HEADER1_FONT_HEIGHT, IndexedColors.BLACK.getIndex()));
    cellStyles.put(STYLE_HEADER1, styleHeader1);

    // ヘッダー2のスタイル (header2)
    CellStyle styleHeader2 = createBaseStyle(workbook, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
    styleHeader2.setFont(createFont(workbook, FONT_HEIGHT, IndexedColors.BLACK.getIndex()));
    cellStyles.put(STYLE_HEADER2, styleHeader2);

    // ヘッダー3のスタイル (header3)
    CellStyle styleHeader3 = createBaseStyle(workbook, HorizontalAlignment.LEFT, VerticalAlignment.TOP);
    styleHeader3.setFont(createFont(workbook, FONT_HEIGHT, IndexedColors.WHITE.getIndex()));
    styleHeader3.setFillForegroundColor(IndexedColors.DARK_BLUE.index);
    styleHeader3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    applyBorders(styleHeader3, BorderStyle.THIN, IndexedColors.GREY_25_PERCENT.getIndex());
    cellStyles.put(STYLE_HEADER3, styleHeader3);

    // // 標準のスタイル
    // CellStyle styleGeneral = workbook.createCellStyle();
    // styleGeneral.setAlignment(HorizontalAlignment.LEFT);
    // styleGeneral.setVerticalAlignment(VerticalAlignment.TOP);
    // styleGeneral.setWrapText(true);
    // styleGeneral.setFont(createCommonFont(workbook));
    // applyCommonBorders(styleGeneral);
    // cellStyles.put("general", styleGeneral);

    // // 改行が入った文字列用のスタイル
    // CellStyle styleStirngWrap = workbook.createCellStyle();
    // styleStirngWrap.setAlignment(HorizontalAlignment.LEFT);
    // styleStirngWrap.setVerticalAlignment(VerticalAlignment.TOP);
    // styleStirngWrap.setWrapText(true);
    // styleStirngWrap.setFont(createCommonFont(workbook));
    // applyCommonBorders(styleStirngWrap);
    // cellStyles.put("stringWrap", styleStirngWrap);

    // // 日時表示用のスタイル
    // DataFormat format = workbook.createDataFormat();
    // CellStyle styleDatetime = workbook.createCellStyle();
    // styleDatetime.setAlignment(HorizontalAlignment.LEFT);
    // styleDatetime.setVerticalAlignment(VerticalAlignment.TOP);
    // styleDatetime.setDataFormat(format.getFormat("yyyy/mm/dd"));
    // styleDatetime.setFont(createCommonFont(workbook));
    // applyCommonBorders(styleDatetime);
    // cellStyles.put("datetime", styleDatetime);

    // // ヘッダーのスタイル
    // CellStyle styleHeader1 = workbook.createCellStyle();
    // styleHeader1.setAlignment(HorizontalAlignment.LEFT);
    // styleHeader1.setVerticalAlignment(VerticalAlignment.TOP);
    // Font fontHeader1 = workbook.createFont();
    // fontHeader1.setFontName("Meiryo UI");
    // fontHeader1.setFontHeightInPoints((short) 16);
    // fontHeader1.setColor(IndexedColors.BLACK.getIndex());
    // styleHeader1.setFont(fontHeader1);
    // cellStyles.put("header1", styleHeader1);

    // // ヘッダーのスタイル
    // CellStyle styleHeader2 = workbook.createCellStyle();
    // styleHeader2.setAlignment(HorizontalAlignment.LEFT);
    // styleHeader2.setVerticalAlignment(VerticalAlignment.TOP);
    // Font fontHeader2 = workbook.createFont();
    // fontHeader2.setFontName("Meiryo UI");
    // fontHeader2.setFontHeightInPoints((short) 11);
    // fontHeader2.setColor(IndexedColors.BLACK.getIndex());
    // styleHeader2.setFont(fontHeader2);
    // cellStyles.put("header2", styleHeader2);

    // // ヘッダーのスタイル
    // CellStyle styleHeader3 = workbook.createCellStyle();
    // styleHeader3.setAlignment(HorizontalAlignment.LEFT);
    // styleHeader3.setVerticalAlignment(VerticalAlignment.TOP);
    // Font fontHeader3 = workbook.createFont();
    // fontHeader3.setFontName("Meiryo UI");
    // fontHeader3.setFontHeightInPoints((short) 11);
    // fontHeader3.setColor(IndexedColors.WHITE.getIndex());
    // styleHeader3.setFont(fontHeader3);
    // styleHeader3.setFillForegroundColor(IndexedColors.DARK_BLUE.index);
    // styleHeader3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    // styleHeader3.setBorderTop(BorderStyle.THIN);
    // styleHeader3.setBorderBottom(BorderStyle.THIN);
    // styleHeader3.setBorderLeft(BorderStyle.THIN);
    // styleHeader3.setBorderRight(BorderStyle.THIN);
    // styleHeader3.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    // styleHeader3.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    // styleHeader3.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    // styleHeader3.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    // cellStyles.put("header3", styleHeader3);

    return cellStyles;
  }

  /**
   * 業務報告書のデータ転記
   * 
   * @param sheet      ワークシートオブジェクト
   * @param detailFull 業務報告書DTO
   * @param cellStyles セルのスタイル情報(HashMap)
   */
  public static void setDetailValues(Sheet sheet, DetailFull detailFull, Map<String, CellStyle> cellStyles) {
    setDetailHeaderValues(sheet, cellStyles);
    setDetailDataValues(sheet, detailFull, cellStyles);
  }

  /**
   * シート全体のスタイルを設定
   * 
   * @param sheet ワークシートオブジェクト
   */
  public static void setSheetStyle(Sheet sheet) {
    // 1列目の列幅
    sheet.setColumnWidth(0, 768);

    // 2列目はテキストの長さに合わせて列幅を設定
    sheet.autoSizeColumn(1);

    // 3列目はテキストの長さに合わせて列幅を設定
    // sheet.autoSizeColumn(2);

    // 3列目は100文字分の幅を設定
    sheet.setColumnWidth(2, 25600);

    // A3セルを基点にウインドウを固定
    sheet.createFreezePane(0, 2);

    // ワークシートの目盛線を非表示
    sheet.setDisplayGridlines(false);

    // シートの表示倍率率(80%)
    sheet.setZoom(80);

  }

  /**
   * Excelファイルを出力
   * 
   * @param workbook ワークブックオブジェクト
   * @param target   Excelファイルのパス
   * @return true: 出力OK false: 出力NG
   */
  public static boolean writeToExcel(Workbook workbook, Path target) {
    try (OutputStream outStream = Files.newOutputStream(target);) {
      workbook.write(outStream);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * Excelファイルを出力
   * 
   * @param workbook  ワークブックオブジェクト
   * @param outStream Excelファイルの出力ストリーム
   * @return true: 出力OK false: 出力NG
   */
  public static boolean writeToExcel(Workbook workbook, OutputStream outStream) {
    try {
      workbook.write(outStream);
    } catch (IOException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  /**
   * セルに値を設定する汎用メソッド
   *
   * @param sheet     ワークシートオブジェクト
   * @param rowNum    行番号
   * @param cellNum   列番号
   * @param value     設定する値
   * @param cellStyle セルのスタイル情報
   */
  private static void setDetailValue(Sheet sheet, int rowNum, int cellNum, Object value, CellStyle cellStyle) {
    // 行、列のオブジェクト生成
    Row row = CellUtil.getRow(rowNum, sheet);
    Cell cell = CellUtil.getCell(row, cellNum);

    // 値の設定
    switch (value) {
      // 文字列型
      case String strValue -> cell.setCellValue(strValue);
      // 日時型
      case LocalDateTime localDateTimeValue -> cell.setCellValue(localDateTimeValue);
      // 日付型
      case LocalDate localDateValue -> cell.setCellValue(localDateValue);
      // 少数型
      case Double dobuleValue -> cell.setCellValue(dobuleValue);
      // 整数型
      case Integer intValue -> cell.setCellValue(intValue);
      // null型
      case null -> cell.setCellValue("");
      // その他の型は文字列として扱うか、エラーログを出す
      default -> {
        cell.setCellValue(value.toString());
        // logger.warn(
        // "Unsupported data type for cell value: " + value.getClass().getName() + " at
        // row " + rowNum + ", col "
        // + cellNum);
      }
    }

    // セルのスタイル設定
    cell.setCellStyle(cellStyle);

  }

  /**
   * 業務報告書のヘッダーを設定
   * 
   * @param sheet      ワークシートオブジェクト
   * @param cellStyles セルのスタイル情報(HashMap)
   */
  private static void setDetailHeaderValues(Sheet sheet, Map<String, CellStyle> cellStyles) {
    // ヘッダー情報定義
    List<HeaderItem> headers = List.of(
        new HeaderItem(0, "業務報告書", STYLE_HEADER1),
        new HeaderItem(2, "案件概要", STYLE_HEADER2),
        new HeaderItem(3, "対象期間", STYLE_HEADER3),
        new HeaderItem(4, "エンド企業名", STYLE_HEADER3),
        new HeaderItem(5, "上位顧客名", STYLE_HEADER3),
        new HeaderItem(6, "業種", STYLE_HEADER3),
        new HeaderItem(7, "案件名", STYLE_HEADER3),
        new HeaderItem(8, "エル・フィールド参画人数", STYLE_HEADER3),
        new HeaderItem(9, "職場勤務／在宅", STYLE_HEADER3),
        new HeaderItem(10, "職場最寄駅", STYLE_HEADER3),
        new HeaderItem(11, "通勤時間(h)", STYLE_HEADER3),
        new HeaderItem(12, "案件参画日", STYLE_HEADER3),
        new HeaderItem(13, "主要技術(言語／フレームワーク／ツール)", STYLE_HEADER3),
        new HeaderItem(14, "データベース", STYLE_HEADER3),
        new HeaderItem(15, "ポジション", STYLE_HEADER3),
        new HeaderItem(17, "①プロジェクト", STYLE_HEADER2),
        new HeaderItem(18, "プロジェクト名", STYLE_HEADER3),
        new HeaderItem(19, "プロジェクト状況", STYLE_HEADER3),
        new HeaderItem(20, "課題・問題点", STYLE_HEADER3),
        new HeaderItem(22, "②プロジェクト", STYLE_HEADER2),
        new HeaderItem(23, "プロジェクト名", STYLE_HEADER3),
        new HeaderItem(24, "プロジェクト状況", STYLE_HEADER3),
        new HeaderItem(25, "課題・問題点", STYLE_HEADER3),
        new HeaderItem(27, "③プロジェクト", STYLE_HEADER2),
        new HeaderItem(28, "プロジェクト名", STYLE_HEADER3),
        new HeaderItem(29, "プロジェクト状況", STYLE_HEADER3),
        new HeaderItem(30, "課題・問題点", STYLE_HEADER3),
        new HeaderItem(32, "④プロジェクト", STYLE_HEADER2),
        new HeaderItem(33, "プロジェクト名", STYLE_HEADER3),
        new HeaderItem(34, "プロジェクト状況", STYLE_HEADER3),
        new HeaderItem(35, "課題・問題点", STYLE_HEADER3),
        new HeaderItem(37, "⑤プロジェクト", STYLE_HEADER2),
        new HeaderItem(38, "プロジェクト名", STYLE_HEADER3),
        new HeaderItem(39, "プロジェクト状況", STYLE_HEADER3),
        new HeaderItem(40, "課題・問題点", STYLE_HEADER3),
        new HeaderItem(42, "今後の予定", STYLE_HEADER2),
        new HeaderItem(43, "今後の予定", STYLE_HEADER3),
        new HeaderItem(45, "顧客の動向・営業情報等", STYLE_HEADER2),
        new HeaderItem(46, "顧客の動向・営業情報等", STYLE_HEADER3),
        new HeaderItem(48, "健康状態", STYLE_HEADER2),
        new HeaderItem(49, "健康状態", STYLE_HEADER3),
        new HeaderItem(51, "上司へ相談", STYLE_HEADER2),
        new HeaderItem(52, "上司へ相談", STYLE_HEADER3),
        new HeaderItem(54, "その他", STYLE_HEADER2),
        new HeaderItem(55, "その他", STYLE_HEADER3));

    // 各ヘッダー項目を設定
    for (HeaderItem item : headers) {
      // ヘッダースタイルを取得
      CellStyle style = cellStyles.get(item.styleKey());
      // ヘッダーの値設定
      setDetailValue(sheet, item.rowNum(), 1, item.text(), style);
    }
  }

  /**
   * 業務報告書のデータを設定
   * 
   * @param sheet      ワークシートオブジェクト
   * @param detailFull 業務報告書DTO
   * @param cellStyles セルのスタイル情報(HashMap)
   */
  private static void setDetailDataValues(Sheet sheet, DetailFull detailFull, Map<String, CellStyle> cellStyles) {
    // セルスタイル（標準）
    CellStyle styleGeneral = cellStyles.get("general");
    // セルスタイル（文字列：改行）
    CellStyle styleStringWrap = cellStyles.get("stringWrap");

    // 固定のデータ項目
    List<DataItem> dataItems = List.of(
        // 対象期間
        new DataItem(3, 2,
            detail -> {
              DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
              return dateFormat.format(detailFull.getPeriodStart()) + " ～ " +
                  dateFormat.format(detailFull.getPeriodEnd());
            },
            STYLE_GENERAL),
        // エンド企業
        new DataItem(4, 2, DetailFull::getEndCompany, STYLE_GENERAL),
        // 上位企業
        new DataItem(5, 2, DetailFull::getTopCompany, STYLE_GENERAL),
        // 業種
        new DataItem(6, 2, DetailFull::getIndustry, STYLE_GENERAL),
        // 案件名
        new DataItem(7, 2, DetailFull::getTopic, STYLE_GENERAL),
        // 参画人数
        new DataItem(8, 2, DetailFull::getParticipantPeple, STYLE_GENERAL),
        // 勤務形態
        new DataItem(9, 2, detal -> convertWorkingStyle(detal.getWorkingStyle()), STYLE_GENERAL),
        // 職場最寄駅
        new DataItem(10, 2, DetailFull::getStation, STYLE_GENERAL),
        // 通勤時間
        new DataItem(11, 2,
            detail -> {
              int commutingTimeHour = (int) ((detail.getCommutingTime() * 60) / 60);
              int commutingTimeMinute = (int) ((detail.getCommutingTime() * 60) % 60);
              return commutingTimeHour + "時間 " + commutingTimeMinute + "分";
            },
            STYLE_GENERAL),
        // 案件参画日
        new DataItem(12, 2, DetailFull::getParticipantDate, STYLE_DATETIME),
        // 主要技術
        new DataItem(13, 2, DetailFull::getMainTechnology, STYLE_GENERAL),
        // データベース
        new DataItem(14, 2, DetailFull::getDatabase, STYLE_GENERAL),
        // ポジション
        new DataItem(15, 2,
            detail -> convertPosition(detail.getPosition()),
            STYLE_GENERAL),

        // 今後の予定
        new DataItem(43, 2,
            detail -> appendLineSeparator(detail.getFuturePlan()),
            STYLE_STRING_WRAP),
        // 顧客の状況
        new DataItem(46, 2,
            detail -> appendLineSeparator(detail.getCustomerTrend()),
            STYLE_STRING_WRAP),
        // 健康状態
        new DataItem(49, 2,
            detail -> appendLineSeparator(detail.getHelthCondition()),
            STYLE_STRING_WRAP),
        // 上司へ相談
        new DataItem(52, 2,
            detail -> appendLineSeparator(detail.getConsultation()),
            STYLE_STRING_WRAP),
        // その他
        new DataItem(55, 2,
            detail -> appendLineSeparator(detail.getOther()),
            STYLE_STRING_WRAP));

    // 固定データ項目を設定
    for (DataItem item : dataItems) {
      CellStyle style = cellStyles.get(item.styleKey());
      Object value = item.valueSupplier().apply(detailFull);
      setDetailValue(sheet, item.rowNum(), item.colNum(), value, style);
    }

    // 個別ロジック：作業内容
    // 18行目から始まり、各プロジェクトは5行間隔
    for (int i = 0; i < detailFull.getWorkDetails().size(); i++) {
      WorkDetail workDetail = detailFull.getWorkDetails().get(i);
      int baseRow = 18 + (i * 5);
      setDetailValue(sheet, baseRow, 2, workDetail.getWorkName(), styleGeneral);
      setDetailValue(sheet, baseRow + 1, 2, appendLineSeparator(workDetail.getSituation()), styleStringWrap);
      setDetailValue(sheet, baseRow + 2, 2, appendLineSeparator(workDetail.getIssuesProblems()), styleStringWrap);
    }
  }

  /**
   * 勤務形態を日本語文字列に変換する
   * 
   * @param workingStyle 勤務形態のキー
   * @return 日本語文字列
   */
  private static String convertWorkingStyle(String workingStyle) {
    return switch (convertNullIntoEmptyString(workingStyle)) {
      case "combinedwork1" -> "併用勤務(在宅率6割以上)";
      case "combinedwork2" -> "併用勤務(在宅率6割未満)";
      case "onsitework" -> "現場勤務";
      case "telework" -> "在宅勤務";
      default -> "選択なし";
    };
  }

  /**
   * ポジションを日本語文字列に変換する
   * 
   * @param position ポジションのキー
   * @return 日本語文字列
   */
  private static String convertPosition(String position) {
    return switch (convertNullIntoEmptyString(position)) {
      case "Programmer" -> "PG";
      case "System Engineer" -> "SE";
      case "System Engineer(Employee Substitution)" -> "SE(社員代替)";
      case "Tester" -> "テスター";
      case "Operator" -> "オペレーター";
      case "Project Leader" -> "PL";
      case "Project Manager" -> "PM";
      case "Employee Substitution" -> "社員代替";
      default -> "選択なし";
    };
  }

  /**
   * Nullを空文字に変換する
   * 
   * @param str 入力文字列
   * @return 変換後文字列
   */
  private static String convertNullIntoEmptyString(String str) {
    return Optional.ofNullable(str).orElse("");
  }

  /**
   * 文字列の末尾に改行を追加する（nullの場合は空文字）
   * 
   * @param str 入力文字列
   * @return 末尾に改行が追加された文字列、または空文字
   */
  private static String appendLineSeparator(String str) {
    return convertNullIntoEmptyString(str) + System.lineSeparator();
  }

  /**
   * 罫線を引く
   * 
   * @param style セルスタイルオブジェクト
   */
  private static void applyCommonBorders(CellStyle style) {
    style.setBorderTop(BorderStyle.THIN);
    style.setBorderBottom(BorderStyle.THIN);
    style.setBorderLeft(BorderStyle.THIN);
    style.setBorderRight(BorderStyle.THIN);
    style.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    style.setRightBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
  }

  /**
   * フォントをプロパティ設定
   * 
   * @param workbook ワークブックオブジェクト
   * @return フォントプロパティ
   */
  private static Font createCommonFont(Workbook workbook) {
    Font font = workbook.createFont();
    font.setFontName(FONT_NAME);
    font.setFontHeightInPoints(FONT_HEIGHT);
    font.setColor(IndexedColors.BLACK.getIndex());
    return font;
  }

    /**
   * 共通のCellStyleを生成し、基本的なアラインメントを設定する
   * 
   * @param workbook            ワークブックオブジェクト
   * @param horizontalAlignment 水平方向のアラインメント
   * @param verticalAlignment   垂直方向のアラインメント
   * @return 設定済みのCellStyleオブジェクト
   */
  private static CellStyle createBaseStyle(Workbook workbook, HorizontalAlignment horizontalAlignment,
      VerticalAlignment verticalAlignment) {
    CellStyle style = workbook.createCellStyle();
    style.setAlignment(horizontalAlignment);
    style.setVerticalAlignment(verticalAlignment);
    return style;
  }

  /**
   * 共通のFontオブジェクトを生成
   * 
   * @param workbook   ワークブックオブジェクト
   * @param fontHeight フォントの高さ
   * @param colorIndex フォント色のインデックス
   * @return 設定済みのFontオブジェクト
   */
  private static Font createFont(Workbook workbook, short fontHeight, short colorIndex) {
    Font font = workbook.createFont();
    font.setFontName(FONT_NAME);
    font.setFontHeightInPoints(fontHeight);
    font.setColor(colorIndex);
    return font;
  }

  /**
   * 共通の罫線設定
   * 
   * @param style       セルスタイルオブジェクト
   * @param borderStyle 罫線スタイルオブジェクト
   * @param borderColor 罫線色インデックス
   */
  private static void applyBorders(CellStyle style, BorderStyle borderStyle, short borderColor) {
    style.setBorderTop(borderStyle);
    style.setBorderBottom(borderStyle);
    style.setBorderLeft(borderStyle);
    style.setBorderRight(borderStyle);
    style.setTopBorderColor(borderColor);
    style.setBottomBorderColor(borderColor);
    style.setLeftBorderColor(borderColor);
    style.setRightBorderColor(borderColor);
  }

}
