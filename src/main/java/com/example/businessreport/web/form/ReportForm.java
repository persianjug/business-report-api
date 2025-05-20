package com.example.businessreport.web.form;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.businessreport.domain.model.WorkDetail;
import com.example.businessreport.web.validator.RangeOfStartDateAndEndDate;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@RangeOfStartDateAndEndDate(startDate = "periodStart", endDate = "periodEnd")
public class ReportForm {
  // 報告書ID
  private Integer reportId;
  
  // 対象期間(開始)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate periodStart;

  // 対象期間(終了)
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate periodEnd;

  // エンド企業名
  @NotNull(message = "エンド企業名は必須項目です。")
  private String endCompany;

  // 上位顧客名
  @NotNull(message = "上位顧客名は必須項目です。")
  private String topCompany;

  // 業種
  @NotNull(message = "業種は必須項目です。")
  private String industry;

  // 案件名
  @NotNull(message = "案件名は必須項目です。")
  private String topic;

  // 参画人数
  @NotNull(message = "参画人数は必須項目です。")
  //  @Pattern(regexp = "[0-9]*", message = "数値で入力してください。")
  private Integer participantPeple;

  // 勤務形態
  @NotNull(message = "勤務形態は必須項目です。")
  private String workingStyle;

  // 職場最寄駅
  @NotNull(message = "職場最寄駅は必須項目です。")
  private String station;

  // 通勤時間
  @NotNull(message = "通勤時間は必須項目です。")
  @Digits(integer = 2, fraction = 2, message = "整数{integer}桁、少数{fraction}桁以内の数値で"
      + "入力してください。")
  private Double commutingTime;

  // 案件参画日
  @NotNull(message = "案件参画日は必須項目です。")
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private LocalDate participantDate;

  // 主要技術
  @NotNull(message = "主要技術は必須項目です。")
  private String mainTechnology;

  // データベース
  @NotNull(message = "データベースは必須項目です。")
  private String database;

  // ポジション
  @NotNull(message = "ポジションは必須項目です。")
  private String position;

  // 全体状況
  private String fullSituation;

  // 作業内容
  private List<WorkDetail> workDetails = initWorkDetails();
  
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

  private List<WorkDetail> initWorkDetails() {
    List<WorkDetail> workDetails = new ArrayList<>();
    for (int i = 1; i <= 5; i++) {
      WorkDetail workDetail = new WorkDetail();
      workDetail.setWorkDetailId(i);
      workDetails.add(workDetail);
    }
    return workDetails;
  }

}
