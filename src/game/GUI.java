package game;

import android.util.Log;
import buttons.Button;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import textures.BitmapText;
import textures.SpriteSheet;

public class GUI {

    private boolean pressed;
    private Button newTree, newMine, newFactory; //building buttons
    private Button farm, upgrade, exit; //selector buttons
    private SpriteSheet woodIcon, rockIcon, metalIcon;
    private BitmapText woodCount, rockCount, metalCount;

	public GUI(Context context){
		int ratio = 4;
		int space = 288;
        //show resource icons
        woodIcon = new SpriteSheet(GamePanel.textures.wood, 1, 1, 0.0);
        woodCount = new BitmapText();
        rockIcon = new SpriteSheet(GamePanel.textures.rock, 1, 1, 0.0);
        rockCount = new BitmapText();
        metalIcon = new SpriteSheet(GamePanel.textures.metal, 1, 1, 0.0);
        metalCount = new BitmapText();
        //place icons
        woodIcon.build(16, 96, 32, 32);
        rockIcon.build(16, 144, 32, 32);
        metalIcon.build(16, 192, 32, 32);
        //init buttons
        newTree = new Button(GamePanel.textures.build_tree, 0, 0, false, context);
        newMine = new Button(GamePanel.textures.build_mine, 0, 0, false, context);
        newFactory = new Button(GamePanel.textures.build_factory, 0, 0, false, context);
        farm = new Button(GamePanel.textures.farm, 0, 0, false, context);
        upgrade = new Button(GamePanel.textures.upgrade, 0, 0, false, context);
        exit = new Button(GamePanel.textures.exit, 0, 0, false, context);
        //set button size
        newTree.resize(4);
        newMine.resize(4);
        newFactory.resize(4);
        farm.resize(4);
        upgrade.resize(4);
        exit.resize(4);
        //position
        newTree.update(0,0);
        newMine.update(80,0);
        newFactory.update(160,0);
        farm.update(GamePanel.getWidth()-224,0);
        upgrade.update(GamePanel.getWidth()-144,0);
        exit.update(GamePanel.getWidth()-64,0);
        //fade starting buttons
        resetGUI();
	}
	public void draw(Canvas canvas, Paint paint){
		//draw gui's
        woodIcon.draw(canvas,paint);
        woodCount.draw("x"+Game.land.player.getWood(), 56,104, canvas);
        rockIcon.draw(canvas,paint);
        rockCount.draw("x"+Game.land.player.getWood(), 56,152, canvas);
        metalIcon.draw(canvas,paint);
        metalCount.draw("x"+Game.land.player.getWood(), 56,200, canvas);

        //draw buttons
        newTree.draw(canvas);
        newMine.draw(canvas);
        newFactory.draw(canvas);
        farm.draw(canvas);
        upgrade.draw(canvas);
        exit.draw(canvas);
	}
	public void update(double mod){
		
	}
	public void down(int x1, int y1){
        if (newTree.down(x1, y1)) pressed = true;
        else if (newMine.down(x1,y1)) pressed = true;
        else if (newFactory.down(x1,y1)) pressed = true;
        else if (farm.down(x1,y1)) pressed = true;
        else if (upgrade.down(x1,y1)) pressed = true;
        else if (exit.down(x1,y1)) pressed = true;
	}
	public void move(int x1, int y1){
        if (newTree.move(x1, y1)) pressed = true;
        if (newMine.move(x1, y1)) pressed = true;
        if (newFactory.move(x1, y1)) pressed = true;
        if (farm.move(x1, y1)) pressed = true;
        if (upgrade.move(x1, y1)) pressed = true;
        if (exit.move(x1, y1)) pressed = true;
	}
	public void up(int x1, int y1){
        if (newTree.up(x1, y1)){

        }
        else if (newMine.up(x1,y1)){

        }
        else if (newFactory.up(x1,y1)){

        }
        else if (farm.up(x1,y1)){
            Game.land.trees.markSelected(true);
            Game.land.rocks.markSelected(true);
            Game.land.player.setTarget(Game.land.player.getObjectX(),0,0);
        }
        else if (upgrade.up(x1,y1)){

        }
        else if (exit.up(x1,y1)){

        }
    }
    public boolean isPressed(){ return pressed; }
    public void setPressed(boolean s){ pressed=s; }
    public void setGUI(boolean f1, boolean f2, boolean f3, boolean f4, boolean f5, boolean f6){
        if (f1) newTree.setFade(255); else newTree.setFade(75);
        if (f2) newMine.setFade(255); else newMine.setFade(75);
        if (f3) newFactory.setFade(255); else newFactory.setFade(75);
        if (f4) farm.setFade(255); else farm.setFade(75);
        if (f5) upgrade.setFade(255); else upgrade.setFade(75);
        if (f6) exit.setFade(255); else exit.setFade(75);
    }
    public void resetGUI(){
        boolean addTree=false;
        boolean addMine=false;
        boolean addFact=false;
        //show tree button
        if (Game.land.player.getWood() >= 1) addTree=true;
        //show mine button
        if (Game.land.player.getWood() >= 5 && Game.land.player.getRocks() >= 1) addMine=true;
        //show factory
        if (Game.land.player.getWood()>= 20 && Game.land.player.getRocks() >= 20) addFact=true;
        //compile settings
        setGUI(addTree,addMine,addFact,false,false,false);
    }
}
