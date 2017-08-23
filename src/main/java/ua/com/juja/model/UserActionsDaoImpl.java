package ua.com.juja.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserActionsDaoImpl implements UserActionsDao {

    private JdbcTemplate template;

    @Autowired
    @Qualifier(value = "logDataSource")
    public void setDataSource(DataSource dataSource) {
        template = new JdbcTemplate(dataSource);
    }

    @Override
    public void log(String userName, String dbName, String action) {
        template.update(
                "INSERT INTO public.user_actions " +
                        "(user_name, db_name, action)" +
                        " VALUES (?, ?, ?)", userName, dbName, action);
    }

    @Override
    public List<UserAction> getAllFor(String userName) {
        return template.query(
                "SELECT * FROM public.user_actions " +
                        "WHERE user_name = ?", new Object[]{userName},
                (resultSet, rowNum) -> {
                    UserAction result = new UserAction();
                    result.setId(resultSet.getInt("id"));
                    result.setUserName(resultSet.getString("user_name"));
                    result.setDbName(resultSet.getString("db_name"));
                    result.setAction(resultSet.getString("action"));
                    return result;
                });
    }
}
