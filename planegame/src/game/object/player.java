package game.object;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import javax.swing.ImageIcon;

public class player {
    public player() {
        this.image = new ImageIcon(getClass().getResource("/game/image/airplanel.png")).getImage();
        this.image_speed = new ImageIcon(getClass().getResource("/game/image/fire.png")).getImage();
    }
    public static final double PLAYER_SIZE=64;
    private double x;
    private double y;
    private final float MAX_SPEED=1f;
    private float speed=0f;
    private float angle=0f;
    private final Image image;
    private final Image image_speed;
    private boolean speedup;
    public void changeLocation(double x,double y)
    {
      this.x=x;
      this.y=y;
    }
    public void update()
    {
        x+=Math.cos(Math.toRadians(angle))*speed;
        y+=Math.sin(Math.toRadians(angle))*speed;
    }
    public void changeAngle(float angle)
    {
        if(angle<0){
            angle=359;
        }
        else if(angle>359){
        angle=0;
        }
        this.angle=angle;
    }
    public void draw(Graphics2D g2){
    AffineTransform oldtransform=g2.getTransform();
    g2.translate(x, y);
    AffineTransform trans=new AffineTransform();
    trans.rotate(Math.toRadians(angle+45),PLAYER_SIZE/2,PLAYER_SIZE/2);
    g2.drawImage(speedup?image_speed:image,trans,null);
    g2.setTransform(oldtransform);
    }
    public double getX()
    {
        return x;
    }
    public double getY(){
    return y;
    }
    public float getAngle(){
    return angle;
    }
    public void speedUp(){
        speedup=true;
        if(speed>MAX_SPEED)
        {
            speed=MAX_SPEED;
        }
        else
        {
            speed+=0.01f;
        }
    }
    public void speedDown()
    {
        speedup=false;
        if(speed<0)
        {
            speed=0;
        }
        else
        {
            speed-=0.03f;
        }
    }
}
