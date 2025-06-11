package com.example.businessreport.web.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.businessreport.domain.dto.DetailFull;
import com.example.businessreport.domain.dto.form.DetailForm;
import com.example.businessreport.domain.helper.ReportMapperHelper;
import com.example.businessreport.domain.model.Detail;
import com.example.businessreport.domain.service.ReportExcelExportService;
import com.example.businessreport.domain.service.ReportService;
import com.example.businessreport.domain.service.WorkDetailService;
import com.example.businessreport.web.helper.DetailUtil;
import com.example.businessreport.web.helper.DetailUtil.ViewMode;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 業務報告書作成 コントローラー
 */
@Controller
@RequestMapping("/report")
public class DetailWebControllerV2 {
  // 業務報告書サービス
  private final ReportService reportService;
  private final WorkDetailService workDetailService;
  private final ReportExcelExportService reportExcelExportService;
  private final ReportMapperHelper reportMapperHelper;

  // Model Attribute名の定数
  private static final String REPORT_LIST_ATTR = "reportlist";
  private static final String REPORT_SHOW_ATTR = "reportShow";
  private static final String REPORT_FULL_ATTR = "reportFull";
  private static final String REPORT_FORM_ATTR = "reportForm";
  private static final String ERROR_MSG_ALL_ATTR = "errorMsgAll";
  private static final String ERROR_MSG_FLASH_ATTR = "errorMessage";
  private static final String SUCCESS_MSG_FLASH_ATTR = "successMessage";
  private static final String ERRORS_ATTR = "errors"; // バリデーションエラーの詳細

  // 画面表示モードの定数
  // private static final String VIEW_MODE_LIST = "list";
  // private static final String VIEW_MODE_CREATE = "create";
  // private static final String VIEW_MODE_BROWSE = "browse";
  // private static final String VIEW_MODE_UPDATE = "update";

  // コンストラクタ(サービスをBean登録)
  @Autowired
  public DetailWebControllerV2(
      ReportService reportService,
      WorkDetailService workDetailService,
      ReportExcelExportService reportExcelExportService,
      ReportMapperHelper reportMapperHelper) {
    this.reportService = reportService;
    this.workDetailService = workDetailService;
    this.reportExcelExportService = reportExcelExportService;
    this.reportMapperHelper = reportMapperHelper;
  }

  // フォームから入力された空文字をnullに変換する
  @InitBinder
  public void initBider(WebDataBinder binder) {
    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
  }

  /**
   * 業務報告書一覧画面を表示する
   * 
   * @param model 画面間インタフェース
   * @return url 遷移先画面
   */
  @GetMapping
  public String showDetailList(Model model) {
    // 業務報告書データを全件取得
    List<DetailFull> detailFull = reportService.findAllReports();

    // 画面へオブジェクトを渡す
    model.addAttribute(REPORT_LIST_ATTR, detailFull);
    model.addAttribute(REPORT_SHOW_ATTR, DetailUtil.getDetailShow(ViewMode.LIST));

    // 画面へ遷移
    return "templete";
  }

  /**
   * 業務報告書作成画面を表示する
   * 
   * @param model 画面間インタフェース
   * @param form  業務報告フォーム
   * @return 遷移先画面
   */
  @PostMapping(params = "reportAddOpen")
  public String showDetailCreate(Model model, @ModelAttribute DetailForm form) {

    // 画面へオブジェクトを渡す
    model.addAttribute(REPORT_SHOW_ATTR, DetailUtil.getDetailShow(ViewMode.CREATE));

    // 画面へ遷移
    return "templete";
  }

  /**
   * 業務報告書参照画面を表示する
   * 
   * @param model 画面間インタフェース
   * @param id    報告書ID
   * @return 遷移先画面
   */
  @PostMapping(params = "reportBrowse")
  public String showDetailBrowse(Model model, @RequestParam("reportBrowse") Integer id) {
    // 業務報告書データを全件取得
    DetailFull detailFull = reportService.findReportById(id);

    // 画面へオブジェクトを渡す
    model.addAttribute(REPORT_FULL_ATTR, detailFull);
    model.addAttribute(REPORT_SHOW_ATTR, DetailUtil.getDetailShow(ViewMode.BROWSE));

    // 画面へ遷移
    return "templete";
  }

