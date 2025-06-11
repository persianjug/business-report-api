package com.example.businessreport.domain.helper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.businessreport.domain.dto.DetailFull;
import com.example.businessreport.domain.dto.form.DetailForm;
import com.example.businessreport.domain.model.Detail;

/**
 * 業務報告書関連DTOとエンティティのマッピングヘルパー
 */
@Component
public class ReportMapperHelper {

    private final ModelMapper modelMapper;

    public ReportMapperHelper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    /**
     * 業務報告書フォームを業務報告書Full DTOへマッピングします。
     * @param form 業務報告書フォーム
     * @return 業務報告書Full DTO
     */
    public DetailFull mapFormToDetailFull(DetailForm form) {
        return modelMapper.map(form, DetailFull.class);
    }

    /**
     * 業務報告書Full DTOを業務報告書フォームへマッピングします。
     * @param detailFull 業務報告書Full DTO
     * @return 業務報告書フォーム
     */
    public DetailForm mapDetailFullToForm(DetailFull detailFull) {
        return modelMapper.map(detailFull, DetailForm.class);
    }

    /**
     * 業務報告書フォームを業務報告書エンティティへマッピングします。
     * @param form 業務報告書フォーム
     * @return 業務報告書エンティティ
     */
    public Detail mapFormToDetail(DetailForm form) {
        return modelMapper.map(form, Detail.class);
    }
}