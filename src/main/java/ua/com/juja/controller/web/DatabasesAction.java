package ua.com.juja.controller.web;

import ua.com.juja.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DatabasesAction extends AbstractAction {

    public DatabasesAction(Service service) {
        super(service);
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("databases", service.databases(getManagerDB(req, resp)));
        goToJsp(req, resp, "databases.jsp");
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/databases");
    }
}
