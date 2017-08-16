package ua.com.juja.controller.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.com.juja.service.Service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Component
public class ActionResolver {

    @Autowired
    private Service service;

    private List<Action> actions;

    public ActionResolver() {
        actions = new LinkedList<>();
        actions.addAll(Arrays.asList(
                new ConnectAction(service),
                new MenuAction(service),
                new HelpAction(service),
                new TablesAction(service),
                new DatabasesAction(service),
                new FindAction(service),
                new CreateDatabaseAction(service),
                new UpdateRecordAction(service),
                new InsertRecordAction(service),
                new TableAction(service),
                new CreateTableAction(service),
                new DeleteRecordAction(service),
                new ClearAction(service),
                new DeleteTableAction(service),
                new DeleteDatabase(service),
                new ErrorAction(service)
        ));
    }

    public Action getAction(String url) {
        for (Action action : actions) {
            if (action.canProcess(url)) {
                return action;
            }
        }
        return new NullAction();
    }
}
