package ua.com.juja.controller.web;

import ua.com.juja.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateDatabaseAction extends AbstractAction {
    public CreateDatabaseAction(Service service) {
        super(service);
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        goToJsp(req, resp, "createDatabase.jsp");
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/createDatabase");
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String database = req.getParameter("databaseName");
        service.createDatabase(getManagerDB(req, resp), database);
        goToJsp(req, resp, "success.jsp");
    }
}
