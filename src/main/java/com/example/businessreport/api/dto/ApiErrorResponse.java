package com.example.businessreport.api.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // nullをエラーレスポンスに含めない
public class ApiErrorResponse {
  private LocalDateTime timestamp;
  private int status;
  private String error;
  private String message;
  private String path;
  private Map<String, String> fieldErrors; // フィールド毎のエラー
  private List<String> globalErrors; // グローバルエラー
}
