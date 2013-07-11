package game;
import java.util.LinkedList;
import textures.SpriteSheet;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Cabins {
	private LinkedList<Cabin> cabins;
	private boolean select;
	
	public Cabins() {
		cabins = new LinkedList<Cabin>();
	}
	public void draw(Canvas canvas, Paint paint){
		for (int i = 0; i < cabins.size(); i++){
			cabins.get(i).draw(canvas, paint);
		}
	}
	public void update(double mod, double mainX, double mainY){
		for (int i = 0; i < cabins.size(); i++){
			cabins.get(i).update(mod, mainX, mainY);
		}
	}
	public void add(int x1){ cabins.add(new Cabin(x1)); }
	public void remove(int i){ cabins.remove(i); }
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
			for (int i = cabins.size()-1; i >= 0; i--){
				if (cabins.get(i).select(x1, y1) && !cabins.get(i).showBorder){
					up = true;
					deselectAll();
					select = true;
					cabins.get(i).showBorder(true);
					break;
				}
			}
		}
		return up;
	}
	public void deselectAll(){
		for (int j = 0;j<cabins.size();j++) 
		cabins.get(j).showBorder(false); 
	}
	/*
	 * Cabin class
	 */
	public static class Cabin {
		private SpriteSheet sprite;
		private SpriteSheet border;
		private boolean showBorder;
		private int type = 0; //0-3
		private int cabX;
		private int cabY;
		private int wood;
		private int maxWood = 256;
		
		public Cabin(int x){
			//type = (int)(Math.random()*4);
			type = 1;
			sprite = new SpriteSheet(GamePanel.textures.cabin, 4, 1, 0.0);
			int width = sprite.getBitWidth();
			int height = sprite.getBitHeight();
			//create border properties
			cabX = x;
			cabY = GamePanel.getHeight() - height*4 - 32;
			sprite.build(x,cabY,width*4,height*4);
			sprite.animate(type);
			border = new SpriteSheet(GamePanel.textures.renderBorder(sprite.getBitmap(), 
					sprite.getSpriteLeft(),
					sprite.getSpriteTop(), width, height), 1, 1, 0.0);
			border.build(x,cabY,((width+2)*4),(height+2)*4);
		}
		public void draw(Canvas canvas, Paint paint){
			if (showBorder) canvas.drawBitmap(border.getBitmap(), border.getSpriteRect(), border.getDestRect(), paint);
			canvas.drawBitmap(sprite.getBitmap(), sprite.getSpriteRect(), sprite.getDestRect(), paint);
		}
		public void update(double mod, double mainX, double mainY){
			sprite.update(mainX+cabX, mainY+cabY);
			if (showBorder) border.update(mainX+cabX-4, mainY+cabY-4);
		}
		public int getX(){ return cabX; }
		public int getY(){ return cabY; }
		public int getType(){ return type; }
		public int getWood(){ return wood; }
		public int getMaxWood(){ return maxWood; }
		public void setX(int x){ this.cabX = x; }
		public void setY(int y){ this.cabY = y; }
		public void setType(int type){ this.type = type; }
		public void showBorder(boolean show){ 
			showBorder = show; 
			if (show) border.resetDest(); 
			//else border.setBitmap(null);
		}
		public void setWood(int wood){ this.wood = wood; }
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
