package io.github.transfusion.app_info_java_graalvm.AppInfo;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static io.github.transfusion.app_info_java_graalvm.AppInfo.Utilities.getResourcesAbsolutePath;

public class MobileProvisionTest {
    private Context createContext() {
        Context ctx = Context.newBuilder().allowAllAccess(true).build();
        ctx.eval("ruby", "Encoding.default_external = 'ISO-8859-1'");
        ctx.eval("ruby", "require 'app-info'");
        return ctx;
    }

    @Test
    void iOSDevelopment() {
        Context ctx = createContext();
        String resourceName = "mobileprovisions/ios_development.mobileprovision";
        String absolutePath = getResourcesAbsolutePath(resourceName);

        MobileProvision subject = MobileProvision.from(ctx, absolutePath);
        Assertions.assertEquals(subject.devices().length, 1);

        Assertions.assertEquals(subject.platform(), "ios");
        Assertions.assertArrayEquals(subject.platforms(), new String[]{"ios"});

        Assertions.assertNotNull(subject.name());
        Assertions.assertNotNull(subject.app_name());

        Assertions.assertEquals(subject.type(), "development");
        Assertions.assertTrue(subject.development_question());
        Assertions.assertFalse(subject.adhoc_question());
        Assertions.assertFalse(subject.appstore_question());
        Assertions.assertFalse(subject.inhouse_question());
        Assertions.assertFalse(subject.enterprise_question());
        Assertions.assertNotNull(subject.team_identifier());
        Assertions.assertNotNull(subject.team_name());

        Assertions.assertNotNull(subject.profile_name());
        Assertions.assertTrue(subject.created_date().isEqual(ZonedDateTime.parse("2020-07-21T16:43:27+08:00")));
        Assertions.assertTrue(subject.expired_date().isEqual(ZonedDateTime.parse("2021-07-21T16:43:27+08:00")));

        Value hashSubscriptLambda = subject.getContext().eval("ruby", "-> recv, arg { recv[arg] }");
        Value res = hashSubscriptLambda.execute(subject.entitlements(), "com.apple.developer.siri");
        Assertions.assertTrue(res.asBoolean());

        Assertions.assertEquals(1, subject.developer_certs().length);

        // additional testing for MobileProvision::DeveloperCertificate
        Assertions.assertEquals(subject.developer_certs()[0].name(), "Apple Development: Shen Wang (A3Z3NZQ8V3)");
        Assertions.assertTrue(subject.developer_certs()[0].created_date().isEqual(ZonedDateTime.parse("2021-05-28T13:33:21Z")));
        Assertions.assertTrue(subject.developer_certs()[0].expired_date().isEqual(ZonedDateTime.parse("2020-05-28T13:33:21Z")));

        Assertions.assertArrayEquals(subject.enabled_capabilities(), new String[]{"Access WiFi Information", "App Groups", "Apple Pay", "Associated Domains", "AutoFill Credential Provider", "ClassKit", "Data Protection", "HealthKit", "HomeKit", "Hotspot", "iCloud", "Inter-App Audio", "Multipath", "Network Extensions", "NFC Tag Reading", "Push Notifications", "SiriKit", "Personal VPN", "Wireless Accessory Configuration", "Wallet", "Low Latency HLS", "App Attest", "Extended Virtual Address Space", "Fonts", "MDM Managed Associated Domains", "Sign In with Apple"});

        ctx.close();
    }

