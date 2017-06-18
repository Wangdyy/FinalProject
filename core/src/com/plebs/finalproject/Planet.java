package com.plebs.finalproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

public class Planet {
	private String name;
	private Texture texture;
	private int x,y;
	private String type;
	private Player playervs = null;
	private boolean makeminigame = false;
	private boolean gameRun;
	
	
	//minigames
	private Remember remember;
	
	public Planet(String info) throws IOException{
		String[] string = info.split(" "); // x , y, file name for texture
		x = Integer.parseInt(string[0]);
		y = Integer.parseInt(string[1]);
		makeName();
		type = "get3";
		texture = new Texture(string[2]);
		playervs = null;
		
		gameRun = false;
	}
	public void makeName() throws IOException{
		FileHandle file = Gdx.files.internal("names.txt");
		BufferedReader reader = new BufferedReader(file.reader());
		ArrayList<String> lines = new ArrayList<String>();
		String line = reader.readLine();
		while( line != null ) {
			lines.add(line);
			line = reader.readLine();
		}
		Random r = new Random();
		name = lines.get(r.nextInt(lines.size()));
	}
	public void drawPlanet(Batch batch){
		batch.draw(texture, x, y);
	}
	public void action(Player player, ArrayList<Planet>planets,Batch batch){
		if (type.equals("get3")){
			player.changeStash(3);
		}
		else if (type.equals("remove3")){
			player.changeStash(-3);
		}
		else if(type.equals("teleport")){
			player.setPosition(planets.get(0).getPosition()[0],planets.get(1).getPosition()[1]);
		}
		else if(type.equals("missturn")){
			player.setturn(true);
		}
		else if(type.equals("minigame")){
			    
			if (gameRun == false){
				makeminigame = true;
			}
			if (makeminigame = true){
				//make minigame
			}
			else{
				playminigame("sequence",batch);
			}
			/*
			if (playervs == null){
				playervs = choosePlayer();
				makeminigame = true;
			}
			else{
				Random r = new Random();
				String[]minigames = {"sequence"};
				String game = minigames[r.nextInt(minigames.length)];
				playminigame(game, batch);
				
			}
			*/
		}
	}
	//public choosePlayer(ArrayList<Player> players, Player mPlayer, 
	public int[] getPosition(){
		return new int[] {x,y};
	}
	//public Player choosePlayer(){
		//if player rectangle is chosen, then it is the player it goes against
		//players =
	public void playminigame(String nGame, Batch batch){
		//1v1 games
		if(nGame.equals("sequence")){
			if (makeminigame == true){
				makeminigame = false;
				//remember = new Remember(new Player(1, new Texture("badlogic.jpg")),new Player(2,new Texture("badlogic.jpg")));
				remember.setStartTime(System.currentTimeMillis());
			}
			else{
				remember.render(batch);;
			}
		}
	}
	
}