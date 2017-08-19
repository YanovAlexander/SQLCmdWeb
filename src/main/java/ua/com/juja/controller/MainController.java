package ua.com.juja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.com.juja.model.DatabaseManager;
import ua.com.juja.service.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
        req.setAttribute("items", service.commandsList());
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

    @RequestMapping(value = "/find",params = { "table" }, method = RequestMethod.GET)
    public String tables(Model model,
                         @RequestParam(value = "table") String table,
                         HttpSession session) {
        DatabaseManager manager = getManager(session);

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

    @RequestMapping(value = "/table", method = RequestMethod.GET)
    public String createTable(HttpSession session) {
        if (getManager(session) == null) {
            session.setAttribute("from-page", "/table");
            return "redirect:/connect";
        }
        return "createTable";
    }

    @RequestMapping(value = "/updateRecord", method = RequestMethod.GET)
    public String updateRecord(HttpServletRequest req, HttpSession session, Model model) {
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

        return "createTable";
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



    private DatabaseManager getManager(HttpSession session) {
        return (DatabaseManager) session.getAttribute("db_manager");
    }

}
