package com.kdhira.dot.util;

public class ColoredString {

    private String string;
    private StringColor color;

    public ColoredString(String s) {
        this(s, StringColor.NONE);
    }

    public ColoredString(String string, StringColor color) {
        this.string = string;
        this.color = color;
    }

    @Override
    public String toString() {
        return color + string + StringColor.NONE;
    }

    public String getString() {
        return string;
    }

    public StringColor getColor() {
        return color;
    }

    public enum StringColor {
        NONE {
            @Override
            public String toString() {
                return "\u001B[0m";
            }
        },
        BLACK {
            @Override
            public String toString() {
                return "\u001B[30m";
            }
        },
        RED {
            @Override
            public String toString() {
                return "\u001B[31m";
            }
        },
        GREEN {
            @Override
            public String toString() {
                return "\u001B[32m";
            }
        },
        YELLOW {
            @Override
            public String toString() {
                return "\u001B[33m";
            }
        },
        BLUE {
            @Override
            public String toString() {
                return "\u001B[34m";
            }
        },
        MAGENTA {
            @Override
            public String toString() {
                return "\u001B[35m";
            }
        },
        CYAN {
            @Override
            public String toString() {
                return "\u001B[36m";
            }
        },
        WHITE {
            @Override
            public String toString() {
                return "\u001B[37m";
            }
        };
    }

}
