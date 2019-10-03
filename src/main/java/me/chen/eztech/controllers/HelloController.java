package me.chen.eztech.controllers;


import me.chen.eztech.dtos.ProjectInitObj;
import me.chen.eztech.services.EZTechWorkflowService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class HelloController {
    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;

//    public HelloController(final RuntimeService runtimeService, final TaskService taskService){
//        this.runtimeService = runtimeService;
//        this.taskService = taskService;
//    }

    @GetMapping("/hello")
    public String hello(Principal principal){
        runtimeService.startProcessInstanceByKey("articleReview");

        System.out.println("Number of tasks created: " + taskService.createTaskQuery().count());

        return "Done";
    }

    @Autowired
    EZTechWorkflowService ezTechWorkflowService;

    @GetMapping("/start")
    public void submit(){
        ezTechWorkflowService.startProcess();
        List<Task> task = taskService.createTaskQuery()
                .list();


        task.sort(Comparator.comparing(Task::getCreateTime).reversed());
        task.forEach(t->{
            System.out.println(t.getCreateTime().toString());

//            taskService.deleteTask(t.getId());
//
            Map<String, Object> variables = new HashMap<>();
            variables.put("approved", true);
            // Approval the review
            taskService.complete(t.getId(), variables);
        });
    }

    @GetMapping("/status")
    public void stopAllProcess(Principal principal){
        String userId = principal.getName();
        List<ProcessInstance> instanceList = runtimeService
                .createProcessInstanceQuery()
                .processDefinitionKey("eztech")
                .includeProcessVariables()
                .startedBy(userId)
                .list();

        instanceList.forEach(processInstance -> {
//            runtimeService.deleteProcessInstance(processInstance.getBusinessKey());

            System.out.print("Business Key: " + processInstance.getProcessDefinitionKey());
            System.out.print(",     Business InstanceID: " + processInstance.getProcessInstanceId());
            System.out.println(",   Business ID: " + processInstance.getProcessDefinitionId());
            processInstance.getProcessVariables().forEach((k,v) ->{
                if(v instanceof ProjectInitObj){
                    ProjectInitObj projectInitObj = (ProjectInitObj) v;
                    System.out.println("Project name: " + projectInitObj.getProjectName());
                    System.out.println("Project Desc: " + projectInitObj.getProjectDesc());
                    System.out.println("Current stage: " + projectInitObj.getCurrentStage());
                }
                else{System.out.println(k + ": " + v);}
            });


            runtimeService.deleteProcessInstance(processInstance.getProcessInstanceId(), "Clean");
        });
    }
}
