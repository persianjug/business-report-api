package com.example.businessreport.domain.model;

import lombok.Data;

@Data
public class WorkDetail {
  // 作業ID
  private Integer workDetailId;
  
  // 報告書ID
  private Integer reportId;

  // 作業名
  private String workName;

  // 状況
  private String situation;

  // 問題点・課題
  private String issuesProblems;
}
