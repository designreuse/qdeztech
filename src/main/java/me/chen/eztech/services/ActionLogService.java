package me.chen.eztech.services;

import me.chen.eztech.models.ActionLog;
import me.chen.eztech.repositories.ActionLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActionLogService {

    @Autowired
    ActionLogRepository actionLogRepository;

    public ActionLog save(ActionLog actionLog){
        return actionLogRepository.save(actionLog);
    }

    /**
     * Get Actions log by owner id
     * @param ownerId
     * @return
     */
    public List<ActionLog> getActionLogsByOwner(String ownerId){
        return actionLogRepository.findTop10ActionLogsByProjectOwnerIdOrderByActionTimeDesc(ownerId);
    }

    /**
     * Get log by logger id
     * @param loggerId
     * @return
     */
    public List<ActionLog> getActionLogsByLogger(String loggerId){
        return actionLogRepository.findTop10ActionLogsByUserIdOrderByActionTimeDesc(loggerId);
    }

}
