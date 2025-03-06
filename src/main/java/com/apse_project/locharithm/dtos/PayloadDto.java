package com.apse_project.locharithm.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PayloadDto {
    private String source_code;
    private int problem_id;
    private int language_id;
}
