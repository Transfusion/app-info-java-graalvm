package io.github.transfusion.app_info_java_graalvm.AppInfo;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.PolyglotException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static io.github.transfusion.app_info_java_graalvm.AppInfo.Utilities.getResourcesAbsolutePath;

public class AppInfoTest {
    private Context createContext() {
        Context ctx = Context.newBuilder().allowAllAccess(true).build();
        ctx.eval("ruby", "Encoding.default_external = 'ISO-8859-1'");
        ctx.eval("ruby", "require 'app-info'");
        return ctx;
    }

    @Test
    void testParsing() throws InvocationTargetException, IllegalAccessException {
        Map<String, Class> MATCH_FILE_TYPES = Map.ofEntries(
                Map.entry("apps/android.apk", APK.class),
                Map.entry("apps/android-24.apk", APK.class),
                Map.entry("apps/tv.apk", APK.class),
                Map.entry("apps/wear.apk", APK.class),
                Map.entry("apps/automotive.apk", APK.class),
                Map.entry("apps/android.aab", AAB.class),
                Map.entry("apps/ipad.ipa", IPA.class),
                Map.entry("apps/iphone.ipa", IPA.class),
                Map.entry("apps/embedded.ipa", IPA.class),
                Map.entry("dsyms/multi_ios.dSYM.zip", DSYM.class),
                Map.entry("dsyms/single_ios.dSYM.zip", DSYM.class),
                Map.entry("mobileprovisions/bplist.mobileprovision", MobileProvision.class),
                Map.entry("mobileprovisions/plist.mobileprovision", MobileProvision.class),
                Map.entry("mobileprovisions/profile.mobileprovision", MobileProvision.class),
                Map.entry("mobileprovisions/signed_plist.mobileprovision", MobileProvision.class),
                Map.entry("mobileprovisions/ios_adhoc.mobileprovision", MobileProvision.class),
                Map.entry("mobileprovisions/ios_appstore.mobileprovision", MobileProvision.class),
                Map.entry("mobileprovisions/ios_development.mobileprovision", MobileProvision.class),
                Map.entry("mobileprovisions/macos_appstore.provisionprofile", MobileProvision.class),
                Map.entry("mobileprovisions/macos_development.provisionprofile", MobileProvision.class),
//
                Map.entry("proguards/single_mapping.zip", Proguard.class),
                Map.entry("proguards/full_mapping.zip", Proguard.class),
//
                Map.entry("apps/macos.zip", MacOS.class),
                Map.entry("apps/macos-signed.zip", MacOS.class)
        );

        Context ctx = createContext();
        AppInfo appInfo = AppInfo.getInstance(ctx);

        for (Map.Entry<String, Class> entry : MATCH_FILE_TYPES.entrySet()) {
            String resourceName = entry.getKey();
            String absolutePath = getResourcesAbsolutePath(resourceName);

            Class clazz = entry.getValue();

            AbstractPolyglotAdapter instance = appInfo.parse_(absolutePath);

            Assertions.assertTrue(clazz.isInstance(instance));
            try {
                Method clear = instance.getClass().getMethod("clear");
                clear.invoke(instance);
            } catch (NoSuchMethodException ignored) {

            }

        }
        ctx.close();
    }

    @Test
    void fileDoesntExist() {
        String absolutePath = "invalid/path";
        Context ctx = createContext();
        AppInfo appInfo = AppInfo.getInstance(ctx);
        Assertions.assertThrows(PolyglotException.class, () -> appInfo.parse_(absolutePath));
        ctx.close();
    }


    @Test
    void invalidFileType() throws IOException {
        Path temp = Files.createTempFile("foo", ".txt");
        String absolutePath = temp.toString();
        Files.write(temp, "Hello World\n".getBytes(StandardCharsets.UTF_8));

        Context ctx = createContext();
        AppInfo appInfo = AppInfo.getInstance(ctx);
        Assertions.assertThrows(PolyglotException.class, () -> appInfo.parse_(absolutePath));

        Files.deleteIfExists(temp);
        ctx.close();
    }
}
