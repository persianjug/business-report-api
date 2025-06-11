package com.example.businessreport.api.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import com.example.businessreport.api.dto.ApiErrorResponse;
import com.example.businessreport.domain.dto.DetailFull;
import com.example.businessreport.domain.dto.form.DetailForm;
import com.example.businessreport.domain.helper.ErrorUtil;
import com.example.businessreport.domain.helper.ReportMapperHelper;
import com.example.businessreport.domain.model.Detail;
import com.example.businessreport.domain.model.WorkDetail;
import com.example.businessreport.domain.service.DetailService;
import com.example.businessreport.domain.service.ReportService;
import com.example.businessreport.domain.service.WorkDetailService;

@RestController
@RequestMapping("/api")
public class DetailApiControllerV2 {

  // 業務報告書サービス
  private final ReportService reportService;
  private final WorkDetailService workDetailService;
  private final ReportMapperHelper reportMapperHelper;

  // コンストラクタ(サービスをBean登録)
  @Autowired
  public DetailApiControllerV2(
      ReportService reportService,
      WorkDetailService workDetailService,
      ReportMapperHelper reportMapperHelper) {
    this.reportService = reportService;
    this.workDetailService = workDetailService;
    this.reportMapperHelper = reportMapperHelper;
  }

  /**
   * 業務報告書データを全件取得します。
   * 
   * @return 業務報告書データのリストを含むResponseEntity
   */
  @GetMapping("/reports")
  public ResponseEntity<List<DetailFull>> getAllReports() {
    // データがない場合は空のリストを返すか、204 No Contentを返すなど、APIの仕様に従う
    // ここでは空リストを返し、常に200 OKとする
    List<DetailFull> detailFull = reportService.findAllReports();
    return ResponseEntity.ok(detailFull);
  }

  /**
   * 業務報告書データを取得
   * 
   * @param reportId 報告書ID
   * @return 業務報告書データを含むResponseEntity
   */
  @GetMapping("/report/{reportId}")
  public ResponseEntity<?> getReport(@PathVariable Integer reportId) {
    DetailFull detailFull = reportService.findReportById(reportId);

    // 404 Not Found
    if (detailFull == null)
      return ResponseEntity.notFound().build();

    // 200 OK
    return ResponseEntity.ok(detailFull);
  }

  /**
   * 作業内容データを取得
   * 
   * @param reportId     報告書ID
   * @param workDetailId 作業ID
   * @return 作業内容データを含むResponseEntity
   */
  @GetMapping("/workdetail/{reportId}/{workDetailId}")
  public ResponseEntity<?> getWorkdetail(@PathVariable Integer reportId, @PathVariable Integer workDetailId) {
    WorkDetail workDetail = workDetailService.findWorkDetailById(reportId, workDetailId);

    // 404 Not Found
    if (workDetail == null)
      return ResponseEntity.notFound().build();

    // 200 OK
    return ResponseEntity.ok(workDetail);
  }

  // 報告書データ追加
  /**
   * 業務報告書を新規登録します。
   * 
   * @param form    業務報告書フォーム
   * @param result  バリデーション結果
   * @param request Webリクエスト
   * @return
   */
  @PostMapping("/reports")
  public ResponseEntity<?> createReport(
      @Validated @RequestBody DetailForm form,
      BindingResult result,
      WebRequest request) {

    // 入力チェック
    if (result.hasErrors()) {
      ApiErrorResponse errorResponse = ErrorUtil.createApiErrorResponse(result, HttpStatus.BAD_REQUEST, request);
      return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // 業務報告書エンティティへマッピング
    Detail detail = reportMapperHelper.mapFormToDetail(form);

    // 業務報告書を登録
    boolean detailCreationSuccess = reportService.createReport(detail);

    // 作業内容を登録
    boolean workDetailsCreationSuccess = false;
    if (detailCreationSuccess && detail.getReportId() != null) {
      workDetailsCreationSuccess = workDetailService.createWorkDetails(form.getWorkDetails(), detail.getReportId());
    }

    // 登録失敗時のエラーレスポンス
    if (!detailCreationSuccess || !workDetailsCreationSuccess) {
      ApiErrorResponse errorResponse = ApiErrorResponse.builder()
          .timestamp(LocalDateTime.now())
          .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
          .message("Failed to create report and/or work details.")
          .path(request.getDescription(false)) // リクエストURIを取得
          .build();
      return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 正常のレスポンス
    // 作成されたリソースのURIをLocationヘッダーに含めることも検討
    // 例: return ResponseEntity.created(URI.create("/api/report/" +
    // detail.getReportId())).body("Report created successfully");
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(Map.of("message", "Report created successfully", "reportId", detail.getReportId()));
  }

  /**
   * 業務報告書を更新します。
   * 
   * @param form    業務報告書フォーム
   * @param result  バリデーション結果
   * @param request Webリクエスト
   * @return 処理結果を示すResponseEntity
   */
  @PutMapping("/report/{reportId}")
  public ResponseEntity<?> updateReport(
      @PathVariable Integer reportId,
      @Validated @RequestBody DetailForm form,
      BindingResult result,
      WebRequest request) {

    // パスパラメータの報告書IDとボディ内の報告書IDが一致するか確認する（セキュリティと一貫性のため）
    if (form.getReportId() != null && !form.getReportId().equals(reportId)) {
      ApiErrorResponse errorResponse = ApiErrorResponse.builder()
          .timestamp(LocalDateTime.now())
          .status(HttpStatus.BAD_REQUEST.value())
          .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
          .message(
              "Report ID in path (" + reportId + ") does not match ID in request body (" + form.getReportId() + ").")
          .path(request.getDescription(false))
          .build();
      return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // フォームに報告書IDを設定
    // これにより、mapFormToDetail(form)の際に、確実に正しいreportIdがDetailオブジェクトにマッピングされる
    form.setReportId(reportId);

    // 更新対象の業務報告書が存在するか確認 (存在しない場合は404 Not Found)
    if (reportService.findReportById(reportId) == null) {
      return ResponseEntity.notFound().build();
    }

    // 入力チェック
    if (result.hasErrors()) {
      ApiErrorResponse errorResponse = ErrorUtil.createApiErrorResponse(result, HttpStatus.BAD_REQUEST, request);
      return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // フォームを業務報告書データへマッピング
    Detail detail = reportMapperHelper.mapFormToDetail(form);

    // 業務報告書を更新
    boolean detailUpdateSuccess = reportService.updateReport(detail);

    // 作業内容を更新
    boolean workDetailsUpdateSuccess = false;
    if (detailUpdateSuccess) {
      workDetailsUpdateSuccess = workDetailService.updateWorkDetails(form.getWorkDetails(), detail.getReportId());
    }

    if (!detailUpdateSuccess || !workDetailsUpdateSuccess) {
      // 更新失敗時のエラーレスポンス
      ApiErrorResponse errorResponse = ApiErrorResponse.builder()
          .timestamp(LocalDateTime.now())
          .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
          .error(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
          .message("Failed to update report and/or work details.")
          .path(request.getDescription(false))
          .build();
      return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 正常のレスポンス
    return ResponseEntity.ok(Map.of("message", "Report updated successfully", "reportId", detail.getReportId()));
  }
}
