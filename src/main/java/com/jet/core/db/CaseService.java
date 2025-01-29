package com.jet.core.db;

public class CaseService {

    public String getVariableName(String name) {
        return name.substring(0, 1).toLowerCase()+ name.substring(1);
    }
}
