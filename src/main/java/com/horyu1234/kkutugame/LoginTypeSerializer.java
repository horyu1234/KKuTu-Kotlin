package com.horyu1234.kkutugame;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * Created by horyu on 2017-04-13.
 */
@Component
public class LoginTypeSerializer implements JsonSerializer<LoginType> {
    @Override
    public JsonElement serialize(LoginType src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("name", src.getName());
        jsonObject.addProperty("login_url", src.getLoginURL());

        return jsonObject;
    }
}
