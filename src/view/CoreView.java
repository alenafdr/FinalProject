package view;

import model.Core;

public class CoreView {
    public static void show(Core core){
        ConsoleHelper.showMessage(core.toString());
    }
}
