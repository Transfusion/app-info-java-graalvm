package io.github.transfusion.app_info_java_graalvm.AppInfo;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;


public abstract class IPA extends AbstractPolyglotAdapter {

    public static IPA from(String path) {
        Context polyglot = Context.newBuilder().
                allowAllAccess(true).build();
        polyglot.eval("ruby", "require 'app-info'");
        IPA ipa = polyglot.eval("ruby", "AppInfo::IPA.new('" + path + "')").as(IPA.class);
        ipa.setContext(polyglot);
        return ipa;
    }

    public abstract String info_path();

    public abstract String app_path();

    public abstract Long size();

    public String humanSize() {
        Context ctx = getContext();
        Value callSize = ctx.eval("ruby", "-> recv, arg { recv.size(human_size: arg) }");
        Value res = callSize.execute(getValue(), true);
        return res.asString();
    }

    /**
     * module Platform
     * MACOS = 'macOS'
     * IOS = 'iOS'
     * ANDROID = 'Android'
     * DSYM = 'dSYM'
     * PROGUARD = 'Proguard'
     * end
     *
     * @return a string from the above
     */
    public abstract String os();

    /**
     * alias file_type os
     *
     * @return a string from the above
     */
    public abstract String file_type();

//    Delegators start here
//    def_delegators :info, :iphone?, :ipad?, :universal?, :build_version, :name,
//            :release_version, :identifier, :bundle_id, :display_name,
//            :bundle_name, :min_sdk_version, :min_os_version, :device_type

    public abstract InfoPlist info();

    public boolean iphone() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv, arg { recv.iphone? }");
        Value res = v.execute(getValue(), true);
        return res.asBoolean();
    }

    public boolean ipad() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv, arg { recv.ipad? }");
        Value res = v.execute(getValue(), true);
        return res.asBoolean();
    }

    public boolean universal() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv, arg { recv.universal? }");
        Value res = v.execute(getValue(), true);
        return res.asBoolean();
    }

    public abstract String build_version();

    public abstract String name();

    public abstract String release_version();

    public abstract String identifier();

    public abstract String bundle_id();

    public abstract String display_name();

    public abstract String bundle_name();

    public abstract String min_sdk_version();

    public abstract String min_os_version();

    public abstract String device_type();


    public void clear() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv { recv.clear! }");
        v.execute(getValue());
    }
}
