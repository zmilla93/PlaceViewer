package com.zrmiller.core.enums;

public enum ExportSelection {

    CANVAS("Entire Canvas"),
    SELECTION("Selection");

    private final String name;

    ExportSelection(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
