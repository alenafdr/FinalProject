package model;

import java.util.HashSet;
import java.util.Set;

public class Project extends Core{
    private Set<Team> teams;

    public Project(Long id, String name) {
        super(id, name);
        teams = new HashSet<>();
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public String getTeamsString(){
        if (teams.isEmpty()) return "0;";
        StringBuilder result = new StringBuilder();
        for (Team team : teams){
            result.append(team.getId() + ";");
        }
        return result.toString();
    }

    public void setTeam(Team team) {
        this.teams.add(team);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(", \n\tteams=\n");
        for (Team team : teams){
            stringBuilder.append("\t\t" + team.toString() + "\n");
        }
        return "Project " +
                "id=" + super.getId() +
                ", name='" + super.getName() + stringBuilder.toString();
    }
}
