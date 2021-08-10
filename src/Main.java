import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Graphics;

import java.util.ArrayList;

public class Main extends JPanel{
    boolean gameActive = true;
    JFrame board = new JFrame("Asteroids");
    int boardsize = 600;
    
    

    public Main(){
        board.setSize(boardsize,boardsize);
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board.setResizable(false);
        board.add(this);

        board.setVisible(true);


    }
    public void paint(Graphics g){
        super.paintComponent(g);
        
        g.fillOval(0,0,100,100);
    }

    public void tick(){

    }





    public static void main(String[] args){
        Main m = new Main();
    }
}
