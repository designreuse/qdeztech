package me.chen.eztech.repositories;

import me.chen.eztech.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, String> {

    public Project getProjectByProjectId(String projectId);
}
