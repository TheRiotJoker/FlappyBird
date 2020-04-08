import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Steuerung {
	final private Random random = new Random();
	private FlappyBirdGui gui;
	private Vogel flappyBird;
	private Timer rohreLoopTimer;
	private Rohr[] rohre = new Rohr[4];
	private int highScore;
	private int counter;
	private Timer repaintTimer;
	private boolean verloren = false;
	private boolean gestartet = false;
	private KeyListener l = new KeyListener() {

		@Override
		public void keyPressed(KeyEvent arg0) {
			if(arg0.getKeyCode() == KeyEvent.VK_SPACE && verloren == false) {
				gestartet = true;
				if(flappyBird.gibTimerlaeuft() == true) {
					flappyBird.gibSpringTimer().cancel();
					flappyBird.gibSpringTimer().purge();
					flappyBird.setzeTimerlaeuft(false);
				}
				flappyBird.springe();
			} else {
				if(arg0.getKeyCode() == KeyEvent.VK_R && verloren) {
					restart();
				}
			}
			
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	};
	public Steuerung(FlappyBirdGui gui) {
		this.gui = gui;
		flappyBird = new Vogel();
		rohre[0] = new Rohr();
		rohre[1] = new Rohr();
		rohre[2] = new Rohr();
		rohre[3] = new Rohr();
		gui.addKeyListener(l);
		anfang();
	}
	public boolean hatVerloren() {
		return verloren;
	}
	private void generiereRohr(final int index, final int x) {
		final int groesse = random.nextInt((230-50)+1)+50;
		rohre[index].setzeOberesRohr(x, 0, groesse);
	}
	private void anfang() {
		Timer startTimer = new Timer();
		starteRepaintTimer();
		rohreLoopTimer = new Timer();
		counter = 0;
		startTimer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				if(gestartet) {
					spielLoop();
					startTimer.cancel();
					startTimer.purge();
				}
				if(counter < 0) {
					flappyBird.getVogelViereck().y++;
				} else {
					if(counter > 0) {
						flappyBird.getVogelViereck().y--;
						if(counter == 11) {
							counter = -12;
						}
					}
				}
				counter++;
				
			}
		}, 0, 50);
		generiereRohr(0, gui.getWidth()+250);
		for(int i = 1; i < rohre.length; i++) {
			generiereRohr(i, rohre[i-1].gibOberesRohr().x+Rohr.xAbstand);
		}
		
	}	
	public void stoppeRepaintTimer() {
		repaintTimer.cancel();
		repaintTimer.purge();
		gui.repaint(); 
	}
	public void starteRepaintTimer() {
		repaintTimer = new Timer();
		//drawLoading(doubleBufferGraphics);
		repaintTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				gui.updateVierecke(rohre, flappyBird.getVogelViereck(), flappyBird.gibFlappybirdImage());
				gui.repaint();
				if(flappyBird.getVogelViereck().height > 354) {
					stoppeRepaintTimer();
				}
			}
			
		}, 0, 2);
	}
	private void spielLoop() {
		flappyBird.starteGravitation();
		rohreLoopTimer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				for(int i = 0; i < rohre.length; i++) {
					rohre[i].verschiebeRohre();
					gui.bodenBewegung();
					if(rohre[i].gibOberesRohr().intersects(flappyBird.getVogelViereck()) || rohre[i].gibUnteresRohr().intersects(flappyBird.getVogelViereck())) {
						if(flappyBird.gibTimerlaeuft()) {
							flappyBird.gibSpringTimer().cancel();
							flappyBird.gibSpringTimer().purge();
						}
						verloren();
					} else {
						if(rohre[i].gibOberesRohr().x == flappyBird.getVogelViereck().x) {
							flappyBird.inkrementiereScore();
							gui.updateScore(flappyBird.gibScore());
						}
					}
					if(rohre[i].gibOberesRohr().x < 0-rohre[i].gibOberesRohr().width) {
						if(i == 0) {
							generiereRohr(i, rohre[3].gibOberesRohr().x+Rohr.xAbstand);
						} else {
							generiereRohr(i, rohre[i-1].gibOberesRohr().x+Rohr.xAbstand);
						}
					}
					if(flappyBird.getVogelViereck().y+flappyBird.getVogelViereck().height > 353) {
						flappyBird.stoppeVogelLoopTimer();
						verloren();
					}
				}
				
			}
			
		}, 0, 7);
	}
	public int gibHighscore() {
		return highScore;
	}
	private void verloren() {
		rohreLoopTimer.cancel();
		rohreLoopTimer.purge();
		rohreLoopTimer = new Timer();
		flappyBird.stoppeSpriteSteuerungsTimer();
		gui.verloren();
		verloren = true;
	}
	private void restart() {
		if(flappyBird.gibScore() > highScore) {
			highScore = flappyBird.gibScore();
			gui.updateHighscore(highScore);
		}
		flappyBird = new Vogel(); //wir vernichten den alten vogel #rip
		verloren = false;
		gestartet = false;
		gui.restarte(flappyBird);
		anfang();
	}
}
