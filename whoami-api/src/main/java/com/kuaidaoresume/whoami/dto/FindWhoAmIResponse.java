package com.kuaidaoresume.whoami.dto;

import lombok.*;
import com.kuaidaoresume.common.api.BaseResponse;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class FindWhoAmIResponse extends BaseResponse {
    private IAmDto iAm;
}
