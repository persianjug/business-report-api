package com.example.businessreport.domain.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.businessreport.domain.dto.DetailFull;
import com.example.businessreport.domain.mapper.DetailMapper;
import com.example.businessreport.domain.model.Detail;
import com.example.businessreport.domain.service.ReportService;

@Service
public class ReportServiceImpl implements ReportService {
  private final DetailMapper detailMapper;
  private ModelMapper modelMapper;

  @Autowired
  public ReportServiceImpl(DetailMapper detailMapper, ModelMapper modelMapper) {
    this.detailMapper = detailMapper;
    this.modelMapper = modelMapper;
  }

  @Override
  public List<DetailFull> findAllReports() {
    return detailMapper.findDetailFullAll();
  }

  @Override
  public DetailFull findReportById(Integer reportId) {
    return detailMapper.findDetailFullById(reportId);
  }

  @Override
  public DetailFull findLatestReport() {
    return detailMapper.findDetailFullLatest();
  }

  @Override
  public boolean createReport(Detail detail) {
    return detailMapper.createDetail(detail);
  }

  @Override
  public boolean updateReport(Detail detail) {
    return detailMapper.updateDetail(detail);
  }
}
