package controller;

import model.Core;
import view.ConsoleHelper;

public abstract class CoreController {
    private DataReceiver dr;

    public CoreController() {
        dr = new DataReceiver();
    }

    public DataReceiver getDr() {
        return dr;
    }

    public void start(){
        String name = getClass().getName();
        name = name.replace("controller.", "");
        name = name.replace("Controller", "");
        exit: do {
            ConsoleHelper.showMessage("Введите команду для сущности " + name + "\n" +
                    DataReceiver.COMMAND_CREATE + "\n" +
                    DataReceiver.COMMAND_READ + "\n" +
                    DataReceiver.COMMAND_READ_ALL + "\n" +
                    DataReceiver.COMMAND_UPDATE + "\n" +
                    DataReceiver.COMMAND_DELETE + "\n" +
                    DataReceiver.COMMAND_EXIT);

            String command = dr.readCommand();
            switch (command){
                case DataReceiver.COMMAND_CREATE:
                    save(create());
                    break;
                case DataReceiver.COMMAND_READ:
                    read();
                    break;
                case DataReceiver.COMMAND_READ_ALL:
                    readAll();
                    break;
                case DataReceiver.COMMAND_UPDATE:
                    update();
                    break;
                case DataReceiver.COMMAND_DELETE:
                    delete();
                    break;
                case DataReceiver.COMMAND_EXIT:
                    break exit;
            }
        } while (true);
    }

    public abstract Core create();

    protected abstract void save(Core core);

    public abstract void read();

    public abstract void readAll();

    public abstract void update();

    public abstract void delete();
}
