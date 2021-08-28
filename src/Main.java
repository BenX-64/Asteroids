import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.Font;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Area;

import java.util.ArrayList;

import java.io.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Main extends JLayeredPane implements KeyListener{
    static boolean gameActive = true;

    static JFrame board = new JFrame("Asteroids");

    final int tickrate = 20; //ticks per second (changing this does nothing)

    static int boardys = 600; //board x and y size
    static int boardxs = 1000;
    
    int hp = 100, dps = 25, ma = 1;
    double mt = 2.5; //player valus
   
    player p = new player(hp, dps, ma, mt, false);
    player pr = new player(hp, dps, ma, mt, true);

    ArrayList<shot> shots = new ArrayList<>();
    ArrayList<ast> astlist = new ArrayList<>();
    int maxasts = 16; //sets limit for max # of asteroids on board at once

    int scorevalue = 0;
    String scorestring = "Score: " + Integer.toString(scorevalue);
    JLabel score = new JLabel(scorestring);
    JLabel bscore = new JLabel(scorestring);

    int hscorevalue = 0;
    int oldhscorevalue = 0;
    String hscorestring = "High Score: " + Integer.toString(hscorevalue);
    JLabel hscore = new JLabel(hscorestring);
    JLabel bighscore = new JLabel(hscorestring);
    JLabel restartinstruct = new JLabel("Press Space to Restart");

    String cst = "Speed: " + String.format("%.2f",p.cs*20);
    JLabel cs = new JLabel(cst);

    String hps = "HP: " + 100*p.hp/p.maxhp + "%";
    JLabel hpi = new JLabel(hps);
    public Main(){

        board.addKeyListener(this);
        board.setSize(boardxs,boardys);
        board.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        board.setResizable(false);
        board.getContentPane().setBackground(Color.black);

        setLayer(board.add(p.icon),1);
        setLayer(board.add(pr.icon),0);

        score.setForeground(Color.white);
        score.setBounds(boardxs/100,0, boardxs, 30);
        score.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        score.setVerticalAlignment(JLabel.TOP);
        score.setHorizontalAlignment(JLabel.LEFT);
        setLayer(board.add(score),0);

        try {
            FileReader reader = new FileReader(new File("").getAbsolutePath()+"/assets/highscore.txt");
            BufferedReader bufferedReader = new BufferedReader(reader);
 
            String line;
 
            while ((line = bufferedReader.readLine()) != null) {
                hscorevalue = Integer.valueOf(line);
                oldhscorevalue = hscorevalue;
            }
            reader.close();
 
        } catch (IOException e) {
            e.printStackTrace();
        }

        hscore.setForeground(Color.white);
        hscore.setBounds(boardxs/100, score.getHeight(), boardxs, 30);
        hscore.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        hscore.setHorizontalAlignment(JLabel.LEFT);
        setLayer(board.add(hscore),0);

        bscore.setForeground(Color.white);
        bscore.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        bscore.setBounds(0,-100, boardxs, boardys);
        bscore.setVerticalAlignment(JLabel.CENTER);
        bscore.setHorizontalAlignment(JLabel.CENTER);
        setLayer(board.add(bscore),0);
        bscore.setVisible(false);

        bighscore.setForeground(Color.white);
        bighscore.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        bighscore.setBounds(0,-60, boardxs, boardys);
        bighscore.setVerticalAlignment(JLabel.CENTER);
        bighscore.setHorizontalAlignment(JLabel.CENTER);
        setLayer(board.add(bighscore),0);
        bighscore.setVisible(false);

        restartinstruct.setForeground(Color.white);
        restartinstruct.setFont(new Font("TimesRoman", Font.PLAIN, 40));
        restartinstruct.setBounds(0, 0, boardxs, boardys);
        restartinstruct.setVerticalAlignment(JLabel.CENTER);
        restartinstruct.setHorizontalAlignment(JLabel.CENTER);
        setLayer(board.add(restartinstruct),0);
        restartinstruct.setVisible(false);


        cs.setForeground(Color.white); //current speed
        cs.setBounds(p.psize, boardys-70, boardxs/2, 20);
        cs.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        setLayer(board.add(cs),0);
        
        hpi.setForeground(Color.white); //hp %
        hpi.setBounds(10,(int)pr.y-20,boardxs, 20);
        hpi.setFont(new Font("TimesRoman", Font.PLAIN, 20));
        setLayer(board.add(hpi),0);

        board.add(this);
        board.setVisible(true);
        setLayer(this, 3);
        
        while(true){
            if(gameActive){
                tick();
            }else{
                System.out.print("");
                //i dont know why but the game doesnt restart if nothing is here
                //my guess is some automatic breaking thing
            }
        }
        
    }

    public void tick(){

        updateText();

        pr.move();
        p.move(); //self explanatory

        for(int i = 0; i< shots.size(); i++){
            shots.get(i).move();
        }
        //collision detecting stuff
        for(int i = 0; i< astlist.size(); i++){
            if(!astlist.get(i).icon.isVisible()){ //skips the bugged asteroids
                continue;
            }

            //collision detector with other asteroids
            //merge asteroids if their masses added up to 3 or less

            for(int j = 0; j < astlist.size()-1; j++){
                //astlist.size()-1 because of the bug described in
                //spawn();
                if(i==j){
                    continue;
                }else{
                    if(astlist.get(i).hitbox.intersects(astlist.get(j).hitbox.getBounds())&& astlist.get(i).mass + astlist.get(j).mass <=3){
                        astCollide(i, j, 0);
                        if(i>0){
                            i--;
                        }
                        spawn((int)Math.random()*3+1);
                        break;
                    }else{

                    }
                }
            }
            //collision detection with player
            if(astlist.get(i).hitbox.intersects(p.hitbox.getBounds())){
                astCollide(i, 0, 1);
                if(i>0){
                    i--;
                }
                if(p.hp<=0){
                    endGame();
                    break;
                }
                
            }
            //Asteroid shot collisions
            for(int j = 0; j< shots.size();j++){
                if(astlist.get(i).hitbox.intersects(shots.get(j).hitbox.getBounds())){
                    astCollide(i, j , 2);
                    if(astlist.get(i).hp <= 0){
                        board.remove(astlist.get(i).icon);
                        astlist.remove(i);
                        if(i>0){
                            i--;
                        }
                    }
                    break;

                }
                //checks for collisions with any shots
            }
            astlist.get(i).move();
        }
        //randomly spawn asteroids if the number is under the limit
        if(astlist.size()<=maxasts){
            int a = 0; 
            for(int i = 0; i< astlist.size(); i++){
                if(astlist.get(i).icon.isVisible()){
                    a++;
                }
            }
            int spawndice = (int)(Math.random()*100+1);
            if(spawndice <= 5){
                int massdice = (int)(Math.random()*100+1);
                if(massdice<=10){
                    spawn(3);
                }else if(massdice<=50){
                    spawn(2);
                }else{
                    spawn(1);
                }
            }

        }
        try {
            Thread.sleep(50); //20 ticks per second
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void spawn(int m){
        astlist.add(new ast(m));
        //band-aid fix for a bug that makes 
        //the newest asteroid teleport to the middle when thrusting is true
        astlist.get(astlist.size()-1).icon.setVisible(false); 
        setLayer(board.add(astlist.get(astlist.size()-1).icon), 2);
        if(astlist.size()>1){
            astlist.get(astlist.size()-2).icon.setVisible(true);
        }
    } //spawn but with more input

    public void shoot(){
        shots.add(new shot((int)p.x+p.psize/2, (int)p.y+p.psize/2, p.dps, p.deg));
        setLayer(board.add(shots.get(shots.size()-1).icon),1);
    }


    public void astCollide(int i, int j, int collisionType){
        /*
        collision type 0: asteroid on asteroid
        collision type 1: asteroid on player
        collision type 2: asteroid on shot
        */

        if(collisionType == 0){
            int amass = astlist.get(i).mass;
            int bmass = astlist.get(j).mass;
            int asize = astlist.get(i).size;
            int bsize = astlist.get(j).size;
            double ax = astlist.get(i).x;
            double bx = astlist.get(j).x;
            double ay = astlist.get(i).y;
            double by = astlist.get(j).y;
            double avx = astlist.get(i).vx;
            double bvx = astlist.get(j).vx;
            double avy = astlist.get(i).vy;
            double bvy = astlist.get(j).vy;
    
            if(j<i){
                board.remove(astlist.get(i).icon);
                astlist.remove(i);
                
                board.remove(astlist.get(j).icon);
                astlist.remove(j);
    
                astlist.add(j,new ast(amass+bmass,
                            (ax+asize/2+bx+bsize/2)/2,
                            (ay+asize/2+by+bsize/2)/2, 
                            (avx+bvx)/(amass+bmass), 
                            (avy+bvy)/(amass+bmass)));
                setLayer(board.add(astlist.get(j).icon),2);
            }else{
                board.remove(astlist.get(j).icon);
                astlist.remove(j);
    
                board.remove(astlist.get(i).icon);
                astlist.remove(i);
                astlist.add(i,new ast(amass+bmass,
                                    (ax+bx)/2,
                                    (ay+by)/2, 
                                    -(avx+bvx)/(amass+bmass), 
                                    (avy+bvy)/(amass+bmass)));
                setLayer(board.add(astlist.get(i).icon),2);
            }
        }
        else if(collisionType == 1){
            p.dmg(astlist.get(i).dmg);
            board.remove(astlist.get(i).icon);
            astlist.remove(i);
            updateText();

        }else if(collisionType==2){
            scorevalue += astlist.get(i).mass*5;
            board.remove(shots.get(j).icon);
            shots.remove(j);
            astlist.get(i).dmga(p.dps);
        }
    }

    public void updateText(){ //updates hp % display
        scorestring = "Score: " + Integer.toString(scorevalue);
        hscorevalue = scorevalue > hscorevalue ? scorevalue : hscorevalue;
        hscorestring = "High Score: " + Integer.toString(hscorevalue);
        hscore.setText(hscorestring);
        score.setText(scorestring);


        hps = "HP: " + 100*p.hp/p.maxhp + "%";
        if(100*p.hp/p.maxhp<=25){
            hpi.setForeground(Color.red);
        }
        hpi.setText(hps);

        cst = "Speed: "+ String.format("%.2f", p.cs*20);
        cs.setText(cst);
    }

    public void paint(Graphics g){
        super.paintComponent(g);
        //does nothing atm
        /*
        g.setColor(Color.black);
        g.fillRect(0, 0, boardxs, boardys);
        */      
    }

    private void endGame(){ //self explanatory
        if(hscorevalue > oldhscorevalue){
            try {
                FileWriter writer = new FileWriter(new File("").getAbsolutePath()+"/assets/highscore.txt", true);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);
     
                bufferedWriter.write(Integer.toString(hscorevalue));
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        gameActive = false;
        
        bscore.setText(scorestring);
        bscore.setHorizontalAlignment(JLabel.CENTER);
        bscore.setVisible(true);

        bighscore.setText(hscorestring);
        bighscore.setHorizontalAlignment(JLabel.CENTER);
        bighscore.setVisible(true);

        restartinstruct.setVisible(true);

        endScreen();
    }

    public void resetGame(){ //self explanatory
        while(astlist.size()>0){
            board.remove(astlist.get(0).icon);
            astlist.remove(0);
        }
        while(shots.size()>0){
            board.remove(shots.get(0).icon);
            shots.remove(0);
        }

        scorevalue = 0;
        scorestring = "Score: " + Integer.toString(scorevalue);
        score.setText(scorestring);

        p.cs = 0.0;
        cst = "Speed: " + String.format("%.2f",p.cs*20);
        cs.setText(cst);

        hpi.setForeground(Color.white);
        hps = "HP: " + 100*p.hp/p.maxhp + "%";
        hpi.setText(hps);

        p.reset();
        pr.reset();

        p.icon.setVisible(true);
        pr.icon.setVisible(true);
        score.setVisible(true);
        hscore.setVisible(true);
        hpi.setVisible(true);
        cs.setVisible(true);

        bscore.setVisible(false);
        bighscore.setVisible(false);
        restartinstruct.setVisible(false);

        gameActive = true;
    }


    public void endScreen(){
        //p.icon.setVisible(false);
        pr.icon.setVisible(false);
        score.setVisible(false);
        hscore.setVisible(false);
        hpi.setVisible(false);
        cs.setVisible(false);


        //resetGame();
        //startGame();
    }

    @Override
    public void keyPressed(KeyEvent arg0){
       // System.out.println(arg0.getKeyCode());
        if(gameActive){
            if(arg0.getKeyCode()==38){ //up arrow
                p.thrustOn();
            }
            if(arg0.getKeyCode() == 39){ //left arrow
                p.isTurning = 1;
                pr.isTurning = 1;
            }
            if(arg0.getKeyCode() == 37){ //right arrow
                p.isTurning = 2;
                pr.isTurning = 2;
            }
            if(arg0.getKeyCode() == 32 && !p.shooting && gameActive){//space bar, shoot
                p.shooting = true; //stops player from holding space
                shoot();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0){
        if(gameActive){
            if(arg0.getKeyCode()==38){ //up arrow
                p.thrustOff();
            }
            //stops turning
            if(p.isTurning != 0 && pr.isTurning!=0 && (arg0.getKeyCode() == 37 || arg0.getKeyCode() == 39)){
                p.isTurning = 0;
                pr.isTurning = 0;
            }
            //releases space(shoot)
            if(arg0.getKeyCode()==32 && p.shooting){
                p.shooting = false;
            }
        }
        if(restartinstruct.isVisible() && arg0.getKeyCode() == 32){
            resetGame();
        }

    }

    @Override
    public void keyTyped(KeyEvent arg0){

    }

    public static void main(String[] args) throws Exception{
        Main m = new Main(); 
    }
}
