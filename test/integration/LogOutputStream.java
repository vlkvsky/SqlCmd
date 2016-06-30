package test.integration;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Вадим Сергеевич on 03.06.2016.
 */
public class LogOutputStream extends OutputStream {

    private String log;

    @Override
    public void write(int b) throws IOException {
        byte[] bytes = new byte[] { (byte)(b & 0xFF00 >> 8), (byte)(b & 0x00FF) };
        log += new String(bytes, "UTF-8");
    }

    public String getData() {
        return log;
    }
}

