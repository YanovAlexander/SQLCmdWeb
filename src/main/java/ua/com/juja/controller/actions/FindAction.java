package ua.com.juja.controller.actions;

import ua.com.juja.controller.AbstractAction;
import ua.com.juja.service.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FindAction extends AbstractAction {

    public FindAction(Service service) {
        super(service);
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableName = req.getParameter("table");
        req.getSession().setAttribute("tableName", tableName);
        req.setAttribute("table", service.find(getManagerDB(req, resp), tableName));
        goToJsp(req, resp, "WEB-INF/view/find.jsp");
    }

    @Override
    public boolean canProcess(String url) {
        return url.startsWith("/find");
    }
}
