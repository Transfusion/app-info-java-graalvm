package io.github.transfusion.app_info_java_graalvm.AppInfo;

import io.github.transfusion.app_info_java_graalvm.MachO.FatFile;
import io.github.transfusion.app_info_java_graalvm.MachO.MachOFile;
import org.graalvm.polyglot.Context;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.github.transfusion.app_info_java_graalvm.AppInfo.Utilities.getResourcesAbsolutePath;

public class DSYMTest {
    private Context createContext() {
        Context ctx = Context.newBuilder().allowAllAccess(true).build();
        ctx.eval("ruby", "Encoding.default_external = 'ISO-8859-1'");
        ctx.eval("ruby", "require 'app-info'");
        return ctx;
    }

    @Test
    void singleMachO() {
        Context ctx = createContext();
        String resourceName = "dsyms/single_ios.dSYM.zip";
        String absolutePath = getResourcesAbsolutePath(resourceName);

        DSYM subject = DSYM.from(ctx, absolutePath);

        Assertions.assertEquals(subject.file_type(), "dSYM");
        Assertions.assertEquals(subject.object(), "iOS");

        Assertions.assertFalse(subject.macho_type_().getValue().isNull());
//        it { expect(subject.macho_type).to be_a ::MachO::MachOFile }
        Assertions.assertTrue(subject.macho_type_() instanceof MachOFile);
        Assertions.assertEquals(subject.release_version(), "1.0");
        Assertions.assertEquals(subject.build_version(), "1");

        Assertions.assertEquals(subject.identifier(), "com.icyleaf.iOS");
        Assertions.assertEquals(subject.bundle_id(), "com.icyleaf.iOS");

        Assertions.assertEquals(subject.machos().length, 1);

        Assertions.assertEquals(subject.machos()[0].type(), "dsym");
        Assertions.assertEquals(subject.machos()[0].uuid(), "ea9bbf2d-bfdd-3ce0-85b2-1cbe7152fca5");
        Assertions.assertEquals(subject.machos()[0].cpu_type(), "arm64");
        Assertions.assertEquals(subject.machos()[0].cpu_name(), "arm64");
        Assertions.assertEquals(subject.machos()[0].size(), 866911);
        Assertions.assertEquals(subject.machos()[0].humanSize(), "846.59 KB");

        // it { expect(subject.machos[0].to_h).to eq data }
        // no need to test this since we can get the raw value with getValue() anyways
        subject.clear();
        ctx.close();
    }

    @Test
    void multiMachO() {
        Context ctx = createContext();
        String resourceName = "dsyms/multi_ios.dSYM.zip";
        String absolutePath = getResourcesAbsolutePath(resourceName);

        DSYM subject = DSYM.from(ctx, absolutePath);

        Assertions.assertEquals(subject.file_type(), "dSYM");
        Assertions.assertEquals(subject.object(), "iOS");

        Assertions.assertFalse(subject.macho_type_().getValue().isNull());
//        it { expect(subject.macho_type).to be_a ::MachO::MachOFile }
        Assertions.assertTrue(subject.macho_type_() instanceof FatFile);
        Assertions.assertEquals(subject.release_version(), "1.0");
        Assertions.assertEquals(subject.build_version(), "2");

        Assertions.assertEquals(subject.identifier(), "com.icyleaf.iOS");
        Assertions.assertEquals(subject.bundle_id(), "com.icyleaf.iOS");

        Assertions.assertEquals(subject.machos().length, 2);

        Assertions.assertEquals(subject.machos()[0].type(), "dsym");
        Assertions.assertEquals(subject.machos()[0].uuid(), "26dfc15d-bdce-351f-b5de-6ee9f5dd6d85");
        Assertions.assertEquals(subject.machos()[0].cpu_type(), "arm");
        Assertions.assertEquals(subject.machos()[0].cpu_name(), "armv7");
        Assertions.assertEquals(subject.machos()[0].size(), 866526);
        Assertions.assertEquals(subject.machos()[0].humanSize(), "846.22 KB");

        // it { expect(subject.machos[0].to_h).to eq data[0] }

        Assertions.assertEquals(subject.machos()[1].type(), "dsym");
        Assertions.assertEquals(subject.machos()[1].uuid(), "17f58291-dd25-3fc8-9417-ccbe8163d33e");
        Assertions.assertEquals(subject.machos()[1].cpu_type(), "arm64");
        Assertions.assertEquals(subject.machos()[1].cpu_name(), "arm64");
        Assertions.assertEquals(subject.machos()[1].size(), 866911);
        Assertions.assertEquals(subject.machos()[1].humanSize(), "846.59 KB");

        // it { expect(subject.machos[1].to_h).to eq data[1] }

        subject.clear();
        ctx.close();

    }
}
