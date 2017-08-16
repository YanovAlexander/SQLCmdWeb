package ua.com.juja.controller.web;


import ua.com.juja.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class HelpAction extends AbstractAction {
    public HelpAction(Service service) {
        super(service);
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        goToJsp(req, resp, "help.jsp");
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/help");
    }
}
