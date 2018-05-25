package pt.codeforce.adbnetwork;

import android.app.Activity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread thread = new Thread(new Runnable() {
            public void run() {

                exec("setprop service.adb.tcp.port  5555");
                exec("stop adbd && start adbd");
            }
        });
        thread.start();
        finish();
    }
    // Executes UNIX command.
    private String exec(String command) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();
            process.waitFor();
            return output.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
