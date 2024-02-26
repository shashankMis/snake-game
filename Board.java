package snakegame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.*;
import java.awt.event.*;

public class Board extends JPanel implements ActionListener {
	
	 private Image apple;
	 private Image dot;
	 private Image head;
	 
	 private final int ALL_DOTS=900;//Hmlog visible ke liye size 300,300 diye h mtlb ye maximum 900 hi le skta h
	 
	 private final int DOT_SIZE=10;// bydefault width le rhe image ka
	 
	 private final int RANDOM_POSITION=29;// ye apple ko random position dene ka variable h
	 
	 private int apple_x;// ye x ka position btega apple ke liye
	 private int apple_y;// ye y ka position btega apple ke liye
	 
	 private final int x[]=new int[ALL_DOTS];
	 private final int y[]=new int[ALL_DOTS];
	 
	 private boolean leftDirection=false;// ye snake ka left direction ko band krega 
	 private boolean rightDirection=true;// ye snake ko right direction se start krega krega 
	 private boolean upDirection=false;
	 private boolean downDirection=false;
	 
	 private int score=0;// ye score dikhaenge
	 private boolean inGame=true;// ye btaega ki game start hi h
	 
	 private int dots;
	 private Timer timer;

	 
	Board() {
		addKeyListener(new TAdapter());// ye  keyboard ka action perform show kraega
		
		setBackground(Color.BLACK);
		setPreferredSize(new Dimension(300,300));
		setFocusable(true);
		
		loadImage();
		initGame();// game ko initialize krne wala method h
	}

	public void loadImage() {
		ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/apple.png"));
		apple=i1.getImage();
		
		ImageIcon i2 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/dot.png"));
		dot=i2.getImage();
		
		ImageIcon i3 = new ImageIcon(ClassLoader.getSystemResource("snakegame/icons/head.png"));
		head=i3.getImage(); 
		
	}
	
	public void initGame() {
		dots=3;
		
		for(int i=0;i<dots;i++)
		{
			y[i]=50;// sare icon ka startinh point y axi se same hi rhega
			
			x[i]=50-i*DOT_SIZE;// ye btaega ki kitna distance ke baad 2 icon attach hoga 1 ke saath
		}
		
		locateApple();// apple ko generate krega
		
		 timer= new Timer(140,this);// ye time time pe snake ka size ko badhaega mtlb snake start hoga 
		timer.start();
	}
	
	public void locateApple()
	{
		int r=(int)(Math.random()*RANDOM_POSITION);// ye random generate krega apple ko x axis se.
		apple_x=r*DOT_SIZE;
		
		r=(int)(Math.random()*RANDOM_POSITION);// ye random generate krega apple ko y axis se.
		apple_y=r*DOT_SIZE;
		
	}
	
	 // Images ko paint kr rhe Frame me:Graphic class ka use krte h jo java.awt me hota h
	
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		
		draw(g);
	}

	public void draw(Graphics g) {
		if(inGame)
		{
			g. drawImage(apple,apple_x,apple_y,this);// ye random position pe apple generate hua wala ko display kraega
			
			for(int i=0;i<dots;i++)
			{
				if(i==0)
				{
					g.drawImage(head,x[i],y[i],this);
				}
				else {
					g.drawImage(dot,x[i],y[i],this);
				}
			}
			Toolkit.getDefaultToolkit().sync();// ye default se initialize krega
		}
		else {
			gameOver(g);
		}
		
	}
	
	public void gameOver(Graphics g)
	{
		String msg="Game Over";
		
		Font font = new Font("SAN_SERIF",Font.BOLD,14);
		FontMetrics metrices =getFontMetrics(font);
		
		g.setColor(Color.WHITE);
		g.setFont(font);
		
		g.drawString(msg,(300-metrices.stringWidth(msg))/2,300/2);
		
		 String text = "Score: " + score;
	        g.drawString(text, 50, 100);
	}
	

	public void actionPerformed(ActionEvent ae) {
		if(inGame)
		{
			checkApple();// ye apple ko detect krne ka method h
			checkCollision(); //ye colllision ka method h
			// ActionListener Interface ka method h.
			move();// move krane ke liye
			
			repaint(); // ye screen ko refresh krega same as pack function
		}	
		
	}
	public void move()
	{
		for(int i=dots;i>0;i--)// ye loop head ke piche wale image ko 1-1 position aage la rha 
		{
			x[i]=x[i-1];
			y[i]=y[i-1];
		}
		if(leftDirection)// ye dot size ko decrease krega
		{
			x[0]=x[0]-DOT_SIZE;
		}
		if(rightDirection)
		{
			x[0]=x[0]+DOT_SIZE;
		}
		if(upDirection)
		{
			y[0]=y[0]-DOT_SIZE;
		}
		if(downDirection) {
			y[0]=y[0]+DOT_SIZE; 
		}
		
	}
	
	public void checkApple()// ye apple ko check krega or apple & snake ka head ka cordinate same hoga to snake ka size ko +1 kr denge 
	{
		
		if((x[0]==apple_x) && (y[0]==apple_y))
		{
			score++;
			dots++;
			locateApple();// ye apple khane ke baad fr se generate krega apple ko random jagah pe
		}
	}
	
	public void checkCollision()// game over ka concept h jo snake aapas me takra jata h tb
	{
		for(int i=dots;i>0;i--)
		{
			if((i>4) && (x[0]==x[i]) && (y[0]==y[i]))//mtlb head kisi v apna element se touch hua tb
			{
				inGame=false;
			}
		}
		// ye boundaries se check krega q ki boundry ka 
		if(y[0]>=300)
		{
			inGame=false;
		}
		if(x[0]>=300)
		{
			inGame=false;
		}
		if(y[0]<0)
		{
			inGame=false;
		}
		if(x[0]<0)
		{
			inGame=false;
		}
		if(!inGame)
		{
			timer.stop();
		}
		
	}
	
	//Key ke Action ko capture krne ke liye "TAdapter" class hoti h jo extends krti h "KeyAdapter" ko or isko Inner Class Bnae h yha pe  
	public class TAdapter extends KeyAdapter // yha pe keyPressed function hota h jo key ke basis pe work krta h. Isko initialize kraenge constructor me addKeyListner(new TAdapter()) ye ActionListner ka function h
	{	
		public void keyPressed(KeyEvent e)
		{
			int key=e.getKeyCode();
			
			if(key==KeyEvent.VK_LEFT && (!rightDirection))// right direction me h to ekdm se left me na move kre isliye 
			{
				leftDirection=true;
				upDirection=false;
				downDirection=false;
			}
			
			if(key==KeyEvent.VK_RIGHT && (!leftDirection))
			{
				rightDirection=true;
				upDirection=false;
				downDirection=false;
			}
			
			if(key==KeyEvent.VK_UP && (!downDirection))
			{
				upDirection=true;
				leftDirection=false;
				rightDirection=false;
			}
			
			if(key==KeyEvent.VK_DOWN && (!upDirection))
			{
				downDirection=true;
				leftDirection=false;
				rightDirection=false;
			}
			
		}
		
	}
}
