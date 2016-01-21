package tanks.bf;

import java.awt.*;
import java.awt.image.ImageObserver;


public abstract class SimpleBFObject implements Drawable {

	// current position on BF
	protected int x;
	protected int y;
	protected BattleField bf;
	protected Image iBFObject;
	
	protected Color color;

//	protected boolean isDestroyed = false;

	public SimpleBFObject() {

	}
	
	public SimpleBFObject(int x, int y, BattleField bf) {
		this.x = x;
		this.y = y;
		this.bf = bf;
	}
	

//	public void destroy() {
//		isDestroyed = true;
//	}
	
	@Override
	public void draw(Graphics g) {
//		if (!isDestroyed) {
//			g.setColor(this.color);
//			g.fillRect(this.getX(), this.getY(), 64, 64);
//		}
		g.drawImage(iBFObject, x, y, x + 64, y + 64, x, y, x + 64, y + 64, new ImageObserver() {
			@Override
			public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
				return false;
			}
		});
	}
	
//	public boolean isDestroyed() {
//		return isDestroyed;
//	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
