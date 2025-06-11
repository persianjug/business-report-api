package com.example.businessreport.domain.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = {RangeOfStartDateAndEndDateValidator.class})
@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RangeOfStartDateAndEndDate {
  String message() default "開始日と終了日は必須入力です。終了日は開始日と同日か、開始日より未来日を入力してください。";

  Class<?>[] groups() default {};
  Class<? extends Payload>[] payload() default {};

  // チェックに使用する項目  
  String startDate();
  String endDate();
  
  @Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
  @Retention(RetentionPolicy.RUNTIME)
  @Documented
  public @interface List {
    RangeOfStartDateAndEndDate[] value();
  }
}
