package com.example.businessreport.web.helper;

import lombok.Data;

@Data
public class DetailShow {
  private boolean showReportList;
  private boolean showReportCreate;
  private boolean showReportBrowse;
  private boolean showReportUpdate;

  public DetailShow(
      boolean showReportList,
      boolean showReportCreate,
      boolean showReportBrowse,
      boolean showReportUpdate) {

    this.showReportList = showReportList;
    this.showReportCreate = showReportCreate;
    this.showReportBrowse = showReportBrowse;
    this.showReportUpdate = showReportUpdate;
  }
}
