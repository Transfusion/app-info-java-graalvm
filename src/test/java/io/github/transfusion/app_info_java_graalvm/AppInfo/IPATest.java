package io.github.transfusion.app_info_java_graalvm.AppInfo;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;

import static io.github.transfusion.app_info_java_graalvm.AppInfo.Utilities.getResourcesAbsolutePath;

public class IPATest {

    private Context createContext() {
        Context ctx = Context.newBuilder().
                allowAllAccess(true).build();
        ctx.eval("ruby", "Encoding.default_external = 'ISO-8859-1'");
        ctx.eval("ruby", "require 'app-info'");
        return ctx;
    }

    @Test
    void iPhone() {
        Context ctx = createContext();
        String resourceName = "apps/iphone.ipa";
        String absolutePath = getResourcesAbsolutePath(resourceName);
        IPA subject = IPA.from(ctx, absolutePath);

        Assertions.assertEquals(subject.os(), "iOS");
        Assertions.assertTrue(subject.iphone());
        Assertions.assertFalse(subject.ipad());
        Assertions.assertFalse(subject.universal());

        Assertions.assertEquals(subject.file(), absolutePath);
        Assertions.assertEquals(subject.build_version(), "5");
        Assertions.assertEquals(subject.release_version(), "1.2.3");
        Assertions.assertEquals(subject.name(), "AppInfoDemo");
        Assertions.assertNull(subject.display_name());

        Assertions.assertEquals(subject.identifier(), "com.icyleaf.AppInfoDemo");
        Assertions.assertEquals(subject.bundle_id(), "com.icyleaf.AppInfoDemo");
        Assertions.assertEquals(subject.device_type(), "iPhone");
        Assertions.assertEquals(subject.min_sdk_version(), "9.3");

        Value hashSubscriptLambda = subject.getContext().eval("ruby", "-> recv, arg { recv[arg] }");
        Value res = hashSubscriptLambda.execute(subject.info().getValue(), "CFBundleVersion");
        Assertions.assertEquals(res.asString(), "5");

        res = hashSubscriptLambda.execute(subject.info().getValue(), "CFBundleShortVersionString");
        Assertions.assertEquals(res.asString(), "1.2.3");

        Assertions.assertArrayEquals(subject.archs(), new String[]{"armv7", "arm64"});

        // testing icons start
        IPAIconHash[] icons = subject.icons_(true);
        Assertions.assertEquals(0, icons.length);

        Assertions.assertEquals(subject.release_type(), "AdHoc");
        Assertions.assertEquals(subject.build_type(), "AdHoc");
        Assertions.assertArrayEquals(subject.devices(), new String[]{"18cf53cddee60c52f9c97b1521e7cbf8342628da", "ff3804d33c70861cdf9a160a527ac49ac9789e6f", "b3a64d388bebbba0affd32199e802e846a44d041", "f3a437ba99f01d8d63d2865ea953dd1bd7611b60", "9d3884f7bea4d1d08d461801613a6bd764b454e6", "09346b519497d8e4667e2e4cc12e4271110fb7e3", "0eb19f068a76855bed306e7c0bd8fca5a5dda6f4", "968c43aa42d29382f1cd87f182e28f00703354cb", "1277fe49b34023c8175541e9159b4c3b28f82447", "5189009fa017d9be990a6ccd02d2263ebc887ea4", "1e52933925991f4a8bfc477225d47fd7159f1782", "286a23fda46c1d1564894f79d0148106344ad08e", "50456fcd9d6995ebbaa3f96d01a543892d603c24", "03aad109adc8d5ec50b879b5845a86d81f1e3fee", "b72ee6d24e5ad51225131afe29e841e651d55f7f", "4f6191e9ebc7e8ed6786f356e30808a974a4762a", "216315043535bb20a8955e35aa5e3332a468326a", "b511c860041ae7f39273dc287f8094d60d1b0a7d", "56a812ac46e6d4547f6cdf300eacfd67d69d8f37", "85d92cdd0304ce81c73b2b85b192c02190c1787f", "82e79ce6283e91810c436f517b8a2bf148e929aa", "f0bcb83e8817c6f23c29f164c7136e8e62c6d9b4", "8cd3461046f066d3ab7ced03911409d227642699", "f27e29e65079e46974213f3d033ef4de351980ea", "571efe033dcf304f6e8c56c427cd5455616b2fee", "eef4c91a9f0b8ae54ec5205155ad0f98e7ce818a", "9c318398b396e6a483760ba76b59db54f72a0e78", "8d63a82c51110842d3cd893a9e3f49203d07c993", "7e4bf0406a5ff48717d5d5d0aa22649186f1f570", "2a60d8ee901eae0bf14ad3c60f61c11b8988051e", "235654600cb8bf5aa608bdb4fd4c75815d8c7fd9", "6f72de53f5ee2385be81ad25a5e8558007932437", "49d9367c51ad9843d8034d03e139b6ee4c755795", "486bc9bf70577106533f747b835964fa968e319d", "e815bc33c7d0ff50a4c41086c27fea296877a9bf", "975d5ce0b69bd79220c64f1931d02d8cc87a931f", "ac2463ba6e16998266bbb33b88bec843f588ffeb", "f2177b4ba816568050b205b3ddee4b07399a759a", "41ef5c2bef5a2dd795fc31a496ac52b1a8448419", "fb72c399fa6ad414709f89cd1269b90138d7cec2", "c1281c3038d9d9496afedf1cce54c538fe177cfa", "9b8a81462cea104192d731a3b0ead74a161676a8", "7c071eb478dd42982601d416d68128aa51cab6ab", "8e8f8c97da557b856c241089ae836e64c982e31f", "768b480f04c7314829cb8904cfaece6bb360e8d3", "b51ea5df270e7e4598d014d2a5a6974d88d93855", "f97b5974c799991b97dda017d3fbd9e1e048790c", "90bf8b54bfe897ce026ae2b48764b79436570e82", "d889d7a431f2f90729fc217e41c378ebd251e5cb", "4617ce6291a489710b0c02bc3d34b70ec621ca7d", "2ad540554cd6d0d18fc50573b8d91ce1950313bd", "04c6603eac9595b4eac2e6fb7c4cf1c0b12ab770", "ad5df9dbe2ecc6fc233b8739e2f6b4edb1d9b525", "2776264987c22e74c2348a9274c7472f2a2d9f10", "4ee4120924499b6715718ebc9a7d49d605d9f1f9", "7d4c2cc34496c0fbcf5185d624b9365ba0d94a10", "681c34853073d8ba18477fc8480de797fc4a5656", "caf8dd3153e8d810d048aea106cdf9e8de85539d", "b695e47764d620cb9115c462e51366ce7d7cc79c", "eb54fd73f65a0bd99b78c27b33c171ab9388add3", "9927efa8b0283f56eb8088803768f440a21999c2", "94e9efc3af2532a57168f17a0460af2c012a4f38", "e90463e5de11135fe6b23a4ee34a8557aad6517a", "5975f2cf0cf7443119ef24d426c6774df5c13350", "fc1f3020fc668e9cd40aba20d91e251cf23b2786", "368c3bccada69a0fde885e965148a4e0f338c7bb", "0d7741bab73db6c23bf831747625c251d3328e5c", "f60774296d83368e9b4afe33757498aac596528a", "62ae9987e973f7586bf757de82e68538bde5b25f", "e3911c96aab4858f6722392578e3c6c840394e9e", "3a3a76cce72e77812d99d21de1c7b195b78feed5", "578553ef722cd6ae43a803aea4dcf4fc24ab5a70", "340ec7fea7480a4f9eca39df6aa9ac3db376750b", "e8170bf7369e017a9b43e904cdfbf512b4f434ec", "8c06c334fcb74bf005aac11463ae78604997d3f9", "27bd7955d9182d5425219779b557120edc3df222", "f17d2e10bac005968df78829964d76186f509b0f", "711c66432750046f887d8d724a770d61b18a353b", "94b8ed3b22a6d7b55e9f9b53d0243edfbf8d2c05", "dc2d75f1dd1ab08c602677a5fa818a70dffaf8d4", "e0021d47ee9bbf68e54b53a6ca0a609204bac227", "157b1db2fede88c02dead46b95edd183435799c8", "4475d6642f1994fc05620cbdaddd69c5ad48f017", "2ef262514aa15c985791cb8011d80b2241f1a4bd", "22e19a65e97e02b9d1b18b0f90faeda65b16e646", "67a4028bec4818cbaafa81db5d914999cabd828f", "75b4c99a7b44be6a8fe125b3868560787c23db2d", "5bee576c6a49eb7766d037cc48a580d913d9c17f", "dd315be58eb76608bd5cd4eb6c6e5d76301e203e", "7508fc847e1edcdd89f49b575d0ba282c3e6d8c9", "4f58e61c2f5a3e4cfd35570bbe44af29eb55bc3c", "1ac354586a96877b3e03c6d8c70d9f253b4a8d63", "32678374d9ac1e8c760a6fafe7273f6496f9b4f9", "1adb25f0165219f08ddfca03a2b1c5cef8d19f7d", "839404b5388b1c3cc3082f5050d6b9218c6b9e64", "faaeac1b99ee317dbfcaa4697212acc94711e442", "866121c6add2f1e36f0af80d5481028c7eebb716", "b47e23b7d4c7d3d530fd89149b316a51cff12e49", "86d0640fbfec72c34b96e1747d44b2238c7bf121", "a002ab5cca1491f25bd53cfa28947284877fa44d"});
        Assertions.assertEquals(subject.team_name(), "QYER Inc");
        Assertions.assertEquals(subject.profile_name(), "iOS Team Provisioning Profile: *");
        Assertions.assertTrue(subject.expired_date().isEqual(ZonedDateTime.parse("2017-07-27T17:44:49+08:00")));
        Assertions.assertEquals(subject.distribution_name(), "iOS Team Provisioning Profile: * - QYER Inc");

        Assertions.assertTrue(subject.mobileprovision_question());
        Assertions.assertEquals(13, subject.mobileprovision().developer_certs().length);

        Assertions.assertTrue(subject.metadata().isNull());
        Assertions.assertFalse(subject.metadata_question());

        Assertions.assertFalse(subject.stored_question());

        // it { expect(subject.info).to be_kind_of AppInfo::InfoPlist }
        // no need to test this since we've already accessed properties of .info earlier
        // it { expect(subject.mobileprovision).to be_kind_of AppInfo::MobileProvision }
        // same with this

        Assertions.assertEquals(0, subject.plugins().length);
        Assertions.assertEquals(0, subject.frameworks().length);

        subject.clear();
        ctx.close();
    }

