package ua.com.juja.controller.actions;

import ua.com.juja.controller.AbstractAction;
import ua.com.juja.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateTableAction extends AbstractAction {
    public CreateTableAction(Service service) {
        super(service);
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableName = req.getParameter("tableName");
        int columnCount = Integer.parseInt(req.getParameter("columnCount"));

        req.setAttribute("tableName", tableName);
        req.setAttribute("columnCount", columnCount);

        goToJsp(req, resp, "WEB-INF/view/table.jsp");
    }

    @Override
    public boolean canProcess(String url) {
        return url.equals("/createTable");
    }
}
