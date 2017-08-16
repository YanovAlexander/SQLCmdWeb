package ua.com.juja.controller.web;

import ua.com.juja.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class InsertRecordAction extends AbstractAction{

    public InsertRecordAction(Service service) {
        super(service);
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String columnNames = req.getParameter("table");
        String substring = columnNames.substring(1, columnNames.length() - 1);
        String[] columnCountArray = substring.split(",");
        req.getSession().setAttribute("columnCount", columnCountArray.length);
        for (int i = 0; i < columnCountArray.length; i++) {
            req.getSession().setAttribute("columnName" + i, columnCountArray[i]);
        }
    }

    @Override
    public boolean canProcess(String url) {
        return url.equals("/insertRecord");
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Integer columnCount = (Integer) req.getSession().getAttribute("columnCount");
            String tableName = (String) req.getSession().getAttribute("tableName");
            Map<String, Object> data = new LinkedHashMap<>();

            for (int index = 0; index < columnCount; index++) {
                data.put((String) req.getSession().getAttribute("columnName" + index),
                        req.getParameter("columnValue" + index));
            }

            service.insert(getManagerDB(req, resp), tableName, data);
            goToJsp(req, resp, "success.jsp");
    }
}
