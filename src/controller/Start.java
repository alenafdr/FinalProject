package controller;

import view.ConsoleHelper;

public class Start {
    public static void main(String[] args) {
        DataReceiver dr = new DataReceiver();
        do{
            ConsoleHelper.showMessage("Выбирете сущность,с которой хотите работать\n" +
                    DataReceiver.COMPANY + "\n" +
                    DataReceiver.CUSTOMER + "\n" +
                    DataReceiver.DEVELOPER + "\n" +
                    DataReceiver.PROJECT + "\n" +
                    DataReceiver.SKILL + "\n" +
                    DataReceiver.TEAM + "\n");

            String entity = dr.readEntity();

            switch (entity){
                case DataReceiver.COMPANY:
                    new CompanyController();
                    break;
                case DataReceiver.CUSTOMER:
                    new CustomerController();
                    break;
                case DataReceiver.DEVELOPER:
                    new DeveloperController();
                    break;
                case DataReceiver.PROJECT:
                    new ProjectController();
                    break;
                case DataReceiver.SKILL:
                    new SkillController();
                    break;
                case DataReceiver.TEAM:
                    new TeamController();
                    break;
            }

        } while (true);

    }
}
