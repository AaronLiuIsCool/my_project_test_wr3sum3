package com.kuaidaoresume.job.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TailorScoreDto {
    private String category;
    private String summaryEN;
    private String summaryZH;
    private String requirementZH;
    private String matchedDegree;
    private String otherDegree;
    private double score;
    private double fullMark;
    private Color color;
    private Map<String, Set<String>> extraInfo;

    public enum Color {
        RED, GREEN
    }
}
