package io.github.transfusion.app_info_java_graalvm.AppInfo.ipa_related;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import io.github.transfusion.app_info_java_graalvm.AppInfo.InfoPlist;
import io.github.transfusion.app_info_java_graalvm.MachO.FatFile;
import io.github.transfusion.app_info_java_graalvm.MachO.MachOFile;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

public abstract class Framework extends AbstractPolyglotAdapter {

    public abstract String display_name();

    public abstract String bundle_name();

    public abstract String release_version();

    public abstract String build_version();

    public abstract String identifier();

    public abstract String bundle_id();

    public abstract String min_sdk_version();

    public abstract String device_type();

    public abstract String name();

//    def macho
//      return unless lib?
//
//    require 'macho'
//            MachO.open(file)
//    end

    /**
     * @return either a {@link io.github.transfusion.app_info_java_graalvm.MachO.FatFile} or a {@link io.github.transfusion.app_info_java_graalvm.MachO.MachOFile} or null
     */
    public AbstractPolyglotAdapter macho_() {
        Context ctx = getContext();
        Value v = getValue().getMember("macho").execute();
        if (v.getMember("is_a?").execute(ctx.eval("ruby", "MachO::FatFile")).asBoolean()) {
            return v.as(FatFile.class);
        }
        if (v.getMember("is_a?").execute(ctx.eval("ruby", "MachO::MachOFile")).asBoolean()) {
            return v.as(MachOFile.class);
        }
        return null;
    }


    /**
     * whether this framework is a library
     *
     * @return may be null
     */
    public Boolean lib_question() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv { recv.lib? }");
        Value res = v.execute(getValue());
        return res.asBoolean();
    }

    public abstract InfoPlist info();

    public abstract String to_s();

    @Override
    public String toString() {
        return to_s();
    }

}
