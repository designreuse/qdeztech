package me.chen.eztech.services;


import me.chen.eztech.models.Project;
import me.chen.eztech.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    ProjectRepository projectRepository;

    /**
     * Create new project
     * @param project
     * @return
     */
    public Project save(Project project){
        return projectRepository.save(project);
    }
}
