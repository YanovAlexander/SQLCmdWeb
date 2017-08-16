package ua.com.juja.controller.web;

import ua.com.juja.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteRecordAction extends AbstractAction {
    public DeleteRecordAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/deleteRecord");
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String record = req.getParameter("record");
            String tableName = (String) req.getSession().getAttribute("tableName");
            service.deleteRecord(getManagerDB(req, resp), tableName, record);
            goToJsp(req, resp, "success.jsp");
    }
}
