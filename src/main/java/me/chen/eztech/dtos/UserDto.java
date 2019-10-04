package me.chen.eztech.dtos;

import lombok.Data;

@Data
public class UserDto {

    private String id;
    private String firstName;
    private String lastName;
    private String displayName;
    private String tenantId;
    private String email;
}
