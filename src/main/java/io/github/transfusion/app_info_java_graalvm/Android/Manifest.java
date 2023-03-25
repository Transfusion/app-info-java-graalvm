package io.github.transfusion.app_info_java_graalvm.Android;

import io.github.transfusion.app_info_java_graalvm.AbstractPolyglotAdapter;
import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Value;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Partial wrapper of Android::Manifest::Component
 */
public abstract class Manifest extends AbstractPolyglotAdapter {
    public abstract static class Component extends AbstractPolyglotAdapter {
        public abstract String type();

        public abstract String name();

        public abstract String icon_id();


        /**
         * @return <pre>{@code [Array<Manifest::IntentFilters < Manifest::IntentFilter>>]}</pre>
         */
        public abstract Value intent_filters();

        public List<IntentFilter> intent_filters_() {
            return iterableToList(intent_filters(), IntentFilter.class);
        }


        /**
         * @return <pre>{@code [Array<Manifest::Meta>]}</pre>
         */
        public abstract Value metas();

        public List<Meta> metas_() {
            return iterableToList(metas(), Meta.class);
        }


        /**
         * @return [REXML::Element]
         */
        public abstract Value elem();
    }

    public abstract static class Activity extends AbstractPolyglotAdapter {
//        # the element is valid Activity element or not
//      # @param [REXML::Element] elem xml element
//      # @return [Boolean]
//        def self.valid?(elem)

        public Boolean launcher_activity_question() {
            Context ctx = getContext();
            Value v = ctx.eval("ruby", "-> recv { recv.launcher_activity? }");
            Value res = v.execute(getValue());
            return res.asBoolean();
        }

        public Boolean default_launcher_activity_question() {
            Context ctx = getContext();
            Value v = ctx.eval("ruby", "-> recv { recv.default_launcher_activity? }");
            Value res = v.execute(getValue());
            return res.asBoolean();
        }
    }

    public abstract static class ActivityAlias extends Activity {
        public abstract String target_activity();
    }

    public abstract static class Application extends Component {
        public Boolean valid_question() {
            Context ctx = getContext();
            Value v = ctx.eval("ruby", "-> recv { recv.valid? }");
            Value res = v.execute(getValue());
            return res.asBoolean();
        }
    }

    public abstract static class IntentFilter extends AbstractPolyglotAdapter {

        //        # the element is valid IntentFilter element or not
//      # @param [REXML::Element] elem xml element
//      # @return [Boolean]
//        def self.valid?(filter)

        /**
         * @return [IntentFilter::Action] intent-filter actions
         */
        public abstract Value actions();

        public List<Action> actions_() {
            return iterableToList(actions(), Action.class);
        }

        /**
         * @return [IntentFilter::Category] intent-filter categories
         */
        public abstract Value categories();

        public List<Category> categories_() {
            return iterableToList(categories(), Category.class);
        }

        /**
         * @return [IntentFilter::Data] intent-filter data
         */
        public abstract Value data();

        public List<Data> data_() {
            return iterableToList(data(), Data.class);
        }


        public Boolean empty() {
            return getValue().getMember("empty?").execute().asBoolean();
        }

//        def exist?(name, type: nil)
//        implemented by loading either of :actions, :categories, or :data into memory then filtering

        public abstract String[] deep_links();

        public Boolean deep_links_question() {
            Context ctx = getContext();
            Value v = ctx.eval("ruby", "-> recv { recv.deep_links? }");
            Value res = v.execute(getValue());
            return res.asBoolean();
        }

        public abstract String[] schemes();

        public Boolean schemes_question() {
            Context ctx = getContext();
            Value v = ctx.eval("ruby", "-> recv { recv.schemes? }");
            Value res = v.execute(getValue());
            return res.asBoolean();
        }

        public Boolean browsable_question() {
            Context ctx = getContext();
            Value v = ctx.eval("ruby", "-> recv { recv.browsable? }");
            Value res = v.execute(getValue());
            return res.asBoolean();
        }


        public abstract static class Action extends AbstractPolyglotAdapter {
            public abstract String name();

            public abstract String type();
        }

        public abstract static class Category extends AbstractPolyglotAdapter {
            public abstract String name();

            public abstract String type();
        }

        public abstract static class Data extends AbstractPolyglotAdapter {
            public abstract String type();

