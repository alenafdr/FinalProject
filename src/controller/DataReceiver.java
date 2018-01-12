package controller;

import view.ConsoleHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class DataReceiver {
    public static final String COMPANY = "company";
    public static final String CUSTOMER = "customer";
    public static final String DEVELOPER = "developer";
    public static final String PROJECT = "project";
    public static final String SKILL = "skill";
    public static final String TEAM = "team";

    public static final String COMMAND_CREATE = "create";
    public static final String COMMAND_READ = "read";
    public static final String COMMAND_READ_ALL = "read all";
    public static final String COMMAND_UPDATE = "update";
    public static final String COMMAND_DELETE = "delete";
    public static final String COMMAND_EXIT = "exit";

    public String readEntity(){
        String entity;
        do {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                entity = reader.readLine();
                if (!entity.equalsIgnoreCase(COMPANY) &&
                    !entity.equalsIgnoreCase(CUSTOMER) &&
                    !entity.equalsIgnoreCase(DEVELOPER) &&
                    !entity.equalsIgnoreCase(PROJECT) &&
                    !entity.equalsIgnoreCase(SKILL) &&
                    !entity.equalsIgnoreCase(TEAM)){
                    ConsoleHelper.showMessage("Нет такой сущности, попробуйте еще раз");
                } else {
                    break;
                }
            } catch (IOException e) {
                ConsoleHelper.showMessage("Ошибка при вводе, попробуйте еще раз");
            }
        } while (true);
        return entity;
    }

    public String readCommand(){
        String command;
        do {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                command = reader.readLine();
                if (!command.equalsIgnoreCase(COMMAND_CREATE) &&
                        !command.equalsIgnoreCase(COMMAND_READ) &&
                        !command.equalsIgnoreCase(COMMAND_READ_ALL) &&
                        !command.equalsIgnoreCase(COMMAND_UPDATE) &&
                        !command.equalsIgnoreCase(COMMAND_DELETE) &&
                        !command.equalsIgnoreCase(COMMAND_EXIT)){
                    ConsoleHelper.showMessage("Нет такой команды, попробуйте еще раз");
                } else {
                    break;
                }
            } catch (IOException e) {
                ConsoleHelper.showMessage("Ошибка при вводе, попробуйте еще раз");
            }
        } while (true);
        return command;
    }

    public long readLong(){
        long result;
        do{
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                result = Long.parseLong(reader.readLine());
                break;
            } catch (Exception e) {
                ConsoleHelper.showMessage("Ошибка при вводе, попробуйте еще раз");
            }
        } while (true);
        return result;
    }

    public String readString(){
        String result;
        do{
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                result = reader.readLine();
                break;
            } catch (Exception e) {
                e.printStackTrace();
                ConsoleHelper.showMessage("Ошибка при вводе, попробуйте еще раз");
            }
        } while (true);
        return result;
    }

    public boolean readBoolean(){
        do{
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            try {
                String answer = reader.readLine();
                if (answer.equalsIgnoreCase("yes")){
                    return true;
                } else if (answer.equalsIgnoreCase("no")){
                    return false;
                } else {
                    ConsoleHelper.showMessage("Неверно введена команда, попробуйте еще раз");
                }
            } catch (Exception e) {
                ConsoleHelper.showMessage("Ошибка при вводе, попробуйте еще раз");
            }
        } while (true);
    }
}
