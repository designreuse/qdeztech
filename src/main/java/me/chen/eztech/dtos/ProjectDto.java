package me.chen.eztech.dtos;


import lombok.Data;

@Data
public class ProjectDto {

    private String id;
    private String name;
    private String desc;
    private String deadline;
    private String currentStage;
    private String currentStageKey;
}
