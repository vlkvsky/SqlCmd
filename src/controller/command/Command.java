package src.controller.command;

/**
 * Created by Вадим Сергеевич on 03.06.2016.
 */
public interface Command {
    boolean canProcess(String command);

    void process(String command);

}
