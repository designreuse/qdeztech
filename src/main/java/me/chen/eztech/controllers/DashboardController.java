package me.chen.eztech.controllers;


import me.chen.eztech.dtos.ProjectDto;
import me.chen.eztech.dtos.ProjectInitObj;
import net.bytebuddy.asm.Advice;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.runtime.Execution;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.idm.api.IdmIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    RepositoryService repositoryService;
    @Autowired
    RuntimeService runtimeService;

    @GetMapping("/dashboard")
    public String dashboard(Principal principal, Model model){

        // Get list of projects I'm working on
        String userId = principal.getName();
        List<ProcessInstance> instanceList = runtimeService
                .createProcessInstanceQuery()
                .processDefinitionKey("eztech")
                .includeProcessVariables()
                .startedBy(userId)
                .list();

        List<ProjectDto> projectDtos = new ArrayList<>();
        instanceList.forEach(processInstance -> {
            ProjectDto projectDto = new ProjectDto();

            List<Execution> executions = runtimeService
                    .createExecutionQuery()
                    .processInstanceId(processInstance.getProcessInstanceId())
                    .list();

            processInstance.getProcessVariables().forEach((k,v) ->{
                if(v instanceof ProjectInitObj){
                    ProjectInitObj projectInitObj = (ProjectInitObj)v;
                    projectDto.setName(projectInitObj.getProjectName());
                    projectDto.setDeadline(projectInitObj.getProjectDueDate());
                    projectDto.setDesc(projectInitObj.getProjectDesc());

                    projectDtos.add(projectDto);
                }
            });
        });

        model.addAttribute("projectList", projectDtos);
        model.addAttribute("projectCnt", projectDtos.size());

        return "dashboard";
    }
}
