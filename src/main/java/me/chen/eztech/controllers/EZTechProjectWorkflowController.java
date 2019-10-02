package me.chen.eztech.controllers;


import me.chen.eztech.dtos.ProjectInitObj;
import me.chen.eztech.services.EZTechProjectWorkflowService;
import me.chen.eztech.services.EZTechWorkflowService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class EZTechProjectWorkflowController {

    @Autowired
    EZTechProjectWorkflowService service;
    @Autowired
    TaskService taskService;

    @PostMapping("/startproject")
    public void startProcess(ProjectInitObj projectInitObj, Principal principal){

        projectInitObj.setProjectOwnerId(principal.getName());
        ProcessInstance processInstance = service.startProcess(projectInitObj);

        // Complete first task createProject
        Task task = taskService
                .createTaskQuery()
                .active()
                .processInstanceId(processInstance.getProcessInstanceId())
                .singleResult();

        // Complete this task
        task.setOwner(principal.getName());
        task.setAssignee(principal.getName());
        taskService.complete(task.getId());

        // Print next task
        task = taskService
                .createTaskQuery()
                .active()
                .processInstanceId(processInstance.getProcessInstanceId())
                .singleResult();

        System.out.println("Current task: " + task.getName());

    }
}
