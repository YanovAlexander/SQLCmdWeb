package ua.com.juja.controller.web;

import ua.com.juja.model.DatabaseManager;
import ua.com.juja.service.Service;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractAction implements Action {
    protected Service service;

    public AbstractAction(Service service) {
        this.service = service;
    }

    protected DatabaseManager getManagerDB(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");

        if (manager != null) {
            return  manager;
        } else {
            resp.sendRedirect(resp.encodeRedirectURL("connect"));
            return DatabaseManager.NULL;
        }
    }

    protected void goToJsp(HttpServletRequest req, HttpServletResponse resp, String path) throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, resp);
    }

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //NOP
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        //NOP
    }

    protected void forwardToError(ServletRequest req, ServletResponse resp, Exception e) {
        req.setAttribute("message", e.getMessage());
        try {
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        } catch (ServletException | IOException e1) {
            e.printStackTrace();
        }
    }
}

