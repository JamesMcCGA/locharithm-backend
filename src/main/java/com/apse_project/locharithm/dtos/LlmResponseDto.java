package com.apse_project.locharithm.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LlmResponseDto {
    private String problem_description;
    private String input_format;
    private String output_format;
    private String sample_input_output;
    private String test_cases;
}
