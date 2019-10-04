package me.chen.eztech.controllers;


import me.chen.eztech.forms.AcceptProjectFmt;
import me.chen.eztech.forms.AssignStudentsFmt;
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
        taskService.setOwner(task.getId(), principal.getName());

        ActionLog actionLog = new ActionLog();
        actionLog.setAction(principal.getName() + " assign student: " + assignStudentsFmt.getSelectedStudents() + " to project: " + processInstance.getProcessVariables().get("name"));
        actionLog.setActionTime(new Timestamp(System.currentTimeMillis()));
        actionLog.setUserId(principal.getName());
        actionLog.setProjectOwnerId(principal.getName());
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

//        runtimeService.createProcessInstanceQuery().

        ActionLog actionLog = new ActionLog();
        actionLog.setAction(principal.getName() + " accept task: " + task.getName());
        actionLog.setActionTime(new Timestamp(System.currentTimeMillis()));
        actionLog.setUserId(principal.getName());
        actionLog.setProjectOwnerId(task.getOwner());
        actionLogService.save(actionLog);

        // Assign next task to self (submitTopic)
        task = service.getCurrentTask(task.getProcessInstanceId());
        taskService.setAssignee(task.getId(), principal.getName());

        return "redirect:/dashboard";
    }
}
