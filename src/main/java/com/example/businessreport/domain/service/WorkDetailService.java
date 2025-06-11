package com.example.businessreport.domain.service;

import java.util.List;

import com.example.businessreport.domain.model.WorkDetail;

/**
 * 作業詳細サービス インタフェース
 */
public interface WorkDetailService {

    /**
     * 指定された業務報告書IDと作業詳細IDで作業詳細を取得します。
     * @param reportId 業務報告書ID
     * @param workDetailId 作業詳細ID
     * @return 作業詳細データ
     */
    WorkDetail findWorkDetailById(Integer reportId, Integer workDetailId);

    /**
     * 作業詳細を新規作成します。
     * @param workDetail 作成する作業詳細エンティティ
     * @return 作成成功の場合true、失敗の場合false
     */
    boolean createWorkDetail(WorkDetail workDetail);

    /**
     * 複数の作業詳細を新規作成します。
     * @param workDetails 作成する作業詳細エンティティのリスト
     * @param reportId 関連する業務報告書ID
     * @return 作成成功の場合true、失敗の場合false
     */
    boolean createWorkDetails(List<WorkDetail> workDetails, Integer reportId);

    /**
     * 作業詳細を更新します。
     * @param workDetail 更新する作業詳細エンティティ
     * @return 更新成功の場合true、失敗の場合false
     */
    boolean updateWorkDetail(WorkDetail workDetail);

    /**
     * 複数の作業詳細を更新します。
     * @param workDetails 更新する作業詳細エンティティのリスト
     * @param reportId 関連する業務報告書ID
     * @return 更新成功の場合true、失敗の場合false
     */
    boolean updateWorkDetails(List<WorkDetail> workDetails, Integer reportId);
}