package me.chen.eztech.services;


import org.flowable.idm.api.IdmIdentityService;
import org.flowable.idm.api.Privilege;
import org.flowable.idm.api.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrivilegeService {

    @Autowired
    IdmIdentityService identityService;


    /**
     * Get user list by privilege name
     * @param privilegeName
     * @return
     */
    public List<User> getUserByPrivilegeName(String privilegeName){
        Privilege privilege = identityService.createPrivilegeQuery().privilegeName(privilegeName).singleResult();
        List<User> users = identityService.getUsersWithPrivilege(privilege.getId());

        return users;
    }
}
