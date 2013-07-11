package game;

import buttons.Button;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GUI {
	private Button buildTree;
	private Button buildCabin;
	private Button buildMine;
	
	private Button upgradeCabin;
	private Button upgradeMine;
	private Button upgradePlane;
	
	private Button farmTree;
	private Button farmRock;
	
	private Button cancel;

	private boolean showGUI;
	
	public GUI(Context context){
		int ratio = 4;
		int space = 288;
		//init butts
		buildTree  = new Button(GamePanel.textures.build, 0,0, true, context);
		buildCabin = new Button(GamePanel.textures.build, 0,0, true, context);
		buildMine  = new Button(GamePanel.textures.build, 0,0, true, context);
		upgradeCabin = new Button(GamePanel.textures.upgrade, 0,0, true, context);
		upgradeMine  = new Button(GamePanel.textures.upgrade, 0,0, true, context);
		upgradePlane = new Button(GamePanel.textures.upgrade, 0,0, true, context);
		farmTree    = new Button(GamePanel.textures.farm, 0,0, true, context);
		farmRock = new Button(GamePanel.textures.farm, 0,0, true, context);
		cancel = new Button(GamePanel.textures.cancel, 0,0, true, context);
		//resize butts
		buildTree.resize(ratio);
		buildCabin.resize(ratio);
		buildMine.resize(ratio);
		upgradeCabin.resize(ratio);
		upgradeMine.resize(ratio);
		upgradePlane.resize(ratio);
		farmTree.resize(ratio);
		farmRock.resize(ratio);
		cancel.resize(ratio);
		//position top buttons
		buildTree.update((GamePanel.getWidth()/2)-(space/2), GamePanel.getHeight()/2);   //left
		buildCabin.update((GamePanel.getWidth()/2)-(space/2), GamePanel.getHeight()/2);   //left
		buildMine.update((GamePanel.getWidth()/2)-(space/2), GamePanel.getHeight()/2);   //left
		upgradeCabin.update((GamePanel.getWidth()/2)-(space/2), GamePanel.getHeight()/2); //left
		upgradeMine.update((GamePanel.getWidth()/2)-(space/2), GamePanel.getHeight()/2); //left
		upgradePlane.update((GamePanel.getWidth()/2)-(space/2), GamePanel.getHeight()/2); //left
		farmTree.update((GamePanel.getWidth()/2)-(space/2), GamePanel.getHeight()/2);    //left
		farmRock.update((GamePanel.getWidth()/2)-(space/2), GamePanel.getHeight()/2);    //left
		cancel.update((GamePanel.getWidth()/2)+(space/2), GamePanel.getHeight()/2);  //right
	}
	public void draw(Canvas canvas, Paint paint){
		//draw gui's
		if (showGUI){
			paint.setARGB(75, 0, 0, 0);
			canvas.drawRect(0,(GamePanel.getHeight()/2)-48,
					GamePanel.getWidth(),
					(GamePanel.getHeight()/2)+48, paint);
			buildTree.draw(canvas);
			buildCabin.draw(canvas);
			buildMine.draw(canvas);
			upgradeCabin.draw(canvas);
			upgradeMine.draw(canvas);
			upgradePlane.draw(canvas);
			farmTree.draw(canvas);
			farmRock.draw(canvas);
			cancel.draw(canvas);
		}
	}
	public void update(double mod){
		
	}
	public void down(int x1, int y1){
		if (showGUI){
			buildTree.down(x1, y1);
			buildCabin.down(x1, y1);
			buildMine.down(x1, y1);
			upgradeCabin.down(x1, y1);
			upgradeMine.down(x1, y1);
			upgradePlane.down(x1, y1);
			farmTree.down(x1, y1);
			farmRock.down(x1, y1);
			cancel.down(x1, y1);
		}
	}
	public void move(int x1, int y1){
		if (showGUI){
			buildTree.move(x1, y1);
			buildCabin.move(x1, y1);
			buildMine.move(x1, y1);
			upgradeCabin.move(x1, y1);
			upgradeMine.move(x1, y1);
			upgradePlane.move(x1, y1);
			farmTree.move(x1, y1);
			farmRock.move(x1, y1);
			cancel.move(x1, y1);
		}
	}
	public void up(int x1, int y1){
		if (showGUI){
			buildTree.up(x1, y1);
			buildCabin.up(x1, y1);
			buildMine.up(x1, y1);
			upgradeCabin.up(x1, y1);
			upgradeMine.up(x1, y1);
			upgradePlane.up(x1, y1);
			if (farmTree.up(x1, y1)){ setGUI(-1); Environment.people.choosePerson(x1); }
			farmRock.up(x1, y1);
			if (cancel.up(x1, y1)){ setGUI(-1); Game.land.deselectAll(); }
		}
	}
	public void setGUI(int currentGUI){
		showGUI = true;
		hideButtons(); //reset
		cancel.reveal(); //always show cancel
		switch(currentGUI){
			case(0): buildTree.reveal(); break;
			case(1): buildCabin.reveal(); break;
			case(2): buildMine.reveal(); break;
			case(3): upgradeCabin.reveal(); break;
			case(4): upgradeMine.reveal(); break;
			case(5): upgradePlane.reveal(); break;
			case(6): farmTree.reveal(); break;
			case(7): farmRock.reveal(); break;
			default: showGUI = false; break; //-1
		}
	}
	public void hideButtons(){
		buildTree.hide();
		buildCabin.hide();
		buildMine.hide();
		upgradeCabin.hide();
		upgradeMine.hide();
		upgradePlane.hide();
		farmTree.hide();
		farmRock.hide();
		cancel.hide();
	}
	public boolean isVisible(){ return showGUI; }
}
