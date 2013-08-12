package game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import textures.SpriteSheet;

public class Player {

    private int wood;
    private int rocks;
    private int metal;
    private int color;
    private int targetX;
    private int radius;

    private double x;
    private double y;
    private double speed;

    private SpriteSheet sprite;

    public Player(int x, int y){
        color = (int)(Math.random()*8);
        radius = 16;
        speed = 256;
        sprite = new SpriteSheet(GamePanel.textures.people, 8, 1, 0.0);
        int width = sprite.getBitWidth();
        int height = sprite.getBitHeight();
        this.x=x;
        this.y = GamePanel.getHeight() - sprite.getBitHeight()*4 - 32;
        sprite.build(this.x,this.y,width*8,height*4); //stretch player-width
        sprite.animate(color);
    }
    public void draw(Canvas canvas, Paint paint){
        canvas.drawBitmap(sprite.getBitmap(), sprite.getSpriteRect(), sprite.getDestRect(), paint);
    }
    public void update(double mod, double mainX, double mainY){
        //set direction and move
        //Log.d("test", "x: "+x+", targetX: "+targetX);
        if (targetX > 0){
            if (Math.abs(x - targetX) > radius){
                if (x < targetX) x+=(speed*mod);
                else x-=(speed*mod);
            }
            else targetX = -1;
        }

        sprite.update(mainX+x, mainY+y);
    }
    //Player input
    public void down(double x, double y){}
    public void move(double x, double y){}
    public void up(int x1, int y1){}
    public void setTarget(int x, int y, int difference){
        if (Math.abs(difference) <= 24) targetX = x;
    }
}
