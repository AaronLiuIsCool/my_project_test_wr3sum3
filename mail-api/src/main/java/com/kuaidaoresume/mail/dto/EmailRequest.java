package com.kuaidaoresume.mail.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.Map;

@Data
@Builder
@Getter
public class EmailRequest {

    @NotBlank(message = "Please provide an email")
    private String to;

    @NotBlank(message = "Please provide a subject")
    private String subject;

    private String from;

    private Map<String, Object> model;

    @NotBlank(message = "Please provide a valid body")
    @JsonProperty("html_body")
    private String htmlBody;
    private String name;
}
