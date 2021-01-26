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
public class PaymentPushController {
    @GetMapping("/payment-by-push/unregistered-payment")
    public String unregisteredPayment(Model model) {
        ArrayList logs = new ArrayList();

        try {
            logs.add(new String[] { "info", "Generando solicitud..."});

            ApiClientFactory factory = new ApiClientFactory();
            NequiPaymentsGatewayClient client = factory.build(NequiPaymentsGatewayClient.class);

            JsonObject jsonResponse = client.servicesPaymentserviceUnregisteredpaymentPost(
                BodyUtils.getBodyUnregisteredPayment(),
                NequiAuth.getInstance().fromEnvVars().getToken(),
                System.getenv(Constants.ENV_VAR_NEQUI_API_KEY)
            );

            logs.add(new String[] { "info", "El API procesó la solicitud correctamente"});

            JsonObject jsonStatus = ClientUtils.getStatusResult(jsonResponse);

            String statusCode = jsonStatus.get("code").getAsString();
            String statusDesc = jsonStatus.get("desc").getAsString();

            if (statusCode != null && Constants.NEQUI_STATUS_CODE_SUCCESS.equals(statusCode)) {
                logs.add(new String[] { "success", "Solicitud de pago realizada correctamente"});

                JsonObject jsonResult = ClientUtils.getResponseResult(jsonResponse, "unregisteredPaymentRS");

                String trnId = jsonResult.get("transactionId").getAsString();

                logs.add(new String[] { "success", String.format("Id Transacción: %s", trnId)});
            } else {
                throw new Exception(String.format("Error %s = %s", statusCode, statusDesc.trim()));
            }
        } catch (Exception e) {
            logs.add(new String[] { "error", e.getMessage() });
        }

        model.addAttribute("logs", logs);

        return "payment_push/unregisteredPayment";
    }
}
