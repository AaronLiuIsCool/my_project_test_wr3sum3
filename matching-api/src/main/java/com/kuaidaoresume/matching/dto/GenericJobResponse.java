package com.kuaidaoresume.matching.dto;

import com.kuaidaoresume.common.api.BaseResponse;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class GenericJobResponse extends BaseResponse {
    private JobDto jobDto;
}
