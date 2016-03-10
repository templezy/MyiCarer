package au.edu.adelaide.sensorlog.app.data;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by ligefei on 20/04/2015.
 */
public class ProcessingThread extends Thread {
    private DataBuffer dataBuffer;
    private boolean stream;
    private Thread thread;
    private ServerSocket ss;
    private boolean running;
    private Socket socket;

    private static final int SERVERPORT = 6000;
    private static final String TAG = "ProcessingThread";

    public ProcessingThread(DataBuffer db, boolean stream) {
        this.dataBuffer = db;
        this.stream = stream;
        this.thread = null;
        this.socket = null;
        try {
            this.ss = new ServerSocket(SERVERPORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.running = true;
    }

    /*@Override
    public void run(){
        dataBuffer.updateDataset();
    }*/
    @Override
    public void run() {
        if (stream) {
            while (running) {
                try {
                    socket = ss.accept();
                    if (thread != null) {
                        thread.interrupt();
                    }
                    thread = new UpdateThread(socket);
                    thread.start();
                } catch (SocketException e) {
//                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            thread = new UpdateThread(socket);
            thread.start();
            Log.v(TAG, "Update Databuffer (File)");
        }
    }

    @Override
    public void interrupt() {
        try {
            ss.close();
            running = false;
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (thread != null) {
            thread.interrupt();
        }
        super.interrupt();
    }

    class UpdateThread extends Thread {

        Socket socket;

        public UpdateThread(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    sleep(49);
                    String lastRecord = dataBuffer.updateDataset();
                    if (socket != null) {
                        try {
                            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                            out.println(lastRecord);
                        } catch (IOException e) {
                            Log.d(TAG, "write to socket error");
                            e.printStackTrace();
                        }
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
