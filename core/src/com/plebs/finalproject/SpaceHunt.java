package com.plebs.finalproject;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.*;
import java.io.*;

public class SpaceHunt extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	ArrayList<Planet>allPlanets; // list of planets
	Scanner inFile;
	boolean storyline; //flag to play the story line
	int storynum; //part of the story line
	InTheDark dark;//minigame
	boolean minigamecreate = false; //flag to determine when to play the minigame
	
	private Texture board; //board of the game
	
	@Override
	public void create(){
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
		board = new Texture("board.png");
		
		FileHandle file = Gdx.files.internal("spotsinfo.txt");
		dark= new InTheDark(new Player(new TextureRegion(img)));
		BufferedReader reader = new BufferedReader(file.reader());
		allPlanets = new ArrayList<Planet>();
		String line = null;
		storyline = true;
		storynum = 0;
		
		try {
			line = reader.readLine();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while( line != null ) {
			try {
				allPlanets.add(new Planet(line));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				line = reader.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public Texture storyboard(){
		if (Gdx.input.isKeyJustPressed(Keys.ENTER) == true){
			storynum += 1; //goes to next line in story
		}
		if (storynum >= 5){
			storyline = false;
			minigamecreate = true;
			return null;
			
		}
		else{
			Texture[]textures = {new Texture("story1.png"), new Texture("story2.png"), new Texture("story3.png"), new Texture("story4.png"), new Texture("story5.png")};
			
			return textures[storynum];
		}
	}
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		/*
		Texture storytex = storyboard();
		if (minigamecreate == true){
			sgame= new ShootGame(new Player(1, new Texture("badlogic.jpg")),new Player(2,new Texture("badlogic.jpg")));
			//sgame.setStartTime(System.currentTimeMillis());
			minigamecreate = false;
		}
		if (storyline == false){
			batch.draw(board, 0, 0);
			for (Planet plan: allPlanets){
				plan.drawPlanet(batch);
			}
			sgame.render(batch);
			storynum = 0;
		}
		
		if (storyline == true){
			batch.draw(storytex, 0, 0);
		}
		
		if (sgame.getMinigameStatus() == true){
			try {
				sgame.render(batch);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		if (dark.getGameRunning() == true){
			dark.render(batch, null);
		}
		batch.end();
		
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
