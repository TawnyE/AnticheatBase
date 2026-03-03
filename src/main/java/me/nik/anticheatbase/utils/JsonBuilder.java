package me.nik.anticheatbase.utils;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public class JsonBuilder {

    private static String text;
    private static String json = "{\"text\":\"" + text + "\"}";
    private String hover;
    private String click;
    private HoverEventType hoverAction;
    private ClickEventType clickAction;

    public JsonBuilder(String text) {
        JsonBuilder.text = ChatUtils.format(text);
    }

    public JsonBuilder setHoverEvent(JsonBuilder.HoverEventType type, String value) {
        hover = ChatUtils.format(value);
        hoverAction = type;
        return this;
    }

    public JsonBuilder setClickEvent(JsonBuilder.ClickEventType type, String value) {
        click = value;
        clickAction = type;
        return this;
    }

    public JsonBuilder buildText() {

        if (!getClick().isPresent() && !getHover().isPresent()) json = "{\"text\":\"" + text + "\"}";

        if (!getClick().isPresent() && getHover().isPresent()) {

            if (hoverAction == HoverEventType.SHOW_ACHIEVEMENT) {
                json = "{\"text\":\"" + text + "\",\"hoverEvent\":{\"action\":\"" + hoverAction.getActionName() + "\",\"value\":\"achievement." + hover + "\"}}";
            } else if (hoverAction == HoverEventType.SHOW_STATISTIC) {
                json = "{\"text\":\"" + text + "\",\"hoverEvent\":{\"action\":\"" + hoverAction.getActionName() + "\",\"value\":\"stat." + hover + "\"}}";
            } else {
                json = "{\"text\":\"" + text + "\",\"hoverEvent\":{\"action\":\"" + hoverAction.getActionName() + "\",\"value\":\"" + hover + "\"}}";
            }
        }

        if (getClick().isPresent() && getHover().isPresent()) {
            json = "{\"text\":\"" + text + "\",\"clickEvent\":{\"action\":\"" + clickAction.getActionName() + "\",\"value\":\"" + click + "\"},\"hoverEvent\":{\"action\":\"" + hoverAction.getActionName() + "\",\"value\":\"" + hover + "\"}}";
        }

        if (getClick().isPresent() && !getHover().isPresent()) {
            json = "{\"text\":\"" + text + "\",\"clickEvent\":{\"action\":\"" + clickAction.getActionName() + "\",\"value\":\"" + click + "\"}}";
        }

        return this;
    }

    public void sendMessage(Player player) {
        BaseComponent[] components = ComponentSerializer.parse(json);
        player.spigot().sendMessage(components);
    }

    public void sendMessage(Collection<? extends UUID> players) {
        BaseComponent[] components = ComponentSerializer.parse(json);

        for (UUID uuid : players) {
            Player p = Bukkit.getPlayer(uuid);
            if (p == null) continue;
            p.spigot().sendMessage(components);
        }
    }

    public String getUnformattedText() {
        return text;
    }

    public String getJson() {
        return json;
    }

    private Optional<String> getHover() {
        return Optional.ofNullable(hover);
    }

    private Optional<String> getClick() {
        return Optional.ofNullable(click);
    }

    public enum ClickEventType {
        OPEN_URL("open_url"),
        RUN_COMMAND("run_command"),
        SUGGEST_TEXT("suggest_command");

        private final String actionName;

        ClickEventType(String actionName) {
            this.actionName = actionName;
        }

        public String getActionName() {
            return actionName;
        }
    }

    public enum HoverEventType {
        SHOW_TEXT("show_text"),
        SHOW_ITEM("show_item"),
        SHOW_ACHIEVEMENT("show_achievement"),
        SHOW_STATISTIC("show_achievement");

        private final String actionName;

        HoverEventType(String actionName) {
            this.actionName = actionName;
        }

        public String getActionName() {
            return actionName;
        }
    }
}
