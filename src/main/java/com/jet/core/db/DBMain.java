package com.jet.core.db;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DBMain {


    public static void main(String[] args) throws IOException {
        String fileName = "db.csv";
        CsvReader csvReader = new CsvReader();
        Map<String, List<DBInfo>> map = csvReader.read("Trade", fileName);
        Creator.create(map, "com.jet.core","omr");
    }



}
