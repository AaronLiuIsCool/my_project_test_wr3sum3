package com.kuaidaoresume.web.controller;

import com.github.structlog4j.ILogger;
import com.github.structlog4j.SLoggerFactory;
import com.kuaidaoresume.web.props.AppProps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.kuaidaoresume.account.client.AccountClient;
import com.kuaidaoresume.account.dto.PasswordResetRequest;
import com.kuaidaoresume.common.api.BaseResponse;
import com.kuaidaoresume.common.auth.AuthConstant;
import com.kuaidaoresume.common.error.ServiceException;
import com.kuaidaoresume.web.service.HelperService;
import com.kuaidaoresume.web.view.Constant;
import com.kuaidaoresume.web.view.PageFactory;
import com.kuaidaoresume.web.view.ResetPage;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

@Controller
public class ResetController {

    public static final String PASSWORD_RESET_PATH = "/password-reset";

    static final ILogger logger = SLoggerFactory.getLogger(ResetController.class);

    @Autowired
    AppProps appProps;

    @Autowired
    private PageFactory pageFactory;

    @Autowired
    private AccountClient accountClient;

    @Autowired
    private HelperService helperService;

    @RequestMapping(value = PASSWORD_RESET_PATH)
    public String passwordReset(@RequestParam(value="email", required = false) String email,
                                Model model,
                                HttpServletRequest request) {
        // TODO google recaptcha
        // reference : https://www.google.com/recaptcha
        if(!isCaptchaValid(appProps.getRecaptchaPrivate(), request.getParameter("g-recaptcha-response"))) {
            helperService.logException(logger, new Exception(), "Non prod env isCaptachValid false test");
            return Constant.VIEW_RESET;
        }

        if (HelperService.isPost(request)) {
            PasswordResetRequest passwordResetRequest = PasswordResetRequest.builder()
                    .email(email)
                    .build();
            BaseResponse baseResponse = null;
            try {
                baseResponse = accountClient.requestPasswordReset(
                        AuthConstant.AUTHORIZATION_WWW_SERVICE, passwordResetRequest);
            } catch (Exception ex) {
                String errMsg = "Failed password reset";
                helperService.logException(logger, ex, errMsg);
                throw new ServiceException(errMsg, ex);
            }
            if (!baseResponse.isSuccess()) {
                helperService.logError(logger, baseResponse.getMessage());
                //throw new ServiceException(baseResponse.getMessage());
                //user email does not exist
                ResetPage resetPage = pageFactory.buildResetPage();
                resetPage.setDenied(true);
                model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, resetPage);
                return Constant.VIEW_RESET;
            }
            logger.info("Initiating password reset"); // log the info
            model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, pageFactory.buildResetConfirmPage());
            return Constant.VIEW_CONFIRM;
        }

        model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, pageFactory.buildResetPage());
        return Constant.VIEW_RESET;
    }

    /**
     * Validates Google reCAPTCHA V2 or Invisible reCAPTCHA.
     * @param secretKey Secret key (key given for communication between your site and Google)
     * @param response reCAPTCHA response from client side. (g-recaptcha-response)
     * @return true if validation successful, false otherwise.
     */
    private synchronized boolean isCaptchaValid(String secretKey, String response) {
        try {
            String url = "https://www.google.com/recaptcha/api/siteverify",
                    params = "secret=" + secretKey + "&response=" + response;

            HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
            http.setDoOutput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded; charset=UTF-8");
            OutputStream out = http.getOutputStream();
            out.write(params.getBytes("UTF-8"));
            out.flush();
            out.close();

            InputStream res = http.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(res, "UTF-8"));

            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            JSONObject json = new JSONObject(sb.toString());
            res.close();

            return json.getBoolean("success");
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return false;
    }
}
