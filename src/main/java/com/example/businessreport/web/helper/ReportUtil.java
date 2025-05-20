package com.example.businessreport.web.helper;

import java.util.HashMap;
import java.util.Map;

public class ReportUtil {

  private static final Map<String, ReportShow> showList = new HashMap<String, ReportShow>() {
    {
      put("list", new ReportShow(true, false, false, false));
      put("create", new ReportShow(false, true, false, false));
      put("browse", new ReportShow(false, false, true, false));
      put("update", new ReportShow(false, false, false, true));
    }
  };

  // インスタンス生成禁止
  private ReportUtil() {
  }

  
  public static ReportShow getReportShow(String keyword) {
    return showList.get(keyword); 
  }
}
