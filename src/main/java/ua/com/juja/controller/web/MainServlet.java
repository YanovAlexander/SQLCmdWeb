package ua.com.juja.controller.web;

import ua.com.juja.model.DatabaseManager;
import ua.com.juja.service.Service;
import ua.com.juja.service.ServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
    private Service service;

    @Override
    public void init() throws ServletException {
        super.init();

        service = new ServiceImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = getAction(req);
        DatabaseManager manager = (DatabaseManager) req.getSession().getAttribute("db_manager");

        if (action.startsWith("/connect")) {
            if (manager == null) {
                req.getRequestDispatcher("connect.jsp").forward(req, resp);
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
            req.getRequestDispatcher("menu.jsp").forward(req, resp);
        } else if (action.startsWith("/help")) {
            req.getRequestDispatcher("help.jsp").forward(req, resp);
        } else if (action.startsWith("/clear")) {
            service.clear(manager, "users");
            req.getRequestDispatcher("clear.jsp").forward(req, resp);
        } else if (action.startsWith("/createdbform")) {
            req.getRequestDispatcher("createdbform.jsp").forward(req, resp);
        } else if (action.startsWith("/insertentryform")) {
            req.getRequestDispatcher("insertentryform.jsp").forward(req, resp);
        } else if (action.startsWith("/createtableform")) {
            req.getRequestDispatcher("createtableform.jsp").forward(req, resp);
        } else if (action.startsWith("/deletedbform")) {
            req.getRequestDispatcher("deletedbform.jsp").forward(req, resp);
        } else if (action.startsWith("/deletetableform")) {
            req.getRequestDispatcher("deletetableform.jsp").forward(req, resp);
        } else if (action.startsWith("/createdb")) {
            String dbname = (String) req.getSession().getAttribute("db_name");
            service.createdatabase(manager, dbname);
            req.getRequestDispatcher("createdb.jsp").forward(req, resp);
        } else if (action.startsWith("/deletedb")) {
            String dbname = (String) req.getSession().getAttribute("db_name_delete");
            service.deletedatabase(manager, dbname);
            req.getRequestDispatcher("deletedb.jsp").forward(req, resp);
        } else if (action.startsWith("/deletetable")) {
            String tableName = (String) req.getSession().getAttribute("table_name_delete");
            service.deletetable(manager, tableName);
            req.getRequestDispatcher("deletetable.jsp").forward(req, resp);
        } else if (action.startsWith("/createtable")) {
            String sql = (String) req.getSession().getAttribute("create_table");
            service.createtable(manager, sql);
            req.getRequestDispatcher("createtable.jsp").forward(req, resp);
        } else if (action.startsWith("/insertentry")) {
            String tableName = (String) req.getSession().getAttribute("table_name_insert");
            String columnNames = (String) req.getSession().getAttribute("column_names_insert");
            String values = (String) req.getSession().getAttribute("values_insert");
            service.insertentry(manager, tableName, columnNames, values);
            req.getRequestDispatcher("insertentry.jsp").forward(req, resp);
        } else if (action.startsWith("/find")) {
            String tableName = req.getParameter("table");
            req.setAttribute("table", service.find(manager, tableName));
            req.getRequestDispatcher("find.jsp").forward(req, resp);
        } else if (action.startsWith("/databaselist")) {
            String databases = req.getParameter("databases");
            req.setAttribute("databases", service.databaselist(manager));
            req.getRequestDispatcher("databaselist.jsp").forward(req, resp);
        } else {
            req.getRequestDispatcher("error.jsp").forward(req, resp);
        }
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
                req.setAttribute("message", e.getMessage());
                req.getRequestDispatcher("error.jsp").forward(req, resp);
            }
        } else if (action.startsWith("/createdbform")) {
            String databaseName = req.getParameter("dbname");
            req.getSession().setAttribute("db_name", databaseName);
            resp.sendRedirect(resp.encodeRedirectURL("createdb"));
        } else if (action.startsWith("/deletedbform")) {
            String databaseNameDelete = req.getParameter("dbname");
            req.getSession().setAttribute("db_name_delete", databaseNameDelete);
            resp.sendRedirect(resp.encodeRedirectURL("deletedb"));
        } else if (action.startsWith("/deletetableform")) {
            String databaseNameDelete = req.getParameter("tablename");
            req.getSession().setAttribute("table_name_delete", databaseNameDelete);
            resp.sendRedirect(resp.encodeRedirectURL("deletetable"));
        } else if (action.startsWith("/createtableform")) {
            String tableName = req.getParameter("tablename");
            String column1 = req.getParameter("column1");
            String column2 = req.getParameter("column2");
            String column3 = req.getParameter("column3");
            String sql = tableName + "(" + column1 + " SERIAL NOT NULL PRIMARY KEY," + column2 +
                    " varchar(255)," + column3 + " varchar(255))";
            req.getSession().setAttribute("create_table", sql);
            resp.sendRedirect(resp.encodeRedirectURL("createtable"));
        } else if (action.startsWith("/insertentryform")) {
            StringBuilder columnNames = new StringBuilder();
            StringBuilder values = new StringBuilder();
            String tableName = req.getParameter("tablename");
            String column1 = req.getParameter("column1");
            String column1Value = req.getParameter("column1_value");
            String column2 = req.getParameter("column2");
            String column2Value = req.getParameter("column2_value");
            String column3 = req.getParameter("column3");
            String column3Value = req.getParameter("column3_value");
            columnNames.append(column1 + " ").append(column2 + " ").append(column3 + " ");
            values.append(column1Value + " ").append(column2Value + " ").append(column3Value + " ");
            req.getSession().setAttribute("table_name_insert", tableName);
            req.getSession().setAttribute("column_names_insert", columnNames.toString());
            req.getSession().setAttribute("values_insert", values.toString());
            resp.sendRedirect(resp.encodeRedirectURL("insertentry"));
        }
    }

    private String getAction(HttpServletRequest req) {
        String requestURI = req.getRequestURI();
        return requestURI.substring(req.getContextPath().length(), requestURI.length());
    }

}
