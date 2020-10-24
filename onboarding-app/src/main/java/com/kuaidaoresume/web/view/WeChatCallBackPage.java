package com.kuaidaoresume.web.view;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WeChatCallBackPage extends Page {

    private String errorMsg;

    // lombok inheritance workaround, details here: https://www.baeldung.com/lombok-builder-inheritance
    @Builder(builderMethodName = "childBuilder")
    public WeChatCallBackPage(String title,
                        String description,
                        String templateName,
                        String cssId,
                        String version,
                        String errorMsg) {
        super(title, description, templateName, cssId, version);
        this.errorMsg = errorMsg;
    }
}