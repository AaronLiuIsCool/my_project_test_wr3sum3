package com.kuaidaoresume.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchingList {
    private List<MatchingDto> matchings;
    private int limit;
    private int offset;
}
