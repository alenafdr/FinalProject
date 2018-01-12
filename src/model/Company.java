package model;

import java.util.HashSet;
import java.util.Set;

public class Company extends Core{
    private Set<Project> projects;

    public Company(Long id, String name) {
        super(id,name);
        projects = new HashSet<>();
    }

    public String getProjectsString() {
        if (projects.isEmpty()) return "0;";
        StringBuilder result = new StringBuilder();
        for (Project project : projects){
            result.append(project.getId() + ";");
        }
        return result.toString();
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public void setProject(Project project) {
        this.projects.add(project);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(", \nprojects=\n");
        for (Project project : projects){
            stringBuilder.append("\t" + project.toString() + "\n");
        }
        return "Company " +
                "id=" + super.getId() +
                ", name='" + super.getName() + '\'' +
                stringBuilder.toString();
    }
}
