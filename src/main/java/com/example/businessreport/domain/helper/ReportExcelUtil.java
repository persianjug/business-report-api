package com.example.businessreport.domain.helper;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.poi.ss.usermodel.BorderStyle;
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

import com.example.businessreport.domain.dto.ReportFull;

public class ReportExcelUtil {

  /**
   * ワークブックオブジェクトの生成
   * @return ワークブックオブジェクト
   */
  public static Workbook createReportWorkbook() {
    return new XSSFWorkbook();
  }

  /**
   * ワークシートオブジェクトの生成
   * @param workbook ワークブックオブジェクト
   * @param sheetName シート名
   * @return ワークシートオブジェクト
   */
  public static Sheet createReportSheet(Workbook workbook, String sheetName) {
    return workbook.createSheet(sheetName);
  }

  /**
   * セルのスタイルを取得する
   * @param workbook ワークブックオブジェクト
   * @return セルのスタイル情報(HashMap)
   */
  public static final Map<String, CellStyle> createCellStyles(Workbook workbook) {
    // マップ
    Map<String, CellStyle> cellStyles = new HashMap<>();

    // 標準のスタイル
    CellStyle styleGeneral = workbook.createCellStyle();
    styleGeneral.setAlignment(HorizontalAlignment.LEFT);
    styleGeneral.setVerticalAlignment(VerticalAlignment.TOP);
    styleGeneral.setWrapText(true);
    Font fontGeneral = workbook.createFont();
    fontGeneral.setFontName("Meiryo UI");
    fontGeneral.setFontHeightInPoints((short) 11);
    fontGeneral.setColor(IndexedColors.BLACK.getIndex());
    styleGeneral.setFont(fontGeneral);
    styleGeneral.setBorderTop(BorderStyle.THIN);
    styleGeneral.setBorderBottom(BorderStyle.THIN);
    styleGeneral.setBorderLeft(BorderStyle.THIN);
    styleGeneral.setBorderRight(BorderStyle.THIN);
    styleGeneral.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    styleGeneral.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    styleGeneral.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    styleGeneral.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    cellStyles.put("general", styleGeneral);

    // 改行が入った文字列用のスタイル    
    CellStyle styleStirngWrap = workbook.createCellStyle();
    styleStirngWrap.setAlignment(HorizontalAlignment.LEFT);
    styleStirngWrap.setVerticalAlignment(VerticalAlignment.TOP);
    styleStirngWrap.setWrapText(true);
    Font fontStirngWrap = workbook.createFont();
    fontStirngWrap.setFontName("Meiryo UI");
    fontStirngWrap.setFontHeightInPoints((short) 11);
    fontStirngWrap.setColor(IndexedColors.BLACK.getIndex());
    styleStirngWrap.setFont(fontStirngWrap);
    styleStirngWrap.setBorderTop(BorderStyle.THIN);
    styleStirngWrap.setBorderBottom(BorderStyle.THIN);
    styleStirngWrap.setBorderLeft(BorderStyle.THIN);
    styleStirngWrap.setBorderRight(BorderStyle.THIN);
    styleStirngWrap.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    styleStirngWrap.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    styleStirngWrap.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    styleStirngWrap.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    cellStyles.put("stringWrap", styleStirngWrap);

    //日時表示用のスタイル
    DataFormat format = workbook.createDataFormat();
    CellStyle styleDatetime = workbook.createCellStyle();
    styleDatetime.setAlignment(HorizontalAlignment.LEFT);
    styleDatetime.setVerticalAlignment(VerticalAlignment.TOP);
    styleDatetime.setDataFormat(format.getFormat("yyyy/mm/dd"));
    Font fontDatetime = workbook.createFont();
    fontDatetime.setFontName("Meiryo UI");
    fontDatetime.setFontHeightInPoints((short) 11);
    fontDatetime.setColor(IndexedColors.BLACK.getIndex());
    styleDatetime.setFont(fontDatetime);
    styleDatetime.setBorderTop(BorderStyle.THIN);
    styleDatetime.setBorderBottom(BorderStyle.THIN);
    styleDatetime.setBorderLeft(BorderStyle.THIN);
    styleDatetime.setBorderRight(BorderStyle.THIN);
    styleDatetime.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    styleDatetime.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    styleDatetime.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    styleDatetime.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    cellStyles.put("datetime", styleDatetime);

    // ヘッダーのスタイル    
    CellStyle styleHeader1 = workbook.createCellStyle();
    styleHeader1.setAlignment(HorizontalAlignment.LEFT);
    styleHeader1.setVerticalAlignment(VerticalAlignment.TOP);
    Font fontHeader1 = workbook.createFont();
    fontHeader1.setFontName("Meiryo UI");
    fontHeader1.setFontHeightInPoints((short) 16);
    fontHeader1.setColor(IndexedColors.BLACK.getIndex());
    styleHeader1.setFont(fontHeader1);
    cellStyles.put("header1", styleHeader1);

    // ヘッダーのスタイル    
    CellStyle styleHeader2 = workbook.createCellStyle();
    styleHeader2.setAlignment(HorizontalAlignment.LEFT);
    styleHeader2.setVerticalAlignment(VerticalAlignment.TOP);
    Font fontHeader2 = workbook.createFont();
    fontHeader2.setFontName("Meiryo UI");
    fontHeader2.setFontHeightInPoints((short) 11);
    fontHeader2.setColor(IndexedColors.BLACK.getIndex());
    styleHeader2.setFont(fontHeader2);
    cellStyles.put("header2", styleHeader2);

    // ヘッダーのスタイル    
    CellStyle styleHeader3 = workbook.createCellStyle();
    styleHeader3.setAlignment(HorizontalAlignment.LEFT);
    styleHeader3.setVerticalAlignment(VerticalAlignment.TOP);
    Font fontHeader3 = workbook.createFont();
    fontHeader3.setFontName("Meiryo UI");
    fontHeader3.setFontHeightInPoints((short) 11);
    fontHeader3.setColor(IndexedColors.WHITE.getIndex());
    styleHeader3.setFont(fontHeader3);
    styleHeader3.setFillForegroundColor(IndexedColors.DARK_BLUE.index);
    styleHeader3.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    styleHeader3.setBorderTop(BorderStyle.THIN);
    styleHeader3.setBorderBottom(BorderStyle.THIN);
    styleHeader3.setBorderLeft(BorderStyle.THIN);
    styleHeader3.setBorderRight(BorderStyle.THIN);
    styleHeader3.setTopBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    styleHeader3.setBottomBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    styleHeader3.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    styleHeader3.setLeftBorderColor(IndexedColors.GREY_25_PERCENT.getIndex());
    cellStyles.put("header3", styleHeader3);

    return cellStyles;
  }