    @Test
    void iPad() {
        Context ctx = createContext();
        String resourceName = "apps/ipad.ipa";
        String absolutePath = getResourcesAbsolutePath(resourceName);
        IPA subject = IPA.from(ctx, absolutePath);

        Assertions.assertEquals(subject.os(), "iOS");
        Assertions.assertFalse(subject.iphone());
        Assertions.assertTrue(subject.ipad());
        Assertions.assertFalse(subject.universal());

        Assertions.assertEquals(subject.file(), absolutePath);
        Assertions.assertEquals(subject.build_version(), "1");
        Assertions.assertEquals(subject.release_version(), "1.0");
        Assertions.assertEquals(subject.min_sdk_version(), "9.3");
        Assertions.assertEquals(subject.name(), "bundle");
        Assertions.assertEquals(subject.bundle_name(), "bundle");
        Assertions.assertNull(subject.display_name());
        Assertions.assertEquals(subject.identifier(), "com.icyleaf.bundle");
        Assertions.assertEquals(subject.bundle_id(), "com.icyleaf.bundle");
        Assertions.assertEquals(subject.device_type(), "iPad");
        Assertions.assertArrayEquals(subject.archs(), new String[]{"armv7", "arm64"});

        // testing icons start
        IPAIconHash[] icons = subject.icons_(true);
        Assertions.assertEquals(7, icons.length);
        IPAIconHash firstIcon = icons[0];
        Assertions.assertArrayEquals(firstIcon.dimensions(), new Long[]{58L, 58L});
        Assertions.assertEquals(firstIcon.name(), "AppIcon29x29@2x~ipad.png");

        Assertions.assertEquals(subject.release_type(), "Enterprise");
        Assertions.assertEquals(subject.build_type(), "Enterprise");
        Assertions.assertNull(subject.devices());

        Assertions.assertEquals(subject.team_name(), "QYER Inc");
        Assertions.assertEquals(subject.profile_name(), "XC: *");
        Assertions.assertTrue(subject.expired_date().isEqual(ZonedDateTime.parse("2017-04-12T17:37:18+08:00")));
        Assertions.assertEquals(subject.distribution_name(), "XC: * - QYER Inc");

        Assertions.assertTrue(subject.mobileprovision_question());
        Assertions.assertEquals(1, subject.mobileprovision().developer_certs().length);

        Assertions.assertTrue(subject.metadata().isNull());
        Assertions.assertFalse(subject.metadata_question());
        Assertions.assertFalse(subject.stored_question());
        Assertions.assertTrue(subject.ipad());

        // it { expect(subject.info).to be_kind_of AppInfo::InfoPlist }
        // no need to test this since we've already accessed properties of .info earlier
        // it { expect(subject.mobileprovision).to be_kind_of AppInfo::MobileProvision }
        // same with this

        Value hashSubscriptLambda = subject.getContext().eval("ruby", "-> recv, arg { recv[arg] }");
        Value res = hashSubscriptLambda.execute(subject.info().getValue(), "CFBundleVersion");
        Assertions.assertEquals(res.asString(), "1");

        res = hashSubscriptLambda.execute(subject.mobileprovision().getValue(), "TeamName");
        Assertions.assertEquals(res.asString(), "QYER Inc");
        Assertions.assertEquals(subject.team_name(), "QYER Inc");

        Assertions.assertEquals(0, subject.plugins().length);
        Assertions.assertEquals(0, subject.frameworks().length);

        subject.clear();
        ctx.close();
    }

