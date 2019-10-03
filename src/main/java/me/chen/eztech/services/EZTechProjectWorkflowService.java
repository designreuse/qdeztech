package me.chen.eztech.services;

import me.chen.eztech.forms.ProjectFmt;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
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
    public ProcessInstance startProcess(ProjectFmt project){

        Map<String, Object> variables = new HashMap<>();
        variables.put("name", project.getProjectName());
        variables.put("desc", project.getProjectDesc());
        variables.put("deadline", project.getProjectDueDate());

        return runtimeService.startProcessInstanceByKey("eztech", variables);
    }


    /**
     * Get current active task
     * @param processInstanceId
     * @return
     */
    public Task getCurrentTask(String processInstanceId){

        return taskService
                .createTaskQuery()
                .active()
                .processInstanceId(processInstanceId)
                .singleResult();
    }


}
