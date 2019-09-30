package me.chen.eztech.services;


import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EZTechWorkflowService {

    @Autowired
    private RuntimeService runtimeService;
    @Autowired
    private TaskService taskService;

    @Transactional
    public void startProcess(){
        runtimeService.startProcessInstanceByKey("articleReview");
    }
}
