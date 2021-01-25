package com.nequi.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.nio.file.Files;

public class BodyUtils {
    public static JsonObject getBodyUnregisteredPayment() throws Exception {
        File file = new File(
            BodyUtils.class
                .getClassLoader()
                .getResource("json/payment_push/unregisteredPayment.json")
                .getFile()
        );

        String json = new String(Files.readAllBytes(file.toPath()));

        return new JsonParser().parse(json).getAsJsonObject();
    }
}
