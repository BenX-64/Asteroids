import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.util.concurrent.TimeUnit;
import javax.swing.Timer;

import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Graphics;

import java.util.ArrayList;

public class Main extends JPanel{
    boolean gameActive = true;

    static JFrame board = new JFrame("Asteroids");


    static int boardys = 600; //board x and y size
    static int boardxs = 1000;
    
    private int mv = 100, hp = 100, dps = 50, ma = 75; //player values
   
    player p = new player(mv, hp, dps, ma);

    public Main(){
        ArrayList<ast> asts = new ArrayList<>();
        asts.add(new ast(0,0,0,0,3));
        board.setSize(boardxs,boardys);
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board.setResizable(false);

        board.add(asts.get(0).icon);
        board.add(p.icon);

        board.add(this);
        board.setVisible(true);
        
        p.rotate(80);


        while(gameActive){
            tick();
        }
    }

    public void tick(){
        p.rotate(5);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void paint(Graphics g){
        super.paintComponent(g);
        g.setColor(Color.black);
        g.fillRect(0, 0, boardxs, boardys);

    }
    public void paintComponent(Graphics g){

    }


    public static void main(String[] args) throws Exception{
        Main m = new Main(); 
    }
}
