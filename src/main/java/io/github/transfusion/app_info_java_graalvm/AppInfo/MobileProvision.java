package io.github.transfusion.app_info_java_graalvm.AppInfo;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import io.github.transfusion.app_info_java_graalvm.Utilities;
import org.graalvm.polyglot.Context;

import java.util.List;

public abstract class MobileProvision extends AbstractPolyglotAdapter {

    public static MobileProvision from(Context polyglot, String path) {
        polyglot.eval("ruby", "require 'app-info'");
        MobileProvision mobileProvision = polyglot.eval("ruby", "AppInfo::MobileProvision.new('" + path + "')").as(MobileProvision.class);
        mobileProvision.setContext(polyglot);
        return mobileProvision;
    }

    public static MobileProvision from(String path) {
        Context polyglot = Utilities.createContext();
        return MobileProvision.from(polyglot, path);
    }

    public static abstract class DeveloperCertificate extends AbstractPolyglotAdapter {

    }

    public abstract List<DeveloperCertificate> developer_certs();
}
