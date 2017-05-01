//a minigame, where a single player tries to collect as many items as possible in a dark map where
//only the immediate area of the player is visible, and an enemy constantly chases them
//items are auto-generated whenever a player collects one (same amount on screen at all times)

package com.qi.finalproject;

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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class InTheDark{
	
	private Player player;
	private Sprite playSprite;
	private Sprite enemy;
	private SpriteBatch batch;
	private Texture shadow = new Texture("black.png");
	
	public InTheDark(Player p1, Texture enem){
		player = p1;
		playSprite = player.getSprite();
		player.setPosition(10,10);
		enemy = new Sprite(enem);
	}
	public void movePlayer(){ //needs keyboard input 
		//player will not be able to move off screen
		if(Gdx.input.isKeyPressed(Keys.LEFT) && playSprite.getX()>0){
			
		} 
		else if(Gdx.input.isKeyPressed(Keys.RIGHT) && playSprite.getX()<(Gdx.graphics.getWidth() - playSprite.getWidth())){
			
		}
	}
	public void render(){
		batch.begin();
		player.getSprite().draw(batch);
		enemy.draw(batch);
		batch.draw(shadow, player.getSprite().getX() - 1550, player.getSprite().getY() + 450); //player at centre of transparent circle
		batch.end();
	}
	public void moveEnemy(){ //moves according to location of player
		enemy.setPosition((float)(enemy.getX() + (player.getSprite().getX() - enemy.getX())*0.1), (float)(enemy.getY() + (player.getSprite().getY() - enemy.getY())*0.1));
	}
}