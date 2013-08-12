package game;
import java.util.LinkedList;
import textures.SpriteSheet;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Mines {
	private LinkedList<Mine> mines;
	private boolean select;
	
	public Mines() {
		mines = new LinkedList<Mine>();
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
			for (int i = mines.size()-1; i >= 0; i--){
				if (mines.get(i).select(x1, y1) && !mines.get(i).showBorder){
					up = true;
					deselectAll();
					select = true;
					mines.get(i).showBorder(true);
					break;
				}
			}
		}
		return up;
	}
	public void deselectAll(){
		for (int j = 0;j<mines.size();j++) 
		mines.get(j).showBorder(false); 
	}
	/*
	 * Factory class
	 */
	public static class Mine {
		private SpriteSheet sprite;
		private SpriteSheet border;
		private boolean showBorder;
		private double maxAnimationTime = 10;
		private double animationTime = maxAnimationTime;
		private int type = 3; //0-3
		private double rate = (type+1)*100;
		private int mineX;
		private int mineY;
		private int minerals;
		private int width;
		private int height;
		
		public Mine(int x){
			sprite = new SpriteSheet(GamePanel.textures.mine_wood, 6, 6, 0.5);
			width = sprite.getBitWidth();
			height = sprite.getBitHeight();
			//create border properties
			mineX = x;
			mineY = GamePanel.getHeight() - height*4 - 32;
			sprite.build(x,mineY,width*4,height*4);
			sprite.animate(type);
			border = new SpriteSheet(GamePanel.textures.renderBorder(sprite.getBitmap(), 
					sprite.getSpriteLeft(), sprite.getSpriteTop(), width, height), 1, 1, 0.0);
			border.build(x,mineY,((width+2)*4),(height+2)*4);
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
			sprite.update(mainX+mineX, mainY+mineY);
			if (showBorder) border.update(mainX+mineX-4, mainY+mineY-4);
		}
		public int getX(){ return mineX; }
		public int getY(){ return mineY; }
		public int getType(){ return type; }
		public int getMinerals(){ return minerals; }
		public double getRate(){ return rate; }
		public void setX(int x){ this.mineX = x; }
		public void setY(int y){ this.mineY = y; }
		public void setType(int type){ this.type = type; }
		public void showBorder(boolean show){ 
			showBorder = show; if (show){ 
				border.resetDest(); 
			}
		}
		public void setMinerals(int minerals){ this.minerals = minerals; }
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
