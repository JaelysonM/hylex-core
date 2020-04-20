package com.uzm.hylex.core.spigot.json;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class JSONMessage {
	private JSONObject chatObject;

	@SuppressWarnings("unchecked")
	public JSONMessage(String text) {
		chatObject = new JSONObject();
		chatObject.put("text", text);
	}

	@SuppressWarnings("unchecked")
	public JSONMessage addExtra(JSONChatExtra.ChatExtra extraObject) {
		if (!chatObject.containsKey("extra")) {
			chatObject.put("extra", new JSONArray());
		}
		JSONArray extra = (JSONArray) chatObject.get("extra");
		extra.add(extraObject.toJSON());
		chatObject.put("extra", extra);
		return this;
	}

	public String toString() {
		return chatObject.toJSONString();
	}



	public static JSONMessage buildMessage(String init) {
		return new JSONMessage(init);
	}
}
