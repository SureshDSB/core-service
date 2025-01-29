package com.jet.core.db;

import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CsvReader {

    private Map<Integer, String> columnMap;

    public Map<String, List<DBInfo>> read(String tableName, String fileName) throws IOException {
        Map<String, List<DBInfo>> map = new HashMap<>();

        columnMap = new HashMap<>();
        File file = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + fileName);
        List<String> lines = Files.readAllLines(Paths.get(String.valueOf(file.toPath())));
        lines.forEach(System.out::println);
        List<DBInfo> infoList = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if(i==0) {
                generateColumnMapper(columnMap, line);
                continue;
            }
            DBInfo rowData = readRow(line, DBInfo.class);
            infoList.add(rowData);
        }
        map.put(tableName, infoList);
        return map;
    }

    private <T> T readRow(String line, Class<T> clazz) {
        T rowData = createInstance(clazz);
        String[] columns = line.split(",");
        for (int i = 0; i < columns.length; i++) {
            setProperty(rowData, columnMap.get(i+1), columns[i]);
        }
        return rowData;
    }


    private void setProperty(Object obj, String column, String value) {

        try{
            Class<?> type = obj.getClass().getMethod("get"+column).getReturnType();
            Method method = obj.getClass().getMethod("set"+column, type);
            method.invoke(obj,value);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    private <T> T createInstance(Class<T> clazz) {
        T rowData;
        try{
            rowData = clazz.getDeclaredConstructor().newInstance();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        return rowData;
    }

    private void generateColumnMapper(Map<Integer, String> columnMap, String line) {
        String[] columns = line.split(",");
        for (int i = 0; i < columns.length; i++) {
            columnMap.put(i+1, columns[i]);
        }
    }
}
