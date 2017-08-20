package ua.com.juja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.juja.model.DatabaseManager;
import ua.com.juja.service.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    private Service service;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String main() {
        return "redirect:/menu";
    }

    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public String menu(HttpServletRequest req) {
        return "menu";
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

    @RequestMapping(value = "/find", params = {"table"}, method = RequestMethod.GET)
    public String tables(Model model,
                         @RequestParam(value = "table") String table,
                         HttpSession session) {
        DatabaseManager manager = getManager(session);
        model.addAttribute("tableName", table);//todo use model for send data to jsp, also on jsp use hidden fields to store some data
        if (manager == null) {
            session.setAttribute("from-page", "/find?table=" + table);
            return "redirect:/connect";
        }

        model.addAttribute("table", service.find(manager, table));

        return "find";
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

    @RequestMapping(value = "/clearTable", method = RequestMethod.POST)
    public String clearTable(@RequestParam("tableName") String tableName,
                             HttpSession session) {
        service.clear(getManager(session), tableName);
        return "success";
    }

    @RequestMapping(value = "/deleteTable", method = RequestMethod.POST)
    public String deleteTable(@RequestParam("tableName") String tableName,
                              HttpSession session) {
        service.deleteTable(getManager(session), tableName);
        return "success";
    }

    @RequestMapping(value = "/table", method = RequestMethod.GET)
    public String createTable(HttpSession session) {
        if (getManager(session) == null) {
            session.setAttribute("from-page", "/table");
            return "redirect:/connect";
        }
        return "createTable";
    }

    @RequestMapping(value = "/updateRecord", method = RequestMethod.GET)
    public String updateRecord(@RequestParam("table") String columnNames,
                               @RequestParam("record") String keyValue,
                               @RequestParam("tableName") String tableName,
                               Model model, HttpSession session) {
        String substring = columnNames.substring(1, columnNames.length() - 1);
        String[] columnCountArray = substring.split(",");
        String keyName = columnCountArray[0];
        model.addAttribute("keyName", keyName);
        model.addAttribute("tableName", tableName);
        model.addAttribute("keyValue", keyValue);
        model.addAttribute("columnCount", columnCountArray.length);
        for (int i = 1; i < columnCountArray.length; i++) {
            session.setAttribute("columnName" + i, columnCountArray[i]);
        }

        return "updateRecord";
    }

    @RequestMapping(value = "/updateRecord", method = RequestMethod.POST)
    public String updatingRecord(@RequestParam("columnCount") Integer columnCount,
                                 @RequestParam("tableName") String tableName,
                                 @RequestParam("keyName") String keyName,
                                 @RequestParam("keyValue") String keyValue,
                                 HttpSession session,
                                 HttpServletRequest req) {
        Map<String, Object> data = new HashMap<>();

        for (int index = 1; index < columnCount; index++) {
            data.put((String) session.getAttribute("columnName" + index),
                    req.getParameter("columnValue" + index));
        }
        service.update(getManager(session), tableName, keyName, keyValue, data);
        return "success";
    }

    @RequestMapping(value = "/insertRecord", method = RequestMethod.GET)
    public String insertRecord(@RequestParam("table") String columnNames,
                               @RequestParam("tableName") String tableName,
                               Model model, HttpSession session) {
        model.addAttribute("tableName", tableName);
        String substring = columnNames.substring(1, columnNames.length() - 1);
        String[] columnCountArray = substring.split(",");
        model.addAttribute("columnCount", columnCountArray.length);
        for (int i = 0; i < columnCountArray.length; i++) {
            session.setAttribute("columnName" + i, columnCountArray[i]);
        }
        return "insertRecord";
    }

    @RequestMapping(value = "/insertRecord", method = RequestMethod.POST)
    public String insertingRecord(@RequestParam("columnCount") Integer columnCount,
                                  @RequestParam("tableName") String tableName,
                                  HttpSession session,
                                  HttpServletRequest req) {
        Map<String, Object> data = new LinkedHashMap<>();
        for (int index = 0; index < columnCount; index++) {
            data.put((String) session.getAttribute("columnName" + index),
                    req.getParameter("columnValue" + index));
        }
        service.insert(getManager(session), tableName, data);
        return "success";
    }


    @RequestMapping(value = "/deleteRecord", method = RequestMethod.POST)
    public String updateRecord(@RequestParam("record") String keyValue,
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

    @RequestMapping(value = "/createDatabase", method = RequestMethod.GET)
    public String createDatabase(HttpSession session) {
        if (getManager(session) == null) {
            session.setAttribute("from-page", "/createDatabase");
            return "redirect:/connect";
        }
        return "createDatabase";
    }

    @RequestMapping(value = "/deleteDatabase", method = RequestMethod.POST)
    public String deleteDatabase(@RequestParam("database") String databaseName,
                               HttpSession session) {
        service.deleteDatabase(getManager(session), databaseName);
        return "success";
    }
    //TODO create Table, create Database, try delete database

    private DatabaseManager getManager(HttpSession session) {
        return (DatabaseManager) session.getAttribute("db_manager");
    }

}
