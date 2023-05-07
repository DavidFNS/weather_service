package uz.weather.service.config;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;

import static uz.weather.service.config.Constants.DATE_TIME_FORMATTER;
public class LocalDateTypeAdapterConfig implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    @Override
    public JsonElement serialize(final LocalDateTime date, final Type typeOfSrc,
                                 final JsonSerializationContext context) {
        return new JsonPrimitive(date.format(DATE_TIME_FORMATTER));
    }

    @Override
    public LocalDateTime deserialize(final JsonElement json, final Type typeOfT,
                                 final JsonDeserializationContext context) throws JsonParseException {
        return LocalDateTime.parse(json.getAsString(), DATE_TIME_FORMATTER);
    }
}
