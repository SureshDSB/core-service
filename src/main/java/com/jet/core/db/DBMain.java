package com.jet.core.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.jet.core.db.PojoCreator.generatePojoJPA;
import static com.jet.core.db.PojoCreator.getClassName;

public class DBMain {
    public static void main(String[] args) throws IOException {
        String fileName = "db.csv";

        CsvReader csvReader = new CsvReader();
        Map<String, List<DBInfo>> map = csvReader.read("Trade", fileName);

        for(Map.Entry<String, List<DBInfo>> entry : map.entrySet()){
            String tableName = entry.getKey();
            List<ColumnType> columns = new ArrayList<>();
            for(DBInfo dbInfo : entry.getValue()){
                columns.add(new ColumnType(dbInfo.getColName(), dbInfo.getType()));
            }
            String content = generatePojoJPA(tableName, "com.jet.core.omr.entity", columns);
            String packageName = "/Users/sureshdo/ARKA/CORE/src/main/java/com/jet/core/omr/entity/";
            FileUtil.createJavaPojoFile(getClassName(tableName), content, packageName);
        }
    }
}
