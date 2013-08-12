package game;
import java.util.LinkedList;
import textures.SpriteSheet;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Factories {
    private LinkedList<Factory> factories;
    private boolean select;

    public Factories() {
        factories = new LinkedList<Factory>();
    }
    public void draw(Canvas canvas, Paint paint){
        for (int i = 0; i < factories.size(); i++){
            factories.get(i).draw(canvas, paint);
        }
    }
    public void update(double mod, double mainX, double mainY){
        for (int i = 0; i < factories.size(); i++){
            factories.get(i).update(mod, mainX, mainY);
        }
    }
    public void add(int x1){ factories.add(new Factory(x1)); }
    public void remove(int i){ factories.remove(i); }
    public void down(int x1, int y1){

    }
    public void move(int x1, int y1, int difference){
        if (difference > 32 && select == true){
            select = false;
            deselectAll();
        }

    }
    public boolean up(int x1, int y1, int difference){
        boolean up = false;
        if (difference <= 32){
            for (int i = factories.size()-1; i >= 0; i--){
                if (factories.get(i).select(x1, y1) && !factories.get(i).showBorder){
                    up = true;
                    deselectAll();
                    select = true;
                    factories.get(i).showBorder(true);
                    break;
                }
            }
        }
        return up;
    }
    public void deselectAll(){
        for (int j = 0;j< factories.size();j++)
            factories.get(j).showBorder(false);
    }
    /*
     * Factory class
     */
    public static class Factory {
        private SpriteSheet sprite;
        private SpriteSheet border;
        private boolean showBorder;
        private double maxAnimationTime = 10;
        private double animationTime = maxAnimationTime;
        private int type = 3; //0-3
        private double rate = (type+1)*100;
        private int factoryX;
        private int factoryY;
        private int metals;
        private int width;
        private int height;

        public Factory(int x){
            sprite = new SpriteSheet(GamePanel.textures.factory_wood, 6, 6, 0.5);
            width = sprite.getBitWidth();
            height = sprite.getBitHeight();
            //create border properties
            factoryX = x;
            factoryY = GamePanel.getHeight() - height*4 - 32;
            sprite.build(x, factoryY,width*4,height*4);
            sprite.animate(type);
            border = new SpriteSheet(GamePanel.textures.renderBorder(sprite.getBitmap(),
                    sprite.getSpriteLeft(), sprite.getSpriteTop(), width, height), 1, 1, 0.0);
            border.build(x, factoryY,((width+2)*4),(height+2)*4);
        }
        public void draw(Canvas canvas, Paint paint){
            if (showBorder) canvas.drawBitmap(border.getBitmap(), border.getSpriteRect(), border.getDestRect(), paint);
            canvas.drawBitmap(sprite.getBitmap(), sprite.getSpriteRect(), sprite.getDestRect(), paint);
        }
        public void update(double mod, double mainX, double mainY){
            //update animation
            if (animationTime > 0) animationTime -= (rate*mod);
            else{
                sprite.animate();
                animationTime = maxAnimationTime;
                border.setBitmap(GamePanel.textures.renderBorder(sprite.getBitmap(),
                        sprite.getSpriteLeft(),	sprite.getSpriteTop(), width, height));
            }
            //update position
            sprite.update(mainX+ factoryX, mainY+ factoryY);
            if (showBorder) border.update(mainX+ factoryX -4, mainY+ factoryY -4);
        }
        public int getX(){ return factoryX; }
        public int getY(){ return factoryY; }
        public int getType(){ return type; }
        public int getMetals(){ return metals; }
        public double getRate(){ return rate; }
        public void setX(int x){ this.factoryX = x; }
        public void setY(int y){ this.factoryY = y; }
        public void setType(int type){ this.type = type; }
        public void showBorder(boolean show){
            showBorder = show; if (show){
                border.resetDest();
            }
        }
        public void setMetals(int metals){ this.metals = metals; }
        public boolean select(int x, int y){
            boolean select = false;
            if (x >= sprite.getDestRect().left && x < sprite.getDestRect().right &&
                    y >= sprite.getDestRect().top && y < sprite.getDestRect().bottom){
                select = true;
            }
            return select;
        }
    }
}
