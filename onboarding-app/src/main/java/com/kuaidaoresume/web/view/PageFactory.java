package com.kuaidaoresume.web.view;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.kuaidaoresume.common.config.KuaidaoresumeProps;
import com.kuaidaoresume.web.props.AppProps;

@Component
public class PageFactory {

    @Autowired
    KuaidaoresumeProps kuaidaoresumeProps;

    @Autowired
    AppProps appProps;

    public Page buildHomePage() {
        return Page.builder()
                .title("Kuaidaoresume - Change the way, you think about employment.")
                .description("Kuaidaoresume Home Page")
                .templateName("home")
                .version(kuaidaoresumeProps.getDeployEnv())
                .build();
    }

    public LoginPage buildLoginPage() {
        return LoginPage.childBuilder()
                .title("Kuaidaoresume Log in")
                .description("Log in to Kuaidao resume to start your career. All you’ll need is your email and password.")
                .templateName("login")
                .cssId("login")
                .version(kuaidaoresumeProps.getDeployEnv())
                .build();
    }

    public Page buildConfirmPage() {
        return Page.builder()
                .title("Open your email and click on the confirmation link!")
                .description("Check your email and click the link for next steps")
                .templateName("confirm")
                .cssId("confirm")
                .version(kuaidaoresumeProps.getDeployEnv())
                .build();
    }

    public Page buildSignupPage() {
        return Page.builder()
                .title("Kuaidaoresume Sign up")
                .description("Sign Up for Aaron's 1 Day Free Kuaidaoresume Trial\", " +
                    "Description: \"Sign up for a 1 day free trial of Kuaidaoresume today to create your resume online.")
                .templateName("signup")
                .cssId("sign-up")
                .version(kuaidaoresumeProps.getDeployEnv())
                .build();
    }
    
    public ActivatePage buildActivatePage() {
        return ActivatePage.childBuilder()
                .title("Activate your Kuaidaoresume account")
                .templateName("activate")
                .cssId("activate")
                .version(kuaidaoresumeProps.getDeployEnv())
                .build();
    }

    public Page buildCompletionPage() {
        return Page.builder()
                .title("You are all done!")
                .description("Please go ahead create your first resume!")
                .templateName("complete")
                .cssId("complete")
                .version(kuaidaoresumeProps.getDeployEnv())
                .build();
    }

    public ResetPage buildResetPage() {
        return ResetPage.childBuilder()
                .title("Password Reset")
                .cssId("sign-up")
                .templateName("reset")
                .description("Reset the password for your Kuaidaoresume account.")
                .recaptchaPublic(appProps.getRecaptchaPublic())
                .version(kuaidaoresumeProps.getDeployEnv())
                .build();
    }

    public Page buildResetConfirmPage() {
        return Page.builder()
                .title("Please check your email for a reset link!")
                .description("Check your email and click the link for next steps.")
                .templateName("confirm")
                .cssId("confirm")
                .version(kuaidaoresumeProps.getDeployEnv())
                .build();

    }

    public ConfirmResetPage buildConfirmResetPage() {
        return ConfirmResetPage.childBuilder()
                .title("Reset your Kuaidaoresume password")
                .description("Follow steps to reset your Kuaidaoresume password.")
                .cssId("sign-up")
                .templateName("confirmreset")
                .version(kuaidaoresumeProps.getDeployEnv())
                .build();
    }
}