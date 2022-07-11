package io.github.transfusion.app_info_java_graalvm;

import org.graalvm.polyglot.Context;

public class Utilities {
    public static Context createContext() {
        Context polyglot = Context.newBuilder().
                allowAllAccess(true).build();
        polyglot.eval("ruby", "Encoding.default_external = 'ISO-8859-1'");
        return polyglot;
    }

}
