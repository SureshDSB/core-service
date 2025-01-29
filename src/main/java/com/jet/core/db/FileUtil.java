package com.jet.core.db;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
    public static final String JAVA_EXTENSION = ".java";

    public static void createJavaFile(String className, String content, String dir){
        try {
            File dirFile = new File(dir);
            if(!dirFile.exists()){
                boolean isCreated = dirFile.mkdirs();
                if(isCreated){
                    System.out.println("Directories created");
                }
            }
            Files.write(Paths.get(dir+ className + JAVA_EXTENSION), content.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
