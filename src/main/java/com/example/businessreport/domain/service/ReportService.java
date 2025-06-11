package com.example.businessreport.domain.service;

import java.util.List;

import com.example.businessreport.domain.dto.DetailFull;
import com.example.businessreport.domain.model.Detail;

/**
 * 業務報告書サービス インタフェース
 */
public interface ReportService {

    /**
     * 全ての業務報告書（Full）を取得します。
     * @return 業務報告書（Full）のリスト
     */
    List<DetailFull> findAllReports();

    /**
     * 指定されたIDの業務報告書（Full）を取得します。
     * @param reportId 業務報告書ID
     * @return 業務報告書（Full）データ
     */
    DetailFull findReportById(Integer reportId);

    /**
     * 最新の業務報告書（Full）を取得します。
     * @return 最新の業務報告書（Full）データ
     */
    DetailFull findLatestReport();
        

    /**
     * 業務報告書を新規作成します。
     * @param detail 作成する業務報告書エンティティ
     * @return 作成成功の場合true、失敗の場合false
     */
    boolean createReport(Detail detail);

    /**
     * 業務報告書を更新します。
     * @param detail 更新する業務報告書エンティティ
     * @return 更新成功の場合true、失敗の場合false
     */
    boolean updateReport(Detail detail);
}