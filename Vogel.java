
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Vogel {
	private int score = 0;
	private Rectangle r;
	private int counter;
	private boolean timerLaeuft = false;
	private boolean gesprungen = false;
	private int imageCounter = 0;
	private Image flappyBirdImage = Toolkit.getDefaultToolkit().createImage("");
	private Image downflapImage = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\yellowbird-downflap.png");
	private Image midflapImage = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\yellowbird-midflap.png");
	private Image upflapImage = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\yellowbird-upflap.png");
	private Timer t;
	private String vogelFarbe = "";
	private final Random random;
	private int gravitationsFaktor = 0;
	private int falleLangsamCounter;
	private int gravitationsDelay = 12;
	private int spriteDelay = 325;
	private Timer vogelLoopTimer;
	private Timer spriteSteuerungsTimer;
	public Vogel() {
		random = new Random();
		int a = (random.nextInt(3)+1);
		r = new Rectangle(140,170,32,26); // 32 - 28 - 28 / 30
		vogelLoopTimer = new Timer();
		spriteSteuerungsTimer = new Timer();
		switch(a) {
		case 1:
			downflapImage = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\yellowbird-downflap.png");
			midflapImage = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\yellowbird-midflap.png");
			upflapImage = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\yellowbird-upflap.png");
			break;
		case 2:
			downflapImage = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\redbird-downflap.png");
			midflapImage = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\redbird-midflap.png");
			upflapImage = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\redbird-upflap.png");
			break;
		case 3:
			downflapImage = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\bluebird-downflap.png");
			midflapImage = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\bluebird-midflap.png");
			upflapImage = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\bluebird-upflap.png");
			break;
		}
		spriteSteuerungsTimer();
	}
	public void springe() {
		t = new Timer();
		gesprungen = true;
		counter = 0;
		imageCounter = 2;
		spriteDelay = 95;
		stoppeSpriteSteuerungsTimer();
		spriteSteuerungsTimer();
		t.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				timerLaeuft = true;
				gravitationsDelay = 12;
				gravitationsFaktor = 1;
				falleLangsamCounter = 0;
				counter++;
				if(r.y > 0 && counter % 2 == 0) {
					r.setLocation(r.x, r.y-1);	
				}
				if(counter == 110) {
					timerLaeuft = false;
					t.cancel();
					t.purge();
				}
			}
			
			}, 0, 1);
		gesprungen = false;
	}
	public void starteGravitation()  {
		vogelLoopTimer.scheduleAtFixedRate(new TimerTask() { //gravitationsTimer

			@Override
			public void run() {
				if(falleLangsamCounter >= 13 ) {
					gravitationsFaktor =  4;
				} else {
					falleLangsamCounter++;	
				}
				if(gesprungen == false && r.y+r.height < 354 ) { //gravitation
					r.setLocation(r.x, r.y+gravitationsFaktor);
				}
			}
		}, 0, 12); 
	}
	public void spriteSteuerungsTimer() {
		spriteSteuerungsTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				imageCounter++;
				setzeSprite(imageCounter);
				if(imageCounter == 4) {
					imageCounter = 0;
				}
				
			}
			
		}, 0 , spriteDelay);
	}
	public void stoppeSpriteSteuerungsTimer() {
		spriteSteuerungsTimer.cancel();
		spriteSteuerungsTimer.purge();
		spriteSteuerungsTimer = new Timer();
	}
	private void setzeSprite(int entsch) {
		switch(entsch) {
		case 1:
			flappyBirdImage = upflapImage;
			break;
		case 2:
			flappyBirdImage = midflapImage;
			break;
		case 3:
			flappyBirdImage = downflapImage;
			break;
		case 4:
			flappyBirdImage = midflapImage;
			break;
		}
	}
	public void stoppeVogelLoopTimer() {
		vogelLoopTimer.cancel();
		vogelLoopTimer.purge();
	}
	public Rectangle getVogelViereck() {
		return r;
	}
	public boolean istGesprungen() {
		return gesprungen;
	}
	public boolean gibTimerlaeuft() {
		return timerLaeuft;
	}
	public void setzeTimerlaeuft(boolean timerLaeuft) {
		this.timerLaeuft = timerLaeuft;
	}
	public Timer gibSpringTimer() {
		return t;
	}
	public Image gibFlappybirdImage() {
		return flappyBirdImage;
	}
	public int gibScore() {
		return score;
	}
	public void inkrementiereScore() {
		score++;
	}
}
