package io.github.transfusion.app_info_java_graalvm.AppInfo;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public abstract class InfoPlist extends AbstractPolyglotAdapter {

    public static InfoPlist from(Context polyglot, String path) {
        polyglot.eval("ruby", "require 'app-info'");
        InfoPlist infoPlist = polyglot.eval("ruby", "AppInfo::InfoPlist.new('" + path + "')").as(InfoPlist.class);
        infoPlist.setContext(polyglot);
        return infoPlist;
    }

    public static InfoPlist from(String path) {
        Context polyglot = Context.newBuilder().
                allowAllAccess(true).build();
        return InfoPlist.from(polyglot, path);
    }

    public abstract String version();

    public abstract String build_version();

    public abstract String release_version();

    public abstract String identifier();

    /**
     * alias bundle_id identifier
     */
    public abstract String bundle_id();

    public abstract String name();

    public abstract String display_name();

    public abstract String bundle_name();

    public abstract String min_os_version();

    /**
     * Extract the Minimum OS Version from the Info.plist (iOS Only)
     * may be nil (null)
     */
    public abstract String min_sdk_version();

    /**
     * Extract the Minimum OS Version from the Info.plist (macOS Only)
     * may be nil (null)
     */
    public abstract String min_system_version();

    /**
     * def icons
     * <p>
     * \@icons ||= ICON_KEYS[device_type]
     * end
     */
    public abstract String[] icons();

    public abstract String device_type();

    public boolean iphone() {
        Context ctx = getContext();
        Value callSize = ctx.eval("ruby", "-> recv, arg { recv.iphone? }");
        Value res = callSize.execute(getValue(), true);
        return res.asBoolean();
    }

    public boolean ipad() {
        Context ctx = getContext();
        Value callSize = ctx.eval("ruby", "-> recv, arg { recv.ipad? }");
        Value res = callSize.execute(getValue(), true);
        return res.asBoolean();
    }

    public boolean universal() {
        Context ctx = getContext();
        Value callSize = ctx.eval("ruby", "-> recv, arg { recv.universal? }");
        Value res = callSize.execute(getValue(), true);
        return res.asBoolean();
    }

    public boolean macos() {
        Context ctx = getContext();
        Value callSize = ctx.eval("ruby", "-> recv, arg { recv.macos? }");
        Value res = callSize.execute(getValue(), true);
        return res.asBoolean();
    }

    public abstract Integer[] device_family();

//    throws an error as of Jul 8 2022, stored is not a method
//    def release_type
//      if stored?
//            'Store'
//            else
//    build_type
//            end
//    end


}
