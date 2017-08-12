package ua.com.juja.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ua.com.juja.model.DatabaseManager;
import ua.com.juja.service.Service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {

    @Autowired
    private Service service;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
                config.getServletContext());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);

        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");

        if (action.startsWith("/connect")) {
            if (manager == null) {
                goToJsp(req, resp, "connect.jsp");
            } else {
                resp.sendRedirect(resp.encodeRedirectURL("menu"));
            }
            return;
        }

        if (manager == null) {
            resp.sendRedirect(resp.encodeRedirectURL("connect"));
            return;
        }

        if (action.startsWith("/menu") || action.equals("/")) {
            req.setAttribute("items", service.commandsList());
            goToJsp(req, resp, "menu.jsp");

        } else if (action.startsWith("/help")) {
            goToJsp(req, resp, "help.jsp");

        } else if (action.startsWith("/tables")) {
            req.setAttribute("tables", service.tables(manager));
            goToJsp(req, resp, "tables.jsp");

        } else if (action.startsWith("/find")) {
            String tableName = req.getParameter("table");
            req.getSession().setAttribute("table", tableName);
            req.setAttribute("table", service.find(manager, tableName));
            goToJsp(req, resp, "find.jsp");

        } else if (action.startsWith("/clear")) {
            String tableName = req.getParameter("table");
            service.clear(manager, tableName);
            goToJsp(req, resp, "success.jsp");

        } else if (action.startsWith("/record")) {
            String table = req.getParameter("table");
            String[] splitTable = table.split("\\[");
            String[] splitPrimaryKey = splitTable[2].split(",");
            String keyName = splitPrimaryKey[0];
            String record = req.getParameter("record");
            String[] splitRecord = record.split("\\[");
            String[] splitKeyValue = splitRecord[1].split(",");
            String keyValue = splitKeyValue[0];
            String tableName = (String) req.getSession().getAttribute("table");
            service.deleteRecord(manager, tableName, keyName, keyValue);
            goToJsp(req, resp, "success.jsp");

        } else if (action.startsWith("/deleteTable")) {
            String tableName = req.getParameter("table");
            service.deleteTable(manager, tableName);
            goToJsp(req, resp, "success.jsp");

        } else {
            goToJsp(req, resp, "error.jsp");
        }
    }

    private void goToJsp(HttpServletRequest req, HttpServletResponse resp, String path) throws ServletException, IOException {
        req.getRequestDispatcher(path).forward(req, resp);
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return requestURI.substring(req.getContextPath().length(), requestURI.length());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);

        if (action.startsWith("/connect")) {
            String databaseName = req.getParameter("dbname");
            String userName = req.getParameter("username");
            String password = req.getParameter("password");

            try {
                DatabaseManager manager = service.connect(databaseName, userName, password);
                req.getSession().setAttribute("db_manager", manager);
                resp.sendRedirect(resp.encodeRedirectURL("menu"));
            } catch (Exception e) {
                e.printStackTrace();
                req.setAttribute("message", e.getMessage());
                goToJsp(req, resp, "error.jsp");
            }
        }
    }
}

