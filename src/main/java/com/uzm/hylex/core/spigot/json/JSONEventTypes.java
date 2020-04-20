package com.uzm.hylex.core.spigot.json;

public class JSONEventTypes {
    public enum ClickEventType {
        RUN_COMMAND, SUGGEST_COMMAND, OPEN_URL, CHANGE_PAGE;

        public String getTypeString() {
            return name().toLowerCase();
        }
    }

    public enum HoverEventType {
        SHOW_TEXT, SHOW_ITEM, SHOW_ACHIEVEMENT;

        public String getTypeString() {
            return name().toLowerCase();
        }
    }
}
