package com.example.businessreport.domain.service.impl;

import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.example.businessreport.domain.dto.DetailFull;
import com.example.businessreport.domain.helper.DetailExcelUtil;
import com.example.businessreport.domain.service.ReportExcelExportService;

public class ReportExcelExportServiceImpl implements ReportExcelExportService {

  @Override
  public boolean exportReportToExcel(DetailFull detailFull, String path) {
    // ワークブック生成
    Workbook outworkbook = DetailExcelUtil.createDetailWorkbook();

    // ワークシート生成
    Sheet outsheet = DetailExcelUtil.createDetailSheet(outworkbook, "業務報告書");

    // セルのスタイル生成
    Map<String, CellStyle> cellStyles = DetailExcelUtil.createCellStyles(outworkbook);

    // 報告書データ転記
    DetailExcelUtil.setDetailValues(outsheet, detailFull, cellStyles);

    // シート全体のスタイル設定
    DetailExcelUtil.setSheetStyle(outsheet);

    // Excel出力
    return DetailExcelUtil.writeToExcel(outworkbook, Path.of(path));
  }

  @Override
  public boolean exportReportToExcel(DetailFull detailFull, OutputStream stream) {
    // ワークブック生成
    Workbook outworkbook = DetailExcelUtil.createDetailWorkbook();

    // ワークシート生成
    Sheet outsheet = DetailExcelUtil.createDetailSheet(outworkbook, "業務報告書");

    // セルのスタイル生成
    Map<String, CellStyle> cellStyles = DetailExcelUtil.createCellStyles(outworkbook);

    // 報告書データ転記
    DetailExcelUtil.setDetailValues(outsheet, detailFull, cellStyles);

    // シート全体のスタイル設定
    DetailExcelUtil.setSheetStyle(outsheet);

    // Excel出力
    return DetailExcelUtil.writeToExcel(outworkbook, stream);
  }
}