package io.github.transfusion.app_info_java_graalvm.AppInfo;

import org.junit.jupiter.api.*;

import static io.github.transfusion.app_info_java_graalvm.AppInfo.Utilities.getResourcesAbsolutePath;

public class InfoPlistTest {
    private IPA ipadIPA;

    @BeforeEach
    void setup() {
        String resourceName = "apps/ipad.ipa";
        String absolutePath = getResourcesAbsolutePath(resourceName);
        ipadIPA = IPA.from(absolutePath);
    }

    @AfterEach
    void tearDown() {
        ipadIPA.clear();
        ipadIPA.getContext().close();
        // sanity check to make sure that the context has indeed been properly closed
        Assertions.assertThrows(IllegalStateException.class, () -> {
            ipadIPA.bundle_id();
        });
    }

    @Test
    void iPadIPA() {
        // test the "constructor"
        InfoPlist infoPlist = InfoPlist.create(ipadIPA.info_path());

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
}