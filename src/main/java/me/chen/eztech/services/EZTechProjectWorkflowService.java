package me.chen.eztech.services;


import me.chen.eztech.dtos.ProjectInitObj;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class EZTechProjectWorkflowService {

    @Autowired
    RuntimeService runtimeService;
    @Autowired
    TaskService taskService;

    @Transactional
    public void startProcess(ProjectInitObj projectInitObj){
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("projectName", projectInitObj.getProjectName());
        variables.put("projectDueDate", projectInitObj.getProjectDueDate());
        variables.put("projectOwnerId", projectInitObj.getProjectOwnerId());
        variables.put("projectDesc", projectInitObj.getProjectDesc());

        runtimeService.startProcessInstanceByKey("eztech", variables);
    }
}
