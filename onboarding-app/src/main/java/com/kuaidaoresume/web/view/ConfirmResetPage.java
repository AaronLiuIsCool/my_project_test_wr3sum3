package com.kuaidaoresume.web.view;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConfirmResetPage extends Page {
    private String errorMessage;
    private String token;

    @Builder(builderMethodName = "childBuilder")
    public ConfirmResetPage(String title,
                            String description,
                            String templateName,
                            String cssId,
                            String version,
                            String errorMessage,
                            String token) {
        super(title, description, templateName, cssId, version);
        this.errorMessage = errorMessage;
        this.token = token;
    }
}