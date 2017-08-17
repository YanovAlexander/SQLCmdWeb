package ua.com.juja.controller;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NullAction implements Action {

    @Override
    public void get(HttpServletRequest req, HttpServletResponse resp) {
        //NOP
    }

    @Override
    public boolean canProcess(String url) {
        return false;
    }

    @Override
    public void post(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        //NOP
    }
}
