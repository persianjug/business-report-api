package com.example.businessreport.domain.service.impl;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.businessreport.domain.dto.ReportFull;
import com.example.businessreport.domain.helper.ReportExcelUtil;
import com.example.businessreport.domain.mapper.ReportMapper;
import com.example.businessreport.domain.model.Report;
import com.example.businessreport.domain.model.WorkDetail;
import com.example.businessreport.domain.service.ReportService;
import com.example.businessreport.web.form.ReportForm;

/**
 * 業務報告書作成サービス 実装
 */
/**
 * 
 */
/**
 * 
 */
@Service
@Transactional
public class ReportServiceImpl implements ReportService {

  // リポジトリ
  private ReportMapper reportMapper;

  // モデルマッパー
  private ModelMapper modelMapper;

  // コンストラクタ(サービスをBean登録)
  @Autowired
  public ReportServiceImpl(ReportMapper reportMapper, ModelMapper modelMapper) {
    this.reportMapper = reportMapper;
    this.modelMapper = modelMapper;
  }

  /**
   * 業務報告書(Full) 全件取得
   * @param なし
   * @return 業務報告書(Full)データ(全件)
   */
  @Override
  public List<ReportFull> findReportFullAll() {
    return reportMapper.findReportFullAll();
  }

  /**
   * 業務報告書(Full) 1件取得
   * @param id 業務報告書ID
   * @return 業務報告書(Full)データ(1件)
   */
  @Override
  public ReportFull findReportFullById(Integer id) {
    return reportMapper.findReportFullById(id);
  }

  /**
   * 業務報告書(Full) 最新の1件取得
   * @return 業務報告書(Full)データ(1件)
   */
  @Override
  public ReportFull findReportFullLatest() {
    return reportMapper.findReportFullLatest();
  }

  /**
   * 作業内容データ 1件取得
   * @param reportId 業務報告書ID
   * @param workDetailId 作業ID
   * @return 作業内容データ(1件)
   */
  @Override
  public WorkDetail findWorkDetailById(Integer reportId, Integer workDetailId) {
    return reportMapper.findWorkDetailById(reportId, workDetailId);
  }

  /**
   * 業務報告書エンティティを業務報告書フォームへマッピングする
   * @param report
   * @return 業務報告書フォーム
   */
  @Override
  public ReportForm mapReportFullToForm(ReportFull reportFull) {
    ReportForm form = new ReportForm();
    modelMapper.map(reportFull, form);
    return form;
  }

  /**
   * 業務報告書フォームを業務報告書エンティティへマッピングする
   * @param report
   * @return 業務報告書フォーム
   */
  @Override
  public ReportFull mapFormToReportFull(ReportForm form) {
    ReportFull reportFull = new ReportFull();
    modelMapper.map(form, reportFull);
    return reportFull;
  }

  /**
   * 業務報告書エンティティを業務報告書フォームへマッピングする
   * @param report
   * @return 業務報告書フォーム
   */
  @Override
  public Report mapFormToReport(ReportForm form) {
    Report report = new Report();
    modelMapper.map(form, report);
    return report;
  }

  /**
   * 業務報告書 登録
   * @param form 業務報告書フォーム
   * @return true: 登録OK  false: 登録NG
   */
  @Override
  public boolean createReport(Report report) {
    return reportMapper.createReport(report);
  }

  /**
   * プロジェクト 登録
   * @param WorkDetail プロジェクト情報
   * @return true: 登録OK  false: 登録NG
   */
  @Override
  public boolean createWorkDetail(WorkDetail workDetail) {
    return reportMapper.createWorkDetail(workDetail);
  }

  /**
   * プロジェクト 登録 
   * @param workDetails プロジェクト情報
   * @return true: 登録OK  false: 登録NG
   */
  @Override
  public boolean createWorkDetailAll(List<WorkDetail> workDetails, Integer reportId) {
    Integer workDetailId = 0;

    for (WorkDetail workDetail : workDetails) {
      workDetailId += 1;
      workDetail.setWorkDetailId(workDetailId);
      workDetail.setReportId(reportId);
      boolean WorkDetailResult = reportMapper.createWorkDetail(workDetail);
      if (!WorkDetailResult) {
        return false;
      }
    }
    return true;
  }

  /**
   * 業務報告書 更新
   * @param form 業務報告書フォーム
   * @return true: 更新OK  false: 更新NG
   */
  @Override
  public boolean updateReport(Report report) {
    return reportMapper.updateReport(report);
  }

  /**
   * プロジェクト 更新 
   * @param WorkDetail プロジェクト情報
   * @return true: 更新OK  false: 更新NG
   */
  @Override
  public boolean updateWorkDetail(WorkDetail workDetail) {
    return reportMapper.updateWorkDetail(workDetail);
  }

  /**
   * プロジェクト 更新 
   * @param workDetails プロジェクト情報
   * @return true: 更新OK  false: 更新NG
   */
  @Override
  public boolean updateWorkDetailAll(List<WorkDetail> workDetails, Integer reportId) {
    Integer workDetailId = 0;

    for (WorkDetail WorkDetail : workDetails) {
      workDetailId += 1;
      WorkDetail.setWorkDetailId(workDetailId);
      WorkDetail.setReportId(reportId);
      boolean WorkDetailResult = reportMapper.updateWorkDetail(WorkDetail);
      if (!WorkDetailResult) {
        return false;
      }
    }
    return true;
  }

  /**
   * 業務報告書のExcelファイルを作成する
   * @param reportFull 業務報告書DTO
   * @param path 出力ファイル名
   * @return true: 作成OK  false: 作成NG
   */
  @Override
  public boolean createReportExcelFile(ReportFull reportFull, String path) {
    // ワークブック生成
    Workbook outworkbook = ReportExcelUtil.createReportWorkbook();

    // ワークシート生成     
    Sheet outsheet = ReportExcelUtil.createReportSheet(outworkbook, "業務報告書");

    // セルのスタイル生成
    Map<String, CellStyle> cellStyles = ReportExcelUtil.createCellStyles(outworkbook);
    
    // 報告書データ転記     
    ReportExcelUtil.setReportValues(outsheet, reportFull, cellStyles);

    // シート全体のスタイル設定
    ReportExcelUtil.setSheetStyle(outsheet);

    // Excel出力
    boolean result = ReportExcelUtil.writeToExcel(outworkbook, Path.of(path));
    
    return result;
  }
  
  /**
   * 業務報告書のExcelファイルを作成する
   * @param reportFull 業務報告書DTO
   * @param stream 出力ストリーム
   * @return true: 作成OK  false: 作成NG
   */
  @Override
  public boolean createReportExcelFile(ReportFull reportFull, OutputStream stream) {
    // ワークブック生成
    Workbook outworkbook = ReportExcelUtil.createReportWorkbook();

    // ワークシート生成     
    Sheet outsheet = ReportExcelUtil.createReportSheet(outworkbook, "業務報告書");

    // セルのスタイル生成
    Map<String, CellStyle> cellStyles = ReportExcelUtil.createCellStyles(outworkbook);
    
    // 報告書データ転記     
    ReportExcelUtil.setReportValues(outsheet, reportFull, cellStyles);

    // シート全体のスタイル設定
    ReportExcelUtil.setSheetStyle(outsheet);

    // Excel出力
    boolean result = ReportExcelUtil.writeToExcel(outworkbook, stream);
    
    return result;
  }
  
}
