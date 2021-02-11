package com.nequi.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class BodyUtils {
    public static JsonObject getBodyUnregisteredPayment() throws Exception {
        return BodyUtils.loadJson("json/payment_push/unregisteredPayment.json");
    }

    public static JsonObject getBodyGenerateQR() throws Exception {
        return BodyUtils.loadJson("json/payment_qr/generateQr.json");
    }

    public static JsonObject getBodyNewSubscription() throws Exception {
        return BodyUtils.loadJson("json/subscriptions/newSubscription.json");
    }

    public static JsonObject getBodyValidateClient() throws Exception {
        return BodyUtils.loadJson("json/cash_in_out/validateClient.json");
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

    public String getRequestDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        return format.format(new Date());
    }

    public String getRequestMessageId() {
        return UUID.randomUUID().toString();
    }
}
