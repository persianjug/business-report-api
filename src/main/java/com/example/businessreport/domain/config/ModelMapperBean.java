package com.example.businessreport.domain.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@ComponentScan
@Component
public class ModelMapperBean {

  @Bean
  public ModelMapper modelMapper() {
    ModelMapper modelMapper = new ModelMapper();
    // マッピングの厳格(完全一致)に設定
    modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    // 型安全のマッチ設定
    modelMapper.getConfiguration().setFullTypeMatchingRequired(true);
    return modelMapper;
  }

}
