package io.github.transfusion.app_info_java_graalvm.AppInfo;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.time.ZonedDateTime;
import java.util.List;

public abstract class MobileProvision extends AbstractPolyglotAdapter {

    public static MobileProvision from(Context polyglot, String path) {
        polyglot.eval("ruby", "require 'app-info'");
        return polyglot.eval("ruby", "AppInfo::MobileProvision.new('" + path + "')").as(MobileProvision.class);
    }

    public abstract String name();

    public abstract String app_name();

    public abstract String type();

    public abstract String[] platforms();

    public abstract String platform();

    public abstract String[] devices();

    public abstract String[] team_identifier();

    public abstract String team_name();

    public abstract String profile_name();

    public abstract ZonedDateTime created_date();

    public abstract ZonedDateTime expired_date();

    public abstract Value entitlements();

    public static abstract class DeveloperCertificate extends AbstractPolyglotAdapter {

    }

    public abstract List<DeveloperCertificate> developer_certs();

    public boolean development_question() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv { recv.development? }");
        Value res = v.execute(getValue());
        return res.asBoolean();
    }

    public boolean appstore_question() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv { recv.appstore? }");
        Value res = v.execute(getValue());
        return res.asBoolean();
    }

    public boolean adhoc_question() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv { recv.adhoc? }");
        Value res = v.execute(getValue());
        return res.asBoolean();
    }

    public boolean enterprise_question() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv { recv.enterprise? }");
        Value res = v.execute(getValue());
        return res.asBoolean();
    }

    public abstract String[] capabilities();
}
