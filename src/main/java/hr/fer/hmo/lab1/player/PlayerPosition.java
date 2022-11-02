package hr.fer.hmo.lab1.player;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author matejc
 * Created on 24.10.2022.
 */

public enum PlayerPosition {
    GOAL_KEEPER("GK"),
    DEFENDER("DEF"),
    MIDFIELDER("MID"),
    FORWARD("FW");

    private final String getShortString;
    private static final Map<String, PlayerPosition> ENUM_MAP;

    PlayerPosition(String getShortString) {
        this.getShortString = getShortString;
    }

    public String getGetShortString() {
        return getShortString;
    }

    static {
        Map<String, PlayerPosition> map = new HashMap<>();
        for (PlayerPosition instance : PlayerPosition.values()) {
            map.put(instance.getGetShortString().toLowerCase(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static PlayerPosition get(String shortName) {
        return ENUM_MAP.get(shortName.toLowerCase());
    }
}
