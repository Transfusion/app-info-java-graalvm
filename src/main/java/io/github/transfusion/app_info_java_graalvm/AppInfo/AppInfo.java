package io.github.transfusion.app_info_java_graalvm.AppInfo;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import org.graalvm.polyglot.*;

import java.util.Arrays;

public class AppInfo {
//    public static AbstractPolyglotAdapter parse(String path) {
//
//    }

    public static void test() {
        Context polyglot = Context.newBuilder().
                allowAllAccess(true).build();
        polyglot.eval("ruby", "require 'app-info'");
        IPA result = polyglot.eval("ruby", "ipa = AppInfo.parse('/Users/transfusion/Downloads/eRecruitment.ipa')").as(IPA.class);

//        System.out.println(result.getClass().getCanonicalName());
//        Value array = polyglot.eval("ruby", "[1,2,42,4]");
//        int result = array.getArrayElement(2).asInt();
//        int size = result.getHashValue("size").asInt();
//        Value accessor = polyglot.eval("ruby", "-> hash, key { hash[key] }");
//        String path = result.app_path();
//        System.out.println(path);
//        final Map<String, Boolean> args = new HashMap<>();
//        args.put("human_size", true);
//        System.out.println(result.getMember("size").execute(args));
//        String app_size = result
//        System.out.println(result.app_path());
//        System.out.println(result.size());
//        System.out.println(result.humanSize());
//        System.out.println(result.os());
//        System.out.println(result.file_type());
//
//        System.out.println(result.iphone());
//        System.out.println(result.ipad());
//        System.out.println(result.universal());
        System.out.println(Arrays.toString(result.info().icons()));
    }
}
