package me.chen.eztech.repositories;

import me.chen.eztech.models.ActionLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActionLogRepository extends JpaRepository<ActionLog, String> {

    public List<ActionLog> getActionLogsByProjectOwnerIdOrderByActionTimeDesc(String ownerId);
}
