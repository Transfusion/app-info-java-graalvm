package io.github.transfusion.app_info_java_graalvm.AppInfo;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public abstract class Proguard extends AbstractPolyglotAdapter {
    public static Proguard from(Context polyglot, String path) {
        polyglot.eval("ruby", "require 'app-info'");
        return polyglot.eval("ruby", "AppInfo::Proguard.new('" + path + "')").as(Proguard.class);
    }

    public abstract String file();

    public abstract String file_type();

    public abstract String uuid();

    public abstract String debug_id();

    public Boolean mapping_question() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv { recv.mapping? }");
        Value res = v.execute(getValue());
        return res.asBoolean();
    }

    public Boolean manifest_question() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv { recv.manifest? }");
        Value res = v.execute(getValue());
        return res.asBoolean();
    }

    public Boolean symbol_question() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv { recv.symbol? }");
        Value res = v.execute(getValue());
        return res.asBoolean();
    }

    public Boolean resource_question() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv { recv.resource? }");
        Value res = v.execute(getValue());
        return res.asBoolean();
    }

    public abstract String package_name();

    public abstract String releasd_version();

    public abstract String version_name();

    public abstract String release_version();

    public abstract String version_code();

    public abstract String build_version();

    /**
     * @return REXML::Document
     */
    public abstract Value manifest();

    public abstract String mapping_path();

    public abstract String manifest_path();

    public abstract String symbol_path();

    public abstract String contents();

    public void clear() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv { recv.clear! }");
        v.execute(getValue());
    }
}
