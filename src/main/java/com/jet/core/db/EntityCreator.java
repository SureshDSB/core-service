package com.jet.core.db;

import java.util.ArrayList;
import java.util.List;

public class EntityCreator {

    private static final String ONE_LINE = "\n";
    private static final String PACKAGE_CLASS_TEMPLATE  = """
    package %s;
    
    import jakarta.persistence.*;
    import lombok.*;
    
    %s
    
    """;

 private static final String ENTITY_TEMPLATE  = """
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    @Table(name= "%s")
    public class %s {
    
    @Id
    %s
    }
    """;

 private static final String COLUMN_TEMPLATE  = """
    @Column(name = "%s")
    private %s %s;
    """;


    public static  String generatePojoJPA(String tableName, String pkgName, List<ColumnType> columns) {
        String classContent = generateJPA(tableName, columns);
        return String.format(PACKAGE_CLASS_TEMPLATE, pkgName, classContent);
    }



    private static String generateJPA(String tableName, List<ColumnType> columns) {
    List<String> columnsInfo  =  new ArrayList<>();
    for (ColumnType column : columns) {
        columnsInfo.add(convertToJPAColumn(column.getColumnName(), column.getType()));
    }
    String colJPA = String.join(ONE_LINE, columnsInfo)+ONE_LINE;
    return String.format(ENTITY_TEMPLATE, tableName, getClassName(tableName), colJPA);
    }

    private static String convertToJPAColumn(String fieldName, String type) {
        return String.format(COLUMN_TEMPLATE, fieldName, type, camelCase(fieldName));
    }

    private static Object camelCase(String s) {
        s= s.toLowerCase();
        while (s.contains("_")){
            s=s.replaceFirst("_[a-z]", String.valueOf(Character.toUpperCase(s.charAt(s.indexOf("_")+1))));
        }
        return s;
    }

    public static String getClassName(String name) {
        return name.substring(0, 1).toUpperCase()+ name.substring(1).toLowerCase();
    }


}
