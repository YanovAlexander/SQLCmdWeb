package ua.com.juja.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;
import ua.com.juja.model.DataSet;
import ua.com.juja.model.DataSetImpl;
import ua.com.juja.model.DatabaseManager;
import ua.com.juja.service.Service;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
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

        try {
            if (action.equals("/connect")) {
                jsp("connect", req, resp);
                return;
            }

            DatabaseManager manager = getManager(req);

            if (manager == null) {
                redirect("connect", resp);
            }

            if (action.equals("/menu") || action.equals("/")) {
                jsp("menu", req, resp);
            } else if (action.equals("/help")) {
                jsp("help", req, resp);
            } else if (action.equals("/createTable")) {
                table(req, resp);
            } else if (action.equals("/table")) {
                jsp("createTable", req, resp);
            } else if (action.equals("/list")) {
                list(manager, req, resp);
            } else if (action.equals("/find")) {
                jsp("tableName", req, resp);
            } else if (action.equals("/clear")) {
                jsp("clear", req, resp);
            } else if (action.equals("/delete")) {
                jsp("delete", req, resp);
            } else if (action.equals("/create")) {
                req.setAttribute("actionURL", "createRecord");
                jsp("tableName", req, resp);
            } else if (action.equals("/createDatabase")) {
                jsp("createDatabase", req, resp);
            } else if (action.equals("/deleteDatabase")) {
                jsp("deleteDatabase", req, resp);
            } else if (action.equals("/update")) {
                req.setAttribute("actionURL", "updateRecord");
                jsp("tableName", req, resp);
            } else {
                jsp("error", req, resp);
            }
        } catch (Exception e) {
            error(req, resp, e);
        }
    }

    private void table(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tableName = request.getParameter("tableName");
        int columnCount = Integer.parseInt(request.getParameter("columnCount"));

        request.setAttribute("tableName", tableName);
        request.setAttribute("columnCount", columnCount);

        jsp("table", request, response);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);

        DatabaseManager manager = getManager(req);

        try {
            if (action.equals("/connect")) {
                connect(req, resp);
            } else if (action.equals("/find")) {
                find(manager, req, resp);
            } else if (action.equals("/clear")) {
                clear(manager, req, resp);
            } else if (action.equals("/delete")) {
                delete(manager, req, resp);
            } else if (action.equals("/createRecord")) {
                prepare(req, manager);
                jsp("create", req, resp);
            } else if (action.equals("/create")) {
                create(manager, req, resp);
            } else if (action.equals("/createDatabase")) {
                createDatabase(manager, req, resp);
            } else if (action.equals("/deleteDatabase")) {
                deleteDatabase(manager, req, resp);
            } else if (action.equals("/table")) {
                tables(manager, req, resp);
            } else if (action.equals("/updateRecord")) {
                prepare(req, manager);
                jsp("update", req, resp);
            } else if (action.equals("/update")) {
                update(manager, req, resp);
            }
        } catch (Exception e) {
            error(req, resp, e);
        }
    }

    private void update(DatabaseManager manager, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException, SQLException {
        String tableName = req.getParameter("tableName");
        Map<String, Object> data = new HashMap<>();

        for (int index = 1; index < getColumnCount(manager, tableName); index++) {
            data.put(req.getParameter("columnName" + index),
                    req.getParameter("columnValue" + index));
        }

        String keyName = req.getParameter("keyName");
        String keyValue = req.getParameter("keyValue");
        manager.update(tableName, keyName, keyValue, data);
        jsp("success", req, resp);
    }

    private void createDatabase(DatabaseManager manager, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String databaseName = req.getParameter("databaseName");

        manager.createDatabase(databaseName);
        jsp("success", req, resp);
    }

    private void deleteDatabase(DatabaseManager manager, HttpServletRequest request,
                                HttpServletResponse response) throws ServletException, IOException {
        String databaseName = request.getParameter("databaseName");

        manager.deleteDatabase(databaseName);
        jsp("success", request, response);
    }

    private void create(DatabaseManager manager, HttpServletRequest req, HttpServletResponse resp) throws SQLException, ServletException, IOException {
        String tableName = req.getParameter("tableName");
        Map<String, Object> data = new HashMap<>();

        for (int index = 1; index <= getColumnCount(manager, tableName); index++) {
            data.put(req.getParameter("columnName" + index),
                    req.getParameter("columnValue" + index));
        }
        manager.create(tableName, data);
        jsp("success", req, resp);
    }

    private void prepare(HttpServletRequest req, DatabaseManager manager) throws SQLException {
        String tableName = req.getParameter("tableName");

        req.setAttribute("columnCount", getColumnCount(manager, tableName));
        req.setAttribute("tableName", tableName);
    }

    private int getColumnCount(DatabaseManager manager, String tableName) throws SQLException {
        return Integer.parseInt(manager.getTableData(tableName).get(0));
    }

    private void delete(DatabaseManager manager, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableName = req.getParameter("tableName");
        String keyName = req.getParameter("keyName");
        String keyValue = req.getParameter("keyValue");

        manager.delete(tableName, keyName, keyValue);
        jsp("success", req, resp);
    }

    private void clear(DatabaseManager manager, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableName = req.getParameter("tableName");
        manager.clear(tableName);
        jsp("success", req, resp);
    }

    private void find(DatabaseManager manager, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableName = req.getParameter("tableName");

        List<String> tableData = manager.getTableData(tableName);

        req.setAttribute("table", getLists(tableData));

        jsp("find", req, resp);
    }

    private List<List<String>> getLists(List<String> tableData) {
        List<List<String>> table = new ArrayList<>(tableData.size() - 1);
        int columnCount = Integer.parseInt(tableData.get(0));
        for (int current = 1; current < tableData.size();) {
            List<String> row = new ArrayList<>(columnCount);
            for (int rowIndex = 0; rowIndex < columnCount; rowIndex++) {
                row.add(tableData.get(current++));
            }
            table.add(row);
        }
        return table;
    }


    private void connect(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        String database = req.getParameter("database");
        String user = req.getParameter("user");
        String password = req.getParameter("password");

        DatabaseManager manager = service.connect(database, user, password);
        req.getSession().setAttribute("manager", manager);
        redirect("menu", resp);
    }


    private void list(DatabaseManager manager, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Set<String> tableList = manager.getTableNames();
        req.setAttribute("tables", tableList);
        jsp("list", req, resp);
    }

    private void tables(DatabaseManager manager, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String tableName = req.getParameter("tableName");
        int columnCount = Integer.parseInt(req.getParameter("columnCount"));
        columnCount = columnCount - 1;
        String keyName = req.getParameter("keyName");

        Map<String, Object> columnParameters = new HashMap<>();
        for (int index = 1; index <= columnCount; index++) {
//            int jindex = index + 1;
            columnParameters.put(req.getParameter("columnName" + index),
                    req.getParameter("columnType" + index));
        }

        manager.createTable(tableName, keyName, columnParameters);
        jsp("success", req, resp);
    }


    private String getAction(HttpServletRequest req) {
        String reqURI = req.getRequestURI();
        return reqURI.substring(req.getContextPath().length(), reqURI.length());
    }


    private void error(HttpServletRequest req, HttpServletResponse resp, Exception e) {
        req.setAttribute("message", e.getMessage());
        try {
            e.printStackTrace();
            jsp("error.jsp", req, resp);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jsp(String jsp, HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher(jsp + ".jsp").forward(req, resp);
    }

    private void redirect(String url, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(resp.encodeRedirectURL(url));
    }

    private DatabaseManager getManager(HttpServletRequest req) {
        return (DatabaseManager) req.getSession().getAttribute("manager");
    }
}
