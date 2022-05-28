package Server;
import java.awt.event.*;
import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;

import Common.Casilla;
import Common.Constantes;
import Common.Dot;
import Common.Mapa;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class GUI implements ActionListener, Constantes{
    
    JFrame ventana;
    JButton next;
    Mapa mapa;
    Dot dot;
    
    ServerSocket server;
    Socket client;
    ObjectInputStream input;
    ObjectOutputStream output;
    
    public GUI(){

        ventana = new JFrame();
        mapa = new Mapa(this);


        ventana.add(mapa.panelTablero);

        next = new JButton("continuar");
        next.addActionListener(this);
        next.setActionCommand("next");

        ventana.add(next, BorderLayout.SOUTH);

        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.pack();
        ventana.setVisible(true);

        dot = new Dot();

        Server server = new Server(dot);
        Thread hilo = new Thread(server);
        hilo.start();

        moveDot();
        run();

    }

    @Override
    public void actionPerformed(ActionEvent e) {  
    }

    public void moveDot(){
        mapa.tablero[dot.lastPosition[X]][dot.lastPosition[Y]].clearDot();
        mapa.tablero[dot.currentPosition[X]][dot.currentPosition[Y]].setAsDot();
    }

    public void run(){
        while (true){
            dot.move();
            moveDot();
            envioDot();
            try {
                Thread.sleep(1000);
                
            } catch (Exception e) {
                //TODO: handle exception
            }
        }
    }
    
    
    public void envioDot(){ 
        try {
            client = new Socket(  "127.0.0.1", 4450);
            output= new ObjectOutputStream(client.getOutputStream());
            output.writeObject(dot.target);
            output.flush();
            output.close();
            client.close();
        }catch (Exception ex) {
            System.out.println("No se ha establecido conexión");
        }
    }
}