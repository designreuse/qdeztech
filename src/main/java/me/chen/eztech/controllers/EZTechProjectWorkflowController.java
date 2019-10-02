package me.chen.eztech.controllers;


import me.chen.eztech.dtos.ProjectInitObj;
import me.chen.eztech.services.EZTechProjectWorkflowService;
import me.chen.eztech.services.EZTechWorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Date;

@RestController
@RequestMapping("/api")
public class EZTechProjectWorkflowController {

    @Autowired
    EZTechProjectWorkflowService service;

    @PostMapping("/startproject")
    public void startProcess(ProjectInitObj projectInitObj, Principal principal){

        projectInitObj.setProjectOwnerId(principal.getName());

        service.startProcess(projectInitObj);
    }
}
