package io.github.transfusion.app_info_java_graalvm.AppInfo;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.github.transfusion.app_info_java_graalvm.AppInfo.Utilities.getResourcesAbsolutePath;

public class InfoPlistTest {
    private static Context ctx;

    @BeforeAll
    static void staticSetup() {
        ctx = Context.newBuilder().
                allowAllAccess(true).build();
    }

    @AfterAll
    static void staticTeardown() {
        ctx.close();
        // sanity check to make sure that the context has indeed been properly closed
        Assertions.assertThrows(IllegalStateException.class, () -> ctx.eval("ruby", "nil"));
    }

    private IPA ipadIPA;

    private MacOS macOS;

    @BeforeEach
    void setup() {
        String resourceName = "apps/ipad.ipa";
        String absolutePath = getResourcesAbsolutePath(resourceName);
        ipadIPA = IPA.from(InfoPlistTest.ctx, absolutePath);

        resourceName = "apps/macos.zip";
        absolutePath = getResourcesAbsolutePath(resourceName);
        macOS = MacOS.from(InfoPlistTest.ctx, absolutePath);
    }

    @AfterEach
    void teardown() {
        ipadIPA.clear();
        macOS.clear();
    }

    @Test
    void iPadIPA() {
        // test the "constructor"
        InfoPlist infoPlist = InfoPlist.from(InfoPlistTest.ctx, ipadIPA.info_path());

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
    }

    @Test
    void macOSApp() {
        InfoPlist infoPlist = InfoPlist.from(InfoPlistTest.ctx, macOS.info_path());

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
    }
}