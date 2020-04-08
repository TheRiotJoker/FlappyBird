import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Rohr {
	private Rectangle oberesRohr;
	private Rectangle unteresRohr;
	static final private int rohrBreite = 60;
	static final private int yAbstand = 95;
	static final public int xAbstand = 230;
	
	public Rohr() {
		oberesRohr = new Rectangle(0,0,0,0);
		unteresRohr = new Rectangle(0,0,0,0);
	}
	public void setzeOberesRohr(int x, int y, int height) {
		oberesRohr.setBounds(x,y, rohrBreite,height);
		setzeUnteresRohr(height);
	}
	private void setzeUnteresRohr(int height) {
		final int unteresRohrHoehe = oberesRohr.y+height+yAbstand;
		unteresRohr.setBounds(oberesRohr.x, unteresRohrHoehe,rohrBreite, 350-unteresRohrHoehe);
	}
	public Rectangle gibOberesRohr() {
		return oberesRohr;
	}
	public Rectangle gibUnteresRohr() {
		return unteresRohr;
	}
	public void verschiebeRohre() {
		oberesRohr.x--;
		unteresRohr.x--;
	}
}
