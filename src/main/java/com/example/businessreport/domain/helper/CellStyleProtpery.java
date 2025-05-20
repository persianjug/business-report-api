package com.example.businessreport.domain.helper;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

public class CellStyleProtpery {
  private CellStyle cellStyle;
  private Font font;
  private String fontFamily;
  private short fontSize;
  private short fontColor;
  private HorizontalAlignment horizontalAlignment;
  private VerticalAlignment verticalAlignment;
  private boolean wrapText;
  private BorderStyle borderStyle;
  private short borderColor;

  // 標準のスタイル  
  public CellStyleProtpery(
      CellStyle cellStyle,
      Font font,
      String fontFamily,
      short fontSize,
      short fontColor,
      HorizontalAlignment horizontalAlignment,
      VerticalAlignment verticalAlignment,
      BorderStyle borderStyle,
      short borderColor) {
    this.cellStyle = cellStyle;
    this.font = font;
    this.fontFamily = fontFamily;
    this.fontSize = fontSize;
    this.fontColor = fontColor;
    this.horizontalAlignment = horizontalAlignment;
    this.verticalAlignment = verticalAlignment;
    this.borderStyle = borderStyle;
    this.borderColor = borderColor;
    this.font = font;
    setFontInfo();
    setAlignmentInfo();
    setBorderInfo();
  }

  // 標準のスタイル  
  public CellStyleProtpery(
      CellStyle cellStyle,
      Font font,
      String fontFamily,
      short fontSize,
      short fontColor,
      HorizontalAlignment horizontalAlignment,
      VerticalAlignment verticalAlignment,
      boolean wrapText,
      BorderStyle borderStyle,
      short borderColor) {
    this.cellStyle = cellStyle;
    this.font = font;
    this.fontFamily = fontFamily;
    this.fontSize = fontSize;
    this.fontColor = fontColor;
    this.horizontalAlignment = horizontalAlignment;
    this.verticalAlignment = verticalAlignment;
    this.wrapText = wrapText;
    this.borderStyle = borderStyle;
    this.borderColor = borderColor;
    this.font = font;
    setFontInfo();
    setAlignmentInfo();
    setBorderInfo();
    setWrapText();
  }

  
  public CellStyle getCellStyle() {
    return cellStyle;
  }

  /**
   * フォント情報を設定
   */
  private void setFontInfo() {
    Font font = this.font;
    font.setFontName(fontFamily);
    font.setFontHeightInPoints(fontSize);
    font.setColor(fontColor);
    cellStyle.setFont(font);
  }

  /**
   * テキストの位置情報を設定 
   */
  private void setAlignmentInfo() {
    cellStyle.setAlignment(horizontalAlignment);
    cellStyle.setVerticalAlignment(verticalAlignment);
  }

  /**
   * 罫線を引く
   */
  private void setBorderInfo() {
    cellStyle.setBorderTop(borderStyle);
    cellStyle.setBorderBottom(borderStyle);
    cellStyle.setBorderLeft(borderStyle);
    cellStyle.setBorderRight(borderStyle);
    cellStyle.setTopBorderColor(borderColor);
    cellStyle.setBottomBorderColor(borderColor);
    cellStyle.setLeftBorderColor(borderColor);
    cellStyle.setLeftBorderColor(borderColor);
  }

  /**
   * セル内の改行有無
   */
  private void setWrapText() {
    cellStyle.setWrapText(wrapText);
  }
  
}
