package com.github.felipeaaa1.libraryapi.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginViewController {

    @GetMapping("/login")
    public String paginLogin(){
        return "login";
    }

    @GetMapping("/")
    @ResponseBody
    public String paginHome(Authentication authentication){
        return "Olá " + authentication.getName();
    }

    @GetMapping("/authorized")
    @ResponseBody
    public String getAuthonrizationCode(@RequestParam("code") String code){
        return "segue o cod:"+code;
    }
}
