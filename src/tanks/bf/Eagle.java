package tanks.bf;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;

public class Eagle extends SimpleBFObject implements Destroyable {

	private volatile boolean isDestroyed;


	public Eagle(int x, int y, BattleField bf) {
		this.x = x;
		this.y = y;
		this.bf = bf;

		iBFObject = bf.imageLoader.iEagle;
		isDestroyed = false;
	}

	@Override
	public void destroy() {
		isDestroyed = true;
	}

	@Override
	public boolean isDestroyed() {
		return isDestroyed;
	}

	public void draw(Graphics g) {
		if (!isDestroyed){
	g.drawImage(iBFObject, x, y, 64, 64, new ImageObserver() {
		@Override
		public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
			return false;
		}
	});
	}
	}
}