package game;

import java.util.LinkedList;
import textures.SpriteSheet;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Trees {
    private LinkedList<Tree> trees;
    private int selected;

    public Trees(){
        trees = new LinkedList<Tree>();
    }
    public void draw(Canvas canvas, Paint paint){
        for (int i = 0; i < trees.size(); i++){
            trees.get(i).draw(canvas, paint);
        }
    }
    public void update(double mod, double mainX, double mainY){
        for (int i = 0; i < trees.size(); i++){
            trees.get(i).update(mod, mainX, mainY);
        }
    }
    public void add(int x1, int y1){ trees.add(new Tree(x1, y1)); }
    public void remove(int i){ trees.remove(i); }
    public void down(int x1, int y1){ }
    public void move(int x1, int y1, int difference){}
    public boolean up(int x1, int y1, int difference){
        boolean up = false;
        if (difference <= 32){
            for (int i = trees.size()-1; i >= 0; i--){
                if (selected > -1) trees.get(selected).showBorder(false);
                //set border properties
                if (trees.get(i).select(x1, y1) && !trees.get(i).showBorder){
                    Game.gui.setGUI(false,false,false,true,false,true);
                    up = true;
                    selected = i;
                    trees.get(selected).showBorder(true);
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
    public int getMarkedTreeX(){
        int x;
        if (selected > -1) x = trees.get(selected).getX()+(trees.get(selected).sprite.getBitWidth()/2);
        else x = Game.land.player.getX();
        return x;
    }
    public void deselectAll(){
        for (int j = 0;j<trees.size();j++) trees.get(j).showBorder(false);
    }
    public void unMarkAll(){
        for (int j = 0;j<trees.size();j++) trees.get(j).setMark(false);
    }
    public void markSelected(boolean marked){
        if (selected > -1) trees.get(selected).setMark(marked);
    }
    public void farmMarked(){
        if (selected > -1){
            if (trees.get(selected).isMarked()){
                trees.remove(selected);
                Game.gui.resetGUI();
                selected = -1;
            }
        }
    }
    public int getSelectedIndex(){ return selected; }
    /*
     * Tree class
     */
    public class Tree {
        private SpriteSheet sprite;
        private SpriteSheet border;
        private SpriteSheet shadow;
        private boolean showBorder;
        private boolean marked;
        private double age;
        private int type = 0; //0-3
        private int treeX;
        private int treeY;

        public Tree(int x, int y){
            type = (int)(Math.random()*4);
            sprite = new SpriteSheet(GamePanel.textures.trees, 6, 1, 0.0);
            shadow = new SpriteSheet(GamePanel.textures.trees, 6, 1, 0.0);
            int width = sprite.getBitWidth();
            int height = sprite.getBitHeight();
            //create border properties
            treeX = x;
            treeY = GamePanel.getHeight() - height*4 - 32;
            sprite.build(x,treeY,width*4,height*4);
            sprite.animate(type);
            shadow.build(x, y, width*4, height*4);
            shadow.animate(5);
            border = new SpriteSheet(GamePanel.textures.renderBorder(sprite.getBitmap(),
                    sprite.getSpriteLeft(),
                    sprite.getSpriteTop(), width, height), 1, 1, 0.0);
            border.build(x,treeY,((width+2)*4),(height+2)*4);
        }
        public void draw(Canvas canvas, Paint paint){
            if (showBorder) canvas.drawBitmap(border.getBitmap(), border.getSpriteRect(), border.getDestRect(), paint);
            canvas.drawBitmap(shadow.getBitmap(), shadow.getSpriteRect(), shadow.getDestRect(), paint);
            canvas.drawBitmap(sprite.getBitmap(), sprite.getSpriteRect(), sprite.getDestRect(), paint);
        }
        public void update(double mod, double mainX, double mainY){
            sprite.update(mainX+treeX, mainY+treeY);
            shadow.update(mainX+treeX, mainY+treeY+shadow.getSpriteHeight()*4);
            if (showBorder) border.update(mainX+treeX-4, mainY+treeY-4);
        }
        public int getX(){ return treeX; }
        public int getY(){ return treeY; }
        public int getType(){ return type; }
        public void setX(int x){ this.treeX = x; }
        public void setY(int y){ this.treeY = y; }
        public void setType(int type){ this.type = type; }
        public void showBorder(boolean show){ showBorder = show; if (show) border.resetDest(); }
        public void setMark(boolean marked){ this.marked=marked; }
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