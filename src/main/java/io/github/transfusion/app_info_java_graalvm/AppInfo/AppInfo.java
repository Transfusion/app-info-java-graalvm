package io.github.transfusion.app_info_java_graalvm.AppInfo;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

/**
 * Wrapper of the root AppInfo module
 */
public abstract class AppInfo extends AbstractPolyglotAdapter {
    public static AppInfo getInstance(Context ctx) {
        return ctx.eval("ruby", "AppInfo").as(AppInfo.class);
    }

    /**
     * Convenience method; use {@link AppInfo#parse_(String)} instead
     *
     * @param path path to file
     * @return polyglot value returned from AppInfo::Parse
     */
    public abstract Value parse(String path);

    public abstract Value dump(String path);

    /**
     * Use this method instead of {@link AppInfo#parse(String)}
     * <pre>
     *    def parse(file)
     *       raise NotFoundError, file unless File.exist?(file)
     *
     *       case file_type(file)
     *       when :ipa then IPA.new(file)
     *       when :apk then APK.new(file)
     *       when :aab then AAB.new(file)
     *       when :mobileprovision then MobileProvision.new(file)
     *       when :dsym then DSYM.new(file)
     *       when :proguard then Proguard.new(file)
     *       when :macos then Macos.new(file)
     *       else
     *         raise UnkownFileTypeError, "Do not detect file type: #{file}"
     *       end
     *     end
     * </pre>
     *
     * @param path path to file
     * @return {@link IPA}, {@link APK}, {@link AAB}, {@link MobileProvision}, {@link DSYM}, {@link Proguard}, {@link MacOS}
     * which are all instances of {@link AbstractPolyglotAdapter}
     * @throws IllegalArgumentException if the file is not recognized as one of these file types
     */
    public AbstractPolyglotAdapter parse_(String path) {
        Value IPAClass = getContext().eval("ruby", "AppInfo::IPA");
        Value APKClass = getContext().eval("ruby", "AppInfo::APK");
        Value AABClass = getContext().eval("ruby", "AppInfo::AAB");
        Value MobileProvisionClass = getContext().eval("ruby", "AppInfo::MobileProvision");
        Value DSYMClass = getContext().eval("ruby", "AppInfo::DSYM");
        Value ProguardClass = getContext().eval("ruby", "AppInfo::Proguard");
        Value MacosClass = getContext().eval("ruby", "AppInfo::Macos");

        Value v = parse(path);
        if (v.getMember("is_a?").execute(IPAClass).asBoolean()) {
            return v.as(IPA.class);
        } else if (v.getMember("is_a?").execute(APKClass).asBoolean()) {
            return v.as(APK.class);
        } else if (v.getMember("is_a?").execute(AABClass).asBoolean()) {
            return v.as(AAB.class);
        } else if (v.getMember("is_a?").execute(MobileProvisionClass).asBoolean()) {
            return v.as(MobileProvision.class);
        } else if (v.getMember("is_a?").execute(DSYMClass).asBoolean()) {
            return v.as(DSYM.class);
        } else if (v.getMember("is_a?").execute(ProguardClass).asBoolean()) {
            return v.as(Proguard.class);
        } else if (v.getMember("is_a?").execute(MacosClass).asBoolean()) {
            return v.as(MacOS.class);
        }
        throw new IllegalArgumentException(String.format("%s is not a recognized file format", path));
    }

}
