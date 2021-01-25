package com.nequi.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.nio.file.Files;

public class BodyUtils {
    public static JsonObject getBodyUnregisteredPayment() throws Exception {
        return BodyUtils.loadJson("json/payment_push/unregisteredPayment.json");
    }

    public static JsonObject getBodyGenerateQR() throws Exception {
        return BodyUtils.loadJson("json/payment_qr/generateQr.json");
    }

    private static JsonObject loadJson(String jsonResourcePath) throws Exception {
        File file = new File(
                BodyUtils.class
                        .getClassLoader()
                        .getResource(jsonResourcePath)
                        .getFile()
        );

        String json = new String(Files.readAllBytes(file.toPath()));

        return new JsonParser().parse(json).getAsJsonObject();
    }
}
