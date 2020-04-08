import java.awt.Color;
import java.util.Random;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;


public class FlappyBirdGui extends JFrame {
	private static final long serialVersionUID = 1L;
	private Steuerung steuerung;
	private final Random randomNumberGen;
	private Image doubleBufferImg;
	private int counter;
	private Graphics doubleBufferGraphics;
	private final Image hintergrundTag = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\hintergrundTag.jpg");
	private final Image hintergrundNacht = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\hintergrundNacht.jpg");
	private Image hintergrund = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\hintergrundTag.jpg");
	private Image boden = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\brazil(boden)1.jpg");
	private Image boden2 = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\brazil(boden)1.jpg");
	private Image bodenNacht = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\bodennacht.jpg");
	private Image bodenTag = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\brazil(boden).jpg");
	private Image unteresRohrBild = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\unteresRohr1.jpg");
	private Image oberesRohrBild = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\oberesRohr1.jpg");
	private final Image unteresRohrBildTag = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\unteresRohr.jpg");
	private final Image oberesRohrBildTag = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\oberesRohr.jpg");
	private final Image unteresRohrBildNacht = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\unteresRohrNacht.jpg");
	private final Image oberesRohrBildNacht = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\oberesRohrNacht.jpg");
	private int xBoden = 0;
	private int score = 0;
	private int highScore = 0;
	boolean verloren = false;
	private Image flappyBirdImage = Toolkit.getDefaultToolkit().createImage("D:\\Java\\FlappyBird\\src\\yellowbird-upflap.jpg");
	private Rectangle vogelRechteck;
	private Rectangle[] oberesRohrViereck = new Rectangle[4];
	private Rectangle[] unteresRohrViereck = new Rectangle[4];
	private Color fontColor;
	private int xBoden2 = 0;
	public FlappyBirdGui() {
		randomNumberGen = new Random();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400,400);
		vogelRechteck = new Rectangle(0,0,0,0);
		for(int i = 0; i < oberesRohrViereck.length; i++) {
			oberesRohrViereck[i] = new Rectangle(0,0,0,0);
			unteresRohrViereck[i] = new Rectangle(0,0,0,0);
		}
		setLocation((int)(Toolkit.getDefaultToolkit().getScreenSize().getWidth()/3),(int)(Toolkit.getDefaultToolkit().getScreenSize().getHeight()/3));
		setTitle("Flappy Bird");
		setResizable(false);
		setVisible(true);
		steuerung = new Steuerung(this);
		xBoden2 = 799;
		setFont(new Font("Arial", Font.BOLD, 16));
		fontColor = Color.black;
		generiereHintergrund();
	}
	private void generiereHintergrund() {
		if(randomNumberGen.nextInt(3)+1 == 2) {
			hintergrund = hintergrundNacht;
			oberesRohrBild = oberesRohrBildNacht;
			unteresRohrBild = unteresRohrBildNacht;
			boden = bodenNacht;
			boden2 = bodenNacht;
			fontColor = Color.white;
		} else {
			fontColor = Color.black;
			boden = bodenTag;
			boden2 = bodenTag;
			hintergrund = hintergrundTag;
			oberesRohrBild = oberesRohrBildTag;
			unteresRohrBild = unteresRohrBildTag;
		}
	}
	public void verloren() {
		verloren = true;
	}
	public void updateHighscore(int highscore) {
		this.highScore = highscore;
	}
	public void updateScore(int score) {
		this.score = score;
	}
	public void updateVierecke(Rohr[] rohre, Rectangle vogel, Image vogelImage) {
		this.vogelRechteck = vogel;
		flappyBirdImage = vogelImage;
		for(int i = 0; i < oberesRohrViereck.length; i++) {
			oberesRohrViereck[i] = rohre[i].gibOberesRohr();
			unteresRohrViereck[i] = rohre[i].gibUnteresRohr();
		}
	}
	public static void main(String[] args) {
		FlappyBirdGui gui = new FlappyBirdGui();
	}
	public void bodenBewegung() {
		counter++;
		if(counter % 4 == 0) {
			xBoden--;
			xBoden2--;
			if(xBoden == 0-boden.getWidth(null)) {
			xBoden = xBoden2+ boden.getWidth(null);
			} else {
				if(xBoden2 == 0-boden.getWidth(null)) {
					xBoden2 = xBoden + boden.getWidth(null);
				}
			}	
		}
	}
	public void paint(Graphics g) {
		doubleBufferImg = createImage(getWidth(), getHeight());
		doubleBufferGraphics = doubleBufferImg.getGraphics();
		paintComponent(doubleBufferGraphics);
		g.drawImage(doubleBufferImg, 0, 0, this);
	}
	public void restarte(Vogel flappyBird) {
		score = 0;
		verloren = false;
		generiereHintergrund();
	}
	private void paintComponent(Graphics g) {
		g.drawImage(hintergrund, 0, -40, getWidth(), getHeight(),this);
		//g.fillRect(flappyBird.getVogelViereck().x, flappyBird.getVogelViereck().y, flappyBird.getVogelViereck().height, flappyBird.getVogelViereck().width); //vogel
		for(int i = 0; i < oberesRohrViereck.length; i++) {
			g.drawImage(oberesRohrBild,unteresRohrViereck[i].x, (-1* oberesRohrBild.getHeight(this))+oberesRohrViereck[i].height, oberesRohrViereck[i].width, unteresRohrBild.getHeight(this), this);
			g.drawImage(unteresRohrBild,unteresRohrViereck[i].x, unteresRohrViereck[i].y, unteresRohrViereck[i].width, unteresRohrBild.getHeight(this), this);
		}
		g.drawImage(flappyBirdImage, vogelRechteck.x, vogelRechteck.y, vogelRechteck.width+4, vogelRechteck.height-2, this);
		g.drawImage(boden, xBoden, 350, boden.getWidth(this), getHeight()-350, this);
		g.drawImage(boden2, xBoden2, 350, boden.getWidth(this), getHeight()-350, this);
		g.setColor(fontColor);
		g.setFont(new Font("Arial", Font.BOLD, 16));
		g.drawString("Score: "+Integer.toString(score), 15, 50);
		g.drawString("Highscore: "+Integer.toString(highScore), 15, 80);
		if(verloren) {
			g.setFont(new Font("Times New Roman", Font.BOLD, 21));
			g.setColor(Color.red);
			g.drawString("Score: "+score, 145, 185);
			g.drawString("Press R to restart", 110, 225);
		}
	}

}
