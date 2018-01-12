package model;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

public class Developer extends Core{
    private String lastName;
    private String specialty;
    private Set<Skill> skills;
    private BigDecimal salary;

    public Developer(Long id, String firstName, String lastName, String specialty, BigDecimal salary) {
        super(id, firstName);
        this.lastName = lastName;
        this.specialty = specialty;
        this.salary = salary;
        skills = new HashSet<>();
    }

    public String getFirstName() {
        return super.getName();
    }

    public String getLastName() {
        return lastName;
    }

    public String getSpecialty() {
        return specialty;
    }

    public String getSkillsString() {

        if (skills.isEmpty()) return "0;";
        StringBuilder result = new StringBuilder();
        for (Skill skill : skills){
            result.append(skill.getId() + ";");
        }
        return result.toString();
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSkill(Skill skill) {
        this.skills.add(skill);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(", \n\t\t\tskills=\n");
        for (Skill skill : skills){
            stringBuilder.append("\t\t\t\t" + skill.toString() + "\n");
        }
        return "Developer " +
                "id=" + super.getId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + lastName + '\'' +
                ", specialty='" + specialty + '\'' +
                ", salary=" + salary +
                stringBuilder.toString();
    }
}