  /**
   * 業務報告書のデータ転記
   * @param sheet ワークシートオブジェクト
   * @param reportFull 業務報告書DTO
   * @param cellStyles セルのスタイル情報(HashMap)
   */
  public static void setReportValues(Sheet sheet, ReportFull reportFull, Map<String, CellStyle> cellStyles) {
    setReportHeaderValues(sheet, cellStyles);
    setReportDataValues(sheet, reportFull, cellStyles);
  }

  /**
   * シート全体のスタイルを設定
   * @param sheet ワークシートオブジェクト
   */
  public static void setSheetStyle(Sheet sheet) {
    // 1列目の列幅    
    sheet.setColumnWidth(0, 768);

    // 2列目はテキストの長さに合わせて列幅を設定    
    sheet.autoSizeColumn(1);

    // 3列目はテキストの長さに合わせて列幅を設定    
//    sheet.autoSizeColumn(2);

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
   * @param workbook ワークブックオブジェクト
   * @param target Excelファイルのパス
   * @return true: 出力OK  false: 出力NG
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
   * @param workbook ワークブックオブジェクト
   * @param outStream Excelファイルの出力ストリーム
   * @return true: 出力OK  false: 出力NG
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
   * セルオブジェクトを生成する
   * @param sheet ワークシートオブジェクト
   * @param rowNum 行番号
   * @param cellNum 列番号
   */
  private static void createCell(Sheet sheet, int rowNum, int cellNum) {
    Row row = CellUtil.getRow(rowNum, sheet);
    CellUtil.getCell(row, cellNum);
  }

  /**
   * セルに値を設定(String)
   * @param sheet ワークシートオブジェクト
   * @param RowNum 行番号
   * @param CellNum 列番号
   * @param value 値
   * @param cellStyle セルのスタイル情報
   */
  private static void setReportValue(Sheet sheet, int RowNum, int CellNum, String value, CellStyle cellStyle) {
    createCell(sheet, RowNum, CellNum);
    sheet.getRow(RowNum).getCell(CellNum).setCellValue(value);
    sheet.getRow(RowNum).getCell(CellNum).setCellStyle(cellStyle);
  }

  /**
   * セルに値を設定(LocalDateTime)
   * @param sheet ワークシートオブジェクト
   * @param RowNum 行番号
   * @param CellNum 列番号
   * @param value 値
   * @param cellStyle セルのスタイル情報
   */
  private static void setReportValue(Sheet sheet, int RowNum, int CellNum, LocalDateTime value, CellStyle cellStyle) {
    createCell(sheet, RowNum, CellNum);
    sheet.getRow(RowNum).getCell(CellNum).setCellValue(value);
    sheet.getRow(RowNum).getCell(CellNum).setCellStyle(cellStyle);
  }

  /**
   * セルに値を設定(LocalDate)
   * @param sheet ワークシートオブジェクト
   * @param RowNum 行番号
   * @param CellNum 列番号
   * @param value 値
   * @param cellStyle セルのスタイル情報
   */
  private static void setReportValue(Sheet sheet, int RowNum, int CellNum, LocalDate value, CellStyle cellStyle) {
    createCell(sheet, RowNum, CellNum);
    sheet.getRow(RowNum).getCell(CellNum).setCellValue(value);
    sheet.getRow(RowNum).getCell(CellNum).setCellStyle(cellStyle);
  }

  /**
   * セルに値を設定(Double)
   * @param sheet ワークシートオブジェクト
   * @param RowNum 行番号
   * @param CellNum 列番号
   * @param value 値
   * @param cellStyle セルのスタイル情報
   */
  private static void setReportValue(Sheet sheet, int RowNum, int CellNum, Double value, CellStyle cellStyle) {
    createCell(sheet, RowNum, CellNum);
    sheet.getRow(RowNum).getCell(CellNum).setCellValue(value);
    sheet.getRow(RowNum).getCell(CellNum).setCellStyle(cellStyle);
  }

  /**
   * セルに値を設定(Integer)
   * @param sheet ワークシートオブジェクト
   * @param RowNum 行番号
   * @param CellNum 列番号
   * @param value 値
   * @param cellStyle セルのスタイル情報
   */
  private static void setReportValue(Sheet sheet, int RowNum, int CellNum, Integer value, CellStyle cellStyle) {
    createCell(sheet, RowNum, CellNum);
    sheet.getRow(RowNum).getCell(CellNum).setCellValue(value);
    sheet.getRow(RowNum).getCell(CellNum).setCellStyle(cellStyle);
  }

  /**
   * 業務報告書のヘッダーを設定
   * @param sheet ワークシートオブジェクト
   * @param cellStyles セルのスタイル情報(HashMap)
   */
  private static void setReportHeaderValues(Sheet sheet, Map<String, CellStyle> cellStyles) {

    CellStyle styleHeader1 = cellStyles.get("header1");
    CellStyle styleHeader2 = cellStyles.get("header2");
    CellStyle styleHeader3 = cellStyles.get("header3");

    setReportValue(sheet, 0, 1, "業務報告書", styleHeader1);
    setReportValue(sheet, 2, 1, "案件概要", styleHeader2);
    setReportValue(sheet, 3, 1, "対象期間", styleHeader3);
    setReportValue(sheet, 4, 1, "エンド企業名", styleHeader3);
    setReportValue(sheet, 5, 1, "上位顧客名", styleHeader3);
    setReportValue(sheet, 6, 1, "業種", styleHeader3);
    setReportValue(sheet, 7, 1, "案件名", styleHeader3);
    setReportValue(sheet, 8, 1, "エル・フィールド参画人数", styleHeader3);
    setReportValue(sheet, 9, 1, "職場勤務／在宅", styleHeader3);
    setReportValue(sheet, 10, 1, "職場最寄駅", styleHeader3);
    setReportValue(sheet, 11, 1, "通勤時間(h)", styleHeader3);
    setReportValue(sheet, 12, 1, "案件参画日", styleHeader3);
    setReportValue(sheet, 13, 1, "主要技術(言語／フレームワーク／ツール)", styleHeader3);
    setReportValue(sheet, 14, 1, "データベース", styleHeader3);
    setReportValue(sheet, 15, 1, "ポジション", styleHeader3);
    setReportValue(sheet, 17, 1, "①プロジェクト", styleHeader2);
    setReportValue(sheet, 18, 1, "プロジェクト名", styleHeader3);
    setReportValue(sheet, 19, 1, "プロジェクト状況", styleHeader3);
    setReportValue(sheet, 20, 1, "課題・問題点", styleHeader3);
    setReportValue(sheet, 22, 1, "②プロジェクト", styleHeader2);
    setReportValue(sheet, 23, 1, "プロジェクト名", styleHeader3);
    setReportValue(sheet, 24, 1, "プロジェクト状況", styleHeader3);
    setReportValue(sheet, 25, 1, "課題・問題点", styleHeader3);
    setReportValue(sheet, 27, 1, "③プロジェクト", styleHeader2);
    setReportValue(sheet, 28, 1, "プロジェクト名", styleHeader3);
    setReportValue(sheet, 29, 1, "プロジェクト状況", styleHeader3);
    setReportValue(sheet, 30, 1, "課題・問題点", styleHeader3);
    setReportValue(sheet, 32, 1, "④プロジェクト", styleHeader2);
    setReportValue(sheet, 33, 1, "プロジェクト名", styleHeader3);
    setReportValue(sheet, 34, 1, "プロジェクト状況", styleHeader3);
    setReportValue(sheet, 35, 1, "課題・問題点", styleHeader3);
    setReportValue(sheet, 37, 1, "⑤プロジェクト", styleHeader2);
    setReportValue(sheet, 38, 1, "プロジェクト名", styleHeader3);
    setReportValue(sheet, 39, 1, "プロジェクト状況", styleHeader3);
    setReportValue(sheet, 40, 1, "課題・問題点", styleHeader3);
    setReportValue(sheet, 42, 1, "今後の予定", styleHeader2);
    setReportValue(sheet, 43, 1, "今後の予定", styleHeader3);
    setReportValue(sheet, 45, 1, "顧客の動向・営業情報等", styleHeader2);
    setReportValue(sheet, 46, 1, "顧客の動向・営業情報等", styleHeader3);
    setReportValue(sheet, 48, 1, "健康状態", styleHeader2);
    setReportValue(sheet, 49, 1, "健康状態", styleHeader3);
    setReportValue(sheet, 51, 1, "上司へ相談", styleHeader2);
    setReportValue(sheet, 52, 1, "上司へ相談", styleHeader3);
    setReportValue(sheet, 54, 1, "その他", styleHeader2);
    setReportValue(sheet, 55, 1, "その他", styleHeader3);
  }

  /**
   * 業務報告書のデータを設定
   * @param sheet ワークシートオブジェクト
   * @param reportFull 業務報告書DTO
   * @param cellStyles セルのスタイル情報(HashMap)
   */
  private static void setReportDataValues(Sheet sheet, ReportFull reportFull, Map<String, CellStyle> cellStyles) {
    CellStyle styleGeneral = cellStyles.get("general");
    CellStyle styleStringWrap = cellStyles.get("stringWrap");
    CellStyle styleDatetime = cellStyles.get("datetime");

    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
    String period = dateFormat.format(reportFull.getPeriodStart()) + " ～ " +
        dateFormat.format(reportFull.getPeriodEnd());
    setReportValue(sheet, 3, 2, period, styleGeneral);

    setReportValue(sheet, 4, 2, reportFull.getEndCompany(), styleGeneral);
    setReportValue(sheet, 5, 2, reportFull.getTopCompany(), styleGeneral);
    setReportValue(sheet, 6, 2, reportFull.getIndustry(), styleGeneral);
    setReportValue(sheet, 7, 2, reportFull.getTopic(), styleGeneral);
    setReportValue(sheet, 8, 2, reportFull.getParticipantPeple(), styleGeneral);

    String workStyle;
    switch (reportFull.getWorkingStyle()) {
    case "combinedwork1":
      workStyle = "併用勤務(在宅率6割以上)";
      break;
    case "combinedwork2":
      workStyle = "併用勤務(在宅率6割未満)";
      break;
    case "onsitework":
      workStyle = "現場勤務";
      break;
    case "telework":
      workStyle = "在宅勤務";
      break;
    default:
      workStyle = "選択なし";
      break;
    }
    setReportValue(sheet, 9, 2, workStyle, styleGeneral);

    setReportValue(sheet, 10, 2, reportFull.getStation(), styleGeneral);
    setReportValue(sheet, 11, 2, reportFull.getCommutingTime(), styleGeneral);
    setReportValue(sheet, 12, 2, reportFull.getParticipantDate(), styleDatetime);
    setReportValue(sheet, 13, 2, reportFull.getMainTechnology(), styleGeneral);
    setReportValue(sheet, 14, 2, reportFull.getDatabase(), styleGeneral);

    String position;
    switch (reportFull.getPosition()) {
    case "Programmer":
      position = "PG";
      break;
    case "System Engineer":
      position = "SE";
      break;
    case "System Engineer(Employee Substitution)":
      position = "SE(社員代替)";
      break;
    case "Tester":
      position = "テスター";
      break;
    case "Operator":
      position = "オペレーター";
      break;
    case "Project Leader":
      position = "PL";
      break;
    case "Project Manager":
      position = "PM";
      break;
    case "Employee Substitution":
      position = "社員代替";
      break;
    default:
      position = "選択なし";
      break;
    }
    setReportValue(sheet, 15, 2, position, styleGeneral);

    setReportValue(sheet, 18, 2, reportFull.getWorkDetails().get(0).getWorkName(), styleGeneral);
    setReportValue(sheet, 19, 2, convertNullIntoEmptyString(reportFull.getWorkDetails().get(0).getSituation()) + System.lineSeparator(), styleStringWrap);
    setReportValue(sheet, 20, 2, convertNullIntoEmptyString(reportFull.getWorkDetails().get(0).getIssuesProblems()) + System.lineSeparator(), styleStringWrap);
    setReportValue(sheet, 23, 2, reportFull.getWorkDetails().get(1).getWorkName(), styleGeneral);
    setReportValue(sheet, 24, 2, convertNullIntoEmptyString(reportFull.getWorkDetails().get(1).getSituation()) + System.lineSeparator(), styleStringWrap);
    setReportValue(sheet, 25, 2, convertNullIntoEmptyString(reportFull.getWorkDetails().get(1).getIssuesProblems()) + System.lineSeparator(), styleStringWrap);
    setReportValue(sheet, 28, 2, reportFull.getWorkDetails().get(2).getWorkName(), styleGeneral);
    setReportValue(sheet, 29, 2, convertNullIntoEmptyString(reportFull.getWorkDetails().get(2).getSituation()) + System.lineSeparator(), styleStringWrap);
    setReportValue(sheet, 30, 2, convertNullIntoEmptyString(reportFull.getWorkDetails().get(2).getIssuesProblems()) + System.lineSeparator(), styleStringWrap);
    setReportValue(sheet, 33, 2, reportFull.getWorkDetails().get(3).getWorkName(), styleGeneral);
    setReportValue(sheet, 34, 2, convertNullIntoEmptyString(reportFull.getWorkDetails().get(3).getSituation()) + System.lineSeparator(), styleStringWrap);
    setReportValue(sheet, 35, 2, convertNullIntoEmptyString(reportFull.getWorkDetails().get(3).getIssuesProblems()) + System.lineSeparator(), styleStringWrap);
    setReportValue(sheet, 38, 2, reportFull.getWorkDetails().get(4).getWorkName(), styleGeneral);
    setReportValue(sheet, 39, 2, convertNullIntoEmptyString(reportFull.getWorkDetails().get(4).getSituation()) + System.lineSeparator(), styleStringWrap);
    setReportValue(sheet, 40, 2, convertNullIntoEmptyString(reportFull.getWorkDetails().get(4).getIssuesProblems()) + System.lineSeparator(), styleStringWrap);
    setReportValue(sheet, 43, 2, convertNullIntoEmptyString(reportFull.getFuturePlan()) + System.lineSeparator(), styleStringWrap);
    setReportValue(sheet, 46, 2, convertNullIntoEmptyString(reportFull.getCustomerTrend()) + System.lineSeparator(), styleStringWrap);
    setReportValue(sheet, 49, 2, convertNullIntoEmptyString(reportFull.getHelthCondition()) + System.lineSeparator(), styleStringWrap);
    setReportValue(sheet, 52, 2, convertNullIntoEmptyString(reportFull.getConsultation()) + System.lineSeparator(), styleStringWrap);
    setReportValue(sheet, 55, 2, convertNullIntoEmptyString(reportFull.getOther() + System.lineSeparator()), styleStringWrap);
  }

  /**
   * Nullを空文字に変換する
   * @param str 入力文字列
   * @return 変換後文字列
   */
  private static String convertNullIntoEmptyString(String str) {
    Optional<String> opt = Optional.ofNullable(str);
    return opt.orElse("");
  }

}
