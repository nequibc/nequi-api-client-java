package com.nequi.controllers;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.google.gson.JsonObject;
import com.nequi.auth.NequiAuth;
import com.nequi.subscriptions.client.NequiSubscriptionsGatewayClient;
import com.nequi.utils.BodyUtils;
import com.nequi.utils.ClientUtils;
import com.nequi.utils.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller()
public class SubscriptionsController {
    @GetMapping("/subscriptions/new")
    public String newSubscription(Model model) {
        ArrayList logs = new ArrayList();

        try {
            logs.add(new String[] { "info", "Creando nueva subscripción..."});

            ApiClientFactory factory = new ApiClientFactory();
            factory.apiKey(System.getenv(Constants.ENV_VAR_NEQUI_API_KEY));

            NequiSubscriptionsGatewayClient client = factory.build(NequiSubscriptionsGatewayClient.class);

            JsonObject jsonResponse = client.servicesSubscriptionpaymentserviceNewsubscriptionPost(
                BodyUtils.getBodyNewSubscription(),
                NequiAuth.getInstance().fromEnvVars().getToken()
            );

            logs.add(new String[] { "info", "El API procesó la solicitud correctamente"});

            JsonObject jsonStatus = ClientUtils.getStatusResult(jsonResponse);

            String statusCode = jsonStatus.get("code").getAsString();
            String statusDesc = jsonStatus.get("desc").getAsString();

            if (statusCode != null && Constants.NEQUI_STATUS_CODE_SUCCESS.equals(statusCode)) {
                logs.add(new String[]{"success", "Subscripción generada correctamente"});

                JsonObject jsonResult = ClientUtils.getResponseResult(jsonResponse, "newSubscriptionRS");

                String subscriptionToken = jsonResult.get("token").getAsString();

                logs.add(new String[] { "success", String.format("Token subscripción: %s", subscriptionToken)});
            } else {
                throw new Exception(String.format("Error %s = %s", statusCode, statusDesc.trim()));
            }
        } catch (Exception e) {
            logs.add(new String[] { "error", e.getMessage() });
        }

        model.addAttribute("logs", logs);

        return "subscriptions/new";
    }
}
