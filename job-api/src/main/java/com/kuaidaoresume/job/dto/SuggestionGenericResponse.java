package com.kuaidaoresume.job.dto;

import com.kuaidaoresume.common.api.BaseResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SuggestionGenericResponse extends BaseResponse {
    private SuggestionDto suggestion;
}