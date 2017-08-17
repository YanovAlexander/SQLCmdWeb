package ua.com.juja.controller.actions;

import ua.com.juja.controller.AbstractAction;
import ua.com.juja.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UpdateRecordAction extends AbstractAction {
    public UpdateRecordAction(Service service) {
        super(service);
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String columnNames = req.getParameter("table");
        String substring = columnNames.substring(1, columnNames.length() - 1);
        String[] columnCountArray = substring.split(",");
        String keyName = columnCountArray[0];
        String keyValue = req.getParameter("record");
        req.getSession().setAttribute("keyName", keyName);
        req.getSession().setAttribute("keyValue", keyValue);
        req.getSession().setAttribute("columnCount", columnCountArray.length);
        for (int i = 1; i < columnCountArray.length; i++) {
            req.getSession().setAttribute("columnName" + i, columnCountArray[i]);
        }
        goToJsp(req, resp, "WEB-INF/view/updateRecord.jsp");
    }

    @Override
    public boolean canProcess(String url) {
        return url.equals("/updateRecord");
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        Integer columnCount = (Integer) req.getSession().getAttribute("columnCount");
            String tableName = (String) req.getSession().getAttribute("tableName");
            Map<String, Object> data = new HashMap<>();

            for (int index = 1; index < columnCount; index++) {
                data.put((String) req.getSession().getAttribute("columnName" + index),
                        req.getParameter("columnValue" + index));
            }
            String keyName = (String) req.getSession().getAttribute("keyName");
            String keyValue = (String) req.getSession().getAttribute("keyValue");

            service.update(getManagerDB(req, resp), tableName, keyName, keyValue, data);
            goToJsp(req, resp, "WEB-INF/view/success.jsp");
    }
}
