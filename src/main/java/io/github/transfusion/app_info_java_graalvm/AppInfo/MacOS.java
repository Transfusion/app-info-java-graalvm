package io.github.transfusion.app_info_java_graalvm.AppInfo;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.time.ZonedDateTime;

public abstract class MacOS extends AbstractPolyglotAdapter {
    public static MacOS from(Context polyglot, String path) {
        polyglot.eval("ruby", "require 'app-info'");
        return polyglot.eval("ruby", "AppInfo::Macos.new('" + path + "')").as(MacOS.class);
    }

    public abstract String file();

    public abstract String info_path();

    public abstract Long size();

    public String humanSize() {
        Context ctx = getContext();
        Value callSize = ctx.eval("ruby", "-> recv, arg { recv.size(human_size: arg) }");
        Value res = callSize.execute(getValue(), true);
        return res.asString();
    }

    public void clear() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv { recv.clear! }");
        v.execute(getValue());
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

//    delegators start here
//    def_delegators :info, :macos?, :iphone?, :ipad?, :universal?, :build_version, :name,
//            :release_version, :identifier, :bundle_id, :display_name,
//            :bundle_name, :min_system_version, :min_os_version, :device_type

    public abstract InfoPlist info();

    public boolean macos() {
        return getValue().getMember("macos?").execute().asBoolean();
    }

    public boolean iphone() {
        return getValue().getMember("iphone?").execute().asBoolean();
    }

    public boolean ipad() {
        return getValue().getMember("ipad?").execute().asBoolean();
    }

    public boolean universal() {
        return getValue().getMember("universal?").execute().asBoolean();
    }

    public abstract String build_version();

    public abstract String name();

    public abstract String release_version();

    public abstract String identifier();

    public abstract String bundle_id();

    public abstract String display_name();

    public abstract String bundle_name();

    public abstract String min_system_version();

    public abstract String min_os_version();

    public abstract String device_type();


    public abstract String team_name();

    public abstract String team_identifier();

    public abstract String profile_name();

    public abstract ZonedDateTime expired_date();

    public abstract String distribution_name();

    public abstract String release_type();

    public boolean stored_question() {
        return getValue().getMember("stored?").execute().asBoolean();
    }


    /**
     * @param convert whether to convert icons to PNG
     * @return may be null
     */
    public MacOSIconHashWrapper icons_(boolean convert) {
        Context ctx = getContext();
        Value callSize = ctx.eval("ruby", "-> recv, arg { recv.icons(convert: arg) }");
        Value res = callSize.execute(getValue(), convert);
        if (res.isNull()) return null;
        return res.as(MacOSIconHashWrapper.class);
    }

    public abstract String[] archs();

//    working but chose not to expose as of Jul. 13 2022
//    def hide_developer_certificates
//      mobileprovision.delete('DeveloperCertificates') if mobileprovision?
//    end

    public abstract MobileProvision mobileprovision();

    public boolean mobileprovision_question() {
        return getValue().getMember("mobileprovision?").execute().asBoolean();
    }

    public abstract String mobileprovision_path();

    public abstract String store_path();

    public abstract String binary_path();

}
