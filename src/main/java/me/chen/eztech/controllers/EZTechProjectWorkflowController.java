package me.chen.eztech.controllers;


import me.chen.eztech.models.ActionLog;
import me.chen.eztech.forms.ProjectFmt;
import me.chen.eztech.models.Project;
import me.chen.eztech.services.ActionLogService;
import me.chen.eztech.services.EZTechProjectWorkflowService;
import me.chen.eztech.services.ProjectService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.sql.Timestamp;

@Controller
public class EZTechProjectWorkflowController {

    @Autowired
    EZTechProjectWorkflowService service;
    @Autowired
    TaskService taskService;
    @Autowired
    RuntimeService runtimeService;

    @Autowired
    ActionLogService actionLogService;
    @Autowired
    ProjectService projectService;

    @PostMapping("/startproject")
    public String startProcess(ProjectFmt projectFmt, Principal principal){

        ProcessInstance processInstance = service.startProcess(projectFmt);

        // Complete first task createProject
        Task task = service.getCurrentTask(processInstance.getProcessInstanceId());

        // Complete this task
        task.setOwner(principal.getName());
        task.setAssignee(principal.getName());
        taskService.complete(task.getId());


        ActionLog actionLog = new ActionLog();
        actionLog.setAction(principal.getName() + " create project: " + projectFmt.getProjectName() + " that due on " + projectFmt.getProjectDueDate());
        actionLog.setActionTime(new Timestamp(System.currentTimeMillis()));
        actionLog.setUserId(principal.getName());
        actionLog.setProjectOwnerId(principal.getName());

        actionLogService.save(actionLog);

        // Save project to external data table
        Project project = new Project();
        project.setProjectId(processInstance.getProcessInstanceId());
        project.setProjectOwner(principal.getName());
        project.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        project.setOwner(principal.getName());
        projectService.save(project);

        return "redirect:/dashboard";

    }
}
