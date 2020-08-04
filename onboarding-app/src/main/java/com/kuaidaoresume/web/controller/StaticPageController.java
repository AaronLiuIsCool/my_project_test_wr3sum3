package com.kuaidaoresume.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.kuaidaoresume.web.view.Constant;
import com.kuaidaoresume.web.view.PageFactory;

@Controller
public class StaticPageController {

    @Autowired
    private PageFactory pageFactory;

    @RequestMapping(value = "/sign-up")
    public String getSignup(Model model) {
        model.addAttribute(Constant.ATTRIBUTE_NAME_PAGE, pageFactory.buildSignupPage());
        return Constant.VIEW_SIGNUP;
    }
}