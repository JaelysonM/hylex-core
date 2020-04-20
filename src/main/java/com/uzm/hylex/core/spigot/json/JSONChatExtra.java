package com.uzm.hylex.core.spigot.json;

import org.json.simple.JSONObject;

public class JSONChatExtra {
    public static class ChatExtra {
        private JSONObject chatExtra;

        @SuppressWarnings("unchecked")
        public ChatExtra(String text) {
            chatExtra = new JSONObject();
            chatExtra.put("text", text);
        }

        @SuppressWarnings("unchecked")
        public ChatExtra clickExtra(JSONEventTypes.ClickEventType action, String value) {
            JSONObject clickEvent = new JSONObject();
            clickEvent.put("action", action.getTypeString());
            clickEvent.put("value", value);
            chatExtra.put("clickEvent", clickEvent);
            return this;
        }

        @SuppressWarnings("unchecked")
        public ChatExtra hoverExtra(JSONEventTypes.HoverEventType action, String value) {
            JSONObject hoverEvent = new JSONObject();
            hoverEvent.put("action", action.getTypeString());
            hoverEvent.put("value", value);
            chatExtra.put("hoverEvent", hoverEvent);
            return this;
        }

        public JSONObject toJSON() {
            return chatExtra;
        }

        public ChatExtra build() {
            return this;
        }
    }
}
