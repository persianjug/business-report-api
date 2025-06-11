package com.example.businessreport.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.businessreport.domain.dto.DetailFull;
import com.example.businessreport.domain.dto.form.DetailForm;
import com.example.businessreport.domain.dto.form.ReportForm;
import com.example.businessreport.domain.helper.ErrorUtilV1;
import com.example.businessreport.domain.model.Detail;
import com.example.businessreport.domain.model.WorkDetail;
import com.example.businessreport.domain.service.impl.DetailServiceImpl;

// @RestController
@RequestMapping("/api")
public class DetailApiController {

  // 業務報告書サービス
  private DetailServiceImpl detailService;

  // コンストラクタ(サービスをBean登録)
  @Autowired
  public DetailApiController(DetailServiceImpl detailService) {
    this.detailService = detailService;
  }

  // 業務報告書データを全件取得
  @GetMapping("/reports")
  public ResponseEntity<?> getAllReports() {
    List<DetailFull> reportFull = detailService.findDetailFullAll();
    return new ResponseEntity<>(reportFull, (reportFull != null ? HttpStatus.OK : HttpStatus.NOT_FOUND));
  }
  
  // 業務報告書データを取得
  @GetMapping("/report/{reportId}")
  public ResponseEntity<?> getReport(@PathVariable Integer reportId) {
    DetailFull reportFull = detailService.findDetailFullById(reportId);
    return new ResponseEntity<>(reportFull, (reportFull != null ? HttpStatus.OK : HttpStatus.NOT_FOUND));
  }

  // 作業内容データを取得
  @GetMapping("/workdetail/{reportId}/{workDetailId}")
  public ResponseEntity<?> getWorkdetail(@PathVariable Integer reportId, @PathVariable Integer workDetailId) {
    WorkDetail workDetail = detailService.findWorkDetailById(reportId, workDetailId);
    return new ResponseEntity<>(workDetail, (workDetail != null ? HttpStatus.OK : HttpStatus.NOT_FOUND));
  }
  
  // 報告書データ追加  
  @PostMapping("/report/create")
  public ResponseEntity<?> createReport(@Validated @RequestBody DetailForm form, BindingResult result) {
    // 入力チェック
    if (result.hasErrors()) {
      return new ResponseEntity<>(ErrorUtilV1.getErrors(result), HttpStatus.BAD_REQUEST);
    }

    // 業務報告書
    Detail detail = detailService.mapFormToDetail(form);

    // フォームを業務報告書データへマッピング
    boolean resultDetail = detailService.createDetail(detail);

    // 作業内容を登録
    boolean resultWorkDetails = false;
    if (resultDetail) {
      resultWorkDetails = detailService.createWorkDetailAll(form.getWorkDetails(), detail.getReportId());
    }

    // 正常のレスポンス
    return ResponseEntity.ok("Report create successfully");
  }

  
  // 報告書データ更新  
  @PostMapping("/report/update")
  public ResponseEntity<?> updateReport(@Validated @RequestBody DetailForm form, BindingResult result) {
    // 入力チェック
    if (result.hasErrors()) {
      return new ResponseEntity<>(ErrorUtilV1.getErrors(result), HttpStatus.BAD_REQUEST);
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

    // 正常のレスポンス
    return ResponseEntity.ok("Report update successfully");
  }

  
  @GetMapping("/report/fielderrors")
  public ResponseEntity<?> getFieldErrors(@Validated ReportForm form, BindingResult result) {
    form = new ReportForm();

    if (result.hasErrors()) {
      return new ResponseEntity<>(result.getFieldErrors(), HttpStatus.BAD_REQUEST);
    }

    return null;
  }

  @GetMapping("/report/allerrors")
  public ResponseEntity<?> getAllErrors(@Validated ReportForm form, BindingResult result) {
    form = new ReportForm();

    if (result.hasErrors()) {
      return new ResponseEntity<>(result.getAllErrors(), HttpStatus.BAD_REQUEST);
    }

    return null;
  }

  @GetMapping("/report/globalerrors")
  public ResponseEntity<?> getGlobalErrors(@Validated ReportForm form, BindingResult result) {
    form = new ReportForm();

    if (result.hasErrors()) {
      return new ResponseEntity<>(result.getGlobalErrors(), HttpStatus.BAD_REQUEST);
    }

    return null;
  }

  @GetMapping("/report/errors")
  public ResponseEntity<?> getErrors(@Validated ReportForm form, BindingResult result) {
    form = new ReportForm();

    if (result.hasErrors()) {
      return new ResponseEntity<>(ErrorUtilV1.getErrors(result), HttpStatus.BAD_REQUEST);
    }

    return null;
  }

}
