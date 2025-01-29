package com.jet.core.db;

public class RepoCreator {

    private static final String PACKAGE_REPO_TEMPLATE  = """
    package %s;
    
    import %s.%s;
    import org.springframework.data.jpa.repository.JpaRepository;
    
    %s
    
    """;


 private static final String REPO_TEMPLATE  = """
    public interface %s extends JpaRepository<%s, Long> {
    
    }
    """;

    public static String generateInterfaceRepo(String repoClassName, String className, String entityPkgName, String repoPkgName) {
        String classContent = generateRepo(repoClassName,className);
        return String.format(PACKAGE_REPO_TEMPLATE, repoPkgName, entityPkgName, className, classContent);
    }

    public static  String generateRepo(String repoClassName, String className) {
        return String.format(REPO_TEMPLATE, repoClassName, className);
    }

}
