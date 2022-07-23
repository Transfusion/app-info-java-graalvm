package io.github.transfusion.app_info_java_graalvm.AppInfo;

import org.graalvm.collections.Pair;
import org.graalvm.polyglot.Context;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import static io.github.transfusion.app_info_java_graalvm.AppInfo.Utilities.getResourcesAbsolutePath;

public class APKTest {
    private Context createContext() {
        Context ctx = Context.newBuilder().allowAllAccess(true).build();
        ctx.eval("ruby", "Encoding.default_external = 'ISO-8859-1'");
        ctx.eval("ruby", "require 'app-info'");
        return ctx;
    }

    @Nested
    class PhoneOrTablet {
        private Context createContext() {
            Context ctx = Context.newBuilder().allowAllAccess(true).build();
            ctx.eval("ruby", "Encoding.default_external = 'ISO-8859-1'");
            ctx.eval("ruby", "require 'app-info'");
            return ctx;
        }

        @Test
        @DisplayName("with Android min SDK under 24")
        void minSDKUnder24() {
            Context ctx = createContext();
            String resourceName = "apps/android.apk";
            String absolutePath = getResourcesAbsolutePath(resourceName);

            APK subject = APK.from(ctx, absolutePath);

            Assertions.assertEquals(subject.size(), 4000563);
            Assertions.assertEquals(subject.humanSize(), "3.82 MB");
            Assertions.assertEquals(subject.os(), "Android");
            Assertions.assertEquals(subject.device_type(), "Phone");
            Assertions.assertEquals(subject.file(), absolutePath);

//        it { expect(subject.apk).to be_a Android::Apk }
            Assertions.assertEquals(subject.release_version(), "2.1.0");
            Assertions.assertEquals(subject.build_version(), "10");
            Assertions.assertEquals(subject.name(), "AppInfoDemo");
            Assertions.assertEquals(subject.bundle_id(), "com.icyleaf.appinfodemo");
            Assertions.assertEquals(subject.identifier(), "com.icyleaf.appinfodemo");

            Assertions.assertEquals(subject.icons().length, 6);
            // own assertions
            Assertions.assertEquals(subject.icons()[0].name(), "YB.webp");
            Assertions.assertArrayEquals(subject.icons()[0].dimensions(), new Long[]{48L, 48L});
            // own assertions end

            Assertions.assertEquals(subject.min_sdk_version(), 14L);
            Assertions.assertEquals(subject.target_sdk_version(), 31L);
            Assertions.assertEquals(subject.sign_version(), "v1");

            Assertions.assertEquals(subject.activities().length, 2);
            Assertions.assertEquals(subject.services().length, 0);
            Assertions.assertEquals(subject.components().length, 2);

            Assertions.assertEquals(subject.use_permissions()[0], "android.permission.ACCESS_NETWORK_STATE");
            Assertions.assertEquals(subject.use_features()[0], "android.hardware.bluetooth");

            //      it { expect(subject.manifest).to be_kind_of Android::Manifest }

            Assertions.assertArrayEquals(subject.manifest().use_permissions(), subject.apk().manifest().use_permissions());

            Assertions.assertArrayEquals(subject.deep_links(), new String[]{"icyleaf.com"});
            Assertions.assertArrayEquals(subject.schemes(), new String[]{"appinfo"});
            Assertions.assertEquals(subject.certificates()[0].path(), "META-INF/CERT.RSA");

            // it { expect(subject.certificates.first.certificate).to be_kind_of(OpenSSL::X509::Certificate) }
            //       it { expect(subject.signs.first).to be_kind_of(AppInfo::APK::Sign) }

            Assertions.assertEquals(subject.signs()[0].path(), "META-INF/CERT.RSA");

            // it { expect(subject.signs.first.sign).to be_kind_of(OpenSSL::PKCS7) }


            // Android::APK related assertions
            Assertions.assertEquals(subject.apk().size(), 4000563);
            Assertions.assertEquals(subject.apk().digest_("sha1"), "7275559051dbaf93e1c605e66729b3068665e128");

            Assertions.assertEquals(subject.apk().time().toLocalTime(), ZonedDateTime.parse("1981-01-01T01:01:02+07:30").toLocalTime());

            List<Pair<String, Long>> list = new ArrayList<>();
            subject.apk().each_file_((s1, s2) -> list.add(Pair.create(s1, s2.getMember("size").execute().asLong())));

            Assertions.assertEquals(list.size(), 733);
            Pair<String, Long> last = list.get(list.size() - 1);
            Assertions.assertEquals(last.getLeft(), "META-INF/MANIFEST.MF");
            Assertions.assertEquals(last.getRight(), 52393);

            Assertions.assertEquals(subject.apk().file("META-INF/MANIFEST.MF").getMember("size").execute().asLong(), 52393);

            List<String> list2 = new ArrayList<>();
            subject.apk().each_entry_(entry -> list2.add(entry.getMember("name").execute().asString()));
            Assertions.assertEquals(list2.size(), 733);
            Assertions.assertEquals(list2.get(list2.size() - 1), "META-INF/MANIFEST.MF");

            List<String> uniqueIcons = subject.apk().icon().getMember("keys").execute().as(List.class);
            Assertions.assertEquals(uniqueIcons.size(), 6);

            Assertions.assertEquals(subject.apk().icon_by_id_("res/uF.xml").getMember("size").execute().asLong(), 1);

            Assertions.assertEquals(subject.apk().label(null), "AppInfoDemo");

            subject.clear();
            ctx.close();
        }