    @Test
    void embedded() {
        Context ctx = createContext();
        String resourceName = "apps/embedded.ipa";
        String absolutePath = getResourcesAbsolutePath(resourceName);
        IPA subject = IPA.from(ctx, absolutePath);

        Assertions.assertEquals(subject.os(), "iOS");
        Assertions.assertFalse(subject.iphone());
        Assertions.assertFalse(subject.ipad());
        Assertions.assertTrue(subject.universal());

        Assertions.assertEquals(subject.file(), absolutePath);
        Assertions.assertEquals(subject.build_version(), "1");
        Assertions.assertEquals(subject.release_version(), "1.0");
        Assertions.assertEquals(subject.min_sdk_version(), "14.3");

        Assertions.assertEquals(subject.name(), "Demo");
        Assertions.assertEquals(subject.bundle_name(), "Demo");
        Assertions.assertNull(subject.display_name());

        Assertions.assertEquals(subject.identifier(), "com.icyleaf.test.Demo");
        Assertions.assertEquals(subject.bundle_id(), "com.icyleaf.test.Demo");

        Assertions.assertEquals(subject.device_type(), "Universal");

        Assertions.assertArrayEquals(subject.archs(), new String[]{"arm64"});

        Assertions.assertEquals(subject.release_type(), "Enterprise");
        Assertions.assertEquals(subject.build_type(), "Enterprise");

        Assertions.assertNull(subject.devices());

        Assertions.assertTrue(subject.mobileprovision_question());
        Assertions.assertEquals(2, subject.mobileprovision().developer_certs().length);

        Assertions.assertTrue(subject.metadata().isNull());
        Assertions.assertFalse(subject.metadata_question());
        Assertions.assertFalse(subject.stored_question());

//        it { expect(subject.info).to be_kind_of AppInfo::InfoPlist }
//        it { expect(subject.mobileprovision).to be_kind_of AppInfo::MobileProvision }

        Value hashSubscriptLambda = subject.getContext().eval("ruby", "-> recv, arg { recv[arg] }");
        Value res = hashSubscriptLambda.execute(subject.info().getValue(), "CFBundleVersion");
        Assertions.assertEquals(res.asString(), "1");

        Assertions.assertEquals(1, subject.plugins().length);

        Assertions.assertEquals("Notification", subject.plugins()[0].name());
        Assertions.assertEquals(subject.plugins()[0].release_version(), "1.0");
        Assertions.assertEquals(subject.plugins()[0].build_version(), "1");
        //     it { expect(subject.plugins[0].info).to be_a AppInfo::InfoPlist }
        Assertions.assertEquals(subject.plugins()[0].bundle_id(), "com.icyleaf.test.Demo.Notification");

        Assertions.assertEquals(subject.frameworks().length, 1);
        Assertions.assertFalse(subject.frameworks()[0].lib_question());
        Assertions.assertEquals(subject.frameworks()[0].name(), "CarPlay.framework");
        Assertions.assertNull(subject.frameworks()[0].release_version());
        Assertions.assertNull(subject.frameworks()[0].build_version());
        Assertions.assertNull(subject.frameworks()[0].bundle_id());

        Assertions.assertNull(subject.frameworks()[0].macho_());

        subject.clear();
        ctx.close();
    }

}
