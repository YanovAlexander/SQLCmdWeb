package ua.com.juja.controller.web;

import ua.com.juja.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteDatabase extends AbstractAction {

    public DeleteDatabase(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/deleteDatabase");
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String databaseName = req.getParameter("database");
        service.deleteDatabase(getManagerDB(req, resp), databaseName);
        goToJsp(req, resp, "success.jsp");
    }
}
