package io.github.transfusion.app_info_java_graalvm.AppInfo;

import java.io.File;

public class Utilities {
    public static String getResourcesAbsolutePath(String resourceName) {
        ClassLoader classLoader = Utilities.class.getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        return file.getAbsolutePath();
    }
}
