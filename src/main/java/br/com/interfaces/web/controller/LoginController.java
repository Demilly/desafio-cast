package br.com.interfaces.web.controller;

import br.com.domain.enums.RoleEnum;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static br.com.domain.enums.RoleEnum.from;

@Controller
@RequestMapping("/login")
public class LoginController {

    @GetMapping
    public String loginPage(@RequestParam(required = false, defaultValue = "CLIENT") String role,
                            @RequestParam(required = false) String error,
                            Model model) {

        RoleEnum validRole = from(role);
        model.addAttribute("role", validRole.name());

        if (error != null) {
            String msg = validRole == RoleEnum.ADMIN ?
                    "Login de administrador inválido" :
                    "Login de cliente inválido";
            model.addAttribute("error", msg);
        }

        return "login/login";
    }
}
