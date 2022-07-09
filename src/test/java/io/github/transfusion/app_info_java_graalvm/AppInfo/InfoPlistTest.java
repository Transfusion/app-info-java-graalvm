package io.github.transfusion.app_info_java_graalvm.AppInfo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.github.transfusion.app_info_java_graalvm.AppInfo.Utilities.getResourcesAbsolutePath;

public class InfoPlistTest {
    private IPA ipadIPA;
    private MacOS macOS;

    @BeforeEach
    void setup() {
        String resourceName = "apps/ipad.ipa";
        String absolutePath = getResourcesAbsolutePath(resourceName);
        ipadIPA = IPA.from(absolutePath);

        resourceName = "apps/macos.zip";
        absolutePath = getResourcesAbsolutePath(resourceName);
        macOS = MacOS.from(absolutePath);
    }

    @AfterEach
    void tearDown() {
        ipadIPA.clear();
        ipadIPA.getContext().close();
        // sanity check to make sure that the context has indeed been properly closed
        Assertions.assertThrows(IllegalStateException.class, () -> ipadIPA.bundle_id());

        macOS.clear();
        macOS.getContext().close();
        Assertions.assertThrows(IllegalStateException.class, () -> macOS.size());

    }

    @Test
    void iPadIPA() {
        // test the "constructor"
        InfoPlist infoPlist = InfoPlist.from(ipadIPA.info_path());

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

        Assertions.assertArrayEquals(infoPlist.icons(), new String[]{"CFBundleIcons~ipad"});
    }

    @Test
    void macOS() {
        InfoPlist infoPlist = InfoPlist.from(macOS.info_path());

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

        Assertions.assertArrayEquals(infoPlist.icons(), new String[]{"CFBundleIconFile", "CFBundleIconName"});
    }
}