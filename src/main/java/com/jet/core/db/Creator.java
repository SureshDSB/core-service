package com.jet.core.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.jet.core.db.EntityCreator.generatePojoJPA;
import static com.jet.core.db.EntityCreator.getClassName;
import static com.jet.core.db.RepoCreator.generateInterfaceRepo;
import static com.jet.core.db.ServiceCreator.DOT;
import static com.jet.core.db.ServiceCreator.generateService;

public class Creator {


    public static final String FULL_PKG_PATH = "/Users/sureshdo/ARKA/CORE/src/main/java/com/jet/core/";

    public static void create(Map<String, List<DBInfo>> map, String basePackage, String baseService) {


        for(Map.Entry<String, List<DBInfo>> entry : map.entrySet()){
            String tableName = entry.getKey();
            List<ColumnType> columns = new ArrayList<>();
            for(DBInfo dbInfo : entry.getValue()){
                columns.add(new ColumnType(dbInfo.getColName(), dbInfo.getType()));
            }

            String entityPkgName = basePackage+DOT+baseService+DOT+"entity";
            String entityClassName = getClassName(tableName);
            String content = generatePojoJPA(tableName,  entityPkgName, columns);
            String packageName = FULL_PKG_PATH +baseService+"/entity/";
            FileUtil.createJavaFile(entityClassName, content, packageName);

            String repoPkgName = basePackage+DOT+baseService+DOT+"repo";
            String repoClassName = getClassName(tableName)+"Repo";
            String repoContent = generateInterfaceRepo(repoClassName, entityClassName, entityPkgName, repoPkgName);
            String repoPackageName = FULL_PKG_PATH+baseService+"/repo/";
            FileUtil.createJavaFile(repoClassName, repoContent, repoPackageName);


            String serPkgName = basePackage+DOT+baseService+DOT+"service";
            String serviceClassName = getClassName(tableName)+"Service";
            String serviceContent  = generateService(serviceClassName, repoClassName, entityClassName,entityPkgName,repoPkgName,serPkgName);
            String servicePackageName = FULL_PKG_PATH+baseService+"/service/";
            FileUtil.createJavaFile(serviceClassName, serviceContent, servicePackageName);

        }

    }
}
