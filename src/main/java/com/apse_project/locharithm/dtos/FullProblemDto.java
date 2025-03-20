package com.apse_project.locharithm.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FullProblemDto {
   private String problemName;
   private String problemDescription;
   private String inputConstraints;
   private String outputConstraints;
   private String inputFormat;
   private String outputFormat;
   private String sampleInput1;
   private String sampleOutput1;
   private String sampleInput2;
   private String sampleOutput2;
   private String testCaseInput1;
   private String testCaseOutput1;
   private String testCaseInput2;
   private String testCaseOutput2;
   private String testCaseInput3;
   private String testCaseOutput3;
   private String testCaseInput4;
   private String testCaseOutput4;
   private String testCaseInput5;
   private String testCaseOutput5;
}
