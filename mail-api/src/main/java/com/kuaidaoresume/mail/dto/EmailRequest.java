package com.kuaidaoresume.mail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.Map;
import java.util.HashMap;

@Data
@Builder
@Getter
public class EmailRequest {

    @NotBlank(message = "Please provide an email")
    private String to;

    @NotBlank(message = "Please provide a subject")
    private String subject;

    @NotBlank(message = "Please provide an from")
    private String from;

    @Builder.Default()
    private Map<String, Object> model = new HashMap<>();

    @NotBlank(message = "Please provide a valid body")
    @JsonProperty("html_body")
    private String htmlBody;
    private String name;
}
