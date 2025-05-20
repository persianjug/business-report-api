package com.example.businessreport.domain.model;

import lombok.Data;

@Data
public class Project {
  // ID
  private Integer projectId;
  
  // 報告書ID
  private Integer reportId;

  // プロジェクト名
  private String name;

  // 状況
  private String situation;

  // 問題点・課題
  private String problem;
}
