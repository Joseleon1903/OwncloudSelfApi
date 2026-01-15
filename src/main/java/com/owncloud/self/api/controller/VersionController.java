package com.owncloud.self.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class VersionController {


    @Autowired
    public VersionController( ) {
    }

    @GetMapping("/")
    public String version() {
        return "Welcome to the ownclod SelfApi Version API 0.0.0.1 !";
    }


}
