package ua.com.juja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ua.com.juja.model.manager.DatabaseManager;
import ua.com.juja.service.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class MainController {

    @Autowired
    private Service service;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main() {
        return "redirect:/menu";
    }

    @RequestMapping(value = "/help", method = RequestMethod.GET)
    public String help() {
        return "help";
    }


    @RequestMapping(value = "/connect", method = RequestMethod.GET)
    public String connect(HttpSession session, Model model) {
        String page = (String) session.getAttribute("from-page");
        session.removeAttribute("from-page");
        model.addAttribute("connection", new Connection(page));

        if (getManager(session) == null) {
            return "connect";
        } else {
            return "menu";
        }
    }

    @RequestMapping(value = "/connect", method = RequestMethod.POST)
    public String connecting(@ModelAttribute("connection") Connection connection,
                             HttpSession session, Model model) {
        try {
            DatabaseManager manager = service.connect(connection.getDbName(),
                    connection.getUserName(), connection.getPassword());
            session.setAttribute("db_manager", manager);
            return "redirect:" + connection.getFromPage();
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", e.getMessage());
            return "error";
        }
    }

    @RequestMapping(value = "/actions/{userName}", method = RequestMethod.GET)
    public String actions(Model model,
                         @PathVariable("userName") String userName) {
        model.addAttribute("actions", service.getAllFor(userName));
        return "actions";
    }

    @RequestMapping(value = "/tables", method = RequestMethod.GET)
    public String list(Model model, HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/tables");
            return "redirect:/connect";
        }

        model.addAttribute("tables", service.tables(manager));
        return "tables";
    }

    @RequestMapping(value = "/find/{table}", method = RequestMethod.GET)
    public String tables(Model model,
                         @PathVariable("table") String table,
                         HttpSession session) {
        DatabaseManager manager = getManager(session);
        model.addAttribute("tableName", table);
        if (manager == null) {
            session.setAttribute("from-page", "/find?table=" + table);
            return "redirect:/connect";
        }

        model.addAttribute("table", service.find(manager, table));
        return "find";
    }

    @RequestMapping(value = "/clear-table", method = RequestMethod.POST)
    public String clearingTable(@RequestParam("tableName") String tableName,
                                HttpSession session) {
        service.clear(getManager(session), tableName);
        return "success";
    }

    @RequestMapping(value = "/delete-table", method = RequestMethod.POST)
    public String deletingTable(@RequestParam("tableName") String tableName,
                                HttpSession session) {
        service.deleteTable(getManager(session), tableName);
        return "success";
    }

    @RequestMapping(value = "/create-table", method = RequestMethod.GET)
    public String createTable(HttpSession session) {
        if (getManager(session) == null) {
            session.setAttribute("from-page", "/table");
            return "redirect:/connect";
        }
        return "create-table-form";
    }

    @RequestMapping(value = "/create-table", method = RequestMethod.POST)
    public String creatingTable(@RequestParam("columnCount") Integer columnCount,
                                @RequestParam("tableName") String tableName,
                                HttpSession session,
                                HttpServletRequest req) {

        List<String> columnParameters = new ArrayList<>();
        for (int i = 1; i < columnCount; i++) {
            columnParameters.add(req.getParameter("columnName" + i));
        }
        service.createTable(getManager(session), tableName, columnParameters);
        return "success";
    }


    @RequestMapping(value = "/create-table-form", method = RequestMethod.GET)
    public String createTableForm(@RequestParam("columnCount") Integer columnCount,
                                  @RequestParam("tableName") String tableName,
                                  Model model) {
        model.addAttribute("tableName", tableName);
        model.addAttribute("columnCount", columnCount);
        return "create-table";
    }

    @RequestMapping(value = "/find/{tableName}/{keyValue}/update-record", method = RequestMethod.GET)
    public String updateRecord(@PathVariable("keyValue") Integer keyValue,
                               @PathVariable("tableName") String tableName,
                               Model model, HttpSession session) {
        List<String> columnNames = getManager(session).getColumnNames(tableName);
        model.addAttribute("columnCount", columnNames.size());
        for (int i = 1; i < columnNames.size(); i++) {
            session.setAttribute("columnName" + i, columnNames.get(i));
        }
        return "update-record";
    }

    @RequestMapping(value = "/find/{tableName}/{keyValue}/update-record", method = RequestMethod.POST)
    public String updatingRecord(@RequestParam("columnCount") Integer columnCount,
                                 @PathVariable("tableName") String tableName,
                                 @PathVariable("keyValue") Integer keyValue,
                                 HttpSession session,
                                 HttpServletRequest req) {
        Map<String, Object> data = new HashMap<>();
        for (int index = 1; index < columnCount; index++) {
            data.put((String) session.getAttribute("columnName" + index),
                    req.getParameter("columnValue" + index));
            session.removeAttribute("columnName" + index);
        }
        service.update(getManager(session), tableName, keyValue, data);
        return "success";
    }

    @RequestMapping(value = "/find/{tableName}/insert-record", method = RequestMethod.GET)
    public String insertRecord(@PathVariable("tableName") String tableName,
                               Model model, HttpSession session) {
        model.addAttribute("tableName", tableName);
        List<String> columnNames = getManager(session).getColumnNames(tableName);
        model.addAttribute("columnCount", columnNames.size());
        for (int i = 0; i < columnNames.size(); i++) {
            session.setAttribute("columnName" + i, columnNames.get(i));
        }
        return "insert-record";
    }

    @RequestMapping(value = "/find/{tableName}/insert-record", method = RequestMethod.POST)
    public String insertingRecord(@RequestParam("columnCount") Integer columnCount,
                                  @PathVariable("tableName") String tableName,
                                  HttpSession session,
                                  HttpServletRequest req) {
        Map<String, Object> data = new LinkedHashMap<>();
        for (int index = 0; index < columnCount; index++) {
            data.put((String) session.getAttribute("columnName" + index),
                    req.getParameter("columnValue" + index));
            session.removeAttribute("columnName" + index);
        }
        service.insertRecord(getManager(session), tableName, data);
        return "success";
    }

    @RequestMapping(value = "/find/delete-record", method = RequestMethod.POST)
    public String deletingRecord(@RequestParam("record") String keyValue,
                                 @RequestParam("tableName") String tableName,
                                 HttpSession session) {
        service.deleteRecord(getManager(session), tableName, keyValue);
        return "success";
    }

    @RequestMapping(value = "/databases", method = RequestMethod.GET)
    public String databases(Model model, HttpSession session) {
        DatabaseManager manager = getManager(session);

        if (manager == null) {
            session.setAttribute("from-page", "/databases");
            return "redirect:/connect";
        }
        model.addAttribute("databases", service.databases(getManager(session)));
        return "databases";
    }

    @RequestMapping(value = "/create-database", method = RequestMethod.GET)
    public String createDatabase(HttpSession session) {
        if (getManager(session) == null) {
            session.setAttribute("from-page", "/createDatabase");
            return "redirect:/connect";
        }
        return "create-database";
    }

    @RequestMapping(value = "/create-database", method = RequestMethod.POST)
    public String creatingDatabase(@RequestParam("databaseName") String databaseName,
                                   HttpSession session) {
        service.createDatabase(getManager(session), databaseName);
        return "success";
    }

    @RequestMapping(value = "/delete-database", method = RequestMethod.POST)
    public String deletingDatabase(@RequestParam("database") String databaseName,
                                   HttpSession session) {
        service.deleteDatabase(getManager(session), databaseName);
        return "success";
    }

    private DatabaseManager getManager(HttpSession session) {
        return (DatabaseManager) session.getAttribute("db_manager");
    }
}