        @Test
        @DisplayName("with Android min SDK 24+")
        void minSDK24Plus() {
            Context ctx = createContext();
            String resourceName = "apps/android-24.apk";
            String absolutePath = getResourcesAbsolutePath(resourceName);

            APK subject = APK.from(ctx, absolutePath);

            Assertions.assertEquals(subject.sign_version(), "unknown");

            subject.clear();
            ctx.close();
        }
    }

    @Test
    void Wear() {
        Context ctx = createContext();
        String resourceName = "apps/wear.apk";
        String absolutePath = getResourcesAbsolutePath(resourceName);

        APK subject = APK.from(ctx, absolutePath);

        Assertions.assertEquals(subject.os(), "Android");
        Assertions.assertTrue(subject.wear());
        Assertions.assertFalse(subject.tv());
        Assertions.assertFalse(subject.automotive());

        // it { expect(subject.os).to eq AppInfo::Platform::ANDROID }

        Assertions.assertEquals(subject.device_type(), "Watch");
        Assertions.assertEquals(subject.file(), absolutePath);

        // it { expect(subject.apk).to be_a Android::Apk }
        Assertions.assertEquals(subject.release_version(), "1.0");
        Assertions.assertEquals(subject.build_version(), "1");

        Assertions.assertEquals(subject.name(), "AppInfoWearDemo");
        Assertions.assertEquals(subject.bundle_id(), "com.icyleaf.appinfoweardemo");
        Assertions.assertEquals(subject.identifier(), "com.icyleaf.appinfoweardemo");

        Assertions.assertEquals(subject.icons().length, 4);
        Assertions.assertEquals(subject.min_sdk_version(), 21);
        Assertions.assertEquals(subject.target_sdk_version(), 23);


        subject.clear();
        ctx.close();
    }

    @Test
    void TV() {
        Context ctx = createContext();
        String resourceName = "apps/tv.apk";
        String absolutePath = getResourcesAbsolutePath(resourceName);

        APK subject = APK.from(ctx, absolutePath);

        Assertions.assertEquals(subject.os(), "Android");
        Assertions.assertFalse(subject.wear());
        Assertions.assertTrue(subject.tv());
        Assertions.assertFalse(subject.automotive());

        // it { expect(subject.os).to eq AppInfo::Platform::ANDROID }
        Assertions.assertEquals(subject.device_type(), "Television");
        Assertions.assertEquals(subject.file(), absolutePath);

        // it { expect(subject.apk).to be_a Android::Apk }
        Assertions.assertEquals(subject.build_version(), "1");
        Assertions.assertEquals(subject.release_version(), "1.0");
        Assertions.assertEquals(subject.name(), "AppInfoTVDemo");
        Assertions.assertEquals(subject.bundle_id(), "com.icyleaf.appinfotvdemo");

        Assertions.assertEquals(subject.identifier(), "com.icyleaf.appinfotvdemo");

        Assertions.assertEquals(subject.icons().length, 4);
        Assertions.assertEquals(subject.min_sdk_version(), 23);
        Assertions.assertEquals(subject.target_sdk_version(), 23);

        subject.clear();
        ctx.close();
    }

    @Test
    void Automotive() {
        Context ctx = createContext();
        String resourceName = "apps/automotive.apk";
        String absolutePath = getResourcesAbsolutePath(resourceName);

        APK subject = APK.from(ctx, absolutePath);

        Assertions.assertEquals(subject.os(), "Android");
        Assertions.assertFalse(subject.wear());
        Assertions.assertFalse(subject.tv());
        Assertions.assertTrue(subject.automotive());

        // it { expect(subject.os).to eq AppInfo::Platform::ANDROID }
        Assertions.assertEquals(subject.device_type(), "Automotive");
        Assertions.assertEquals(subject.file(), absolutePath);

        // it { expect(subject.apk).to be_a Android::Apk }
        Assertions.assertEquals(subject.build_version(), "3");
        Assertions.assertEquals(subject.release_version(), "2.0");
        Assertions.assertEquals(subject.name(), "AutoMotive");
        Assertions.assertEquals(subject.bundle_id(), "com.icyleaf.appinfo.automotive");

        Assertions.assertEquals(subject.identifier(), "com.icyleaf.appinfo.automotive");

        Assertions.assertEquals(subject.icons().length, 6);
        Assertions.assertEquals(subject.min_sdk_version(), 29);
        Assertions.assertEquals(subject.target_sdk_version(), 31);

        subject.clear();
        ctx.close();
    }
}
