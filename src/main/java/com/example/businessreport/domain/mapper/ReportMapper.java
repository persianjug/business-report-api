package com.example.businessreport.domain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.businessreport.domain.dto.ReportFull;
import com.example.businessreport.domain.model.Report;
import com.example.businessreport.domain.model.WorkDetail;



@Mapper
public interface ReportMapper {

  List<Report> findReportAll();

  Report findReportById(Integer reportId);

  List<WorkDetail> findWorkDetailAll();

  WorkDetail findWorkDetailById(Integer reportId, Integer workDetailId);

  List<ReportFull> findReportFullAll();

  ReportFull findReportFullById(Integer reportId);

  ReportFull findReportFullLatest();
  
  boolean createReport(Report report);
  
  boolean createWorkDetail(WorkDetail workDetail);

  boolean updateReport(Report report);

  boolean updateWorkDetail(WorkDetail workDetail);
  
}
