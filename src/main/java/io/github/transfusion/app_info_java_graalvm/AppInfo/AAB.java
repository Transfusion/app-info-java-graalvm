package io.github.transfusion.app_info_java_graalvm.AppInfo;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import io.github.transfusion.app_info_java_graalvm.AppInfo.Protobuf.Node;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.proxy.ProxyIterator;

public abstract class AAB extends AbstractPolyglotAdapter {

    public static AAB from(Context polyglot, String path) {
        polyglot.eval("ruby", "require 'app-info'");
        return polyglot.eval("ruby", "AppInfo::AAB.new('" + path + "')").as(AAB.class);
    }

    public abstract Long size();

    public String humanSize() {
        Context ctx = getContext();
        Value callSize = ctx.eval("ruby", "-> recv, arg { recv.size(human_size: arg) }");
        Value res = callSize.execute(getValue(), true);
        return res.asString();
    }

    public abstract String os();

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

    public abstract Long target_sdk_version();


    public abstract String[] use_features();

    public abstract String[] use_permissions();

    public abstract Node[] activities();

    public abstract Node[] services();

    /**
     * def components
     *      \@components ||= manifest.components.transform_values
     * end
     * @return an Enumerator
     */
    public abstract Value components();

    public abstract String sign_version();

    public abstract APK.Sign[] signs();

    public abstract APK.Certificate[] certificates();


    public void clear() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv { recv.clear! }");
        v.execute(getValue());
    }

    public abstract String[] deep_links();

    public abstract String[] schemes();
}
