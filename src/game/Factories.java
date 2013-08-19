package game;
import java.util.LinkedList;
import textures.SpriteSheet;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Factories {
    private LinkedList<Factory> factories;
    private int selected, woodCost, rockCost;

    public Factories() {
        selected = -1;
        factories = new LinkedList<Factory>();
        woodCost = 10;
        rockCost = 10;
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
    public void down(int x1, int y1){ }
    public void move(int x1, int y1, int difference){ }
    public boolean up(int x1, int y1, int difference){
        boolean up = false;
        if (difference <= 32){
            for (int i = factories.size()-1; i >= 0; i--){
                if (selected > -1) factories.get(selected).showBorder(false);
                //set border properties
                if (factories.get(i).select(x1, y1) && !factories.get(i).showBorder){
                    Game.gui.setGUI(false,
                            false,
                            false,
                            factories.get(i).getMetalQuantity()>0, //farm metal from factory
                            false,
                            true);
                    up = true;
                    selected = i;
                    factories.get(selected).showBorder(true);
                    break;
                }
                else{
                    Game.gui.resetGUI();
                    unMarkAll();
                    selected = -1;
                }
            }
        }
        return up;
    }
    public void deselectAll(){
        for (int j = 0;j< factories.size();j++) factories.get(j).showBorder(false);
        selected = -1;
    }
    public void unMarkAll(){
        for (int j = 0;j<factories.size();j++) factories.get(j).setMark(false);
        selected = -1;
    }
    public void markSelected(boolean marked){
        if (selected > -1) factories.get(selected).setMark(marked);
    }
    public void farmMarked(){
        if (selected > -1){
            if (factories.get(selected).isMarked()){
                Game.land.player.addWood(factories.get(selected).getMetalQuantity());
                Game.gui.addSplashText("+"+(factories.get(selected).getMetalQuantity()),
                        Game.land.player.getObjectX()+GamePanel.game.getMainX()-16,
                        GamePanel.getHeight()-48);
                factories.get(selected).setMetalQuantity(0);
                //mines.remove(selected);
                Game.gui.resetGUI();
                selected = -1;
            }
        }
    }
    public boolean isBuildable(int x){
        //if the space is open, it can build
        boolean build = true;
        for (int i = 0; i < factories.size(); i++){
            if (Math.abs(x-factories.get(i).getX()) < factories.get(i).getSpriteWidth()*4){
                build = false;
                break;
            }
        }
        return build;
    }
    public int getSelectedIndex(){ return selected; }
    public int getWoodCost(){ return woodCost; }
    public int getRockCost(){ return rockCost; }
    /*
     * Factory class
     */
    public static class Factory {
        private SpriteSheet sprite;
        private SpriteSheet border;
        private boolean showBorder;
        private boolean marked;
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
            sprite.update(mainX+factoryX-(width*2), mainY+ factoryY);
            if (showBorder) border.update(mainX+factoryX-4-(width*2), mainY+ factoryY -4);
        }
        public int getSpriteWidth(){ return sprite.getSpriteWidth(); }
        public int getMetalQuantity(){ return metals; }
        public int getX(){ return factoryX; }
        public int getY(){ return factoryY; }
        public int getType(){ return type; }
        public double getRate(){ return rate; }
        public void setX(int x){ this.factoryX = x; }
        public void setY(int y){ this.factoryY = y; }
        public void setType(int type){ this.type = type; }
        public void showBorder(boolean show){ showBorder = show; if (show) border.resetDest(); }
        public void setMark(boolean marked){ this.marked=marked; }
        public void setMetalQuantity(int q){ metals=q; }
        public boolean isMarked(){ return marked; }
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
