package model;

import java.util.HashSet;
import java.util.Set;

public class Customer extends Core{
    private String lastName;
    private String address;
    private Set<Project> projects;

    public Customer(Long id, String firstName, String lastName, String address) {
        super(id, firstName);
        this.lastName = lastName;
        this.address = address;
        projects = new HashSet<>();
    }

    public String getFirstName() {
        return super.getName();
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public Set<Project> getProjects() {
        return projects;
    }

    public String getProjectsString() {
        if (projects.isEmpty()) return "0;";
        StringBuilder result = new StringBuilder();
        for (Project project : projects){
            result.append(project.getId() + ";");
        }
        return result.toString();
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
        return "Customer " +
                "id=" + super.getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                stringBuilder.toString();
    }

}
