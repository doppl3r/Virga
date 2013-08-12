package game;

import buttons.Button;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

public class GUI {

	
	public GUI(Context context){
		int ratio = 4;
		int space = 288;
		//init butts
		//buildTree  = new Button(GamePanel.textures.build_tree, 0,0, true, context);

		//resize butts
		//buildTree.resize(ratio);

		//position top buttons
		//buildTree.update((GamePanel.getWidth()/2)-(space/2), GamePanel.getHeight()/2);   //left
	}
	public void draw(Canvas canvas, Paint paint){
		//draw gui's

	}
	public void update(double mod){
		
	}
	public void down(int x1, int y1){

	}
	public void move(int x1, int y1){

	}
	public void up(int x1, int y1){

	}
}
