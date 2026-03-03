package me.nik.anticheatbase.files;

import me.nik.anticheatbase.Anticheat;
import me.nik.anticheatbase.files.commentedfiles.CommentedFileConfiguration;
import me.nik.anticheatbase.managers.Initializer;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Checks implements Initializer {

    private static final String[] HEADER = new String[]{
            "+----------------------------------------------------------------------------------------------+",
            "|                                                                                              |",
            "|                                          Anticheat                                           |",
            "|                                                                                              |",
            "|                          Website: https://www.youranticheatwebsite.com                       |",
            "|                                                                                              |",
            "|                                         Author: Nik                                          |",
            "|                                                                                              |",
            "+----------------------------------------------------------------------------------------------+"
    };

    private final JavaPlugin plugin;
    private CommentedFileConfiguration configuration;
    private static boolean exists;

    public Checks(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    /**
     * @return the config.yml as a CommentedFileConfiguration
     */
    public CommentedFileConfiguration getConfig() {
        return this.configuration;
    }

    @Override
    public void initialize() {

        File configFile = new File(this.plugin.getDataFolder(), "checks.yml");

        exists = configFile.exists();

        boolean setHeaderFooter = !exists;

        boolean changed = setHeaderFooter;

        this.configuration = CommentedFileConfiguration.loadConfiguration(this.plugin, configFile);

        if (setHeaderFooter) this.configuration.addComments(HEADER);

        for (Setting setting : Setting.values()) {

            setting.reset();

            changed |= setting.setIfNotExists(this.configuration);
        }

        if (changed) this.configuration.save();
    }

    @Override
    public void shutdown() {
        for (Setting setting : Setting.values()) setting.reset();
    }

    public enum Setting {
        AIM("aim", "", "Aim Check"),
        AIM_A("aim.a", true, "GCD check"),
        AIM_B("aim.b", true, "Snap check"),
        AIM_C("aim.c", true, "Consistency check"),
        AIM_D("aim.d", true, "Smoothness check"),
        AIM_E("aim.e", true, "Acceleration check"),
        AIM_MAX_VL("aim.max_vl", 10, "Max VL"),
        AIM_COMMANDS("aim.commands", Collections.singletonList("ban %player% Aim Assist"), "Commands"),

        AUTOCLICKER("autoclicker", "", "AutoClicker Check"),
        AUTOCLICKER_A("autoclicker.a", true, "CPS check"),
        AUTOCLICKER_B("autoclicker.b", true, "Consistency check"),
        AUTOCLICKER_C("autoclicker.c", true, "Double-click check"),
        AUTOCLICKER_D("autoclicker.d", true, "Skewness check"),
        AUTOCLICKER_MAX_VL("autoclicker.max_vl", 15, "Max VL"),
        AUTOCLICKER_COMMANDS("autoclicker.commands", Collections.singletonList("ban %player% AutoClicker"), "Commands"),

        KILLAURA("killaura", "", "KillAura Check"),
        KILLAURA_A("killaura.a", true, "Post-tick check"),
        KILLAURA_B("killaura.b", true, "Multi-hit check"),
        KILLAURA_C("killaura.c", true, "Switch check"),
        KILLAURA_D("killaura.d", true, "Inertia check"),
        KILLAURA_MAX_VL("killaura.max_vl", 10, "Max VL"),
        KILLAURA_COMMANDS("killaura.commands", Collections.singletonList("ban %player% KillAura"), "Commands"),

        REACH("reach", "", "Reach Check"),
        REACH_A("reach.a", true, "Reach check"),
        REACH_MAX_VL("reach.max_vl", 5, "Max VL"),
        REACH_COMMANDS("reach.commands", Collections.singletonList("ban %player% Reach"), "Commands"),

        VELOCITY("velocity", "", "Velocity Check"),
        VELOCITY_A("velocity.a", true, "Vertical check"),
        VELOCITY_B("velocity.b", true, "Horizontal check"),
        VELOCITY_MAX_VL("velocity.max_vl", 5, "Max VL"),
        VELOCITY_COMMANDS("velocity.commands", Collections.singletonList("ban %player% Velocity"), "Commands"),

        FLY("fly", "", "Fly Check"),
        FLY_A("fly.a", true, "Vertical check"),
        FLY_B("fly.b", true, "Prediction check"),
        FLY_C("fly.c", true, "Stable Y check"),
        FLY_D("fly.d", true, "Acceleration check"),
        FLY_MAX_VL("fly.max_vl", 10, "Max VL"),
        FLY_COMMANDS("fly.commands", Collections.singletonList("ban %player% Fly"), "Commands"),

        SPEED("speed", "", "Speed Check"),
        SPEED_A("speed.a", true, "Predictive check"),
        SPEED_B("speed.b", true, "Air check"),
        SPEED_C("speed.c", true, "Strafe check"),
        SPEED_D("speed.d", true, "Ground check"),
        SPEED_MAX_VL("speed.max_vl", 10, "Max VL"),
        SPEED_COMMANDS("speed.commands", Collections.singletonList("ban %player% Speed"), "Commands"),

        NOFALL("nofall", "", "NoFall Check"),
        NOFALL_A("nofall.a", true, "Spoof check"),
        NOFALL_B("nofall.b", true, "Reset check"),
        NOFALL_MAX_VL("nofall.max_vl", 5, "Max VL"),
        NOFALL_COMMANDS("nofall.commands", Collections.singletonList("ban %player% NoFall"), "Commands"),

        JESUS("jesus", "", "Jesus Check"),
        JESUS_A("jesus.a", true, "Liquid check"),
        JESUS_B("jesus.b", true, "Walking check"),
        JESUS_MAX_VL("jesus.max_vl", 5, "Max VL"),
        JESUS_COMMANDS("jesus.commands", Collections.singletonList("ban %player% Jesus"), "Commands"),

        NOSLOW("noslow", "", "NoSlow Check"),
        NOSLOW_A("noslow.a", true, "Item check"),
        NOSLOW_MAX_VL("noslow.max_vl", 5, "Max VL"),
        NOSLOW_COMMANDS("noslow.commands", Collections.singletonList("ban %player% NoSlow"), "Commands"),

        OMNISPRINT("omnisprint", "", "OmniSprint Check"),
        OMNISPRINT_A("omnisprint.a", true, "Sprint check"),
        OMNISPRINT_MAX_VL("omnisprint.max_vl", 5, "Max VL"),
        OMNISPRINT_COMMANDS("omnisprint.commands", Collections.singletonList("ban %player% OmniSprint"), "Commands"),

        FASTCLIMB("fastclimb", "", "FastClimb Check"),
        FASTCLIMB_A("fastclimb.a", true, "Climb check"),
        FASTCLIMB_MAX_VL("fastclimb.max_vl", 5, "Max VL"),
        FASTCLIMB_COMMANDS("fastclimb.commands", Collections.singletonList("ban %player% FastClimb"), "Commands"),

        TIMER("timer", "", "Timer Check"),
        TIMER_A("timer.a", true, "Balance check"),
        TIMER_MAX_VL("timer.max_vl", 10, "Max VL"),
        TIMER_COMMANDS("timer.commands", Collections.singletonList("ban %player% Timer"), "Commands"),

        SCAFFOLD("scaffold", "", "Scaffold Check"),
        SCAFFOLD_A("scaffold.a", true, "Rotation check"),
        SCAFFOLD_B("scaffold.b", true, "Expansion check"),
        SCAFFOLD_MAX_VL("scaffold.max_vl", 10, "Max VL"),
        SCAFFOLD_COMMANDS("scaffold.commands", Collections.singletonList("ban %player% Scaffold"), "Commands"),

        BADPACKETS("badpackets", "", "BadPackets Check"),
        BADPACKETS_A("badpackets.a", true, "Spam check"),
        BADPACKETS_B("badpackets.b", true, "Invalid states check"),
        BADPACKETS_MAX_VL("badpackets.max_vl", 5, "Max VL"),
        BADPACKETS_COMMANDS("badpackets.commands", Collections.singletonList("ban %player% Bad Packets"), "Commands");

        private final String key;
        private final Object defaultValue;
        private boolean excluded;
        private final String[] comments;
        private Object value = null;

        Setting(String key, Object defaultValue, String... comments) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments != null ? comments : new String[0];
        }

        Setting(String key, Object defaultValue, boolean excluded, String... comments) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments != null ? comments : new String[0];
            this.excluded = excluded;
        }

        /**
         * Gets the setting as a boolean
         *
         * @return The setting as a boolean
         */
        public boolean getBoolean() {
            this.loadValue();
            return (boolean) this.value;
        }

        public String getKey() {
            return this.key;
        }

        /**
         * @return the setting as an int
         */
        public int getInt() {
            this.loadValue();
            return (int) this.getNumber();
        }

        /**
         * @return the setting as a long
         */
        public long getLong() {
            this.loadValue();
            return (long) this.getNumber();
        }

        /**
         * @return the setting as a double
         */
        public double getDouble() {
            this.loadValue();
            return this.getNumber();
        }

        /**
         * @return the setting as a float
         */
        public float getFloat() {
            this.loadValue();
            return (float) this.getNumber();
        }

        /**
         * @return the setting as a String
         */
        public String getString() {
            this.loadValue();
            return String.valueOf(this.value);
        }

        private double getNumber() {
            if (this.value instanceof Integer) {
                return (int) this.value;
            } else if (this.value instanceof Short) {
                return (short) this.value;
            } else if (this.value instanceof Byte) {
                return (byte) this.value;
            } else if (this.value instanceof Float) {
                return (float) this.value;
            }

            return (double) this.value;
        }

        /**
         * @return the setting as a string list
         */
        @SuppressWarnings("unchecked")
        public List<String> getStringList() {
            this.loadValue();
            return (List<String>) this.value;
        }

        private boolean setIfNotExists(CommentedFileConfiguration fileConfiguration) {
            this.loadValue();

            if (exists && this.excluded) return false;

            if (fileConfiguration.get(this.key) == null) {
                List<String> comments = Stream.of(this.comments).collect(Collectors.toList());
                if (this.defaultValue != null) {
                    fileConfiguration.set(this.key, this.defaultValue, comments.toArray(new String[0]));
                } else {
                    fileConfiguration.addComments(comments.toArray(new String[0]));
                }

                return true;
            }

            return false;
        }

        /**
         * Resets the cached value
         */
        public void reset() {
            this.value = null;
        }

        /**
         * @return true if this setting is only a section and doesn't contain an actual value
         */
        public boolean isSection() {
            return this.defaultValue == null;
        }

        /**
         * Loads the value from the config and caches it if it isn't set yet
         */
        private void loadValue() {
            if (this.value != null) return;
            this.value = Anticheat.getInstance().getChecks().get(this.key);
        }
    }
}
