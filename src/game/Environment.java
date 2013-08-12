package game;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Environment {
	private int mainX, mainY;
	private int tileCount = 128;
	private int tileSize = 32;
	private int downX;
	private int downY;
	private int dragX;
	private int dragY;
	private int objectiveX;
	private double rate1;
	private double rate2;
	private double rate3;
	private double rate4;
	private Mountains layer1;
	private Mountains layer2;
	private Mountains layer3;
	private Earth layer4;
	private Clouds clouds;
	private Factories factories;
	private Mines mines;
	private Trees trees;
	private Rocks rocks;
    private Player player;
	
	public Environment(){
		mainX = (GamePanel.getWidth()/2)-((tileCount*tileSize)/2);
		mainY = 0;
		//sprite = new SpriteSheet(GamePanel.textures.terrain, 1, 1, 0.0);
		//newTerrain = GamePanel.textures.renderMatrix(tiles, sprite, 4);
		layer1 = new Mountains();
		layer2 = new Mountains();
		layer3 = new Mountains();
		layer4 = new Earth(tileCount, tileSize);
		rate1 = 0.25;
		rate2 = 0.50;
		rate3 = 0.75;
		rate4 = 1.0;
		factories = new Factories();
		mines = new Mines();
		trees = new Trees();
		rocks = new Rocks();
		clouds = new Clouds();
        player = new Player(layer4.getWidth() / 2, 0);
		//test
		factories.add(layer4.getWidth() / 2); //add factory_wood to the center of the map
		for (int i = 0; i < 24; i++)
		trees.add((int)(Math.random()*layer4.getWidth()), 0);
		for (int i = 0; i < 8; i++) 
		rocks.add((int)(Math.random()*layer4.getWidth()), 0);
		clouds.construct(layer4.getWidth());
		//draw mountains
		addMountains();
	}
	public void draw(Canvas canvas, Paint paint){
		//draw layers
		layer1.draw(canvas, paint);
		layer2.draw(canvas, paint);
		layer3.draw(canvas, paint);
		layer4.draw(canvas, paint);
		//objects
		clouds.draw(canvas, paint); //draw clouds
		rocks.draw(canvas, paint);  //draw rocks
		trees.draw(canvas, paint);  //draw trees
		factories.draw(canvas, paint); //draw factories
		mines.draw(canvas, paint);
        //player
        player.draw(canvas, paint);
	}
	public void update(double mod){
		//mountains
		layer1.update(mod, (mainX-(downX-dragX))*rate1, mainY);
		layer2.update(mod, (mainX-(downX-dragX))*rate2, mainY);
		layer3.update(mod, (mainX-(downX-dragX))*rate3, mainY);
		layer4.update(mod, (mainX-(downX-dragX))*rate4, mainY);
		//objects
		factories.update(mod, (mainX - (downX - dragX)) * rate4, mainY);//factories
		mines.update(mod,  (mainX-(downX-dragX))*rate4, mainY);//mines
		clouds.update(mod, (mainX-(downX-dragX))*rate4, mainY);//clouds
		trees.update(mod,  (mainX-(downX-dragX))*rate4, mainY);
		rocks.update(mod,  (mainX-(downX-dragX))*rate4, mainY);
        //player
        player.update(mod,  (mainX-(downX-dragX))*rate4, mainY);
	}
	public void down(int x1, int y1){
		downX = dragX = x1;
		factories.down(x1, y1);
		mines.down(x1, y1);
		trees.down(x1, y1);
		rocks.down(x1, y1);
	}
	public void move(int x1, int y1){
		//check drag
		dragX = x1;
		if (mainX-(downX-dragX) > 0){ mainX = 0; downX = x1;}
		else if (mainX-(downX-dragX) < -(layer4.getWidth() - GamePanel.getWidth())) {
			mainX = -(layer4.getWidth() - GamePanel.getWidth());
			downX = x1;
		}
		factories.move(x1, y1, Math.abs(downX - dragX));
		mines.move(x1, y1, Math.abs(downX-dragX));
		trees.move(x1, y1, Math.abs(downX-dragX));
		rocks.move(x1, y1, Math.abs(downX-dragX));
	}
	public void up(int x1, int y1){
		//check factories
		//release drag
		mainX -= (downX-dragX);
		mainY -= (downY-dragY);
		deselectAll();
		objectiveX = -mainX + x1;
		if (factories.up(x1, y1, Math.abs(downX-dragX))){  }
		else if (mines.up(x1, y1, Math.abs(downX-dragX))){  }
		else if (trees.up(x1, y1, Math.abs(downX-dragX))){  }
		else if (rocks.up(x1, y1, Math.abs(downX-dragX))){  }
        else player.setTarget(-mainX + x1,y1,(downX-dragX)); //move player
		downX = downY = dragX = dragY = 0;
	}
	public void deselectAll(){
		trees.deselectAll();
		rocks.deselectAll();
		factories.deselectAll();
		mines.deselectAll();
	}
	public void addMountains(){
		int spacing = 0;
		int maxSpacing = 360;
		int x = -256;
		int y = 256;
		int offset = 32;
		int yOffset = 0;
		int height = 0;
		
		//layer1
		while (x - spacing < (layer4.getWidth()-GamePanel.getWidth())*rate1){
			spacing = (int)(Math.random()*maxSpacing);
			yOffset = ((int)((Math.random()*offset)/4)*4);
			height = ((GamePanel.getHeight() - (y+yOffset) - 32)/4);
			x += spacing;
			layer1.add(x, y+yOffset, height, 0);
		}
		//layer2
		x = -256; y += offset; maxSpacing = 720;
		while (x - spacing < (layer4.getWidth()-GamePanel.getWidth())*rate2){
			spacing = (int)(Math.random()*maxSpacing);
			yOffset = ((int)((Math.random()*offset)/4)*4);
			height = ((GamePanel.getHeight() - (y+yOffset) - 32)/4);
			x += spacing;
			layer2.add(x, y+yOffset, height, 1);
		}
		//layer3
		x = -256; y += offset; maxSpacing = 1080;
		while (x - spacing < (layer4.getWidth()-GamePanel.getWidth())*rate3){
			spacing = (int)(Math.random()*maxSpacing);
			yOffset = ((int)((Math.random()*offset)/4)*4);
			height = ((GamePanel.getHeight() - (y+yOffset) - 32)/4);
			x += spacing;
			layer3.add(x, y+yOffset, height, 2);
		}
	}
	public int getMainX(){ return mainX; }
	public int getMainY(){ return mainY; }
	public int getObjectiveX(){ return objectiveX; }
}
