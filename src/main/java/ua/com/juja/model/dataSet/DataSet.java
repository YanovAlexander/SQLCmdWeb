package ua.com.juja.model.dataSet;

import java.util.List;
import java.util.Set;

public interface DataSet {
    void put(String name, Object value);

    List<Object> getValues();

    Set<String> getNames();

    Object get(String name);
}
