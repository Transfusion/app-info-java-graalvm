package io.github.transfusion.app_info_java_graalvm.AppInfo;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import io.github.transfusion.app_info_java_graalvm.Android.Manifest;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public abstract class APK extends AbstractPolyglotAdapter {

    public static APK from(Context polyglot, String path) {
        polyglot.eval("ruby", "require 'app-info'");
        return polyglot.eval("ruby", "AppInfo::APK.new('" + path + "')").as(APK.class);
    }

    /**
     * @return path to the APK on disk
     */
    public abstract String file();

    public abstract Long size();

    public String humanSize() {
        Context ctx = getContext();
        Value callSize = ctx.eval("ruby", "-> recv, arg { recv.size(human_size: arg) }");
        Value res = callSize.execute(getValue(), true);
        return res.asString();
    }

    public abstract String os();

    public abstract String file_type();

    /*def_delegators :apk, :manifest, :resource, :dex


    def_delegators :manifest, :version_name, :package_name, :target_sdk_version,
            :components, :services, :use_permissions, :use_features,
            :deep_links, :schemes*/

    public abstract Manifest manifest();

    /**
     * @return Android::Resource
     */
    public abstract Value resource();

    /**
     * @return Android::Dex
     */
    public abstract Value dex();

    public abstract String[] use_permissions();

    public abstract String[] use_features();

    public abstract String[] deep_links();

    public abstract String[] schemes();

    public abstract Long target_sdk_version();

    public abstract String release_version();

    public abstract String identifier();

    public abstract String bundle_id();

    public abstract String version_code();

    public abstract String build_version();

    public abstract String name();

    /**
     * @return AppInfo::APK::Device
     */
    public abstract String device_type();

    public boolean wear() {
        return getValue().getMember("wear?").execute().asBoolean();
    }

    public boolean tv() {
        return getValue().getMember("tv?").execute().asBoolean();
    }

    public boolean automotive() {
        return getValue().getMember("automotive?").execute().asBoolean();
    }

    public abstract Long min_sdk_version();

    public abstract Long min_os_version();

    public abstract String sign_version();

    public abstract APK.Sign[] signs();

    public abstract APK.Certificate[] certificates();

    /**
     * def activities
     * components.select { |c| c.type == 'activity' }
     * end
     *
     * @return Android::Manifest::Component as of the time of writing
     * instead of Android::Manifest::Activity
     */
    public abstract Manifest.Component[] activities();

    public abstract Manifest.Component[] services();

    public abstract Manifest.Component[] components();

    public abstract io.github.transfusion.app_info_java_graalvm.Android.APK apk();

    public abstract AndroidIconHash[] icons();

    public static abstract class Sign extends AbstractPolyglotAdapter {
        public abstract String path();

        /**
         * @return <a href="https://ruby-doc.org/stdlib-3.1.1/libdoc/openssl/rdoc/OpenSSL/PKCS7.html">OpenSSL::PKCS7</a>
         */
        public abstract Value sign();
    }


    public static abstract class Certificate extends AbstractPolyglotAdapter {
        public abstract String path();

        /**
         * @return <a href="https://docs.ruby-lang.org/en/master/OpenSSL/X509/Certificate.html">OpenSSL::X509::Certificate</a>
         */
        public abstract Value certificate();
    }

    public void clear() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv { recv.clear! }");
        v.execute(getValue());
    }
}
