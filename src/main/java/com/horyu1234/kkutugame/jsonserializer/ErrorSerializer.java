package com.horyu1234.kkutugame.jsonserializer;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.horyu1234.kkutugame.Error;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

/**
 * Created by horyu on 2017-04-26.
 */
@Component
public class ErrorSerializer implements JsonSerializer<Error> {
    @Override
    public JsonElement serialize(Error src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getMessage());
    }
}
