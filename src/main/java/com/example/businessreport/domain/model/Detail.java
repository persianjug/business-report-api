package com.example.businessreport.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class Detail {
  // 報告書ID
  private Integer reportId;

  // タイトル
  private String title;

  // 対象期間(開始)
  private LocalDate periodStart;

  // 対象期間(終了)
  private LocalDate periodEnd;

  // エンド企業名
  private String endCompany;

  // 上位顧客名
  private String topCompany;

  // 業種
  private String industry;

  // 案件名
  private String topic;

  // 参画人数
  private Integer participantPeple;

  // 職場形態
  private String workingStyle;

  // 職場最寄り駅
  private String station;

  // 通勤時間（時）
  private Integer commutingTimeHour;

  // 通勤時間（分）
  private Integer commutingTimeMinute;

  // 案件参画日
  private LocalDate participantDate;

  // 主要技術
  private String mainTechnology;

  // データベース
  private String database;

  // ポジション
  private String position;

  // 全体状況
  private String fullSituation;
  
  // 今後の予定
  private String futurePlan;

  // 顧客の動向・営業情報等
  private String customerTrend;

  // 健康状態
  private String helthCondition;

  // 上司に相談
  private String consultation;

  // その他
  private String other;

  // 作成日
  private LocalDateTime createAt;

  // 更新日
  private LocalDateTime updateAt;
}
