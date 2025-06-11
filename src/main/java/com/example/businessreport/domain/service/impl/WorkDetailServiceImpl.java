package com.example.businessreport.domain.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.businessreport.domain.mapper.DetailMapper;
import com.example.businessreport.domain.model.WorkDetail;
import com.example.businessreport.domain.service.WorkDetailService;

@Service
public class WorkDetailServiceImpl implements WorkDetailService {
  private final DetailMapper detailMapper;
  private ModelMapper modelMapper;

  @Autowired
  public WorkDetailServiceImpl(DetailMapper detailMapper, ModelMapper modelMapper) {
    this.detailMapper = detailMapper;
    this.modelMapper = modelMapper;
  }

  @Override
  public WorkDetail findWorkDetailById(Integer reportId, Integer workDetailId) {
    return detailMapper.findWorkDetailById(reportId, workDetailId);
  }

  @Override
  public boolean createWorkDetail(WorkDetail workDetail) {
    return detailMapper.createWorkDetail(workDetail);
  }

  @Override
  public boolean createWorkDetails(List<WorkDetail> workDetails, Integer reportId) {
    // WorkDetailIdの採番はここで実施
    for (int i = 0; i < workDetails.size(); i++) {
      WorkDetail workDetail = workDetails.get(i);
      workDetail.setWorkDetailId(i + 1); // 1から始まるIDを割り当て
      workDetail.setReportId(reportId);
      boolean result = detailMapper.createWorkDetail(workDetail);
      if (!result) {
        return false; // 失敗したら即座にfalseを返す
      }
    }
    return true;
  }

  @Override
  public boolean updateWorkDetail(WorkDetail workDetail) {
    return detailMapper.updateWorkDetail(workDetail);
  }

  @Override
  public boolean updateWorkDetails(List<WorkDetail> workDetails, Integer reportId) {
    // WorkDetailIdの採番はここで実施
    for (int i = 0; i < workDetails.size(); i++) {
      WorkDetail workDetail = workDetails.get(i);
      workDetail.setWorkDetailId(i + 1); // 1から始まるIDを割り当て
      workDetail.setReportId(reportId);
      boolean result = detailMapper.updateWorkDetail(workDetail);
      if (!result) {
        return false; // 失敗したら即座にfalseを返す
      }
    }
    return true;
  }

}
