package me.chen.eztech.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ProjectInitObj implements Serializable {

    private String projectName;
    private String projectDueDate;
    private String projectOwnerId;
    private String projectDesc;

}
