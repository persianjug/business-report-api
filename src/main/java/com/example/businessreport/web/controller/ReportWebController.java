package com.example.businessreport.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.businessreport.domain.dto.ReportFull;
import com.example.businessreport.domain.model.Report;
import com.example.businessreport.domain.service.ReportService;
import com.example.businessreport.web.form.ReportForm;
import com.example.businessreport.web.helper.ReportUtil;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 業務報告書作成 コントローラー
 */
@Controller
@RequestMapping
public class ReportWebController {

  // 業務報告書サービス
  private ReportService reportService;

  // コンストラクタ(サービスをBean登録)
  @Autowired
  public ReportWebController(ReportService reportService) {
    this.reportService = reportService;
  }

  // フォームから入力された空文字をnullに変換する      
  @InitBinder
  public void initBider(WebDataBinder binder) {
    binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
  }

  /**
   * 業務報告書一覧画面を表示する
   * @param model 画面間インタフェース
   * @return url 遷移先画面
   */
  @GetMapping("/report")
  public String showReportList(Model model) {
    // 業務報告書データを全件取得
    List<ReportFull> reportFull = reportService.findReportFullAll();

    // 画面へオブジェクトを渡す
    model.addAttribute("reportlist", reportFull);
    model.addAttribute("reportShow", ReportUtil.getReportShow("list"));

    // 画面へ遷移
    return "templete";
  }

  /**
   * 業務報告書作成画面を表示する
   * @param model 画面間インタフェース
   * @param form 業務報告フォーム
   * @return 遷移先画面
   */
  @PostMapping(value = "/report", params = "reportAddOpen")
  public String showReportCreate(Model model, @ModelAttribute ReportForm form) {

    // 画面へオブジェクトを渡す
    model.addAttribute("reportShow", ReportUtil.getReportShow("create"));

    // 画面へ遷移
    return "templete";
  }

  /**
   * 業務報告書参照画面を表示する
   * @param model 画面間インタフェース
   * @param id 報告書ID
   * @return 遷移先画面
   */
  @PostMapping(value = "/report", params = "reportBrowse")
  public String showReportBrowse(Model model, @RequestParam("reportBrowse") String id) {
    // 業務報告書データを全件取得
    ReportFull reportFull = reportService.findReportFullById(Integer.valueOf(id));

    // 画面へオブジェクトを渡す
    model.addAttribute("reportFull", reportFull);
    model.addAttribute("reportShow", ReportUtil.getReportShow("browse"));

    // 画面へ遷移
    return "templete";
  }

  /**
   * 業務報告書編集画面を表示する
   * @param model 画面間インタフェース
   * @param id 報告書ID
   * @return 遷移先画面
   */
  @PostMapping(value = "/report", params = "reportEdit")
  public String showReportEdit(Model model, @RequestParam("reportEdit") String id) {
    // 業務報告書データを取得
    ReportFull reportFull = reportService.findReportFullById(Integer.valueOf(id));

    // 業務報告書データをフォームへマッピング
    ReportForm form = reportService.mapReportFullToForm(reportFull);

    // 画面へオブジェクトを渡す
    model.addAttribute("reportForm", form);
    model.addAttribute("reportShow", ReportUtil.getReportShow("update"));

    // 画面へ遷移
    return "templete";
  }

  /**
   * 業務報告書参照、作成、編集画面を閉じる
   * @param model 画面間インタフェース
   * @return 遷移先画面
   */
  @PostMapping(value = "/report", params = "reportFormClose")
  public String closeReportAdd(Model model) {
    // 業務報告書一覧画面を表示する
    return showReportList(model);
  }

  /**
   * 業務報告書の最新情報を取得する
   * @param model 画面間インタフェース
   * @param form 業務報告フォーム
   * @param result 入力チェック結果
   * @return 遷移先画面
   */
  @PostMapping(value = "/report", params = "reportLatest")
  public String importReportLatest(Model model, @ModelAttribute ReportForm form) {
    // 業務報告書データ最新の1件を取得
    ReportFull reportFull = reportService.findReportFullLatest();
    // 報告書IDの新たに採番するため、クリア
    reportFull.setReportId(null);
    
    // 業務報告書データをフォームへマッピング
    form = reportService.mapReportFullToForm(reportFull);
    
    // 画面へオブジェクトを渡す
    model.addAttribute("reportForm", form);

    // 作成画面へ遷移
    return showReportCreate(model, form);
  }

  
  
