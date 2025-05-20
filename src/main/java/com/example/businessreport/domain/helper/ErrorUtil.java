package com.example.businessreport.domain.helper;

import java.util.HashMap;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

public final class ErrorUtil {
  
  private ErrorUtil() {
  }
  
  public static Map<String, String> getErrors(BindingResult result) {
    Map<String, String> errors = new HashMap<>();

    for (FieldError error : result.getFieldErrors()) {
      errors.put(error.getField(), error.getDefaultMessage());
    }

    for (ObjectError error : result.getGlobalErrors()) {
      errors.put(error.getObjectName(), error.getDefaultMessage());
    }

    return errors;
    
  }
  
}
