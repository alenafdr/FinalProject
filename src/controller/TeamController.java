package controller;

import model.Core;
import model.Developer;
import model.Team;
import service.DeveloperDAO;
import service.TeamDAO;
import view.ConsoleHelper;
import view.CoreView;

import java.util.List;

public class TeamController extends CoreController{
    private DataReceiver dr;
    private TeamDAO teamDAO;
    private DeveloperDAO developerDAO;

    public TeamController() {
        super();
        dr = super.getDr();
        teamDAO = new TeamDAO();
        developerDAO = new DeveloperDAO();
        super.start();
    }

    @Override
    public Team create(){
        ConsoleHelper.showMessage("Введите id для нового объекта");
        long id = dr.readLong();

        ConsoleHelper.showMessage("Введите name для нового объекта");
        String name = dr.readString();

        Team team = new Team(id, name);

        //после создания объекта заполняем разрботчиков
        long idDev;

        do {
            ConsoleHelper.showMessage("Введите id developer для нового объекта или 0, чтобы продолжить");

            Developer developer;
            for (Core core : developerDAO.getAll()){ //показать всех разработчиков, которые есть в базе
                developer = (Developer) core;
                CoreView.show(developer);
            }

            idDev = dr.readLong();
            if (idDev == 0) continue;
            if (developerDAO.getById(idDev) == null){
                ConsoleHelper.showMessage("Skill с таким id не существует, перейти в меню сущности developer? yes/no");
                if (dr.readBoolean()){
                    DeveloperController developerController = new DeveloperController(); //переходим в меню skill
                } else {
                    continue;
                }
            }
            team.setDeveloper(developerDAO.getById(idDev));
            ConsoleHelper.showMessage("Добавлен developer " + developerDAO.getById(idDev));

        } while (idDev != 0);

        return team;
    }

    @Override
    public void save(Core core) {
        Team team = (Team) core;
        long id = team.getId();
        while (teamDAO.getById(id) != null){
            ConsoleHelper.showMessage("Объект с таким id уже существует, хотите перейти к обновлению? yes/no");
            if (dr.readBoolean()){
                update();
                return;
            } else {
                ConsoleHelper.showMessage("Введите другой id");
                id = dr.readLong();
            }
        }

        team.setId(id);
        if (teamDAO.save(team)){
            ConsoleHelper.showMessage("Объект создан");
        } else {
            ConsoleHelper.showMessage("Объект не создан, попробуйте еще раз");
        }
    }

    @Override
    public void read(){
        do{
            ConsoleHelper.showMessage("Введите id объекта или 0 чтобы выйти из этого меню");
            long id = dr.readLong();
            if (id == 0) {
                break;
            }
            Team team = teamDAO.getById(id);
            if (team != null){
                CoreView.show(team);
            } else {
                ConsoleHelper.showMessage("Нет такого объекта");
            }
        } while (true);
    }

    @Override
    public void readAll(){

        List<Core> teams = teamDAO.getAll();
        if (teams.isEmpty()){
            ConsoleHelper.showMessage("Список пуст");
        } else {
            Team team;
            for (Core core : teams){
                team = (Team) core;
                CoreView.show(team);
            }
        }
    }

    @Override
    public void update(){
        Team team;
        long id;
        do {
            ConsoleHelper.showMessage("Введите id, который хотите обновить или 0, чтобы выйти");
            id = dr.readLong();
            if (id == 0) return;
            team = teamDAO.getById(id);
            if (team == null){
                ConsoleHelper.showMessage("Нет такого объекта");
            }
        } while (team == null);

        if (teamDAO.update(id, create())){
            ConsoleHelper.showMessage("Объект обновлен");
        } else {
            ConsoleHelper.showMessage("Не удалось обновить объект, создан новый");
        }
    }

    @Override
    public void delete(){
        ConsoleHelper.showMessage("Введите id удаляемого объекта");
        long id = dr.readLong();

        if (teamDAO.remove(teamDAO.getById(id))){
            ConsoleHelper.showMessage("Объект удален");
        } else {
            ConsoleHelper.showMessage("Нет такого объекта");
        }
    }
}
