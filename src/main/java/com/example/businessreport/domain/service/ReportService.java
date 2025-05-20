package com.example.businessreport.domain.service;

import java.io.OutputStream;
import java.util.List;

import com.example.businessreport.domain.dto.ReportFull;
import com.example.businessreport.domain.model.Report;
import com.example.businessreport.domain.model.WorkDetail;
import com.example.businessreport.web.form.ReportForm;

/**
 * 報告書作成サービス インタフェース
 */
public interface ReportService {

  List<ReportFull> findReportFullAll();

  ReportFull findReportFullById(Integer id);

  ReportFull findReportFullLatest();

  WorkDetail findWorkDetailById(Integer reportId, Integer workDetailId);
  
  ReportFull mapFormToReportFull(ReportForm form);

  ReportForm mapReportFullToForm(ReportFull reportFull);

  Report mapFormToReport(ReportForm form);
  
  boolean createReport(Report report);
  
  boolean createWorkDetail(WorkDetail WorkDetail);

  boolean createWorkDetailAll(List<WorkDetail> WorkDetails, Integer reportId);

  boolean updateReport(Report report);
  
  boolean updateWorkDetail(WorkDetail WorkDetail);

  boolean updateWorkDetailAll(List<WorkDetail> WorkDetails, Integer reportId);

  boolean createReportExcelFile(ReportFull reportFull, String path);

  boolean createReportExcelFile(ReportFull reportFull, OutputStream stream);

}
