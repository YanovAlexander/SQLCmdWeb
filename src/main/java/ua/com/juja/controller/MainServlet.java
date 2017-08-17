package ua.com.juja.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MainServlet extends HttpServlet {
//
//    @Autowired
//    private ActionResolver actions;
//
//    @Override
//    public void init(ServletConfig config) throws ServletException {
//        super.init(config);
//        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
//                config.getServletContext());
//    }
//    //TODO implements disconnect from db, implements error when trying delete db which you connected
//
//
//
//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Action action = actions.getAction(getActionName(req));
//
//        action.get(req, resp);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Action action = actions.getAction(getActionName(req));
//
//        action.post(req, resp);
//    }
//
//    private String getActionName(HttpServletRequest req) {
//        String requestURI = req.getRequestURI();
//        return requestURI.substring(req.getContextPath().length(), requestURI.length());
//    }
//
//


}

