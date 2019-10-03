package me.chen.eztech.controllers;

import me.chen.eztech.models.ActionLog;
import me.chen.eztech.services.ActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    ActionLogService actionLogService;


    /**
     * Get all actions log that process belongs to my
     * @param principal
     */
    @GetMapping("/actionlog/owner")
    public List<ActionLog> getActions(Principal principal){

        return actionLogService.getActionLogsByOwner(principal.getName());
    }
}
