package com.example.businessreport.web.validator;

import java.time.LocalDate;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RangeOfStartDateAndEndDateValidator implements ConstraintValidator<RangeOfStartDateAndEndDate, Object> {
  // 開始日  
  private String startDate;
  // 終了日  
  private String endDate;

  @Override
  public void initialize(RangeOfStartDateAndEndDate constraintAnnotation) {
    this.startDate = constraintAnnotation.startDate();
    this.endDate = constraintAnnotation.endDate();
  }

  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    // 開始日、終了日取得
    BeanWrapper wrapper = new BeanWrapperImpl(value);
    LocalDate startDateValue = (LocalDate) wrapper.getPropertyValue(startDate);
    LocalDate endDateValue = (LocalDate) wrapper.getPropertyValue(endDate);

    // 開始日、終了日どちらかはNG
    if (startDateValue == null || endDateValue == null) {
      return false;
    }

    // 開始日と終了日が同日か、終了日が開始日の未来日であるか    
    return startDateValue.isEqual(endDateValue) ||
        startDateValue.isBefore(endDateValue);
  }

}
