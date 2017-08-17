package ua.com.juja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ua.com.juja.service.Service;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MainController {

    @Autowired
    private Service service;

    @RequestMapping(value = "/menu", method = RequestMethod.GET)
    public String  menu(HttpServletRequest req) {
        req.setAttribute("items", service.commandsList());
        return "menu";
    }

     @RequestMapping(value = "/help", method = RequestMethod.GET)
    public String  help() {
        return "help";
    }

}