  /**
   * 業務報告書編集画面を表示する
   * 
   * @param model 画面間インタフェース
   * @param id    報告書ID
   * @return 遷移先画面
   */
  @PostMapping(params = "reportEdit")
  public String showDetailEdit(Model model, @RequestParam("reportEdit") Integer id) {
    // 業務報告書データを取得
    DetailFull detailFull = reportService.findReportById(id);

    // 業務報告書データをフォームへマッピング
    DetailForm form = reportMapperHelper.mapDetailFullToForm(detailFull);

    // 画面へオブジェクトを渡す
    model.addAttribute(REPORT_FORM_ATTR, form);
    model.addAttribute(REPORT_SHOW_ATTR, DetailUtil.getDetailShow(ViewMode.UPDATE));

    // 画面へ遷移
    return "templete";
  }

  /**
   * 業務報告書参照、作成、編集画面を閉じる
   * 
   * @param model 画面間インタフェース
   * @return 遷移先画面
   */
  @PostMapping(params = "reportFormClose")
  public String closeDetailAdd(Model model) {
    // 業務報告書一覧画面を表示する
    return showDetailList(model);
  }

  /**
   * 業務報告書の最新情報を取得する
   * 
   * @param model  画面間インタフェース
   * @param form   業務報告フォーム
   * @param result 入力チェック結果
   * @return 遷移先画面
   */
  @PostMapping(params = "reportLatest")
  public String importDetailLatest(Model model, @ModelAttribute DetailForm form) {
    // 業務報告書データ最新の1件を取得
    DetailFull detailFull = reportService.findLatestReport();

    // 報告書IDの新たに採番するため、クリア
    if (detailFull != null) {
      detailFull.setReportId(null);
    }

    // 業務報告書データをフォームへマッピング
    if (detailFull != null) {
      form = reportMapperHelper.mapDetailFullToForm(detailFull);
    }

    // 画面へオブジェクトを渡す
    model.addAttribute(REPORT_FORM_ATTR, form);

    // 作成画面へ遷移
    return showDetailCreate(model, form);
  }

  /**
   * 業務報告書を登録する
   * 
   * @param model              画面間インタフェース
   * @param form               業務報告フォーム
   * @param result             入力チェック結果
   * @param redirectAttributes リダイレクト時の属性
   * @return 遷移先画面
   */
  @PostMapping(params = "reportCreate")
  public String createDetail(Model model,
      @ModelAttribute @Validated DetailForm form,
      BindingResult result,
      RedirectAttributes redirectAttributes) {

    // 入力チェック
    if (handleValidationErrors(result, model, ViewMode.CREATE)) {
      return "templete";
    }

    // 業務報告書エンティティにマッピング
    Detail detail = reportMapperHelper.mapFormToDetail(form);

    // 登録処理を実行
    boolean success = performSaveOrUpdate(true, detail, form, redirectAttributes);

    // 登録失敗時（例: エラーメッセージ表示）
    if (!success) {
      redirectAttributes.addFlashAttribute(ERROR_MSG_FLASH_ATTR, "業務報告書の登録に失敗しました。");
      return "redirect:/report";
    }

    // 登録成功時
    redirectAttributes.addFlashAttribute(SUCCESS_MSG_FLASH_ATTR, "業務報告書を登録しました。");
    return "redirect:/report";
  }

  /**
   * 業務報告書を更新する
   * 
   * @param model  画面間インタフェース
   * @param form   業務報告フォーム
   * @param result 入力チェック結果
   * @return 遷移先画面
   */
  @PostMapping(params = "reportUpdate")
  public String updateDetail(
      Model model,
      @ModelAttribute @Validated DetailForm form,
      BindingResult result,
      RedirectAttributes redirectAttributes) {

    // 入力チェック
    if (handleValidationErrors(result, model, ViewMode.UPDATE)) {
      // "detailShow" は "reportShow" に統一すると良いでしょう
      model.addAttribute(REPORT_SHOW_ATTR, DetailUtil.getDetailShow(ViewMode.UPDATE));
      return "templete";
    }

    // フォームを業務報告書データへマッピング
    Detail detail = reportMapperHelper.mapFormToDetail(form);

    // 更新処理を実行
    boolean success = performSaveOrUpdate(false, detail, form, redirectAttributes);

    // 更新失敗時
    if (!success) {
      redirectAttributes.addFlashAttribute(ERROR_MSG_FLASH_ATTR, "業務報告書の更新に失敗しました。");
      return "redirect:/report";
    }

    // 更新成功時
    redirectAttributes.addFlashAttribute(SUCCESS_MSG_FLASH_ATTR, "業務報告書を更新しました。");
    return "redirect:/report";
  }

