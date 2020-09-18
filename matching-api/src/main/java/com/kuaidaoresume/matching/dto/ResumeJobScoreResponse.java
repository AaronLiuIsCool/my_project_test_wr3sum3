package com.kuaidaoresume.matching.dto;

import com.kuaidaoresume.common.api.BaseResponse;
import lombok.*;
import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ResumeJobScoreResponse extends BaseResponse  {
    private Collection<ResumeJobScoreDto> resumeJobScores;
}