            public abstract String host();

            public abstract String mime_type();

            public abstract String path();

            public abstract String path_pattern();

            public abstract String path_prefix();

            public abstract String port();

            public abstract String scheme();
        }

    }

    /**
     * @return [REXML::Document] manifest xml
     */
    public abstract Value doc();

    /**
     * # used permission array
     * # @note return empty array when the manifest includes no use-parmission element
     *
     * @return <pre>{@code [Array<String>] permission names}</pre>
     */
    public abstract String[] use_permissions();

    /**
     * # used features array
     * # @note return empty array when the manifest includes no use-features element
     *
     * @return <pre>{@code [Array<String>] features names}</pre>
     */
    public abstract String[] use_features();

    /**
     * # Returns the manifest's application element or nil, if there isn't any.
     *
     * @return <pre>{@code [Android::Manifest::Application] the manifest's application element}</pre>
     */
    public abstract Application application();

    /**
     * # @note return empty array when the manifest include no components
     *
     * @return <pre>{@code [Array<Android::Manifest::Component>] all components in apk}</pre>
     */
    public abstract Value components();

    public List<Component> components_() {
        return iterableToList(components(), Component.class);
    }

    public abstract Value activities();

    private List<Activity> activities__() {
        return iterableToList(activities(), Activity.class);
    }

    public List<Activity> activities_() {
        Value activityAliasClass = getContext().eval("ruby", "Android::Manifest::ActivityAlias");
//        Value activityClass = getContext().eval("ruby", "Android::Manifest::Activity");

        List<Activity> activities = activities__();
        return activities.stream().map(activity -> activity.getValue().getMember("is_a?").execute(activityAliasClass).asBoolean() ?
                activity.getValue().as(ActivityAlias.class) : activity.getValue().as(Activity.class)).collect(Collectors.toList());
    }

    /**
     * # @note return empty array when the manifest include no services
     *
     * @return <pre>{@code [Array<Android::Manifest::Component>] all services in the apk}</pre>
     */
    public abstract Value services();

    public List<Component> services_() {
        return iterableToList(services(), Component.class);
    }

    /**
     * # @note return empty array when the manifest not include http or https scheme(s) of data
     *
     * @return <pre>{@code [Array<String>] all schemes in intent filters}</pre>
     */
    public abstract String[] schemes();

    public abstract Value launcherActivities();

    private List<Activity> launcherActivities__() {
        return iterableToList(launcherActivities(), Activity.class);
    }

    /**
     * # @note return empty array when the manifest include no activities
     *
     * @return <pre>{@code [Array<Android::Manifest::Activity & ActivityAlias>] all activities that are launchers in the apk}</pre>
     */
    public List<Activity> launcherActivities_() {
        Value activityAliasClass = getContext().eval("ruby", "Android::Manifest::ActivityAlias");
//        Value activityClass = getContext().eval("ruby", "Android::Manifest::Activity");

        return launcherActivities__().stream().map(activity -> activity.getValue().getMember("is_a?").execute(activityAliasClass).asBoolean() ?
                activity.getValue().as(ActivityAlias.class) : activity.getValue().as(Activity.class)).collect(Collectors.toList());
    }

    /**
     * # application package name
     *
     * @return [String]
     */
    public abstract String package_name();

    /**
     * application version code
     *
     * @return [Integer]
     */
    public abstract Long version_code();

    /**
     * @param lang language code like 'ja', 'cn', ...
     * @return [String]
     */
    public abstract String version_name(String lang);

    /**
     * @return [Integer] minSdkVersion in uses element
     */
    public abstract Long min_sdk_ver();

    /**
     * @return [Integer] targetSdkVersion in uses element
     */
    public abstract Long target_sdk_version();

    /**
     * @param lang language code like 'ja', 'cn', ...
     * @return [String] application label string(if resouce is provided), or label resource id
     */
    public abstract String label(String lang);

    /**
     * @param indent [Integer] indent size(bytes)
     *               default 4
     * @return [String] raw xml string
     */
    public abstract String to_xml(Long indent);


    /**
     * Wrapper for Android::Manifest::Meta
     */
    public abstract static class Meta extends AbstractPolyglotAdapter {
        public abstract String name();

        public abstract String resource();

        public abstract String value();
    }
}
