package me.chen.eztech.dtos;

import lombok.Data;

import java.util.Date;

@Data
public class TaskDto {

    private String id;
    private String name;
    private String desc;

    private Date dueDate;
}