  /**
   * 業務報告書のExcelを作成する
   * 
   * @param model    画面間インタフェース
   * @param id       報告書ID
   * @param response レスポンス情報
   */
  @PostMapping(params = "reportExcel")
  public void createExcelDetail(@RequestParam("reportExcel") Integer id, HttpServletResponse response) {
    // 業務報告書データを取得
    DetailFull detailFull = reportService.findReportById(id);

    // データが見つからない場合のエラー処理
    if (detailFull == null) {
      // Excel作成失敗としてエラーを返すか、例外を投げる
      // 例: throw new ReportNotFoundException("Report with ID " + id + " not found for
      // Excel export.");
      try {
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "指定された業務報告書が見つかりません。");
      } catch (IOException e) {
        System.err.println("Error sending not found error: " + e.getMessage());
      }
      return;
    }

    // 業務報告書のExcelファイル名作成
    String fileName = "業務報告書_" +
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
        ".xlsx";

    try {
      // レスポンスヘッダーを設定 (ブラウザでのExcelファイルのダウンロード指定)
      // ExcelのMIMEタイプを設定
      response.addHeader(
          "Content-Disposition",
          "attachment; filename*=UTF-8''" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.name()));
      response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

      // Excelファイルを出力ストリームに書き込み
      // 自動的にExcelファイルがダウンロードされる
      try (ServletOutputStream stream = response.getOutputStream()) {
        boolean resultExcel = reportExcelExportService.exportReportToExcel(detailFull, stream);
        if (!resultExcel) {
          // Excel作成失敗時のエラー処理
          try {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Excelファイルの生成に失敗しました。");
          } catch (IOException e) {
            System.err.println("Error sending internal server error: " + e.getMessage());
          }
        }
      }
    } catch (IOException e) {
      System.err.println("Error creating Excel file or writing to response: " + e.getMessage());
      e.printStackTrace();
      try {
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Excelファイルのエクスポート中にエラーが発生しました。");
      } catch (IOException sendErrorE) {
        System.err.println("Error sending internal server error during exception handling: " + sendErrorE.getMessage());
      }
    }
  }

  /**
   * バリデーションエラーを処理し、Modelにエラー情報を追加します。
   * 
   * @param result   BindingResultオブジェクト
   * @param model    Modelオブジェクト
   * @param viewMode 現在のビューモード (例: "create", "update")
   * @return エラーがある場合はtrue、ない場合はfalse
   */
  private boolean handleValidationErrors(BindingResult result, Model model, ViewMode mode) {
    if (result.hasErrors()) {
      Map<String, String> errors = result.getFieldErrors().stream()
          .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
      result.getGlobalErrors().forEach(error -> errors.put(error.getObjectName(), error.getDefaultMessage()));

      model.addAttribute(REPORT_SHOW_ATTR, DetailUtil.getDetailShow(mode));
      model.addAttribute(ERROR_MSG_ALL_ATTR, "入力に誤りがあります。");
      model.addAttribute(ERRORS_ATTR, errors); // 詳細なエラー情報を画面に渡す
      return true;
    }
    return false;
  }

  /**
   * 業務報告書の登録または更新を実行します。
   * 
   * @param isCreate           新規作成の場合はtrue、更新の場合はfalse
   * @param detail             業務報告書エンティティ
   * @param form               業務報告フォーム (作業詳細用)
   * @param redirectAttributes リダイレクト属性
   * @return 処理が成功した場合はtrue、失敗した場合はfalse
   */
  private boolean performSaveOrUpdate(boolean isCreate, Detail detail, DetailForm form,
      RedirectAttributes redirectAttributes) {

    // 報告書データ 新規作成 or 更新
    boolean detailSuccess = isCreate ? reportService.createReport(detail) : reportService.updateReport(detail);

    // 作業内容データ 新規作成 or 更新
    boolean workDetailsSuccess = false;
    if (detailSuccess && detail.getReportId() != null) { // IDは新規作成時に採番される
      if (isCreate) {
        workDetailsSuccess = workDetailService.createWorkDetails(form.getWorkDetails(), detail.getReportId());
      } else {
        workDetailsSuccess = workDetailService.updateWorkDetails(form.getWorkDetails(), detail.getReportId());
      }
    }

    return detailSuccess && workDetailsSuccess;
  }

}