  /**
   * 業務報告書を登録する
   * @param model 画面間インタフェース
   * @param form 業務報告フォーム
   * @param result 入力チェック結果
   * @return 遷移先画面
   */
  @PostMapping(value = "/report", params = "reportCreate")
  public String createReport(Model model, @ModelAttribute @Validated ReportForm form, BindingResult result) {
    // 入力チェック
    if (result.hasErrors()) {

      Map<String, String> errors = new HashMap<>();
      for (FieldError error : result.getFieldErrors()) {
        errors.put(error.getField(), error.getDefaultMessage());
      }

      for (ObjectError error : result.getGlobalErrors()) {
        errors.put(error.getObjectName(), error.getDefaultMessage());
      }
      
      System.out.println("errors: " + errors);

      
      
      model.addAttribute("reportShow", ReportUtil.getReportShow("create"));
      model.addAttribute("errorMsgAll", "入力に誤りがあります。");
      return "templete";
    }

    // 業務報告書 
    Report report = reportService.mapFormToReport(form);

    // 業務報告書を登録
    boolean resultReport = reportService.createReport(report);

    // 作業内容を登録
    boolean resultWorkDetails = false;
    if (resultReport) {
      resultWorkDetails = reportService.createWorkDetailAll(form.getWorkDetails(), report.getReportId());
    }

    // 一覧画面へリダイレクト
    return "redirect:/report";
  }

  /**
   * 業務報告書を更新する
   * @param model 画面間インタフェース
   * @param form 業務報告フォーム
   * @param result 入力チェック結果
   * @return 遷移先画面
   */
  @PostMapping(value = "/report", params = "reportUpdate")
  public String updateReport(Model model, @ModelAttribute @Validated ReportForm form, BindingResult result) {
    // 入力チェック
    if (result.hasErrors()) {
      model.addAttribute("reportShow", ReportUtil.getReportShow("update"));
      model.addAttribute("errorMsg  All", "入力に誤りがあります。");
      return "templete";
    }

    // フォームを業務報告書データへマッピング
    Report report = reportService.mapFormToReport(form);

    // 業務報告書を更新
    boolean resultReport = reportService.updateReport(report);

    // 作業内容を更新
    boolean resultWorkDetails = false;
    if (resultReport) {
      resultWorkDetails = reportService.updateWorkDetailAll(form.getWorkDetails(), report.getReportId());
    }

    // 一覧画面へリダイレクト
    return "redirect:/report";
  }

  /**
   * 業務報告書のExcelを作成する
   * @param model 画面間インタフェース
   * @param form 業務報告フォーム
   * @param response レスポンス情報
   * @return 遷移先画面
   */
  @PostMapping(value = "/report", params = "reportExcel")
  public void createExcelReport(@RequestParam("reportExcel") String id, HttpServletResponse response) {
    // 業務報告書データを取得
    ReportFull reportFull = reportService.findReportFullById(Integer.valueOf(id));

    // 業務報告書のExcelファイル名作成
    String path = "業務報告書_" +
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) +
        ".xlsx";

    // ①レスポンスヘッダーを設定(ブラウザでのExcelファイルのダウンロード指定)
    // ②Excelファイル名のOutputStemオブジェクト作成
    // これらの指定をすることでブラウザで自動的にExcelファイルがダウンロードされる
    ServletOutputStream stream = null;
    try {
      response.addHeader(
          "Content-Disposition",
          "attachment; filename*=UTF-8''" + URLEncoder.encode(path, StandardCharsets.UTF_8.name()));
      stream = response.getOutputStream();
    } catch (UnsupportedEncodingException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    // 業務報告書のExcel作成
    boolean resultExcel = reportService.createReportExcelFile(reportFull, stream);
  }

}
