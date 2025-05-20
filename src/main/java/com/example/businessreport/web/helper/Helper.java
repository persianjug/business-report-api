package com.example.businessreport.web.helper;

import com.example.businessreport.web.form.ReportForm;

public class Helper {

	private Helper() {
	}
	
	public static ReportForm createDate(ReportForm form) {
		ReportForm result = new ReportForm();
		result.setTopCompany("テスト企業");
		
		return result;
	}
}
