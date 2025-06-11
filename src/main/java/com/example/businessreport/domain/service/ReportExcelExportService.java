package com.example.businessreport.domain.service;

import java.io.OutputStream;

import com.example.businessreport.domain.dto.DetailFull;

/**
 * 業務報告書Excelエクスポートサービス インタフェース
 */
public interface ReportExcelExportService {
  /**
   * 業務報告書のExcelファイルを指定されたパスに作成します。
   * 
   * @param detailFull 業務報告書DTO
   * @param path       出力ファイルパス
   * @return 作成成功の場合true、失敗の場合false
   */
  boolean exportReportToExcel(DetailFull detailFull, String path);

  /**
   * 業務報告書のExcelファイルを出力ストリームに作成します。
   * 
   * @param detailFull 業務報告書DTO
   * @param stream     出力ストリーム
   * @return 作成成功の場合true、失敗の場合false
   */
  boolean exportReportToExcel(DetailFull detailFull, OutputStream stream);
}