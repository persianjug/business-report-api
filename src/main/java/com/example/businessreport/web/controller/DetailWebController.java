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

import com.example.businessreport.domain.dto.DetailFull;
import com.example.businessreport.domain.dto.form.DetailForm;
import com.example.businessreport.domain.model.Detail;
import com.example.businessreport.domain.service.impl.DetailServiceImpl;
import com.example.businessreport.web.helper.DetailUtil;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 業務報告書作成 コントローラー
 */
// @Controller
// @RequestMapping
public class DetailWebController {

  // 業務報告書サービス
  private DetailServiceImpl detailService;

  // コンストラクタ(サービスをBean登録)
  @Autowired
  public DetailWebController(DetailServiceImpl detailService) {
    this.detailService = detailService;
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
  public String showDetailList(Model model) {
    // 業務報告書データを全件取得
    List<DetailFull> detailFull = detailService.findDetailFullAll();

    // 画面へオブジェクトを渡す
    model.addAttribute("reportlist", detailFull);
    model.addAttribute("reportShow", DetailUtil.getDetailShow("list"));

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
  public String showDetailCreate(Model model, @ModelAttribute DetailForm form) {

    // 画面へオブジェクトを渡す
    model.addAttribute("reportShow", DetailUtil.getDetailShow("create"));

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
  public String showDetailBrowse(Model model, @RequestParam("reportBrowse") String id) {
    // 業務報告書データを全件取得
    DetailFull detailFull = detailService.findDetailFullById(Integer.valueOf(id));

    // 画面へオブジェクトを渡す
    model.addAttribute("reportFull", detailFull);
    model.addAttribute("reportShow", DetailUtil.getDetailShow("browse"));

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
  public String showDetailEdit(Model model, @RequestParam("reportEdit") String id) {
    // 業務報告書データを取得
    DetailFull detailFull = detailService.findDetailFullById(Integer.valueOf(id));

    // 業務報告書データをフォームへマッピング
    DetailForm form = detailService.mapDetailFullToForm(detailFull);

    // 画面へオブジェクトを渡す
    model.addAttribute("reportForm", form);
    model.addAttribute("reportShow", DetailUtil.getDetailShow("update"));

    // 画面へ遷移
    return "templete";
  }

  /**
   * 業務報告書参照、作成、編集画面を閉じる
   * @param model 画面間インタフェース
   * @return 遷移先画面
   */
  @PostMapping(value = "/report", params = "reportFormClose")
  public String closeDetailAdd(Model model) {
    // 業務報告書一覧画面を表示する
    return showDetailList(model);
  }

  /**
   * 業務報告書の最新情報を取得する
   * @param model 画面間インタフェース
   * @param form 業務報告フォーム
   * @param result 入力チェック結果
   * @return 遷移先画面
   */
  @PostMapping(value = "/report", params = "reportLatest")
  public String importDetailLatest(Model model, @ModelAttribute DetailForm form) {
    // 業務報告書データ最新の1件を取得
    DetailFull detailFull = detailService.findDetailFullLatest();
    // 報告書IDの新たに採番するため、クリア
    detailFull.setReportId(null);
    
    // 業務報告書データをフォームへマッピング
    form = detailService.mapDetailFullToForm(detailFull);
    
    // 画面へオブジェクトを渡す
    model.addAttribute("reportForm", form);

    // 作成画面へ遷移
    return showDetailCreate(model, form);
  }
  
  
  /**
   * 業務報告書を登録する
   * @param model 画面間インタフェース
   * @param form 業務報告フォーム
   * @param result 入力チェック結果
   * @return 遷移先画面
   */
  @PostMapping(value = "/report", params = "reportCreate")
  public String createDetail(Model model, @ModelAttribute @Validated DetailForm form, BindingResult result) {
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
      
      model.addAttribute("reportShow", DetailUtil.getDetailShow("create"));
      model.addAttribute("errorMsgAll", "入力に誤りがあります。");
      return "templete";
    }

    // 業務報告書 
    Detail detail = detailService.mapFormToDetail(form);

    // 業務報告書を登録
    boolean resultDetail = detailService.createDetail(detail);

    // 作業内容を登録
    boolean resultWorkDetails = false;
    if (resultDetail) {
      resultWorkDetails = detailService.createWorkDetailAll(form.getWorkDetails(), detail.getReportId());
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
  public String updateDetail(Model model, @ModelAttribute @Validated DetailForm form, BindingResult result) {
    // 入力チェック
    if (result.hasErrors()) {
      model.addAttribute("detailShow", DetailUtil.getDetailShow("update"));
      model.addAttribute("errorMsg  All", "入力に誤りがあります。");
      return "templete";
    }

    // フォームを業務報告書データへマッピング
    Detail detail = detailService.mapFormToDetail(form);

    // 業務報告書を更新
    boolean resultDetail = detailService.updateDetail(detail);

    // 作業内容を更新
    boolean resultWorkDetails = false;
    if (resultDetail) {
      resultWorkDetails = detailService.updateWorkDetailAll(form.getWorkDetails(), detail.getReportId());
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
  public void createExcelDetail(@RequestParam("reportExcel") String id, HttpServletResponse response) {
    // 業務報告書データを取得
    DetailFull detailFull = detailService.findDetailFullById(Integer.valueOf(id));

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
    boolean resultExcel = detailService.createDetailExcelFile(detailFull, stream);
  }

}
