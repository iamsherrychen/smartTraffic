package com.wistron.swpc.wismarttrafficlight.constant;

public enum SubPhaseEnum {

    /**
     * 时相 1
     */
    ONE("1"),

    /**
     * 时相 2
     */
    TWO("2"),

    /**
     * 时相 3
     */
    THREE("3"),

    /**
     * 时相 4
     */
    FOUR("4"),

    /**
     * 时相 5
     */
    FIVE("5"),

    /**
     * 时相 6
     */
    SIX("6"),

    /**
     * 时相 7
     */
    SEVEN("7"),

    /**
     * 时相 8
     */
    EIGHT("8");

    private String id;

    SubPhaseEnum(String id) {
        this.id = id;
    }

    public static SubPhaseEnum getInstance(String id) {
        SubPhaseEnum instance = null;
        for (SubPhaseEnum item : values()) {
            if (item.id.equals(id)) {
                instance = item;
                break;
            }
        }
        return instance;
    }

}
