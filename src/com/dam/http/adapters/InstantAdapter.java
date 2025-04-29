package com.dam.http.adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.Instant;

public class InstantAdapter extends TypeAdapter<Instant> {
    @Override
    public void write(JsonWriter jsonWriter, Instant instant) throws IOException {
        if (jsonWriter != null && instant != null) {
            jsonWriter.value(instant.toEpochMilli());
        } else if (jsonWriter != null) {
            jsonWriter.nullValue();
        }
    }

//    @Override
    public Instant read(JsonReader jsonReader) throws IOException {

        // Проверка null-значения в JSON
        if (jsonReader.peek() == JsonToken.NULL) {
            jsonReader.nextNull();
            return null;
        }

        try {
            return Instant.ofEpochMilli(Long.parseLong(jsonReader.nextString()));
        } catch (NumberFormatException e) {
            throw new IOException("Invalid format", e);
        }

    }


}