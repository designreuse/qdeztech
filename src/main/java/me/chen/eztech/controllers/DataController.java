package me.chen.eztech.controllers;

import me.chen.eztech.dtos.UserDto;
import me.chen.eztech.models.ActionLog;
import me.chen.eztech.services.ActionLogService;
import me.chen.eztech.services.PrivilegeService;
import org.flowable.idm.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    ActionLogService actionLogService;
    @Autowired
    PrivilegeService privilegeService;


    /**
     * Get all actions log that process belongs to my
     * @param principal
     */
    @GetMapping("/actionlog/owner")
    public List<ActionLog> getActions(Principal principal){

        return actionLogService.getActionLogsByOwner(principal.getName());
    }

}
