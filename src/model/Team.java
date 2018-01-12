package model;

import java.util.HashSet;
import java.util.Set;

public class Team extends Core{
    private Set<Developer> developers;

    public Team(Long id, String name) {
        super(id,name);
        developers = new HashSet<>();
    }

    public Set<Developer> getDevelopers() {
        return developers;
    }

    public String getDevelopersString(){
        if (developers.isEmpty()) return "0;";
        StringBuilder result = new StringBuilder();
        for (Developer developer : developers){
            result.append(developer.getId() + ";");
        }
        return result.toString();
    }

    public void setDeveloper(Developer developer) {
        this.developers.add(developer);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(", \n\t\tdevelopers=\n");
        for (Developer developer : developers){
            stringBuilder.append("\t\t\t" + developer.toString() + "\n");
        }
        return "Team " +
                "id=" + super.getId() +
                ", name='" + super.getName() +
                stringBuilder.toString();
    }
}
