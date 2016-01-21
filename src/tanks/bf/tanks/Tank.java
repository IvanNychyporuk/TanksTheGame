package tanks.bf.tanks;

import tanks.bf.Destroyable;
import tanks.bf.Drawable;

public interface Tank extends Drawable, Destroyable {

	public boolean isTankActing();

	public void setTankActing(boolean acting);

	public void refreshActions();

	public void move();

	public Bullet fire();

	public int getX();

	public int getY();
	
	public Direction getDirection();
	
	public void updateX(int x);

	public void updateY(int y);
	
	public int getSpeed();

	public void turn(Action action);

	public void pushAction();

	public int actionsSize();

}
