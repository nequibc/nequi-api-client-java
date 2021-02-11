package com.nequi.controllers;

import com.nequi.auth.NequiAuth;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

@Controller()
public class AuthController {
    @GetMapping("/auth/token")
    public String getToken(Model model) {
        ArrayList logs = new ArrayList();

        try {
            logs.add(new String[] { "info", "Obteniendo token..." });

            String token = NequiAuth.getInstance().fromEnvVars().getToken();

            logs.add(new String[] { "success", "Token obtenido correctamente" });
            logs.add(new String[] { "success", String.format("Token: %s", token) });
        } catch (Exception e) {
            logs.add(new String[] { "error", e.getMessage() });
        }

        model.addAttribute("logs", logs);

        return "auth/token";
    }
}
