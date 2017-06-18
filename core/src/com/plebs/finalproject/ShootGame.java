package com.plebs.finalproject;

import java.awt.geom.Point2D;
import java.util.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class ShootGame {
	private boolean game;
	private boolean minigamerun;
	private boolean addstash; //determines if the star stashes have already been added
	
	//textures
	private Texture background;
	private Texture target;
	private Texture explosion;
	//cursors
	private Texture p1Crosshair;
	private Texture p2Crosshair;
	private Texture p1CrosshairHit;
	private Texture p2CrosshairHit;
	
	private Player player1;
	private Player player2;
	private Player winner;
	private Player loser;
	
	private ArrayList<ShootPlanet>targets;
	private Random rand;
	
	private Point2D p1Cursor;
	private Point2D p2Cursor;
	
	private boolean p1Shoot; //keeps track of if players are shooting
	private boolean p2Shoot;
	
	private int p1Score;
	private int p2Score;
	
	//time
	private int seconds;
	private long startTime; //time when this was made
	private long p1BulletTime;
	private long p2BulletTime;
	
	private Timer makeTargetTime;
	
	//font things
	BitmapFont font;
	BitmapFont countFont; //font for countdown
	FreeTypeFontGenerator generator;
	FreeTypeFontParameter parameter;

	public ShootGame(Player p1, Player p2){
		game = false;
		minigamerun = true;
		addstash = false;
		
		background = new Texture("shootbackground.jpg");
		target = new Texture("earth.png");
		p1Crosshair = new Texture("p1cursor.png");
		p1CrosshairHit = new Texture("p1cursorhit.png");
		p2Crosshair = new Texture("p2cursor.png");
		p2CrosshairHit = new Texture("p2cursorhit.png");
		explosion = new Texture("explosion_transparent.png");
		
		player1 = p1;
		player2 = p2;
		targets = new ArrayList<ShootPlanet>();
		rand = new Random();
		
		p1Cursor = new Point2D.Double(750,300);
		p2Cursor = new Point2D.Double(750,300);
		
		p1Shoot = false;
		p2Shoot = false;
		
		p1Score = 0;
		p2Score = 0;
		
		startTime = System.currentTimeMillis();
		p1BulletTime = -1000;
		p2BulletTime = -1000;
		
		makeTargetTime = new Timer();
		makeTargetTime.schedule(new TimerTask(){
			@Override
			public void run(){
				makeTargets();
			}
		}
		, 0,10000);
		
		//fonts
		font = new BitmapFont();
		countFont = new BitmapFont();
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("space.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 72;
		parameter.borderColor = Color.BLACK;
		font = generator.generateFont(parameter);
		parameter.size = 600;
		countFont = generator.generateFont(parameter);
		generator.dispose();
	}
	public void timer(){
		//timer for game
		if ((int)((System.currentTimeMillis() - startTime)/1000) > seconds){
			seconds += 1;
		}
	}
	public void makeTargets(){
		//gets called every ten seconds
		//makes list of new targets to hit
		targets = new ArrayList<ShootPlanet>();
		for(int i = 0; i <100; i++){
			targets.add(new ShootPlanet(new Point2D.Double((((rand.nextInt(37))+1)*40)+20,(((rand.nextInt(10))+11)*40)+20)));
		}
	}
	public void drawTargets(Batch batch){
		if (seconds%10 != 9){
			for(ShootPlanet t: targets){
				if (t.getFrame() == 0){
					batch.draw(target,(int)(t.getPoint().getX()-16),(int)(t.getPoint().getY()-16));
				}
				else{
					TextureRegion explodeTex = new TextureRegion(explosion,((t.getFrame()-1)%4)*32,(((int)((t.getFrame()-1)/4))+1)*32,32,32);
					batch.draw(explodeTex,(int)(t.getPoint().getX()-16),(int)(t.getPoint().getY()-16));
				}
				
			}
		}
		
	}
	public void moveCursor(){
		//moves cursor for both players
		//player 1
		if (Gdx.input.isKeyPressed(Keys.S) == true){
			p1Cursor.setLocation(p1Cursor.getX(), Math.max(0,p1Cursor.getY() - 5));
		}
		if (Gdx.input.isKeyPressed(Keys.W) == true){
			p1Cursor.setLocation(p1Cursor.getX(), Math.min(900,p1Cursor.getY() + 5));
		}
		if (Gdx.input.isKeyPressed(Keys.A) == true){
			p1Cursor.setLocation(Math.max(0,p1Cursor.getX()-5),p1Cursor.getY());
		}
		if (Gdx.input.isKeyPressed(Keys.D) == true){
			p1Cursor.setLocation(Math.min(1500,p1Cursor.getX()+5),p1Cursor.getY());
		}
		//player 2
		if (Gdx.input.isKeyPressed(Keys.DOWN) == true){
			p2Cursor.setLocation(p2Cursor.getX(), Math.max(0,p2Cursor.getY() - 5));
		}
		if (Gdx.input.isKeyPressed(Keys.UP) == true){
			p2Cursor.setLocation(p2Cursor.getX(), Math.min(900,p2Cursor.getY() + 5));
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT) == true){
			p2Cursor.setLocation(Math.max(0,p2Cursor.getX()-5),p2Cursor.getY());
		}
		if (Gdx.input.isKeyPressed(Keys.RIGHT) == true){
			p2Cursor.setLocation(Math.min(1500,p2Cursor.getX()+5),p2Cursor.getY());
		}
	}
	public void shootCursor(){
		//shoots the cursor
		p1Shoot = Gdx.input.isKeyJustPressed(Keys.SHIFT_LEFT);
		p2Shoot = Gdx.input.isKeyJustPressed(Keys.SHIFT_RIGHT);
		if (p1Shoot == true){
			p1BulletTime = System.currentTimeMillis();
		}
		if (p2Shoot == true){
			p2BulletTime = System.currentTimeMillis();
		} 
		
	}
	public void drawCursor(Batch batch) throws InterruptedException{
		//draws cursors for both
		if (p1Shoot == false && (System.currentTimeMillis() - p1BulletTime) >100){
			batch.draw(p1Crosshair,(int)(p1Cursor.getX()-16),(int)(p1Cursor.getY()-16));
		}
		else{
			batch.draw(p1CrosshairHit,(int)(p1Cursor.getX()-16),(int)(p1Cursor.getY()-16));
		}
		if (p2Shoot == false && (System.currentTimeMillis() - p2BulletTime) >100){
			batch.draw(p2Crosshair,(int)(p2Cursor.getX()-16),(int)(p2Cursor.getY()-16));
		}
		else{
			batch.draw(p2CrosshairHit,(int)(p2Cursor.getX()-16),(int)(p2Cursor.getY()-16));
			
		}
		
	}
	public void hitTarget(){
		//sets score 
		Iterator<ShootPlanet> iter = targets.iterator();
		while (iter.hasNext()) {
		    ShootPlanet point = iter.next();
		    point.hit(p1Cursor, p1Shoot);
		    if (point.getHitStatus() == true){ 
		    	if (point.getFrame() == 0 && System.currentTimeMillis() - point.getHitTime() == 0){
		    		p1Score += 1;
		    	}
		    	continue;
		    }
		    point.hit(p2Cursor, p2Shoot);
		    if (point.getHitStatus() == true && System.currentTimeMillis() - point.getHitTime() == 0){ 
		    	if (point.getFrame() == 0){
		    		p2Score += 1;
		    	}
		    }
		    if (point.getFrame() == 24){
		    	iter.remove();
		    }
		}
	}
	public void getWinner(){
		if (p1Score > p2Score){
			winner = player1;
			loser = player2;
		}
		else if (p2Score > p1Score){
			winner = player2;
			loser = player1;
		}
		else{
			winner = null;
			loser = null;
		}
	}
	public boolean getMinigameStatus(){
		return minigamerun;
	}
	public void update(){
		timer();
		if (seconds > 4){
			game = true;
		}
		if (seconds >= 125){
			game = false;
			if (addstash == false){
				winner.changeStash(3);
				loser.changeStash(-3);
				addstash = true;
			}
			getWinner();
			if (Gdx.input.isKeyPressed(Keys.ENTER) == true){
				minigamerun = false;
			}
		}
		if (game == true){
			moveCursor();
			shootCursor();
			hitTarget();
		}
	}
	public void render(Batch batch) throws InterruptedException{
		update();
		batch.draw(background,0,0);
		if (game == true){
			drawTargets(batch);
			drawCursor(batch);
			font.draw(batch, ""+p1Score, 10, 50);
			font.draw(batch, ""+p2Score, 1450,50);
			font.draw(batch, ""+(124-seconds),760,50);
		}
		if (seconds <= 4){
			countFont.draw(batch, ""+(5-seconds),760,850);
		}
		if (seconds >125){
			font.draw(batch,"TIME'S UP!!!",400,500);
			if (winner == null){
				font.draw(batch, "IT'S A TIE",400,400);
			}
			else{
				font.draw(batch,winner.getName() + " WON!!",400,415);
				font.draw(batch, winner.getName()+" -> + 3 stars",400,340);
				font.draw(batch, loser.getName()+" -> - 3 stars",400,270);
			}
			
		}
	}
}
class ShootPlanet{
	private Point2D point;
	
	private int nFrame; //frame that it is on
	
	private boolean isHit;
	private long hitTime;
	
	public ShootPlanet(Point2D p){
		nFrame = 0;
		point = p;
		isHit = false;
		hitTime = -1;
	}
	public void hit(Point2D c, boolean sCursor){
		if (isHit == true){
			if (System.currentTimeMillis() - hitTime >= 50){
				nFrame +=1;
				hitTime = System.currentTimeMillis();
			}
		}
		if (point.distance(c) <= 16 && sCursor == true && isHit == false){
			isHit = true;
			hitTime = System.currentTimeMillis();
			
		}
	}
	public boolean getHitStatus(){
		return isHit;
	}
	public Point2D getPoint(){
		return point;
	}
	public int getFrame(){
		return nFrame;
	}
	public long getHitTime(){
		return hitTime;
	}
}
