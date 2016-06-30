package src.view;


import java.util.Scanner;

/**
 * Created by Вадим Сергеевич on 02.06.2016.
 */
public class Console implements View {

    @Override
    public void write(String message) {
        System.out.println(message);
    }

    @Override
    public String read() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
}
