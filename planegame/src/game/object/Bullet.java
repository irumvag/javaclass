package game.object;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RadialGradientPaint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

public class Bullet {
    private double x;
    private double y;
    private final Shape shape;
    private final float angle;
    private double size;
    private float speed = 1f;

    public Bullet(double x, double y, float angle, double size, float speed) {
        x += player.PLAYER_SIZE / 2 - (size / 2);
        y += player.PLAYER_SIZE / 2 - (size / 2);
        this.x = x;
        this.y = y;
        this.angle = angle;
        this.size = size;
        this.speed = speed;
        this.shape = new Ellipse2D.Double(0, 0, size, size);
    }

    public void update() {
        x += Math.cos(Math.toRadians(angle)) * speed;
        y += Math.sin(Math.toRadians(angle)) * speed;
    }

    public boolean check(int width, int height) {
        return !(x <= -size || y <= -size || x > width || y > height);
    }

    public void draw(Graphics2D g2) {
        AffineTransform oldTransform = g2.getTransform();
        g2.translate(x, y);

        // Create radial gradient for glowing effect
        float radius = (float) size / 2;
        float centerX = (float) size / 2;
        float centerY = (float) size / 2;
        float[] dist = {0.0f, 1.0f};
        Color[] colors = {new Color(255, 255, 0), new Color(255, 69, 0)}; // Yellow to Orange-Red
        RadialGradientPaint gp = new RadialGradientPaint(centerX, centerY, radius, dist, colors);

        g2.setPaint(gp);
        g2.fill(shape);
        g2.setTransform(oldTransform);
    }

    public Shape getShape() {
        return new Area(new Ellipse2D.Double(x, y, size, size));
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getSize() {
        return size;
    }

    public double getCenterX() {
        return x + size / 2;
    }

    public double getCenterY() {
        return y + size / 2;
    }
}