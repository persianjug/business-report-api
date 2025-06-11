package com.example.businessreport.domain.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.businessreport.domain.dto.DetailFull;
import com.example.businessreport.domain.model.Detail;
import com.example.businessreport.domain.model.WorkDetail;

@Mapper
public interface DetailMapper {

  List<Detail> findDetailAll();

  Detail findDetailById(Integer reportId);

  List<WorkDetail> findWorkDetailAll();

  WorkDetail findWorkDetailById(Integer reportId, Integer workDetailId);

  List<DetailFull> findDetailFullAll();

  DetailFull findDetailFullById(Integer reportId);

  DetailFull findDetailFullLatest();
  
  boolean createDetail(Detail Detail);
  
  boolean createWorkDetail(WorkDetail workDetail);

  boolean updateDetail(Detail Detail);

  boolean updateWorkDetail(WorkDetail workDetail);
  
}
