package com.example.businessreport.domain.helper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import com.example.businessreport.api.dto.ApiErrorResponse;

public final class ErrorUtil {

  private ErrorUtil() {
  }

  /**
   * BindingResultからAPIエラーレスポンスを生成します。
   * 
   * @param result  BindingResultオブジェクト
   * @param status  HTTPステータスコード
   * @param request WebRequestオブジェクト
   * @return ApiErrorResponseオブジェクト
   */
  public static ApiErrorResponse createApiErrorResponse(BindingResult result, HttpStatus status, WebRequest request) {
    Map<String, String> fieldErrors = null;
    if (result.hasFieldErrors()) {
      fieldErrors = result.getFieldErrors().stream()
          .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
    }

    List<String> globalErrors = null;
    if (result.hasGlobalErrors()) {
      globalErrors = result.getGlobalErrors().stream()
          .map(ObjectError::getDefaultMessage)
          .collect(Collectors.toList());
    }

    String path = null;
    if (request instanceof ServletWebRequest) {
      path = ((ServletWebRequest) request).getRequest().getRequestURI();
    }

    return ApiErrorResponse.builder()
        .timestamp(LocalDateTime.now())
        .status(status.value())
        .error(status.getReasonPhrase())
        .message("Validation failed.") // 汎用的なエラーメッセージ
        .path(path)
        .fieldErrors(fieldErrors)
        .globalErrors(globalErrors)
        .build();
  }

  // 既存のgetErrorsメソッドは、フィールドエラーのMapを返すのみなので、必要に応じて保持するか統合する
  public static Map<String, String> getErrors(BindingResult result) {
    return result.getFieldErrors().stream()
        .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));
  }
}
