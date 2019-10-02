package me.chen.eztech.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class ProjectInitObj {

    private String projectName;
    private String projectDueDate;
    private String projectOwnerId;
    private String projectDesc;

}
