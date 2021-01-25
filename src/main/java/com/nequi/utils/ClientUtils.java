package com.nequi.utils;

import com.google.gson.JsonObject;

public class ClientUtils {
    /**
     * Gets the JSON object status from the AWS Client response header
     * @param jsonResponse JSON object returned by the AWS Client call
     * @return JSON object with the request status information
     */
    public static JsonObject getStatusResult(JsonObject jsonResponse) {
        JsonObject jsonStatus = jsonResponse
                .getAsJsonObject("ResponseMessage")
                .getAsJsonObject("ResponseHeader")
                .getAsJsonObject("Status");

        String statusCode = jsonStatus.get("StatusCode").getAsString();
        String statusDesc = jsonStatus.get("StatusDesc").getAsString();

        JsonObject jsonResult = new JsonObject();
        jsonResult.addProperty("code", statusCode != null ? statusCode : "");
        jsonResult.addProperty("desc", statusDesc != null ? statusDesc : "");

        return jsonResult;
    }

    /**
     * Gets the JSON object result from the AWS Client response body
     * @param jsonResponse JSON object returned by the AWS Client call
     * @param key JSON key to look up
     * @return JSON object extracted from the response
     */
    public static JsonObject getResponseResult(JsonObject jsonResponse, String key) {
        JsonObject jsonResult = jsonResponse
            .getAsJsonObject("ResponseMessage")
            .getAsJsonObject("ResponseBody")
            .getAsJsonObject("any")
            .getAsJsonObject(key);

        return jsonResult != null ? jsonResult : new JsonObject();
    }
}
