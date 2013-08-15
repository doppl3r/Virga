package game;

import java.util.LinkedList;

import textures.SpriteSheet;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Rocks {
	private LinkedList<Rock> rocks;
	private int selected, rockCost;
	
	public Rocks(){
		rocks = new LinkedList<Rock>();
        rockCost = 0; //wut
	}
	public void draw(Canvas canvas, Paint paint){
		for (int i = 0; i < rocks.size(); i++){
			rocks.get(i).draw(canvas, paint);
		}
	}
	public void update(double mod, double mainX, double mainY){
		for (int i = 0; i < rocks.size(); i++){
			rocks.get(i).update(mod, mainX, mainY);
		}
	}
	public void add(int x1, int y1){ rocks.add(new Rock(x1, y1)); }
	public void remove(int i){ rocks.remove(i); }
	public void down(int x1, int y1){ }
	public void move(int x1, int y1, int difference){ }
	public boolean up(int x1, int y1, int difference){
		boolean up = false;
		if (difference <= 32){
            for (int i = rocks.size()-1; i >= 0; i--){
                if (selected > -1) rocks.get(selected).showBorder(false);
                //set border properties
                if (rocks.get(i).select(x1, y1) && !rocks.get(i).showBorder){
                    Game.gui.setGUI(false,false,false,true,false,true);
                    up = true;
                    selected = i;
                    rocks.get(selected).showBorder(true);
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
        for (int j = 0;j<rocks.size();j++) rocks.get(j).showBorder(false);
        selected = -1;
	}
    public void unMarkAll(){
        for (int j = 0;j<rocks.size();j++) rocks.get(j).setMark(false);
        selected = -1;
    }
    public void markSelected(boolean marked){
        if (selected > -1) rocks.get(selected).setMark(marked);
    }
    public void farmMarked(){
        if (selected > -1){
            if (rocks.get(selected).isMarked()){
                Game.land.player.addRocks(rocks.get(selected).getRockQuantity());
                Game.gui.addSplashText("+"+(rocks.get(selected).getRockQuantity()),
                        Game.land.player.getObjectX()+GamePanel.game.getMainX()-16,
                        GamePanel.getHeight()-48);
                rocks.remove(selected);
                Game.gui.resetGUI();
                selected = -1;
            }
        }
    }
    public int getSelectedIndex(){ return selected; }
    public int getRockCost(){ return rockCost; }
	/*
	 * Tree class
	 */
	public class Rock {
		private SpriteSheet sprite;
		private SpriteSheet border;
		private boolean showBorder;
        private boolean marked;
        private int rocks;
		private int type = 0; //0-3
		private int rockX;
		private int rockY;
		
		public Rock(int x, int y){
			type = (int)(Math.random()*4);
            rocks = type+1;
			sprite = new SpriteSheet(GamePanel.textures.rocks, 4, 1, 0.0);
			int width = sprite.getBitWidth();
			int height = sprite.getBitHeight();
			//create border properties
			rockX = x;
			rockY = GamePanel.getHeight() - height*4 - 32;
			sprite.build(x,rockY,width*4,height*4);
			sprite.animate(type);
			border = new SpriteSheet(GamePanel.textures.renderBorder(sprite.getBitmap(), 
					sprite.getSpriteLeft(),
					sprite.getSpriteTop(), width, height), 1, 1, 0.0);
			border.build(x,rockY,((width+2)*4),(height+2)*4);
		}
		public void draw(Canvas canvas, Paint paint){
			if (showBorder) canvas.drawBitmap(border.getBitmap(), border.getSpriteRect(), border.getDestRect(), paint);
			canvas.drawBitmap(sprite.getBitmap(), sprite.getSpriteRect(), sprite.getDestRect(), paint);
		}
		public void update(double mod, double mainX, double mainY){
			sprite.update(mainX+rockX, mainY+rockY);
			if (showBorder) border.update(mainX+rockX-4, mainY+rockY-4);
		}
        public int getRockQuantity(){ return rocks; }
		public int getX(){ return rockX; }
		public int getY(){ return rockY; }
		public int getType(){ return type; }
		public void setX(int x){ this.rockX = x; }
		public void setY(int y){ this.rockY = y; }
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
