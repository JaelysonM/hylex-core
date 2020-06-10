package com.uzm.hylex.core.skins.shared.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.Serializable;

/**
 * Created by McLive on 28.02.2019.
 */
public class Property implements Serializable {
  private String name;
  private String value;
  private String signature;

  boolean valuesFromJson(JsonObject obj) {
    if (obj.has("properties")) {
      JsonArray properties = obj.getAsJsonArray("properties");
      JsonObject propertiesObject = properties.get(0).getAsJsonObject();

      String signature = propertiesObject.get("signature").getAsString();
      String value = propertiesObject.get("value").getAsString();

      this.setSignature(signature);
      this.setValue(value);

      return true;
    }

    return false;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getValue() {
    return value;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }
}