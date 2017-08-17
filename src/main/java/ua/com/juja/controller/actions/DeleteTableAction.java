package ua.com.juja.controller.actions;

import ua.com.juja.controller.AbstractAction;
import ua.com.juja.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteTableAction extends AbstractAction {

    public DeleteTableAction(Service service) {
        super(service);
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/deleteTable");
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String tableName = req.getParameter("table");
        service.deleteTable(getManagerDB(req, resp), tableName);
        goToJsp(req, resp, "WEB-INF/view/success.jsp");
    }
}
