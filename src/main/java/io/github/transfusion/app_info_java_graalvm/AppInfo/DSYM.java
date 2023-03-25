package io.github.transfusion.app_info_java_graalvm.AppInfo;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import io.github.transfusion.app_info_java_graalvm.MachO.FatFile;
import io.github.transfusion.app_info_java_graalvm.MachO.MachOFile;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.util.List;

public abstract class DSYM extends AbstractPolyglotAdapter {

    public static DSYM from(Context polyglot, String path) {
        polyglot.eval("ruby", "require 'app-info'");
        return polyglot.eval("ruby", "AppInfo::DSYM.new('" + path + "')").as(DSYM.class);
    }

    public abstract String file_type();

    public abstract String object();

    /**
     * @return either a {@link io.github.transfusion.app_info_java_graalvm.MachO.FatFile} or a {@link io.github.transfusion.app_info_java_graalvm.MachO.MachOFile} or null
     */
    public AbstractPolyglotAdapter macho_type_() {
        Context ctx = getContext();
        Value v = getValue().getMember("macho_type").execute();
        if (v.getMember("is_a?").execute(ctx.eval("ruby", "MachO::FatFile")).asBoolean()) {
            return v.as(FatFile.class);
        }
        if (v.getMember("is_a?").execute(ctx.eval("ruby", "MachO::MachOFile")).asBoolean()) {
            return v.as(MachOFile.class);
        }
        return null;
    }

    public abstract Value machos();

    public List<MachO> machos_() {
        return iterableToList(machos(), MachO.class);
    }

    public abstract String release_version();

    public abstract String build_version();

    public abstract String identifier();

    public abstract String bundle_id();

    /**
     * @return a Hash; @info ||= CFPropertyList.native_types(CFPropertyList::List.new(file: info_path).value)
     */
    public abstract Value info();

    public abstract String info_path();

    public abstract String app_path();

    public static abstract class MachO extends AbstractPolyglotAdapter {
        public abstract String cpu_name();

        public abstract String cpu_type();

        public abstract String type();

        public abstract String uuid();

        public abstract String debug_id();

        public abstract Long size();

        public String humanSize() {
            Context ctx = getContext();
            Value callSize = ctx.eval("ruby", "-> recv, arg { recv.size(human_size: arg) }");
            Value res = callSize.execute(getValue(), true);
            return res.asString();
        }

        /**
         * @return MachO::Headers::MachHeader64
         */
        public abstract Value header();
    }

    public abstract String contents();

    public void clear() {
        Context ctx = getContext();
        Value v = ctx.eval("ruby", "-> recv { recv.clear! }");
        v.execute(getValue());
    }
}
