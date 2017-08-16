package ua.com.juja.controller.web;

import ua.com.juja.model.DatabaseManager;
import ua.com.juja.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ConnectAction extends AbstractAction {
    public ConnectAction(Service service) {
        super(service);
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        goToJsp(req, resp, "connect.jsp");
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/connect");
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) {
        String databaseName = req.getParameter("dbname");
        String userName = req.getParameter("username");
        String password = req.getParameter("password");

        try {

            DatabaseManager manager = service.connect(databaseName, userName, password);
            req.getSession().setAttribute("db_manager", manager);
            resp.sendRedirect(resp.encodeRedirectURL("menu"));

        } catch (Exception e) {
           forwardToError(req, resp, e);
        }
    }
}
