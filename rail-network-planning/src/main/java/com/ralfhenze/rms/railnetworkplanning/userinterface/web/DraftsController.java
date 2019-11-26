package com.ralfhenze.rms.railnetworkplanning.userinterface.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;

@Controller
public class DraftsController {

    @GetMapping("/drafts")
    public String drafts(Model model) {
        model.addAttribute("drafts", Arrays.asList(1, 2, 3));

        return "drafts";
    }
}
