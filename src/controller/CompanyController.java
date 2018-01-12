package controller;

import model.Company;
import model.Core;
import model.Project;
import service.CompanyDAO;
import service.ProjectDAO;
import view.ConsoleHelper;
import view.CoreView;

import java.util.List;

public class CompanyController extends CoreController{
    private DataReceiver dr;
    private ProjectDAO projectDAO;
    private CompanyDAO companyDAO;

    public CompanyController() {
        super();
        dr = super.getDr();
        projectDAO = new ProjectDAO();
        companyDAO = new CompanyDAO();
        super.start();
    }

    @Override
    public Company create() {
        ConsoleHelper.showMessage("Введите id для нового объекта");
        long id = dr.readLong();

        ConsoleHelper.showMessage("Введите name для нового объекта");
        String name = dr.readString();

        Company company = new Company(id, name);

        //после создания объекта заполняем команды
        long idProject;

        do {
            ConsoleHelper.showMessage("Введите id project для нового объекта или 0, чтобы продолжить");

            Project project;
            for (Core core : projectDAO.getAll()){ //показать все проекты, которые есть в базе
                project = (Project) core;
                CoreView.show(project);
            }

            idProject = dr.readLong();
            if (idProject == 0) continue;
            if (projectDAO.getById(idProject) == null){
                ConsoleHelper.showMessage("Project с таким id не существует, перейти в меню сущности project? yes/no");
                if (dr.readBoolean()){
                    ProjectController projectController = new ProjectController(); //переходим в меню skill
                } else {
                    continue;
                }
            }
            company.setProject(projectDAO.getById(idProject));
            ConsoleHelper.showMessage("Добавлен project " + projectDAO.getById(idProject));

        } while (idProject != 0);

        return company;
    }

    @Override
    public void save(Core core) {
        Company company = (Company) core;
        long id = company.getId();
        while (companyDAO.getById(id) != null){
            ConsoleHelper.showMessage("Объект с таким id уже существует, хотите перейти к обновлению? yes/no");
            if (dr.readBoolean()){
                update();
                return;
            } else {
                ConsoleHelper.showMessage("Введите другой id");
                id = dr.readLong();
            }
        }

        company.setId(id);
        if (companyDAO.save(company)){
            ConsoleHelper.showMessage("Объект сохранен");
        } else {
            ConsoleHelper.showMessage("Объект не сохранен, попробуйте еще раз");
        }
    }

    @Override
    public void read() {
        do{
            ConsoleHelper.showMessage("Введите id объекта или 0 чтобы выйти из этого меню");
            long id = dr.readLong();
            if (id == 0) {
                break;
            }
            Company company = companyDAO.getById(id);
            if (company != null){
                CoreView.show(company);
            } else {
                ConsoleHelper.showMessage("Нет такого объекта");
            }
        } while (true);
    }

    @Override
    public void readAll() {
        List<Core> companies = companyDAO.getAll();
        if (companies.isEmpty()){
            ConsoleHelper.showMessage("Список пуст");
        } else {
            Company company;
            for (Core core : companies){
                company = (Company) core;
                CoreView.show(company);
            }
        }
    }

    @Override
    public void update() {
        Company company;
        long id;
        do {
            ConsoleHelper.showMessage("Введите id, который хотите обновить или 0, чтобы выйти");
            id = dr.readLong();
            if (id == 0) return;
            company = companyDAO.getById(id);
            if (company == null){
                ConsoleHelper.showMessage("Нет такого объекта");
            }
        } while (company == null);

        if (companyDAO.update(id, create())){
            ConsoleHelper.showMessage("Объект обновлен");
        } else {
            ConsoleHelper.showMessage("Не удалось обновить объект, создан новый");

        }
    }

    @Override
    public void delete() {
        ConsoleHelper.showMessage("Введите id удаляемого объекта");
        long id = dr.readLong();

        if (companyDAO.remove(companyDAO.getById(id))){
            ConsoleHelper.showMessage("Объект удален");
        } else {
            ConsoleHelper.showMessage("Нет такого объекта");
        }
    }
}
