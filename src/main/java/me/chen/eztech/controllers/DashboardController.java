package me.chen.eztech.controllers;


import net.bytebuddy.asm.Advice;
import org.flowable.engine.RepositoryService;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.idm.api.IdmIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    RepositoryService repositoryService;

    @GetMapping("/dashboard")
    public String dashboard(){

        return "dashboard";
    }
}
