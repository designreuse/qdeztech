package me.chen.eztech.controllers;


import me.chen.eztech.forms.*;
import me.chen.eztech.models.ActionLog;
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
import java.util.List;

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

    /**
     * Kick off project
     * @param projectFmt
     * @param principal
     * @return
     */
    @PostMapping("/startproject")
    public String startProcess(ProjectFmt projectFmt, Principal principal){

        ProcessInstance processInstance = service.startProcess(projectFmt);

        // Complete first task createProject
        Task task = service.getCurrentTask(processInstance.getProcessInstanceId());

        // Complete this task
        task.setAssignee(principal.getName());
        taskService.complete(task.getId());


        ActionLog actionLog = new ActionLog();
        actionLog.setAction(principal.getName() + " create project: " + projectFmt.getProjectName() + " that due on " + projectFmt.getProjectDueDate());
        actionLog.setActionTime(new Timestamp(System.currentTimeMillis()));
        actionLog.setUserId(principal.getName());
        actionLog.setProjectOwnerId(service.getStarterByProcessId(processInstance.getProcessInstanceId()));

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


    /**
     * Assign student (single for now)
     * @param assignStudentsFmt
     * @param principal
     * @return
     */
    @PostMapping("/assignstudents")
    public String assignStudents(AssignStudentsFmt assignStudentsFmt, Principal principal){

         // Write to database
        Project project = projectService.getProjectById(assignStudentsFmt.getProjectId());
        project.setStudentId(assignStudentsFmt.getSelectedStudents());

        projectService.save(project);

        // Move to next step
        // Complete first task createProject
        ProcessInstance processInstance = runtimeService
                .createProcessInstanceQuery()
                .includeProcessVariables()
                .processInstanceId(assignStudentsFmt.getProjectId()).singleResult();
        Task task = service.getCurrentTask(processInstance.getProcessInstanceId());
        // Complete task
        taskService.complete(task.getId());

        // Set Assignee to next task
        task = service.getCurrentTask(processInstance.getProcessInstanceId());
        task.setAssignee(assignStudentsFmt.getSelectedStudents());
        taskService.setAssignee(task.getId(), assignStudentsFmt.getSelectedStudents());

        ActionLog actionLog = new ActionLog();
        actionLog.setAction(principal.getName() + " assign student: " + assignStudentsFmt.getSelectedStudents() + " to project: " + processInstance.getProcessVariables().get("name"));
        actionLog.setActionTime(new Timestamp(System.currentTimeMillis()));
        actionLog.setUserId(principal.getName());
        actionLog.setProjectOwnerId(service.getStarterByProcessId(task.getProcessInstanceId()));
        actionLogService.save(actionLog);

        List<Task> taskList = taskService.createTaskQuery().taskCandidateOrAssigned(assignStudentsFmt.getSelectedStudents()).list();
        return "redirect:/dashboard";
    }


    /**
     * Student accept the project invitation
     * @param acceptProjectFmt
     * @param principal
     * @return
     */
    @PostMapping("/acceptproject")
    public String acceptProject(AcceptProjectFmt acceptProjectFmt, Principal principal){

        // Get the task
        Task task = taskService.createTaskQuery().taskId(acceptProjectFmt.getTaskId()).singleResult();
        taskService.setVariable(acceptProjectFmt.getTaskId(), "comment", acceptProjectFmt.getAcceptInvitationComment());
        taskService.complete(acceptProjectFmt.getTaskId());

        ActionLog actionLog = new ActionLog();
        actionLog.setAction(principal.getName() + " accept task: " + task.getName());
        actionLog.setActionTime(new Timestamp(System.currentTimeMillis()));
        actionLog.setUserId(principal.getName());
        actionLog.setProjectOwnerId(service.getStarterByProcessId(task.getProcessInstanceId()));
        actionLogService.save(actionLog);

        // Assign next task to self (submitTopic)
        task = service.getCurrentTask(task.getProcessInstanceId());
        taskService.setAssignee(task.getId(), principal.getName());

        // Update process variable set student ID for it
        runtimeService.setVariable(task.getProcessInstanceId(), "studentid", principal.getName());

        return "redirect:/dashboard";
    }

    /**
     * Submit the topic
     * @param submitTopicFmt
     * @param principal
     * @return
     */
    @PostMapping("/submittopic")
    public String submitTopic(SubmitTopicFmt submitTopicFmt, Principal principal){
        // Get task
        Task task = taskService.createTaskQuery().taskId(submitTopicFmt.getTaskId()).singleResult();
        taskService.setVariable(submitTopicFmt.getTaskId(), "topic", submitTopicFmt.getSubmitTopicContent());
        taskService.complete(submitTopicFmt.getTaskId());

        ActionLog actionLog = new ActionLog();
        actionLog.setAction(principal.getName() + " submitted topic");
        actionLog.setActionTime(new Timestamp(System.currentTimeMillis()));
        actionLog.setUserId(principal.getName());
        actionLog.setProjectOwnerId(service.getStarterByProcessId(task.getProcessInstanceId()));
        actionLogService.save(actionLog);

        // Assign next task to professor (reviewTopic)
        task = service.getCurrentTask(task.getProcessInstanceId());
        taskService.setAssignee(task.getId(), service.getStarterByProcessId(task.getProcessInstanceId()));
        taskService.setOwner(task.getId(), service.getStarterByProcessId(task.getProcessInstanceId()));
        taskService.setVariable(task.getId(), "topic", submitTopicFmt.getSubmitTopicContent());
        taskService.setVariable(task.getId(), "submitter",principal.getName());

        return "redirect:/dashboard";

    }


    @PostMapping("/reviewtopic")
    public String reviewTopic(ReviewTopicFmt reviewTopicFmt, Principal principal){

        // Get task
        Task task = taskService.createTaskQuery().taskId(reviewTopicFmt.getTaskId()).includeProcessVariables().singleResult();
        taskService.setVariable(reviewTopicFmt.getTaskId(),"reviewcomment", reviewTopicFmt.getReviewTopicContent());
        taskService.complete(reviewTopicFmt.getTaskId());

        String assignee = task.getProcessVariables().get("submitter").toString();

        ActionLog actionLog = new ActionLog();
        actionLog.setAction(principal.getName() + " submitted topic review and approved.");
        actionLog.setActionTime(new Timestamp(System.currentTimeMillis()));
        actionLog.setUserId(principal.getName());
        actionLog.setProjectOwnerId(service.getStarterByProcessId(task.getProcessInstanceId()));
        actionLogService.save(actionLog);

        // Assign next task to the student submitted topic review
        task = service.getCurrentTask(task.getProcessInstanceId());
        taskService.setAssignee(task.getId(), assignee);


        return "redirect:/dashboard";
    }

    @PostMapping("/submitproposal")
    public String submitProposal(SubmitProposalFmt proposalFmt, Principal principal){

        // Get task
        Task task = taskService.createTaskQuery().taskId(proposalFmt.getTaskId()).includeProcessVariables().singleResult();
        taskService.setVariable(proposalFmt.getTaskId(),"topicbrifing", proposalFmt.getSubmitProposalContent());
        taskService.complete(proposalFmt.getTaskId());


        ActionLog actionLog = new ActionLog();
        actionLog.setAction(principal.getName() + " submitted proposal.");
        actionLog.setActionTime(new Timestamp(System.currentTimeMillis()));
        actionLog.setUserId(principal.getName());
        actionLog.setProjectOwnerId(service.getStarterByProcessId(task.getProcessInstanceId()));
        actionLogService.save(actionLog);

        // Assign next task to the student submitted topic review
        task = service.getCurrentTask(task.getProcessInstanceId());
        taskService.setAssignee(task.getId(), service.getStarterByProcessId(task.getProcessInstanceId()));
        taskService.setVariable(task.getId(), "proposalbrifing", proposalFmt.getSubmitProposalContent());
        taskService.setVariable(task.getId(), "submitter",principal.getName());

        return "redirect:/dashboard";
    }

    @PostMapping("/reviewproposal")
    public String reviewProposal(ReviewProposalFmt reviewProposalFmt, Principal principal){

        // Get task
        Task task = taskService.createTaskQuery().taskId(reviewProposalFmt.getTaskId()).includeProcessVariables().singleResult();
        taskService.setVariable(reviewProposalFmt.getTaskId(),"reviewcomment", reviewProposalFmt.getReviewProposalContent());
        taskService.complete(reviewProposalFmt.getTaskId());

        String assignee = task.getProcessVariables().get("submitter").toString();

        ActionLog actionLog = new ActionLog();
        actionLog.setAction(principal.getName() + " submitted proposal review and approved.");
        actionLog.setActionTime(new Timestamp(System.currentTimeMillis()));
        actionLog.setUserId(principal.getName());
        actionLog.setProjectOwnerId(service.getStarterByProcessId(task.getProcessInstanceId()));
        actionLogService.save(actionLog);

        // Assign next task to the student submitted topic review
        task = service.getCurrentTask(task.getProcessInstanceId());
        taskService.setAssignee(task.getId(), assignee);


        return "redirect:/dashboard";
    }

    @PostMapping("/submitresearchmethod")
    public String submitResearchMethod(SubmitResearchMethodFmt researchMethodFmt, Principal principal){

        // Get task
        Task task = taskService.createTaskQuery().taskId(researchMethodFmt.getTaskId()).includeProcessVariables().singleResult();
        taskService.setVariable(researchMethodFmt.getTaskId(),"researchmethod", researchMethodFmt.getSubmitResearchMethod());
        taskService.complete(researchMethodFmt.getTaskId());


        ActionLog actionLog = new ActionLog();
        actionLog.setAction(principal.getName() + " submitted proposal.");
        actionLog.setActionTime(new Timestamp(System.currentTimeMillis()));
        actionLog.setUserId(principal.getName());
        actionLog.setProjectOwnerId(service.getStarterByProcessId(task.getProcessInstanceId()));
        actionLogService.save(actionLog);

        // Assign next task to the student submitted topic review
        task = service.getCurrentTask(task.getProcessInstanceId());
        taskService.setAssignee(task.getId(), service.getStarterByProcessId(task.getProcessInstanceId()));
        taskService.setVariable(task.getId(), "researchmethod", researchMethodFmt.getSubmitResearchMethod());
        taskService.setVariable(task.getId(), "submitter",principal.getName());

        return "redirect:/dashboard";
    }

    @PostMapping("/reviewresearchmethod")
    public String reviewResearchMethod(ReviewResearchMethodFmt reviewResearchMethodFmt, Principal principal){

        // Get task
        Task task = taskService.createTaskQuery().taskId(reviewResearchMethodFmt.getTaskId()).includeProcessVariables().singleResult();
        taskService.setVariable(reviewResearchMethodFmt.getTaskId(),"reviewcomment", reviewResearchMethodFmt.getReviewResearchMethodContent());
        taskService.complete(reviewResearchMethodFmt.getTaskId());

        String assignee = task.getProcessVariables().get("submitter").toString();

        ActionLog actionLog = new ActionLog();
        actionLog.setAction(principal.getName() + " submitted research method review and approved.");
        actionLog.setActionTime(new Timestamp(System.currentTimeMillis()));
        actionLog.setUserId(principal.getName());
        actionLog.setProjectOwnerId(service.getStarterByProcessId(task.getProcessInstanceId()));
        actionLogService.save(actionLog);

        // Assign next task to the student submitted topic review
        task = service.getCurrentTask(task.getProcessInstanceId());
        taskService.setAssignee(task.getId(), assignee);

        return "redirect:/dashboard";
    }

    @PostMapping("/submitresearchdata")
    public String submitResearchData(SubmitResearchDataFmt researchDataFmt, Principal principal){

        // Get task
        Task task = taskService.createTaskQuery().taskId(researchDataFmt.getTaskId()).includeProcessVariables().singleResult();
        taskService.setVariable(researchDataFmt.getTaskId(),"researchdatabrifing", researchDataFmt.getSubmitResearchData());
        taskService.complete(researchDataFmt.getTaskId());


        ActionLog actionLog = new ActionLog();
        actionLog.setAction(principal.getName() + " submitted research date.");
        actionLog.setActionTime(new Timestamp(System.currentTimeMillis()));
        actionLog.setUserId(principal.getName());
        actionLog.setProjectOwnerId(service.getStarterByProcessId(task.getProcessInstanceId()));
        actionLogService.save(actionLog);

        // Assign next task to the student submitted topic review
        task = service.getCurrentTask(task.getProcessInstanceId());
        taskService.setAssignee(task.getId(), service.getStarterByProcessId(task.getProcessInstanceId()));
        taskService.setVariable(task.getId(), "researchdatabrifing", researchDataFmt.getSubmitResearchData());
        taskService.setVariable(task.getId(), "submitter",principal.getName());

        return "redirect:/dashboard";
    }

    @PostMapping("/reviewresearchdata")
    public String reviewResearchData(ReviewResearchDataFmt reviewResearchDataFmt, Principal principal){

        // Get task
        Task task = taskService.createTaskQuery().taskId(reviewResearchDataFmt.getTaskId()).includeProcessVariables().singleResult();
        taskService.setVariable(reviewResearchDataFmt.getTaskId(),"reviewcomment", reviewResearchDataFmt.getReviewResearchDataContent());
        taskService.complete(reviewResearchDataFmt.getTaskId());

        String assignee = task.getProcessVariables().get("submitter").toString();

        ActionLog actionLog = new ActionLog();
        actionLog.setAction(principal.getName() + " reviewed research data and approved.");
        actionLog.setActionTime(new Timestamp(System.currentTimeMillis()));
        actionLog.setUserId(principal.getName());
        actionLog.setProjectOwnerId(service.getStarterByProcessId(task.getProcessInstanceId()));
        actionLogService.save(actionLog);

        // Assign next task to the student submitted topic review
        task = service.getCurrentTask(task.getProcessInstanceId());
        taskService.setAssignee(task.getId(), assignee);

        return "redirect:/dashboard";
    }


    @PostMapping("/submitanalysisdata")
    public String submitAnalysisData(SubmitAnalysisDataFmt researchDataFmt, Principal principal){

        // Get task
        Task task = taskService.createTaskQuery().taskId(researchDataFmt.getTaskId()).includeProcessVariables().singleResult();
        taskService.setVariable(researchDataFmt.getTaskId(),"analysisdatabrifing", researchDataFmt.getSubmitAnalysisData());
        taskService.complete(researchDataFmt.getTaskId());


        ActionLog actionLog = new ActionLog();
        actionLog.setAction(principal.getName() + " submitted analysis date.");
        actionLog.setActionTime(new Timestamp(System.currentTimeMillis()));
        actionLog.setUserId(principal.getName());
        actionLog.setProjectOwnerId(service.getStarterByProcessId(task.getProcessInstanceId()));
        actionLogService.save(actionLog);

        // Assign next task to the student submitted topic review
        task = service.getCurrentTask(task.getProcessInstanceId());
        taskService.setAssignee(task.getId(), service.getStarterByProcessId(task.getProcessInstanceId()));
        taskService.setVariable(task.getId(), "analysisdatabrifing", researchDataFmt.getSubmitAnalysisData());
        taskService.setVariable(task.getId(), "submitter",principal.getName());

        return "redirect:/dashboard";
    }

    @PostMapping("/reviewresearchdataresult")
    public String reviewResearchData(ReviewResearchDataResultFmt reviewResearchDataResultFmt, Principal principal){

        // Get task
        Task task = taskService.createTaskQuery().taskId(reviewResearchDataResultFmt.getTaskId()).includeProcessVariables().singleResult();
        taskService.setVariable(reviewResearchDataResultFmt.getTaskId(),"reviewcomment", reviewResearchDataResultFmt.getReviewResearchDataResultContent());
        taskService.complete(reviewResearchDataResultFmt.getTaskId());

        String assignee = task.getProcessVariables().get("submitter").toString();

        ActionLog actionLog = new ActionLog();
        actionLog.setAction(principal.getName() + " reviewed research data result and approved.");
        actionLog.setActionTime(new Timestamp(System.currentTimeMillis()));
        actionLog.setUserId(principal.getName());
        actionLog.setProjectOwnerId(service.getStarterByProcessId(task.getProcessInstanceId()));
        actionLogService.save(actionLog);

        // Assign next task to the student submitted topic review
        task = service.getCurrentTask(task.getProcessInstanceId());
        taskService.setAssignee(task.getId(), assignee);

        return "redirect:/dashboard";
    }

    @PostMapping("/submitconclusion")
    public String submitAnalysisData(SubmitConclusionFmt conclusionFmt, Principal principal){

        // Get task
        Task task = taskService.createTaskQuery().taskId(conclusionFmt.getTaskId()).includeProcessVariables().singleResult();
        taskService.setVariable(conclusionFmt.getTaskId(),"conclusionbrifing", conclusionFmt.getSubmitConclusion());
        taskService.complete(conclusionFmt.getTaskId());


        ActionLog actionLog = new ActionLog();
        actionLog.setAction(principal.getName() + " submitted conclusion.");
        actionLog.setActionTime(new Timestamp(System.currentTimeMillis()));
        actionLog.setUserId(principal.getName());
        actionLog.setProjectOwnerId(service.getStarterByProcessId(task.getProcessInstanceId()));
        actionLogService.save(actionLog);

        // Assign next task to the student submitted topic review
        task = service.getCurrentTask(task.getProcessInstanceId());
        taskService.setAssignee(task.getId(), service.getStarterByProcessId(task.getProcessInstanceId()));
        taskService.setVariable(task.getId(), "conclusionbrifing", conclusionFmt.getSubmitConclusion());
        taskService.setVariable(task.getId(), "submitter",principal.getName());

        return "redirect:/dashboard";
    }

    @PostMapping("/reviewconclusion")
    public String reviewResearchData(ReviewConclusionFmt conclusionFmt, Principal principal){

        // Get task
        Task task = taskService.createTaskQuery().taskId(conclusionFmt.getTaskId()).includeProcessVariables().singleResult();
        taskService.setVariable(conclusionFmt.getTaskId(),"reviewcomment", conclusionFmt.getReviewConclusionContent());
        taskService.complete(conclusionFmt.getTaskId());

        String assignee = task.getProcessVariables().get("submitter").toString();

        ActionLog actionLog = new ActionLog();
        actionLog.setAction(principal.getName() + " reviewed conclusion and approved.");
        actionLog.setActionTime(new Timestamp(System.currentTimeMillis()));
        actionLog.setUserId(principal.getName());
        actionLog.setProjectOwnerId(service.getStarterByProcessId(task.getProcessInstanceId()));
        actionLogService.save(actionLog);

        // Assign next task to the student submitted topic review
        task = service.getCurrentTask(task.getProcessInstanceId());
        taskService.setAssignee(task.getId(), assignee);

        return "redirect:/dashboard";
    }
}
