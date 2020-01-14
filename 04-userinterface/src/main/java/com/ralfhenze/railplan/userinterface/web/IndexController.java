package com.ralfhenze.railplan.userinterface.web;

import com.ralfhenze.railplan.userinterface.web.views.IndexView;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    /**
     * Shows the landing page.
     */
    @GetMapping("/")
    @ResponseBody
    public String showLandingPage() {
        return new IndexView().getHtml();
    }
}
