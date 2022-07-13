package io.github.transfusion.app_info_java_graalvm.AppInfo;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.github.transfusion.app_info_java_graalvm.AppInfo.Utilities.getResourcesAbsolutePath;

public class InfoPlistTest {

    private Context createContext() {
        Context ctx = Context.newBuilder().
                allowAllAccess(true).build();
        ctx.eval("ruby", "Encoding.default_external = 'ISO-8859-1'");
        ctx.eval("ruby", "require 'app-info'");
        return ctx;
    }

    @Test
    void iPadIPA() {
        Context ctx = createContext();
        String resourceName = "apps/ipad.ipa";
        String absolutePath = getResourcesAbsolutePath(resourceName);
        IPA ipadIPA = IPA.from(ctx, absolutePath);

        InfoPlist infoPlist = InfoPlist.from(ctx, ipadIPA.info_path());

        Assertions.assertEquals(infoPlist.build_version(), "1");
        Assertions.assertEquals(infoPlist.release_version(), "1.0");
        Assertions.assertEquals(infoPlist.name(), "bundle");
        Assertions.assertEquals(infoPlist.bundle_name(), "bundle");
        Assertions.assertNull(infoPlist.display_name());
        Assertions.assertEquals(infoPlist.identifier(), "com.icyleaf.bundle");
        Assertions.assertEquals(infoPlist.bundle_id(), "com.icyleaf.bundle");
        Assertions.assertEquals(infoPlist.device_type(), "iPad");
        Assertions.assertEquals(infoPlist.min_sdk_version(), "9.3");
        Assertions.assertEquals(infoPlist.min_os_version(), "9.3");

        Value hashSubscriptLambda = infoPlist.getContext().eval("ruby", "-> recv, arg { recv[arg] }");
        Value res = hashSubscriptLambda.execute(infoPlist.getValue(), "CFBundleVersion");
        Assertions.assertEquals(res.asString(), "1");

        res = hashSubscriptLambda.execute(infoPlist.getValue(), "CFBundleShortVersionString");
        Assertions.assertEquals(res.asString(), "1.0");

        Assertions.assertArrayEquals(infoPlist.icons(), new String[]{"CFBundleIcons~ipad"});
        ctx.close();
    }

    @Test
    void macOSApp() {
        Context ctx = createContext();
        String resourceName = "apps/macos.zip";
        String absolutePath = getResourcesAbsolutePath(resourceName);
        MacOS macOS = MacOS.from(ctx, absolutePath);

        InfoPlist infoPlist = InfoPlist.from(ctx, macOS.info_path());

        Assertions.assertEquals(infoPlist.build_version(), "1");
        Assertions.assertEquals(infoPlist.release_version(), "1.0");
        Assertions.assertEquals(infoPlist.name(), "GuiApp");
        Assertions.assertEquals(infoPlist.bundle_name(), "GuiApp");
        Assertions.assertNull(infoPlist.display_name());
        Assertions.assertEquals(infoPlist.identifier(), "com.icyleaf.macos.GUIApp");
        Assertions.assertEquals(infoPlist.bundle_id(), "com.icyleaf.macos.GUIApp");
        Assertions.assertEquals(infoPlist.device_type(), "macOS");
        Assertions.assertEquals(infoPlist.min_system_version(), "11.3");
        Assertions.assertEquals(infoPlist.min_os_version(), "11.3");

        Value hashSubscriptLambda = infoPlist.getContext().eval("ruby", "-> recv, arg { recv[arg] }");
        Value res = hashSubscriptLambda.execute(infoPlist.getValue(), "CFBundleVersion");
        Assertions.assertEquals(res.asString(), "1");

        res = hashSubscriptLambda.execute(infoPlist.getValue(), "CFBundleShortVersionString");
        Assertions.assertEquals(res.asString(), "1.0");

        Assertions.assertArrayEquals(infoPlist.icons(), new String[]{"CFBundleIconFile", "CFBundleIconName"});
        ctx.close();
    }
}