package com.project.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/whoami")
public class IdentityController {
    @GetMapping
    public String getIdentity(@RequestAttribute("id") String userId) {
        return userId;
    }
}
