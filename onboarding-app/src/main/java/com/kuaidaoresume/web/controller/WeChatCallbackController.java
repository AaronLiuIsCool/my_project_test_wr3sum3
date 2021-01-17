package com.kuaidaoresume.web.controller;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.account.client.AccountClient;
import com.kuaidaoresume.account.dto.AccountDto;
import com.kuaidaoresume.account.dto.GenericAccountResponse;
import com.kuaidaoresume.account.dto.WechatAccountDto;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.auth.Sessions;
import com.kuaidaoresume.common.env.EnvConfig;
import com.kuaidaoresume.common.env.EnvConstant;
import com.kuaidaoresume.web.props.AppProps;
import com.kuaidaoresume.web.service.HelperService;
import com.kuaidaoresume.web.service.WeChatService;
import com.kuaidaoresume.web.view.Constant;
import com.kuaidaoresume.web.view.PageFactory;
import com.kuaidaoresume.web.view.WeChatCallBackPage;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class WeChatCallbackController {
    static final ILogger logger = SLoggerFactory.getLogger(WeChatCallbackController.class);

    @Autowired
    private PageFactory pageFactory;

    @Autowired
    private EnvConfig envConfig;

    @Autowired
    private AppProps appProps;

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private HelperService helperService;

    @Autowired
    private WeChatService weChatService;

    private static final String WECHAT_ERROR_CODE = "errcode";
    private static final String WECHAT_OPEN_ID = "openid";
    private static final String WECHAT_UNION_ID = "unionid";

    @RequestMapping(value = "/wechat-callback")
    public String callback(@RequestParam(value="code") String code,
                           @RequestParam(value="state", required=false) String state,
                           Model model,
                           HttpServletRequest request,
                           HttpServletResponse response) {

        WeChatCallBackPage weChatCallBackPage = pageFactory.buildWeChatCallBackPage();

        /**
         * Example response
         * Success {
         *      "access_token":"38_lOJI9d1G9wV6hd0w7Ek6K455Bn5X57S3Sax64zwkVQtQkm3hhROjbtvs7JzkaOJRlhaDl_wHAjwTSUeXGGKoasQ5rf-4IO58PuLe0Y35zGo",
         *      "expires_in":7200,
         *      "refresh_token":"38_WALYStT36v5dNx09ckL5tow2NbSQrYzHdAdWtM_Xbf36033PlE63uon7YFvCw3O8PSb8alB6Cozcc-bQOuzaJuxBeUm-Ie6quHQ6c4N4wfA",
         *      "openid":"oeoCI58eDvUWSL8R-G0KQAPeb5vc",
         *      "scope":"snsapi_login",
         *      "unionid":"o5JOX59suUDR_yblqbmkdRMTYAt8"
         * }
         * Error {"errcode":40163,"errmsg":"code been used, hints: [ req_id: sFHEhnXlRa-R ]"}
         */
        JSONObject wechatResponse = weChatService.authByWeChat(code);
        try {
            if (wechatResponse.has(WECHAT_ERROR_CODE)) {
                switch (wechatResponse.getInt(WECHAT_ERROR_CODE)) { // TODO: need to support multi language
                    case 40163:
                        weChatCallBackPage.setErrorMsg("微信确认码被使用, 请重试...");
                        break;
                    default:
                        weChatCallBackPage.setErrorMsg("微信服务异常, 请稍后重试...");
                        break;
                }
            } else {
                final String openId = wechatResponse.getString(WECHAT_OPEN_ID);
                final String unionId = wechatResponse.getString(WECHAT_UNION_ID);
                AccountDto account = null;

                // check if we have account with this open id
                GenericAccountResponse accountResponse;
                try {
                    accountResponse = accountClient.getAccountByOpenId(AuthConstant.AUTHORIZATION_WWW_SERVICE, openId);
                    if (accountResponse != null) {
                        if (accountResponse.isSuccess()) {
                            account = accountResponse.getAccount();
                        } else {
                            helperService.logError(logger, accountResponse.getMessage());
                        }
                    }
                } catch (FeignException e) {
                    if (e.status() == HttpServletResponse.SC_NOT_FOUND) {
                        logger.debug("No Account associated to the WeChat account found", e, e.getMessage());
                    } else {
                        helperService.logException(logger, e, String.format("accountClient.getAccountByOpenId() failed with openId %s", openId));
                        throw e;
                    }
                } catch (Exception e) {
                    helperService.logException(logger, e, String.format("accountClient.getAccountByOpenId() failed with openId %s", openId));
                    throw e;
                }

                if (account == null) {
                    // if no account found we create the account
                    WechatAccountDto wechatAccountDto = new WechatAccountDto();
                    wechatAccountDto.setOpenid(openId);
                    wechatAccountDto.setUnionid(unionId);

                    accountResponse = accountClient.createAccountByWechat(AuthConstant.AUTHORIZATION_WWW_SERVICE, wechatAccountDto);
                    if (accountResponse == null || !accountResponse.isSuccess()) {
                        if (accountResponse != null) logger.error("Wechat account create failed", accountResponse.getMessage());
                        else logger.error("Wechat account create failed");

                        weChatCallBackPage.setErrorMsg("服务异常, 请稍后重试...");
                    } else {
                        account = accountResponse.getAccount();
                    }
                }

                if (account!= null) {
                    Sessions.loginUser(account.getId(),
                            account.isSupport(),
                            false, // TODO: Add rememberMe
                            appProps.getSigningSecret(),
                            envConfig.getExternalApex(),
                            response);

                    helperService.trackEventAsync(account.getId(), "login");
                    helperService.syncUserAsync(account.getId());

                    String scheme = envConfig.getScheme();
                    String url = HelperService.buildUrl(scheme, "app." + envConfig.getExternalApex());
                    return "redirect:" + url;
                }
            }
        } catch(JSONException exception) {
            logger.error("Wechat page error", exception);
        }

        model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, weChatCallBackPage);
        return Constant.VIEW_WECHAT_CALLBACK;
    }
}
