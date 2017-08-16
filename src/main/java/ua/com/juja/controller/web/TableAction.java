package ua.com.juja.controller.web;

import ua.com.juja.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TableAction extends AbstractAction {

    public TableAction(Service service) {
        super(service);
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        goToJsp(req, resp, "createTable.jsp");
    }

    @Override
    public boolean canProcess(String url) {
        return url.equals("/table");
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String tableName = req.getParameter("tableName");
        int columnCount = Integer.parseInt(req.getParameter("columnCount"));

        List<String> columnParameters = new ArrayList<>();
        for (int i = 1; i < columnCount; i++) {
            columnParameters.add(req.getParameter("columnName" + i));
        }
        service.createTable(getManagerDB(req, resp), tableName, columnParameters);
        goToJsp(req, resp, "success.jsp");
    }
}
