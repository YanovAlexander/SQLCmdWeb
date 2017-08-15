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
import java.util.*;

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

        }else if (action.startsWith("/databaseList")) {
            req.setAttribute("databases", service.databases(manager));
            goToJsp(req, resp, "databaseList.jsp");

        } else if (action.startsWith("/find")) {
            String tableName = req.getParameter("table");
            req.getSession().setAttribute("tableName", tableName);
            req.setAttribute("table", service.find(manager, tableName));
            goToJsp(req, resp, "find.jsp");

        }else if (action.equals("/createDatabase")) {
            goToJsp(req, resp,"createDatabase.jsp");

        } else if (action.equals("/updateRecord")) {
            prepare(req);
            goToJsp(req, resp, "update.jsp");

        }else if (action.equals("/insertRecord")) {
            prepareInsert(req);
            goToJsp(req, resp, "insert.jsp");

        }else if (action.equals("/table")) {
            goToJsp(req, resp, "createTable.jsp");

        }else if (action.equals("/createTable")) {
            table(req, resp);

        } else {
            goToJsp(req, resp, "error.jsp");
        }
    }

    private void prepareInsert(HttpServletRequest req) {
        String columnNames = req.getParameter("table");
        String substring = columnNames.substring(1, columnNames.length() - 1);
        String[] columnCountArray = substring.split(",");
        req.getSession().setAttribute("columnCount", columnCountArray.length);
        for (int i = 0; i < columnCountArray.length; i++) {
            req.getSession().setAttribute("columnName" + i, columnCountArray[i]);
        }
    }

    private void table(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableName = req.getParameter("tableName");
        int columnCount = Integer.parseInt(req.getParameter("columnCount"));

        req.setAttribute("tableName", tableName);
        req.setAttribute("columnCount", columnCount);

        goToJsp(req, resp, "table.jsp");
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
        DatabaseManager databaseManager = getManager(req);
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
        } else if (action.equals("/update")) {
            Integer columnCount = (Integer) req.getSession().getAttribute("columnCount");
            String tableName = (String) req.getSession().getAttribute("tableName");
            Map<String, Object> data = new HashMap<>();

            for (int index = 1; index < columnCount; index++) {
                data.put((String) req.getSession().getAttribute("columnName" + index),
                        req.getParameter("columnValue" + index));
            }
            String keyName = (String) req.getSession().getAttribute("keyName");
            String keyValue = (String) req.getSession().getAttribute("keyValue");

            service.update(databaseManager, tableName, keyName, keyValue, data);
            goToJsp(req, resp, "success.jsp");

        }else if (action.equals("/insert")) {
            Integer columnCount = (Integer) req.getSession().getAttribute("columnCount");
            String tableName = (String) req.getSession().getAttribute("tableName");
            Map<String, Object> data = new LinkedHashMap<>();

            for (int index = 0; index < columnCount; index++) {
                data.put((String) req.getSession().getAttribute("columnName" + index),
                        req.getParameter("columnValue" + index));
            }

            service.insert(databaseManager, tableName, data);
            goToJsp(req, resp, "success.jsp");

        }else if (action.equals("/table")) {
            tables(databaseManager, req, resp);

        }else if (action.equals("/createDatabase")) {
            createDatabase(databaseManager, req, resp);
            goToJsp(req, resp, "success.jsp");
        } else if (action.startsWith("/deleteRecord")) {
            String record = req.getParameter("record");
            String tableName = (String) req.getSession().getAttribute("tableName");
            service.deleteRecord(databaseManager, tableName, record);
            goToJsp(req, resp, "success.jsp");
        } else if (action.startsWith("/clear")) {
            String tableName = req.getParameter("table");
            service.clear(databaseManager, tableName);
            goToJsp(req, resp, "success.jsp");

        } else if (action.startsWith("/deleteTable")) {
            String tableName = req.getParameter("table");
            service.deleteTable(databaseManager, tableName);
            goToJsp(req, resp, "success.jsp");

        }
        else if (action.startsWith("/deleteDatabase")) {
            String databaseName = req.getParameter("database");
            service.deleteDatabase(databaseManager, databaseName);
            goToJsp(req, resp, "success.jsp");

        }
    }

    private void createDatabase(DatabaseManager databaseManager, HttpServletRequest req, HttpServletResponse resp) {
        String database = req.getParameter("databaseName");
        service.createDatabase(databaseManager, database);
    }

    private void tables(DatabaseManager databaseManager, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableName = req.getParameter("tableName");
        int columnCount = Integer.parseInt(req.getParameter("columnCount"));

        List<String> columnParameters = new ArrayList<>();
        for (int i = 1; i < columnCount; i++) {
            columnParameters.add(req.getParameter("columnName" + i));
        }
        service.createTable(databaseManager, tableName, columnParameters);
        goToJsp(req, resp, "success.jsp");
    }

    private void prepare(HttpServletRequest req) {
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
    }


    private DatabaseManager getManager(HttpServletRequest req) {
        return (DatabaseManager) req.getSession().getAttribute("db_manager");
    }
}

