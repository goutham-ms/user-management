package com.merchant.user_onboarding.configuration;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class BackSlashHandlingConfig  implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        if (requestURI.contains("\\") && !requestURI.contains("\\\\")) {
            System.err.println("Invalid request received: single backslash found in URI " + requestURI);

            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST,
                    "Invalid characters detected in the request URL.");
            return;
        }

        String correctedURI = requestURI.replace("\\\\", "\\");

        if (!requestURI.equals(correctedURI)) {
            request.getRequestDispatcher(correctedURI).forward(request, response);
            return;
        }

        chain.doFilter(request, response);
    }
}