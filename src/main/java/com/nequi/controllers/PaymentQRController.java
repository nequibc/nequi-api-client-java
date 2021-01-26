package com.nequi.controllers;

import com.amazonaws.mobileconnectors.apigateway.ApiClientFactory;
import com.google.gson.JsonObject;
import com.nequi.auth.NequiAuth;
import com.nequi.payments.client.NequiPaymentsGatewayClient;
import com.nequi.utils.BodyUtils;
import com.nequi.utils.ClientUtils;
import com.nequi.utils.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller()
public class PaymentQRController {
    @GetMapping("/payment-qr/generate-qr-code")
    public String generateQrCode(Model model) {
        ArrayList logs = new ArrayList();

        try {
            logs.add(new String[] { "info", "Generando código QR..."});

            ApiClientFactory factory = new ApiClientFactory();
            factory.apiKey(System.getenv(Constants.ENV_VAR_NEQUI_API_KEY));

            NequiPaymentsGatewayClient client = factory.build(NequiPaymentsGatewayClient.class);

            JsonObject jsonResponse = client.servicesPaymentserviceGeneratecodeqrPost(
                BodyUtils.getBodyGenerateQR(),
                NequiAuth.getInstance().fromEnvVars().getToken()
            );

            logs.add(new String[] { "info", "El API procesó la solicitud correctamente"});

            JsonObject jsonStatus = ClientUtils.getStatusResult(jsonResponse);

            String statusCode = jsonStatus.get("code").getAsString();
            String statusDesc = jsonStatus.get("desc").getAsString();

            if (statusCode != null && Constants.NEQUI_STATUS_CODE_SUCCESS.equals(statusCode)) {
                logs.add(new String[] { "success", "Código generado correctamente"});

                JsonObject jsonResult = ClientUtils.getResponseResult(jsonResponse, "generateCodeQRRS");

                String codeQR = jsonResult.get("codeQR").getAsString();

                logs.add(new String[] { "success", String.format("Código QR: %s", codeQR)});
            } else {
                throw new Exception(String.format("Error %s = %s", statusCode, statusDesc.trim()));
            }
        } catch (Exception e) {
            logs.add(new String[] { "error", e.getMessage() });
        }

        model.addAttribute("logs", logs);

        return "payment_qr/generateQRCode";
    }
}
