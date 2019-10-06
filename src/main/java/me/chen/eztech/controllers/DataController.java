package me.chen.eztech.controllers;

import me.chen.eztech.dtos.UserDto;
import me.chen.eztech.models.ActionLog;
import me.chen.eztech.services.ActionLogService;
import me.chen.eztech.services.EZTechProjectWorkflowService;
import me.chen.eztech.services.PrivilegeService;
import org.flowable.idm.api.User;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    ActionLogService actionLogService;
    @Autowired
    PrivilegeService privilegeService;
    @Autowired
    EZTechProjectWorkflowService ezTechProjectWorkflowService;


    /**
     * Get all actions log that process belongs to my
     * @param principal
     */
    @GetMapping("/actionlog/owner")
    public List<ActionLog> getActions(Principal principal){

        return actionLogService.getActionLogsByOwner(principal.getName());
    }

    @GetMapping("/topic/content/{taskid}")
    public String getSubmittedTopicContent(@PathVariable("taskid") String taskId){

        Task task = ezTechProjectWorkflowService.getTaskById(taskId);
        Map<String, Object> variables = task.getProcessVariables();

        Object topic = variables.get("topic");

        return topic == null ? "" : topic.toString();
    }

    @GetMapping("/proposal/content/{taskid}")
    public String getProposalBrifing(@PathVariable("taskid") String taskId){
        Task task = ezTechProjectWorkflowService.getTaskById(taskId);
        Map<String, Object> variables = task.getProcessVariables();
        Object proposalBrifing = variables.get("proposalbrifing");

        return proposalBrifing == null ? "" : proposalBrifing.toString();
    }

    @GetMapping("/researchmethod/content/{taskid}")
    public String getResearchMethodBrifing(@PathVariable("taskid") String taskId){
        Task task = ezTechProjectWorkflowService.getTaskById(taskId);
        Map<String, Object> variables = task.getProcessVariables();
        Object reseachMethodBrifing = variables.get("researchmethod");

        return reseachMethodBrifing == null ? "" : reseachMethodBrifing.toString();
    }

    @GetMapping("/researchdata/content/{taskid}")
    public String getResearchDataBrifing(@PathVariable("taskid") String taskId){
        Task task = ezTechProjectWorkflowService.getTaskById(taskId);
        Map<String, Object> variables = task.getProcessVariables();
        Object reseacDataBrifing = variables.get("researchdatabrifing");

        return reseacDataBrifing == null ? "" : reseacDataBrifing.toString();
    }

    @GetMapping("/researchdataresult/content/{taskid}")
    public String getResearchDataResultBrifing(@PathVariable("taskid") String taskId){
        Task task = ezTechProjectWorkflowService.getTaskById(taskId);
        Map<String, Object> variables = task.getProcessVariables();
        Object reseacDataResultBrifing = variables.get("analysisdatabrifing");

        return reseacDataResultBrifing == null ? "" : reseacDataResultBrifing.toString();
    }

    @GetMapping("/conclusion/content/{taskid}")
    public String getConclusionBrifing(@PathVariable("taskid") String taskId){

        Task task = ezTechProjectWorkflowService.getTaskById(taskId);
        Map<String, Object> variables = task.getProcessVariables();
        Object conclusionBrifing = variables.get("conclusionbrifing");

        return conclusionBrifing == null ? "" : conclusionBrifing.toString();
    }

}
