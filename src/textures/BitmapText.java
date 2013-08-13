package textures;
import android.graphics.Canvas;
import android.graphics.Rect;
import game.GamePanel;
import textures.SpriteSheet;


public final class BitmapText{
	//private boolean rendered;
    private SpriteSheet text1, text2;
	//private Bitmap render;
	public BitmapText(){
        text1 = new SpriteSheet(GamePanel.textures.text1, 16, 6, 0.0);
        text2 = new SpriteSheet(GamePanel.textures.text2, 16, 6, 0.0);

        text1.resize(2);
        text2.resize(2);
	}
	public final void draw(String text, int x, int y, Canvas canvas){
        for (int i = 0; i < text.length(); i++){
            //top
            text2.update(x+(i*text2.getBitWidth()),y-text2.getRatio());
            text2.animate((int)(text.charAt(i))-32);
            text2.draw(canvas);
            //right
            text2.update(x+text2.getRatio()+(i*text2.getBitWidth()),y);
            text2.animate((int)(text.charAt(i))-32);
            text2.draw(canvas);
            //bottom
            text2.update(x+(i*text2.getBitWidth()),y+text2.getRatio());
            text2.animate((int)(text.charAt(i))-32);
            text2.draw(canvas);
            //left
            text2.update(x - text2.getRatio() + (i * text2.getBitWidth()), y);
            text2.animate((int) (text.charAt(i)) - 32);
            text2.draw(canvas);
        }
        for (int i = 0; i < text.length(); i++){
            //center
            text1.update(x+(i*text1.getBitWidth()),y);
            text1.animate((int)(text.charAt(i))-32);
            text1.draw(canvas);
        }
	}
	public final int getX(int a){
		return ((a-32)%16)*8;
	}
	public final int getY(int a){
		return ((a-32)/16)*8;
	}
}
