package me.chen.eztech.controllers;


import me.chen.eztech.dtos.ProjectDto;
import me.chen.eztech.models.ActionLog;
import me.chen.eztech.services.ActionLogService;
import me.chen.eztech.services.EZTechProjectWorkflowService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    EZTechProjectWorkflowService service;

    @Autowired
    ActionLogService actionLogService;

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model){

        // Get list of projects I'm working on
        String userId = principal.getName();
        List<ProcessInstance> instanceList = runtimeService
                .createProcessInstanceQuery()
                .processDefinitionKey("eztech")
                .includeProcessVariables()
                .startedBy(userId)
                .orderByStartTime().desc()
                .list();

        List<ProjectDto> projectDtos = new ArrayList<>();
        instanceList.forEach(processInstance -> {
            ProjectDto projectDto = new ProjectDto();

            // Get current stage
            Task task = service.getCurrentTask(processInstance.getProcessInstanceId());
            Map<String, Object> variables = processInstance.getProcessVariables();
            projectDto.setName(variables.get("name").toString());
            projectDto.setDesc(variables.get("desc").toString());
            projectDto.setDeadline(variables.get("deadline").toString());
            projectDto.setCurrentStage(task.getName());

            projectDtos.add(projectDto);
        });

        model.addAttribute("projectList", projectDtos);
        model.addAttribute("projectCnt", projectDtos.size());

        // Get Activities
        List<ActionLog> actionLogs = actionLogService.getActionLogsByOwner(userId);
        model.addAttribute("activities", actionLogs);

        return "dashboard";
    }
}
