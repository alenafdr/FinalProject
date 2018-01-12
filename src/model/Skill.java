package model;

public class Skill extends Core{

    public Skill(long id, String name) {
        super(id,name);
    }

    @Override
    public String toString() {
        return "Skill " +
                "id=" + super.getId() +
                ", name='" + super.getName() + '\'';
    }
}
