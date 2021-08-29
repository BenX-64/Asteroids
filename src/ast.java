import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import java.awt.geom.Area;

public class ast {
    private int maxhp; //self explanatory
    double x = 0;
    double y = 0; 
    double vx, vy;
    int mass;
    int size;

    double msp = 0.5; //max speed possible, in msp per TICK
    int ms; //max speed
    int deg; //direction of asteroid movement, NOT rotation

    boolean isHit = false;

    //hp values for different masses
    int hp;
    int hp1 = 13;
    int hp2 = 50;
    int hp3 = 100;

    //sprite sizes for different amsses
    int size1 = 50;
    int size2 = 75;
    int size3 = 100;

    //dmg values for different masses
    int dmg;
    int dmg1 = 25;
    int dmg2 = 50;
    int dmg3 = 75;

    JLabel icon;
    ImageIcon image;
    String iconPath, iconPath3b;

    Area hitbox; //self explanatory

    static int numAsts = 0;

    public ast(int mass){
        this.mass = mass;

        //makes the speed, and direction of the asteroid completely random
        ms = (int)(Math.random()*msp+3);
        deg = (int)(Math.random()*360+ 1);

        vx = (ms*Math.sin(Math.toRadians(deg)));
        vy = (ms*Math.cos(Math.toRadians(deg)));
        
        iconPath = new File("").getAbsolutePath()+ "/assets/asteroids/asteroid" + mass + ".png";
        iconPath3b = new File("").getAbsolutePath()+ "/assets/asteroids/asteroid3b.png";
        image = new ImageIcon(iconPath);
        this.icon = new JLabel(image);


        if(mass == 1){
            hp = hp1;
            size = size1;
            dmg = dmg1;
        }else if(mass == 2){
            hp = hp2;
            size = size2;
            dmg = dmg2;
        }else{
            hp = hp3;
            size = size3;
            dmg = dmg3;
        }

        //determines the side the asteroid spawns
        int sided = (int)(Math.random()*4+1);
        if(sided == 1){ //left
            this.x = 0;
            this.y = (int)(Math.random()*Main.boardys+0);
        }
        if(sided == 2){ //top
            this.y = 0;
            this.x = (int)(Math.random()*Main.boardxs+0);
        }
        if(sided == 3){ //right
            this.x = Main.boardxs - size;
            this.y = (int)(Math.random()*Main.boardys+0);
        }
        if(sided == 4){ //bottom
            this.x = (int)(Math.random()*Main.boardxs+0);
            this.y = Main.boardys - size;
        }

        this.icon.setBounds((int)x,(int)y,size,size);
        hitbox = new Area(icon.getBounds());

    }
    public ast(int mass, double x, double y, double vx, double vy){
        this.mass = mass;

        ms = (int)(Math.random()*msp+3);
        deg = (int)(Math.random()*360+ 1);

        this.x = x;
        this.y = y;
        this.vx = vy;
        this.vy = vy;
        
        iconPath = new File("").getAbsolutePath()+ "/assets/asteroids/asteroid" + mass + ".png";
        iconPath3b = new File("").getAbsolutePath()+ "/assets/asteroids/asteroid3b.png";
        image = new ImageIcon(iconPath);
        this.icon = new JLabel(image);


        if(mass == 1){
            hp = hp1;
            size = size1;
            dmg = dmg1;
        }else if(mass == 2){
            hp = hp2;
            size = size2;
            dmg = dmg2;
        }else{
            hp = hp3;
            size = size3;
            dmg = dmg3;
        }

        this.icon.setBounds((int)x,(int)y,size,size);
        hitbox = new Area(icon.getBounds());

    }
    public void move(){ //self explanatory
        antiOOB();
        x-=this.vx;
        y-=this.vy;
        this.icon.setBounds((int)x,(int)y,size,size); 
        hitbox = new Area(icon.getBounds()); //updates hitbox position
    }

    public void antiOOB(){  //prevents asteroid from leaving the screen
        if(x + size/2 >= Main.boardxs){ //OOB right wall
            vx*=-1;
            //x = psize/2;
        }
        if(x < -size/2){ //OOB left wall
            vx*=-1;
            //x = Main.boardxs - psize/2;
        }
        if(y + size/2 > Main.boardys){ //OOB bottom wall
            vy*=-1;
            //y = -psize/2;
        }
        if(y < -size/2){ //OOB top wall
            vy*=-1;
            //y = Main.boardys-psize/2;
        }
    }   
    public void dmga(int dmg){ //dmga for dmg asteroid
        hp -= dmg;
        if(mass == 3 && hp <= 50){ //makes big asteroids crack
            ImageIcon tempImage = new ImageIcon(iconPath3b);
            icon.setIcon(tempImage);
            icon.setBounds((int)x , (int)y, size, size);
        }
    }

}
