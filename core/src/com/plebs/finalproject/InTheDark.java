//a minigame, where a single player tries to collect as many items as possible in a dark map where
//only the immediate area of the player is visible, and an enemy constantly chases them
//items are auto-generated whenever a player collects one (same amount on screen at all times)

//TO DO: HEALTH -> heartts, COUNTDOWN, ITEMCOUNT STATS, SPRITE MECH, PRETTY 
package com.plebs.finalproject;

import java.awt.geom.Point2D;
import java.util.*;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class InTheDark{
	Random rand = new Random();
	private boolean gamerun; //determines if the minigame is running
	private boolean addstash; //determines if the star stashes have already been added or not
	
	//Sprites and Textures
	private Player user;
	private Sprite userSprite;
	private Texture shadow = new Texture("inthedark/shadow.png"); 
	private Texture map = new Texture("inthedark/dungeon.jpg");
	private Sprite enemy = new Sprite(new Texture("inthedark/enemy.png")); 
	private Texture item = new Texture("inthedark/item.png");
	
	//holds information
	private ArrayList<Rectangle> itemList = new ArrayList<Rectangle>();
	int itemsCollected = 0;
	private long startTime; //time when class is first created
	
	//counter
	private int seconds;
	
	//font stuff
	BitmapFont font75;
	FreeTypeFontGenerator generator;
	FreeTypeFontParameter parameter;
	
	public InTheDark(Player p1){
		gamerun = true;
		addstash = false;
		user = p1;
		userSprite = p1.getSprite();
		user.setPosition(750,450);
		for(int i = 0; i<15; i++){ //loops to create item locations on map
			int x = rand.nextInt(1400);
			int y = rand.nextInt(800);
			itemList.add(new Rectangle(x, y, item.getWidth(), item.getHeight()));
		}
		seconds = 0;
		enemy.setOriginCenter();
		enemy.setPosition(-10, -10);
		setStartTime(System.currentTimeMillis());
		
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Galaxy Force.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = 120; //font size 120 pixels
		parameter.size = 75;
		font75 = generator.generateFont(parameter);
		font75.setColor(Color.WHITE);
		generator.dispose(); //disposes to avoid memory leaks
	}
	public boolean getGameRunning(){
		//gets the status of the game itself and determines when to exit it
		return gamerun;
	}
	public boolean getRunning(){ //if player still has time
		if(seconds > 64 || seconds < 4 ){
			return false;
		}
		return true;
	}
	public void movePlayer(){ //needs keyboard input 
		//player will not be able to move off screen
		if(Gdx.input.isKeyPressed(Keys.LEFT) && userSprite.getX()>0){
			user.setPosition(userSprite.getX() - 12, userSprite.getY());
			user.setDirect(1);
			user.addFrame();
		} 
		else if(Gdx.input.isKeyPressed(Keys.RIGHT) && userSprite.getX()<(Gdx.graphics.getWidth() - userSprite.getWidth())){
			user.setPosition(userSprite.getX() + 12, userSprite.getY());
			user.setDirect(2);
			user.addFrame();
		}
		if(Gdx.input.isKeyPressed(Keys.DOWN) && userSprite.getY()>12){
			user.setPosition(userSprite.getX(), userSprite.getY() - 12);
			user.setDirect(0);
			user.addFrame();
		} 
		else if(Gdx.input.isKeyPressed(Keys.UP) && userSprite.getY()<(Gdx.graphics.getHeight() - userSprite.getHeight())){
			user.setPosition(userSprite.getX(), userSprite.getY() + 12);
			user.setDirect(3);
			user.addFrame();
		}
	}
	
	public void update(){
		timer();
		if (getRunning() == true){
			movePlayer();
			collect();
			moveEnemy();
			getHit();
			userSprite = user.getSprite();
		}
		else{
			if (seconds > 64){
				if (addstash == false){
					user.changeStash(itemsCollected);
					addstash = true;
				}
				if (Gdx.input.isKeyPressed(Keys.ENTER) == true){
					gamerun = false;
				}
			}
		}
	}
	
	public void render(SpriteBatch batch, ShapeRenderer shaperenderer){
		update();
		batch.draw(map, 0, 0);
		//shaperenderer.setColor(Color.RED);
		//shaperenderer.rect(1180, 880, health, 40);
		if (getRunning() == true){
			for(Rectangle r : itemList){
				batch.draw(item, r.x, r.y);
			}
			user.render(batch);
			enemy.draw(batch);
			batch.draw(shadow, userSprite.getX() + userSprite.getWidth()/2 - 1650, userSprite.getY() + userSprite.getHeight()/2 - 1000);
			//positions picture so that player texture drawn to center of transparent circle
			
			//lets player know about their stats
			font75.draw(batch, "" + (64 - seconds), 760, 700);
			//lets player know about how much they collected
			font75.draw(batch, ""+ itemsCollected, 0, 700);
		}
		else{
			if (seconds < 4){
				font75.draw(batch, "" + (4-seconds), 750, 450);
			}
			else{
				font75.draw(batch, "You won " + itemsCollected + " stars!",300,500);
				font75.draw(batch, "<ENTER> to exit", 0, 50);
			}
		}
	}
	
	public void moveEnemy(){ //moves according to location of player
		enemy.setPosition((float)(enemy.getX() + (userSprite.getX() - enemy.getX())*0.01), (float)(enemy.getY() + (userSprite.getY() - enemy.getY())*0.01));
		enemy.rotate((float)(12));
		
	}
	public void getHit(){ //detects collision between player and enemy, reduces health 
		if((int)((System.currentTimeMillis() - startTime)/200) > seconds*5){ //checks every 0.2 seconds so player doesn't die too fast
			if(userSprite.getBoundingRectangle().overlaps(enemy.getBoundingRectangle())){ //player sprite collides with item
				itemsCollected = Math.max(0,  itemsCollected - (rand.nextInt(5)+1));
				enemy.setPosition(-10,-10);
			}
		}
	}
	public void collect(){ //checks if player is touching items
		for(int i = 0; i<itemList.size(); i++){
			if(userSprite.getBoundingRectangle().overlaps(itemList.get(i))){ //player sprite collides with item
				itemsCollected += 1;
				itemList.remove(i); //removes item from list, will not check or render it anymore 
				itemList.add(new Rectangle(rand.nextInt(1400), rand.nextInt(800),item.getWidth(),item.getHeight())); //generates new item
			}
		}
	}
	public void timer(){ //times game
		if((int)((System.currentTimeMillis() - startTime)/1000) > seconds){
			seconds += 1;
		}
	}	
	
	public void setStartTime(long time){
		startTime = time;
	}
	
	public void showTime(){
		//System.out.println((int)((System.currentTimeMillis() - startTime)/1000));
	}
}