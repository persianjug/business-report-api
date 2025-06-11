package com.example.businessreport.domain.service;

import java.io.OutputStream;
import java.util.List;

import com.example.businessreport.domain.dto.DetailFull;
import com.example.businessreport.domain.dto.form.DetailForm;
import com.example.businessreport.domain.model.Detail;
import com.example.businessreport.domain.model.WorkDetail;

/**
 * 報告書作成サービス インタフェース
 */
public interface DetailService {

  List<DetailFull> findDetailFullAll();

  DetailFull findDetailFullById(Integer id);

  DetailFull findDetailFullLatest();

  WorkDetail findWorkDetailById(Integer reportId, Integer workDetailId);
  
  DetailFull mapFormToDetailFull(DetailForm form);

  DetailForm mapDetailFullToForm(DetailFull DetailFull);

  Detail mapFormToDetail(DetailForm form);
  
  boolean createDetail(Detail detail);
  
  boolean createWorkDetail(WorkDetail WorkDetail);

  boolean createWorkDetailAll(List<WorkDetail> WorkDetails, Integer reportId);

  boolean updateDetail(Detail detail);
  
  boolean updateWorkDetail(WorkDetail WorkDetail);

  boolean updateWorkDetailAll(List<WorkDetail> WorkDetails, Integer reportId);

  boolean createDetailExcelFile(DetailFull DetailFull, String path);

  boolean createDetailExcelFile(DetailFull DetailFull, OutputStream stream);

}
