package com.suttori.entity.enams;

public enum Locales {
    En, Ua;

    public static boolean contains(String test) {
        for (Locales l : Locales.values()) {
            if (l.name().equals(test)) {
                return true;
            }
        }
        return false;
    }
}
