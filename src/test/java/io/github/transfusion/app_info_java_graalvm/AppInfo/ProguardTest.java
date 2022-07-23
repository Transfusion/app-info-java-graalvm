package io.github.transfusion.app_info_java_graalvm.AppInfo;

import org.graalvm.polyglot.Context;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.github.transfusion.app_info_java_graalvm.AppInfo.Utilities.getResourcesAbsolutePath;

public class ProguardTest {
    private Context createContext() {
        Context ctx = Context.newBuilder().allowAllAccess(true).build();
        ctx.eval("ruby", "Encoding.default_external = 'ISO-8859-1'");
        ctx.eval("ruby", "require 'app-info'");
        return ctx;
    }

    @Test
    void singleMapping() {
        Context ctx = createContext();
        String resourceName = "proguards/single_mapping.zip";
        String absolutePath = getResourcesAbsolutePath(resourceName);

        Proguard subject = Proguard.from(ctx, absolutePath);

        Assertions.assertEquals(subject.file_type(), "Proguard");
        Assertions.assertEquals(subject.uuid(), "81384aeb-4837-5f73-a771-417b4399a483");
        Assertions.assertTrue(subject.mapping_question());
        Assertions.assertFalse(subject.symbol_question());
        Assertions.assertFalse(subject.resource_question());
        Assertions.assertFalse(subject.manifest_question());
        Assertions.assertTrue(subject.manifest().isNull());
        Assertions.assertNull(subject.package_name());
        Assertions.assertNull(subject.version_name());
        Assertions.assertNull(subject.version_code());
        Assertions.assertNull(subject.release_version());
        Assertions.assertNull(subject.build_version());

        subject.clear();
        ctx.close();
    }

    @Test
    void fullMapping() {
        Context ctx = createContext();
        String resourceName = "proguards/full_mapping.zip";
        String absolutePath = getResourcesAbsolutePath(resourceName);

        Proguard subject = Proguard.from(ctx, absolutePath);

        Assertions.assertEquals(subject.file_type(), "Proguard");
        Assertions.assertEquals(subject.uuid(), "81384aeb-4837-5f73-a771-417b4399a483");
        Assertions.assertTrue(subject.mapping_question());
        Assertions.assertTrue(subject.symbol_question());
        Assertions.assertTrue(subject.resource_question());
        Assertions.assertTrue(subject.manifest_question());
        Assertions.assertFalse(subject.manifest().isNull());
        Assertions.assertEquals(subject.package_name(), "com.icyleaf.appinfo");
        Assertions.assertEquals(subject.version_name(), "1.0");
        Assertions.assertEquals(subject.version_code(), "1");
        Assertions.assertEquals(subject.release_version(), "1.0");
        Assertions.assertEquals(subject.build_version(), "1");

        subject.clear();
        ctx.close();
    }

    // unnecessary to port #CustomMappingFileName over since it interacts with the filesystem and creates some mock
    // mapping files, then does expect(parser.mapping?).to be true
    // the objective of testing this wrapper is primarily to ensure that everything exposed by the app_info gem is accessible
    // the two tests above already invoke parser.mapping?
}
