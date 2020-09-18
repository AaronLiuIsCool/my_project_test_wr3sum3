package com.kuaidaoresume.web.view;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPage extends Page {
    private boolean denied;
    private String recaptchaPublic;

    @Builder(builderMethodName = "childBuilder")
    public ResetPage(String title, String description, String templateName, String cssId, String version, boolean denied, String recaptchaPublic) {
        super(title, description, templateName, cssId, version);
        this.denied = denied;
        this.recaptchaPublic = recaptchaPublic;
    }
}
