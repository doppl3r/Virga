package game;
import java.util.LinkedList;

import textures.BitmapText;
import textures.SpriteSheet;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Mines {
	private LinkedList<Mine> mines;
    private SpriteSheet rockIcon;
    private BitmapText text;
    private int selected, woodCost, rockCost;
    private boolean upgrade;
	
	public Mines() {
        selected = -1;
		mines = new LinkedList<Mine>();
        woodCost = 5;
        rockCost = 5;
	}
	public void draw(Canvas canvas, Paint paint){
		for (int i = 0; i < mines.size(); i++){
			mines.get(i).draw(canvas, paint);
		}
	}
	public void update(double mod, double mainX, double mainY){
		for (int i = 0; i < mines.size(); i++){
			mines.get(i).update(mod, mainX, mainY);
		}
	}
	public void add(int x1){ mines.add(new Mine(x1)); }
	public void remove(int i){ mines.remove(i); }
	public void down(int x1, int y1){ }
	public void move(int x1, int y1, int difference){ }
	public boolean up(int x1, int y1, int difference){
		boolean up = false;
        if (difference <= 32){
            for (int i = mines.size()-1; i >= 0; i--){
                if (selected > -1) mines.get(selected).showBorder(false);
                //set border properties
                if (mines.get(i).select(x1, y1) && !mines.get(i).showBorder){
                    Game.gui.setGUI(false,
                            false,
                            false,
                            mines.get(i).getRockQuantity()>0, //farm rocks from mine
                            mines.get(i).isUpgradable(),
                            true);
                    up = true;
                    selected = i;
                    mines.get(selected).showBorder(true);
                    break;
                }
                else{
                    Game.gui.resetGUI();
                    unMarkAll();
                }
            }
        }
		return up;
	}
	public void deselectAll(){
		for (int j = 0;j<mines.size();j++) mines.get(j).showBorder(false);
        selected = -1;
	}
    public void unMarkAll(){
        for (int j = 0;j<mines.size();j++) mines.get(j).setMark(false);
        selected = -1;
    }
    public void markSelected(boolean marked, boolean upgrade){
        this.upgrade = upgrade;
        if (selected > -1) mines.get(selected).setMark(marked);
    }
    public void farmMarked(){
        if (selected > -1){
            if (mines.get(selected).isMarked()){
                if (!upgrade){
                    Game.land.player.addRocks(mines.get(selected).getRockQuantity());
                    Game.gui.addSplashText("+"+(mines.get(selected).getRockQuantity()),
                            Game.land.player.getObjectX()+GamePanel.game.getMainX()-16,
                            GamePanel.getHeight()-48);
                    mines.get(selected).setRockQuantity(0);
                }
                else mines.get(selected).upgrade();

                deselectAll();
                unMarkAll();
                Game.gui.resetGUI();
            }
        }
    }
    public boolean isBuildable(int x){
        //if the space is open, it can build
        boolean build = true;
        for (int i = 0; i < mines.size(); i++){
            if (Math.abs(x-mines.get(i).getX()) < mines.get(i).getSpriteWidth()*4){
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
	public static class Mine {
		private SpriteSheet sprite;
		private SpriteSheet border;
		private boolean showBorder;
        private boolean marked;
		private double maxAnimationTime = 10;
		private double animationTime = maxAnimationTime;
		private boolean upgraded; //was 'type'
		private double rate = 200;
        private double timer;
        private int timerRate;
        private int maxTimer;
		private int mineX;
		private int mineY;
		private int rocks;
        private int maxRocks;
		private int width;
		private int height;
        private int metalUpgradeCost;
		
		public Mine(int x){
            rocks = 1;
            timer = maxTimer = 5000;
            timerRate = 83; //1 minute iterations
            maxRocks = 5;
            metalUpgradeCost = 10;


			sprite = new SpriteSheet(GamePanel.textures.mine_wood, 6, 6, 0.5);
			width = sprite.getBitWidth();
			height = sprite.getBitHeight();
			//create border properties
			mineX = x;
			mineY = GamePanel.getHeight() - height*4 - 32;
			sprite.build(x,mineY,width*4,height*4);
			sprite.animate(0);
			border = new SpriteSheet(GamePanel.textures.renderBorder(sprite.getBitmap(), 
					sprite.getSpriteLeft(), sprite.getSpriteTop(), width, height), 1, 1, 0.0);
			border.build(x,mineY,((width+2)*4),(height+2)*4);
		}
		public void draw(Canvas canvas, Paint paint){
			if (showBorder) canvas.drawBitmap(border.getBitmap(), border.getSpriteRect(), border.getDestRect(), paint);
			canvas.drawBitmap(sprite.getBitmap(), sprite.getSpriteRect(), sprite.getDestRect(), paint);
		}
		public void update(double mod, double mainX, double mainY){
            process(mod);
			//update animation
			if (animationTime > 0) animationTime -= (rate*mod);
			else{
                if (rocks < maxRocks){ //animate if it's not full
                    sprite.animate();
                    animationTime = maxAnimationTime;
                    border.setBitmap(GamePanel.textures.renderBorder(sprite.getBitmap(),
                    sprite.getSpriteLeft(),	sprite.getSpriteTop(), width, height));
                }
            }
			//update position
			sprite.update(mainX+mineX-(width*2), mainY+mineY);
			if (showBorder) border.update(mainX+mineX-4-(width*2), mainY+mineY-4);
		}
        public int getSpriteWidth(){ return sprite.getSpriteWidth(); }
        public int getRockQuantity(){ return rocks; }
		public int getX(){ return mineX; }
		public int getY(){ return mineY; }
		public boolean getUpgraded(){ return upgraded; }
		public double getRate(){ return rate; }
		public void setX(int x){ this.mineX = x; }
		public void setY(int y){ this.mineY = y; }
		public void setUpgraded(boolean upgraded){ this.upgraded = upgraded; }
		public void showBorder(boolean show){ showBorder = show; if (show) border.resetDest(); }
        public void setMark(boolean marked){ this.marked=marked; }
        public void setRockQuantity(int q){ rocks = q; }
        public boolean isMarked(){ return marked; }
        public boolean isUpgradable(){ return Game.land.player.getMetal() >= metalUpgradeCost && !upgraded; }
		public boolean select(int x, int y){
			boolean select = false;
			if (x >= sprite.getDestRect().left && x < sprite.getDestRect().right &&
				y >= sprite.getDestRect().top && y < sprite.getDestRect().bottom){
				select = true;
			}
			return select;
		}
        public void process(double mod){
            //grow if the timer is ready
            if (rocks < maxRocks){ //only check upgrades if under development
                if (timer > 0) timer -= (mod*timerRate); //tick the tock
                else { //if it's ready to grow, do this stuff
                    timer = maxTimer; //reset ticker
                    rocks++;
                    if (showBorder){
                        Game.gui.setGUI(false,
                            false,
                            false,
                            getRockQuantity()>0, //farm rocks from mine
                            isUpgradable(),
                            true);
                    }
                }
            }
        }
        public void upgrade(){
            if (!upgraded){
                upgraded = true;
                maxRocks = 10;
                rate = 400;
                Game.gui.addSplashText("Upgraded!",
                        Game.land.player.getObjectX()+GamePanel.game.getMainX()-64,
                        GamePanel.getHeight()-48);
                timerRate = 167; //30 seconds iteration
                Game.land.player.addMetal(-metalUpgradeCost);
                sprite = new SpriteSheet(GamePanel.textures.mine_metal, 6, 6, 0.5);
                sprite.build(mineX,mineY,width*4,height*4);
                //sprite.animate(0);
                //re-render border
                border = new SpriteSheet(GamePanel.textures.renderBorder(sprite.getBitmap(),
                        sprite.getSpriteLeft(),
                        sprite.getSpriteTop(), width, height), 1, 1, 0.0);
                border.build(mineX,mineY,((width+2)*4),(height+2)*4);
            }
        }
	}
}
