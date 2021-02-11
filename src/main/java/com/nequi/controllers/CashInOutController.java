package com.nequi.controllers;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.google.gson.JsonObject;
import com.nequi.auth.NequiAuth;
import com.nequi.banking.client.NequiBankingAgentsGatewayClient;
import com.nequi.utils.BodyUtils;
import com.nequi.utils.ClientUtils;
import com.nequi.utils.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller()
public class CashInOutController {
    @GetMapping("/cash-in-out/validate-client")
    public String validateClient(Model model) {
        ArrayList logs = new ArrayList();

        try {
            logs.add(new String[] { "info", "Validando cliente..."});

            ApiClientFactory factory = new ApiClientFactory();
            factory.apiKey(System.getenv(Constants.ENV_VAR_NEQUI_API_KEY));

            NequiBankingAgentsGatewayClient client = factory.build(NequiBankingAgentsGatewayClient.class);

            JsonObject jsonResponse = client.servicesClientserviceValidateclientPost(
                BodyUtils.getBodyValidateClient(),
                NequiAuth.getInstance().fromEnvVars().getToken()
            );

            logs.add(new String[] { "info", "El API procesó la solicitud correctamente"});

            JsonObject jsonStatus = ClientUtils.getStatusResult(jsonResponse);

            String statusCode = jsonStatus.get("code").getAsString();
            String statusDesc = jsonStatus.get("desc").getAsString();

            if (statusCode != null && Constants.NEQUI_STATUS_CODE_SUCCESS.equals(statusCode)) {
                logs.add(new String[]{"success", "Validación de cliente finalizada"});

                JsonObject jsonResult = ClientUtils.getResponseResult(jsonResponse, "validateClientRS");

                String availableLimit = jsonResult.get("availableLimit").getAsString();
                String customerName = jsonResult.get("customerName").getAsString();

                logs.add(new String[] { "success", String.format("Cliente: %s", customerName)});
                logs.add(new String[] { "success", String.format("Límite disponible: %s", availableLimit)});
            } else {
                throw new Exception(String.format("Error %s = %s", statusCode, statusDesc.trim()));
            }
        } catch (Exception e) {
            logs.add(new String[] { "error", e.getMessage() });
        }

        model.addAttribute("logs", logs);

        return "cash_in_out/validateClient";
    }
}
