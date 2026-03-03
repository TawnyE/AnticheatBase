package me.nik.anticheatbase.checks.types;

import lombok.Getter;
import me.nik.anticheatbase.Anticheat;
import me.nik.anticheatbase.api.events.AnticheatViolationEvent;
import me.nik.anticheatbase.checks.annotations.Development;
import me.nik.anticheatbase.checks.annotations.Experimental;
import me.nik.anticheatbase.checks.enums.CheckCategory;
import me.nik.anticheatbase.checks.enums.CheckType;
import me.nik.anticheatbase.files.Config;
import me.nik.anticheatbase.files.commentedfiles.CommentedFileConfiguration;
import me.nik.anticheatbase.managers.profile.Profile;
import me.nik.anticheatbase.utils.BetterStream;
import me.nik.anticheatbase.utils.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
public abstract class AbstractCheck {

    protected final Profile profile;

    private final boolean enabled;

    private final Set<String> commands = new LinkedHashSet<>();

    private final boolean enabledSetback;

    private final String checkName, checkType, fullCheckName, description;
    private final boolean experimental;
    private final CheckCategory checkCategory;
    private final boolean development;
    private int vl;
    private final int maxVl;
    private float buffer;
    private String verbose;

    public AbstractCheck(Profile profile, CheckType check, String type, String description) {

        this.profile = profile;
        this.checkName = check.getCheckName();
        this.checkType = type;
        this.description = description;

        final CommentedFileConfiguration config = Anticheat.getInstance().getChecks();
        final String checkNameLower = this.checkName.toLowerCase();
        final String checkTypeLower = type.toLowerCase().replace(" ", "_");

        this.enabledSetback = !Config.Setting.GHOST_MODE.getBoolean()
                && (check == CheckType.SPEED || check == CheckType.FLY || check == CheckType.MOTION);

        this.enabled = type.isEmpty()
                ? config.getBoolean(checkNameLower + ".enabled")
                : config.getBoolean(checkNameLower + "." + checkTypeLower + ".enabled", config.getBoolean(checkNameLower + "." + checkTypeLower));

        this.maxVl = config.getInt(checkNameLower + ".max_vl");

        if (profile != null) {
            this.commands.addAll(
                    BetterStream.applyAndGet(config.getStringList(checkNameLower + ".commands"),
                            command -> command.replace("%player%", profile.getPlayer().getName())
                    )
            );
        }

        Class<? extends AbstractCheck> clazz = this.getClass();
        this.experimental = clazz.isAnnotationPresent(Experimental.class);
        this.development = clazz.isAnnotationPresent(Development.class);
        this.checkCategory = check.getCheckCategory();
        this.fullCheckName = this.checkName + (type.isEmpty() ? "" : (" (" + type + ")"));
    }

    protected void debug(Object info) {
        Bukkit.broadcastMessage(String.valueOf(info));
    }

    public void fail(String... verbose) {
        this.verbose = String.join(", ", verbose);
        fail();
    }

    public void fail() {
        if (this.development) return;
        if (this.vl < 0) this.vl = 0;

        final Player p = profile.getPlayer();
        if (p == null) return;

        AnticheatViolationEvent violationEvent = new AnticheatViolationEvent(
                p,
                this.checkName,
                this.description,
                this.checkType,
                verbose,
                this.vl++,
                this.maxVl,
                this.experimental);

        Bukkit.getPluginManager().callEvent(violationEvent);

        if (violationEvent.isCancelled() || this.experimental) {
            this.vl--;
            return;
        }

        if (this.enabledSetback) profile.getMovementData().getSetbackProcessor().setback(true);

        if (this.vl >= this.maxVl) {
            MiscUtils.consoleCommand(this.commands);
            this.vl = 0;
            this.buffer = 0;
        }
    }

    public void resetVl() {
        this.vl = 0;
    }

    public void setVl(int vl) {
        this.vl = vl;
    }

    protected float increaseBuffer() {
        return ++this.buffer;
    }

    protected float increaseBufferBy(double amount) {
        return this.buffer += (float) amount;
    }

    protected float decreaseBuffer() {
        return this.buffer = Math.max(0, this.buffer - 1);
    }

    protected float decreaseBufferBy(double amount) {
        return this.buffer = (float) Math.max(0, this.buffer - amount);
    }

    public void resetBuffer() {
        this.buffer = 0;
    }

    protected String format(int scale, double val) {
        return String.format("%." + scale + "f", val);
    }
}
