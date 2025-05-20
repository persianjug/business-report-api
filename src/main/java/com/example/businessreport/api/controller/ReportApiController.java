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
import org.springframework.web.bind.annotation.RestController;

import com.example.businessreport.domain.dto.ReportFull;
import com.example.businessreport.domain.helper.ErrorUtil;
import com.example.businessreport.domain.model.Report;
import com.example.businessreport.domain.model.WorkDetail;
import com.example.businessreport.domain.service.ReportService;
import com.example.businessreport.domain.service.impl.ReportServiceImpl;
import com.example.businessreport.web.form.ReportForm;

@RestController
@RequestMapping("/api")
public class ReportApiController {

  // 業務報告書サービス
  private ReportService reportService;

  // コンストラクタ(サービスをBean登録)
  @Autowired
  public ReportApiController(ReportServiceImpl reportService) {
    this.reportService = reportService;
  }

  // 業務報告書データを全件取得
  @GetMapping("/reports")
  public ResponseEntity<?> getAllReports() {
    List<ReportFull> reportFull = reportService.findReportFullAll();
    return new ResponseEntity<>(reportFull, (reportFull != null ? HttpStatus.OK : HttpStatus.NOT_FOUND));
  }
  
  // 業務報告書データを取得
  @GetMapping("/report/{reportId}")
  public ResponseEntity<?> getReport(@PathVariable Integer reportId) {
    ReportFull reportFull = reportService.findReportFullById(reportId);
    return new ResponseEntity<>(reportFull, (reportFull != null ? HttpStatus.OK : HttpStatus.NOT_FOUND));
  }

  // 作業内容データを取得
  @GetMapping("/workdetail/{reportId}/{workDetailId}")
  public ResponseEntity<?> getWorkdetail(@PathVariable Integer reportId, @PathVariable Integer workDetailId) {
    WorkDetail workDetail = reportService.findWorkDetailById(reportId, workDetailId);
    return new ResponseEntity<>(workDetail, (workDetail != null ? HttpStatus.OK : HttpStatus.NOT_FOUND));
  }
  
  // 報告書データ追加  
  @PostMapping("/report/create")
  public ResponseEntity<?> createReport(@Validated @RequestBody ReportForm form, BindingResult result) {
    // 入力チェック
    if (result.hasErrors()) {
      return new ResponseEntity<>(ErrorUtil.getErrors(result), HttpStatus.BAD_REQUEST);
    }

    // 業務報告書 
    Report report = reportService.mapFormToReport(form);

    // フォームを業務報告書データへマッピング
    boolean resultReport = reportService.createReport(report);

    // 作業内容を登録
    boolean resultWorkDetails = false;
    if (resultReport) {
      resultWorkDetails = reportService.createWorkDetailAll(form.getWorkDetails(), report.getReportId());
    }

    // 正常のレスポンス
    return ResponseEntity.ok("Report create successfully");
  }

  
  // 報告書データ更新  
  @PostMapping("/report/update")
  public ResponseEntity<?> updateReport(@Validated @RequestBody ReportForm form, BindingResult result) {
    // 入力チェック
    if (result.hasErrors()) {
      return new ResponseEntity<>(ErrorUtil.getErrors(result), HttpStatus.BAD_REQUEST);
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
      return new ResponseEntity<>(ErrorUtil.getErrors(result), HttpStatus.BAD_REQUEST);
    }

    return null;
  }

}