    @Test
    void iOSAdhoc() {
        Context ctx = createContext();
        String resourceName = "mobileprovisions/ios_adhoc.mobileprovision";
        String absolutePath = getResourcesAbsolutePath(resourceName);

        MobileProvision subject = MobileProvision.from(ctx, absolutePath);

        Assertions.assertEquals(subject.devices().length, 1);
        Assertions.assertEquals(subject.platform(), "ios");
        Assertions.assertArrayEquals(subject.platforms(), new String[]{"ios"});

        Assertions.assertNotNull(subject.name());
        Assertions.assertNotNull(subject.app_name());

        Assertions.assertEquals(subject.type(), "adhoc");

        Assertions.assertFalse(subject.development_question());
        Assertions.assertTrue(subject.adhoc_question());
        Assertions.assertFalse(subject.appstore_question());
        Assertions.assertFalse(subject.inhouse_question());
        Assertions.assertFalse(subject.enterprise_question());
        Assertions.assertNotNull(subject.team_identifier());
        Assertions.assertNotNull(subject.team_name());
        Assertions.assertNotNull(subject.profile_name());
        Assertions.assertTrue(subject.created_date().isEqual(ZonedDateTime.parse("2020-07-21T16:44:54+08:00")));
        Assertions.assertTrue(subject.expired_date().isEqual(ZonedDateTime.parse("2020-10-24T16:40:28+08:00")));


        Assertions.assertEquals(1, subject.developer_certs().length);

        // additional testing for MobileProvision::DeveloperCertificate
        Assertions.assertEquals(subject.developer_certs()[0].name(), "Apple Distribution: Niceliving (Beijing) Technology Co., Ltd. (WKR87TTKML)");
        Assertions.assertTrue(subject.developer_certs()[0].created_date().isEqual(ZonedDateTime.parse("2020-10-24T08:40:28Z")));
        Assertions.assertTrue(subject.developer_certs()[0].expired_date().isEqual(ZonedDateTime.parse("2019-10-25T08:40:28Z")));
        Assertions.assertArrayEquals(subject.enabled_capabilities(), new String[]{"In-App Purchase", "GameKit", "Access WiFi Information", "App Groups", "Apple Pay", "Associated Domains", "AutoFill Credential Provider", "ClassKit", "Data Protection", "HealthKit", "HomeKit", "Hotspot", "iCloud", "Inter-App Audio", "Multipath", "Network Extensions", "NFC Tag Reading", "Push Notifications", "SiriKit", "Personal VPN", "Wireless Accessory Configuration", "Wallet", "Low Latency HLS", "App Attest", "Extended Virtual Address Space", "Fonts", "MDM Managed Associated Domains", "Sign In with Apple"});
    }

    @Test
    void iOSAppStore() {
        Context ctx = createContext();
        String resourceName = "mobileprovisions/ios_appstore.mobileprovision";
        String absolutePath = getResourcesAbsolutePath(resourceName);

        MobileProvision subject = MobileProvision.from(ctx, absolutePath);

        Assertions.assertNull(subject.devices());
        Assertions.assertEquals(subject.platform(), "ios");
        Assertions.assertArrayEquals(subject.platforms(), new String[]{"ios"});

        Assertions.assertNotNull(subject.name());
        Assertions.assertNotNull(subject.app_name());

        Assertions.assertEquals(subject.type(), "appstore");

        Assertions.assertFalse(subject.development_question());
        Assertions.assertFalse(subject.adhoc_question());
        Assertions.assertTrue(subject.appstore_question());
        Assertions.assertFalse(subject.inhouse_question());
        Assertions.assertFalse(subject.enterprise_question());
        Assertions.assertNotNull(subject.team_identifier());
        Assertions.assertNotNull(subject.team_name());
        Assertions.assertNotNull(subject.profile_name());
        System.out.println(subject.created_date());
        System.out.println(subject.expired_date());
        Assertions.assertTrue(subject.created_date().isEqual(ZonedDateTime.parse("2020-07-21T16:45:25+08:00")));
        Assertions.assertTrue(subject.expired_date().isEqual(ZonedDateTime.parse("2020-10-24T16:40:28+08:00")));


        Assertions.assertEquals(1, subject.developer_certs().length);

        // additional testing for MobileProvision::DeveloperCertificate
        Assertions.assertEquals(subject.developer_certs()[0].name(), "Apple Distribution: Niceliving (Beijing) Technology Co., Ltd. (WKR87TTKML)");

        Assertions.assertTrue(subject.developer_certs()[0].created_date().isEqual(ZonedDateTime.parse("2020-10-24T08:40:28Z")));
        Assertions.assertTrue(subject.developer_certs()[0].expired_date().isEqual(ZonedDateTime.parse("2019-10-25T08:40:28Z")));
        Assertions.assertArrayEquals(subject.enabled_capabilities(), new String[]{"In-App Purchase", "GameKit", "Access WiFi Information", "App Groups", "Apple Pay", "Associated Domains", "AutoFill Credential Provider", "ClassKit", "Data Protection", "HealthKit", "HomeKit", "Hotspot", "iCloud", "Inter-App Audio", "Multipath", "Network Extensions", "NFC Tag Reading", "Push Notifications", "SiriKit", "Personal VPN", "Wireless Accessory Configuration", "Wallet", "Low Latency HLS", "App Attest", "Extended Virtual Address Space", "Fonts", "MDM Managed Associated Domains", "Sign In with Apple"});
    }
}
