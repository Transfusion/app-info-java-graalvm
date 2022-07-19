package io.github.transfusion.app_info_java_graalvm.AppInfo;

import io.github.transfusion.app_info_java_graalvm.AppInfo.Protobuf.Attribute;
import io.github.transfusion.app_info_java_graalvm.AppInfo.Protobuf.Node;
import org.graalvm.polyglot.Context;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static io.github.transfusion.app_info_java_graalvm.AppInfo.Utilities.getResourcesAbsolutePath;

public class AABTest {
    private Context createContext() {
        Context ctx = Context.newBuilder().allowAllAccess(true).build();
        ctx.eval("ruby", "Encoding.default_external = 'ISO-8859-1'");
        ctx.eval("ruby", "require 'app-info'");
        return ctx;
    }


    @Test
    void aabPhoneOrTablet() {
        Context ctx = createContext();
        String resourceName = "apps/android.aab";
        String absolutePath = getResourcesAbsolutePath(resourceName);

        AAB subject = AAB.from(ctx, absolutePath);

        Assertions.assertEquals(subject.size(), 3618865);
        Assertions.assertEquals(subject.humanSize(), "3.45 MB");
        Assertions.assertEquals(subject.os(), "Android");

        // own assertion
        Assertions.assertEquals(subject.device_type(), "Phone");
        Assertions.assertFalse(subject.wear());
        Assertions.assertFalse(subject.tv());
        Assertions.assertFalse(subject.automotive());

        Assertions.assertEquals(subject.min_sdk_version(), 14);
        Assertions.assertEquals(subject.target_sdk_version(), 31);

        Assertions.assertArrayEquals(subject.deep_links(), new String[]{"icyleaf.com"});
        Assertions.assertArrayEquals(subject.schemes(), new String[]{"appinfo"});

        Assertions.assertEquals(subject.certificates()[0].path(), "META-INF/KEY0.RSA");

        Assertions.assertTrue(subject.certificates()[0].certificate().getMember("is_a?").
                execute(ctx.eval("ruby", "OpenSSL::X509::Certificate")).asBoolean());

        Assertions.assertEquals(subject.signs()[0].path(), "META-INF/KEY0.RSA");
        Assertions.assertTrue(subject.signs()[0].sign().getMember("is_a?").
                execute(ctx.eval("ruby", "OpenSSL::PKCS7")).asBoolean());

        Assertions.assertEquals(subject.activities().length, 2);
        Assertions.assertEquals(subject.services().length, 0);
        Assertions.assertTrue(subject.components().hasIterator());

        Assertions.assertArrayEquals(subject.use_permissions(), new String[]{"android.permission.ACCESS_NETWORK_STATE"});
        Assertions.assertArrayEquals(subject.use_features(), new String[]{"android.hardware.bluetooth"});

        // it { expect(subject.manifest).to be_kind_of AppInfo::Protobuf::Manifest }
        /*
         * irb(main):217:0> i.manifest.activities[0].intent_filter[0].action[0].attributes["name"]
         * =>
         * #<AppInfo::Protobuf::Attribute:0x177d6f8
         *  @name="name",
         *  @namespace="http://schemas.android.com/apk/res/android",
         *  @resource_id=16842755,
         *  @resources=nil,
         *  @value="android.intent.action.VIEW">
         * irb(main):218:0> i.manifest.activities[0].intent_filter[0].action[0].name
         * => "android.intent.action.VIEW"
         */
        List<Node> activities = List.of(subject.activities());
        Assertions.assertEquals(activities.size(), 2);

        Node firstActivity = activities.get(0);
        List<Node> intentFilters = List.of(firstActivity.getChild("intent_filter"));
        Assertions.assertEquals(intentFilters.size(), 2);

        Node intentFilter = intentFilters.get(0);

        Assertions.assertEquals(intentFilter.children_(), Arrays.asList("action", "category", "data"));
        Node[] children = intentFilter.getChild("action");
        Assertions.assertEquals(children.length, 1);
        Node actionNode = children[0];
        Assertions.assertTrue(actionNode.children_().isEmpty());
        Assertions.assertEquals(actionNode.attributes_(), List.of("name"));

        Attribute nameAttr = actionNode.getAttribute("name");
        Assertions.assertEquals("name", nameAttr.name());
        Assertions.assertEquals("http://schemas.android.com/apk/res/android", nameAttr.namespace());
        Assertions.assertEquals(16842755, nameAttr.resource_id());

        Assertions.assertTrue(nameAttr.value().isString());
        Assertions.assertEquals("android.intent.action.VIEW", nameAttr.value().asString());

        subject.clear();
        ctx.close();
    }
}
