package io.github.transfusion.app_info_java_graalvm.AppInfo;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static io.github.transfusion.app_info_java_graalvm.AppInfo.Utilities.getResourcesAbsolutePath;

public class MacOSTest {
    private Context createContext() {
        Context ctx = Context.newBuilder().allowAllAccess(true).build();
        ctx.eval("ruby", "Encoding.default_external = 'ISO-8859-1'");
        ctx.eval("ruby", "require 'app-info'");
        return ctx;
    }


    @Test
    void unsignedApp() {
        Context ctx = createContext();
        String resourceName = "apps/macos.zip";
        String absolutePath = getResourcesAbsolutePath(resourceName);

        MacOS subject = MacOS.from(ctx, absolutePath);
        Assertions.assertEquals(subject.os(), "macOS");
        Assertions.assertEquals(subject.file_type(), "macOS");
        Assertions.assertTrue(subject.macos());
        Assertions.assertFalse(subject.iphone());
        Assertions.assertFalse(subject.ipad());
        Assertions.assertFalse(subject.universal());

        Assertions.assertEquals(subject.file(), absolutePath);
        Assertions.assertEquals(subject.build_version(), "1");
        Assertions.assertEquals(subject.release_version(), "1.0");
        Assertions.assertEquals(subject.name(), "GuiApp");
        Assertions.assertEquals(subject.bundle_name(), "GuiApp");
        Assertions.assertNull(subject.display_name());
        Assertions.assertEquals(subject.identifier(), "com.icyleaf.macos.GUIApp");
        Assertions.assertEquals(subject.bundle_id(), "com.icyleaf.macos.GUIApp");
        Assertions.assertEquals(subject.device_type(), "macOS");
        Assertions.assertEquals(subject.min_os_version(), "11.3");
        Assertions.assertEquals(subject.min_system_version(), "11.3");

        Value hashSubscriptLambda = subject.getContext().eval("ruby", "-> recv, arg { recv[arg] }");
        Value res = hashSubscriptLambda.execute(subject.info().getValue(), "CFBundleVersion");
        Assertions.assertEquals(res.asString(), "1");

        res = hashSubscriptLambda.execute(subject.info().getValue(), "CFBundleShortVersionString");
        Assertions.assertEquals(res.asString(), "1.0");

        Assertions.assertArrayEquals(subject.archs(), new String[]{"x86_64", "arm64"});
        Assertions.assertEquals(subject.release_type(), "Debug");

        Assertions.assertFalse(subject.mobileprovision_question());
        Assertions.assertFalse(subject.stored_question());

        // it { expect(subject.info).to be_kind_of AppInfo::InfoPlist }
        // no need to test this since we've already accessed properties of .info earlier

        Assertions.assertNotNull(subject.icons_(true));
        Assertions.assertNotNull(subject.icons_(true).sets());
        Assertions.assertEquals(subject.icons_(true).sets().size(), 3);

        subject.clear();
        ctx.close();
    }

    @Test
    void signedApp() {
        Context ctx = createContext();
        String resourceName = "apps/macos-signed.zip";
        String absolutePath = getResourcesAbsolutePath(resourceName);

        MacOS subject = MacOS.from(ctx, absolutePath);
        Assertions.assertEquals(subject.os(), "macOS");
        Assertions.assertTrue(subject.macos());
        Assertions.assertFalse(subject.iphone());
        Assertions.assertFalse(subject.ipad());
        Assertions.assertFalse(subject.universal());

        Assertions.assertEquals(subject.file(), absolutePath);
        Assertions.assertEquals(subject.build_version(), "1");
        Assertions.assertEquals(subject.release_version(), "1.0");
        Assertions.assertEquals(subject.name(), "GuiApp");
        Assertions.assertEquals(subject.bundle_name(), "GuiApp");
        Assertions.assertNull(subject.display_name());
        Assertions.assertEquals(subject.identifier(), "com.icyleaf.macos.GUIApp");
        Assertions.assertEquals(subject.bundle_id(), "com.icyleaf.macos.GUIApp");
        Assertions.assertEquals(subject.device_type(), "macOS");
        Assertions.assertEquals(subject.min_os_version(), "11.3");
        Assertions.assertEquals(subject.min_system_version(), "11.3");

        Value hashSubscriptLambda = subject.getContext().eval("ruby", "-> recv, arg { recv[arg] }");
        Value res = hashSubscriptLambda.execute(subject.info().getValue(), "CFBundleVersion");
        Assertions.assertEquals(res.asString(), "1");

        res = hashSubscriptLambda.execute(subject.info().getValue(), "CFBundleShortVersionString");
        Assertions.assertEquals(res.asString(), "1.0");

        Assertions.assertArrayEquals(subject.archs(), new String[]{"x86_64", "arm64"});
        Assertions.assertEquals(subject.release_type(), "Release");

        Assertions.assertFalse(subject.stored_question());

        // it { expect(subject.info).to be_kind_of AppInfo::InfoPlist }

        Assertions.assertTrue(subject.mobileprovision_question());
        Assertions.assertEquals(subject.team_name(), "Samuel Sharps");
        Assertions.assertEquals(subject.profile_name(), "Layouts");
        Assertions.assertNotNull(subject.expired_date());
        Assertions.assertNotNull(subject.distribution_name());
        Assertions.assertNull(subject.icons_(true));

        subject.clear();
        ctx.close();
    }
}
