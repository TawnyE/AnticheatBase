package me.nik.anticheatbase.wrappers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.util.Vector;

@Getter
@RequiredArgsConstructor
public class WrapperPlayClientUseEntity {

    public enum Action {
        ATTACK,
        INTERACT,
        INTERACT_AT,
        UNKNOWN
    }

    private final int targetId;
    private final Action action;
    private final Vector targetVector;
}
