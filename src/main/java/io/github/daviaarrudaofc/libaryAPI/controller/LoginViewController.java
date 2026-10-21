package io.github.daviaarrudaofc.libaryAPI.controller;

import io.github.daviaarrudaofc.libaryAPI.security.CustomAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String paginaLogin(){
        return "login";
    }

    @GetMapping("/")
    @ResponseBody // vai pegar o retorno e colocar no corpo da resposta
    public String paginaHome(Authentication authentication){
        if(authentication instanceof CustomAuthentication customAuthentication){ //debugando e vendo como se comporta as variaveis a
            // a partir de login por google no OAuth2
            System.out.println(customAuthentication.getUsuario());
        }
        return "óla " + authentication.getName();

    }

    @GetMapping("/authorized")
    @ResponseBody
    public String getAuthorizationCode(@RequestParam("code") String code){
        return "Seu authorization code: " + code;
    }


}
