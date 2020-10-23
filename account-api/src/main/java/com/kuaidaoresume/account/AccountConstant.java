package com.kuaidaoresume.account;

public class AccountConstant {
    public static final String SERVICE_NAME = "account-service";

    // Notifications template
    public static final String RESET_PASSWORD_TMPL = "<div>We received a request to reset the password on your account. To do so, click the below link. If you did not request this change, no action is needed. <br/> <a href=\"%s\">%s</a></div>";
    public static final String ACTIVATE_ACCOUNT_TMPL = "<div><p>Hi %s, and welcome to SmartResume.Careers!</p><a href=\"%s\">Please click here to finish setting up your account.</a></p></div><br/><br/><div>If you have trouble clicking on the link, please copy and paste this link into your browser: <br/><a href=\"%s\">%s</a></div>";
    public static final String CONFIRM_EMAIL_TMPL = "<div>Hi %s!</div>To confirm your new email address, <a href=\"%s\">please click here</a>.</div><br/><br/><div>If you have trouble clicking on the link, please copy and paste this link into your browser: <br/><a href=\"%s\">%s</a></div>";

    public static final String ACTIVATE_ACCOUNT_TMPL_CN_ZH = "@Oscar Please paste welcome body here.";
    public static final String RESET_PASSWORD_TMPL_CN_ZH = "@Oscar 这个是 reset password";
    public static final String CONFIRM_EMAIL_TMPL_CN_ZH = "这个是 password changes 之后的 ";

    public static final String GREETING_WORD_CN_ZH = "您好";

    // email title
    public static final String RESET_PASSWORD_EMAIL_TITLE = "Please reset your Smartresume password";
    public static final String RESET_PASSWORD_EMAIL_TITLE_CN_ZH = "请更改你的快刀简历账户密码";
    public static final String ACTIVATE_ACCOUNT_EMAIL_TITLE = "Please activate your Smartresume account";
    public static final String ACTIVATE_ACCOUNT_EMAIL_TITLE_CN_ZH = "欢迎使用快刀简历，请激活账户";
}
