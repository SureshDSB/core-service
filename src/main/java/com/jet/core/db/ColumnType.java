package com.jet.core.db;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ColumnType {
    private String columnName;
    private String type;

}
