package controller;

import model.Core;
import model.Project;
import model.Team;
import service.ProjectDAO;
import service.TeamDAO;
import view.ConsoleHelper;
import view.CoreView;

import java.util.List;

public class ProjectController extends CoreController{
    private DataReceiver dr;
    private TeamDAO teamDAO;
    private ProjectDAO projectDAO;

    public ProjectController() {
        super();
        dr = super.getDr();
        teamDAO = new TeamDAO();
        projectDAO = new ProjectDAO();
        start();
    }

    public Project create(){
        ConsoleHelper.showMessage("Введите id для нового объекта");
        long id = dr.readLong();

        ConsoleHelper.showMessage("Введите name для нового объекта");
        String name = dr.readString();

        Project project = new Project(id, name);

        //после создания объекта заполняем команды
        long idTeam;

        do {
            ConsoleHelper.showMessage("Введите id team для нового объекта или 0, чтобы продолжить");

            Team team;
            for (Core core : teamDAO.getAll()){ //показать все команды, которые есть в базе
                team = (Team) core;
                CoreView.show(team);
            }

            idTeam = dr.readLong();
            if (idTeam == 0) continue;
            if (teamDAO.getById(idTeam) == null){
                ConsoleHelper.showMessage("Skill с таким id не существует, перейти в меню сущности team? yes/no");
                if (dr.readBoolean()){
                    TeamController teamController = new TeamController(); //переходим в меню skill
                } else {
                    continue;
                }
            }
            project.setTeam(teamDAO.getById(idTeam));
            ConsoleHelper.showMessage("Добавлена team " + teamDAO.getById(idTeam));

        } while (idTeam != 0);


        return project;
    }

    @Override
    public void save(Core core) {
        Project project = (Project) core;
        long id = project.getId();

        while (projectDAO.getById(id) != null){
            ConsoleHelper.showMessage("Объект с таким id уже существует, хотите перейти к обновлению? yes/no");
            if (dr.readBoolean()){
                update();
                return;
            } else {
                ConsoleHelper.showMessage("Введите другой id");
                id = dr.readLong();
            }
        }

        project.setId(id);
        if (projectDAO.save(project)){
            ConsoleHelper.showMessage("Объект создан");
        } else {
            ConsoleHelper.showMessage("Объект не создан, попробуйте еще раз");
        }
    }

    public void read(){
        do{
            ConsoleHelper.showMessage("Введите id объекта или 0 чтобы выйти из этого меню");
            long id = dr.readLong();
            if (id == 0) {
                break;
            }
            Project project = projectDAO.getById(id);
            if (project != null){
                CoreView.show(project);
            } else {
                ConsoleHelper.showMessage("Нет такого объекта");
            }
        } while (true);
    }

    public void readAll(){
        List<Core> projects = projectDAO.getAll();
        if (projects.isEmpty()){
            ConsoleHelper.showMessage("Список пуст");
        } else {
            Project project;
            for (Core core : projects){
                project = (Project) core;
                CoreView.show(project);
            }
        }
    }

    public void update(){
        Project project;
        long id;
        do {
            ConsoleHelper.showMessage("Введите id, который хотите обновить или 0, чтобы выйти");
            id = dr.readLong();
            if (id == 0) return;
            project = projectDAO.getById(id);
            if (project == null){
                ConsoleHelper.showMessage("Нет такого объекта");
            }
        } while (project == null);

        if (projectDAO.update(id, create())){
            ConsoleHelper.showMessage("Объект обновлен");
        } else {
            ConsoleHelper.showMessage("Не удалось обновить объект, создан новый");
        }
    }

    public void delete(){
        ConsoleHelper.showMessage("Введите id удаляемого объекта");
        long id = dr.readLong();

        if (projectDAO.remove(projectDAO.getById(id))){
            ConsoleHelper.showMessage("Объект удален");
        } else {
            ConsoleHelper.showMessage("Нет такого объекта");
        }
    }
}
