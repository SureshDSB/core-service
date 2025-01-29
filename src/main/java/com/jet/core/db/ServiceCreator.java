package com.jet.core.db;


public class ServiceCreator {

    public static final String DOT = ".";

    private final static CaseService caseService = new CaseService();

    private static final String PACKAGE_CLASS_TEMPLATE  = """
    package %s;
    
    import %s;
    import %s;
    import lombok.RequiredArgsConstructor;
    import org.springframework.stereotype.Service;
    
    %s
    
    """;

 private static final String SERVICE_TEMPLATE  = """
    @Service
    @RequiredArgsConstructor
    public class %s {
    
        private final %s %s;
    
        %s
        %s
        %s
    
    }
    """;

 private static final String SAVE_METHOD_TEMPLATE  = """
    public %s create%s(%s %s) {
            return %s.save(%s);
        }
    """;

 private static final String GET_METHOD_TEMPLATE  = """
    public %s get%sById(Long id) {
            return %s.findById(id).orElse(null);
        }
    """;
private static final String UPDATE_METHOD_TEMPLATE  = """
    public void update(%s %s) {
            %s.findById(%s.getId())
            .ifPresent(x -> {
            // update fields
            %s.save(x);
         });
        }
    """;

    public static  String generateService(String serviceClassName, String repoClassName, String entityClassName, String entityPkgName, String repoPkgName, String servicePkgName) {
        String impEntityPkgName = entityPkgName+ DOT + entityClassName;
        String impERepoPkgName = repoPkgName+ DOT + repoClassName;
        String classContent = generateClassService(serviceClassName, repoClassName, entityClassName);
        return String.format(PACKAGE_CLASS_TEMPLATE, servicePkgName, impEntityPkgName, impERepoPkgName, classContent);
    }



    private static String generateClassService(String serviceClassName, String repoClassName, String entityClassName) {
        String repoClassVarName = caseService.getVariableName(repoClassName);
        String save = generateSaveMethod(entityClassName, repoClassVarName);
        String get = generateGetMethod(entityClassName, repoClassVarName);
        String upd = generateUpdateMethod(entityClassName, repoClassVarName);
        return String.format(SERVICE_TEMPLATE, serviceClassName, repoClassName, repoClassVarName, save, get, upd);
    }

    private static String generateSaveMethod(String entityClassName, String repoClassVarName) {
        String entityClassVarName = caseService.getVariableName(entityClassName);
        return String.format(SAVE_METHOD_TEMPLATE, entityClassName, entityClassName,entityClassName, entityClassVarName, repoClassVarName, entityClassVarName);
    }


    private static String generateGetMethod(String entityClassName, String repoClassVarName) {
        return String.format(GET_METHOD_TEMPLATE, entityClassName, entityClassName, repoClassVarName);
    }

    private static String generateUpdateMethod(String entityClassName, String repoClassVarName) {
        String entityClassVarName = caseService.getVariableName(entityClassName);
        return String.format(UPDATE_METHOD_TEMPLATE, entityClassName, entityClassVarName, repoClassVarName, entityClassVarName, repoClassVarName);
    }


}
