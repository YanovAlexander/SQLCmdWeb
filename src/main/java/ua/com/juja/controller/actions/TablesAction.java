package ua.com.juja.controller.actions;

import ua.com.juja.controller.AbstractAction;
import ua.com.juja.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TablesAction extends AbstractAction {
    public TablesAction(Service service) {
        super(service);
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("tables", service.tables(getManagerDB(req, resp)));
        goToJsp(req, resp, "WEB-INF/view/tables.jsp");
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/tables");
    }
}