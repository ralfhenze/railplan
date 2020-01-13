package com.ralfhenze.railplan.userinterface.web;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ControllerHelper {

    public static String redirectTo(final String url, final HttpServletResponse response) {
        try {
            response.sendRedirect(url);
        } catch (IOException ex) {
            return "";
        }

        return "";
    }
}
