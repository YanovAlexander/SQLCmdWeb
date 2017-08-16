package ua.com.juja.controller.web;

import ua.com.juja.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MenuAction extends AbstractAction {

    public MenuAction(Service service) {
        super(service);
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            req.setAttribute("items", service.commandsList());
            goToJsp(req, resp, "menu.jsp");
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/menu") || url.equals("/");
    }
}
