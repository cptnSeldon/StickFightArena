package view.menu;


import org.dyn4j.dynamics.BodyFixture;
import org.dyn4j.dynamics.World;
import org.dyn4j.geometry.Geometry;
import org.dyn4j.geometry.TextShape;
import view.BodyRenderer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameMenu {


	private World world;
	private List<BodyRenderer> menuObjects;
	private List<GameMenuButton> menuButtons;
		//start
	private IAction startAction;
		//pause
	private IAction continueAction;
		//quit
	private IAction quitAction;

	public GameMenu(World world) {

		this.menuObjects = new ArrayList<>();
		this.menuButtons = new ArrayList<>();
		this.world = world;
	}

	private void addObjectToGame(BodyRenderer br) {

		br.setActive(false);
		menuObjects.add(br);
		world.addBody(br);
	}

	private void addGameMenuButtonToGame(GameMenuButton gmb, IAction action) {

		menuButtons.add(gmb);
		gmb.setAction(action);

		for(BodyRenderer br : gmb.getBodies()){
			addObjectToGame(br);
		}
	}

	//MAIN MENU
	public void showStartMenu () {
		BodyRenderer background = new BodyRenderer(new Color(0,0,0,95));
		BodyRenderer messageStart = new BodyRenderer(new Color(255,0,0));

		background.addFixture(new BodyFixture(Geometry.createRectangle(100,40)));
		messageStart.addFixture(new BodyFixture(new TextShape(11,7,90,"START")));

		addObjectToGame(background);
		addObjectToGame(messageStart);
		//buttons
		GameMenuButton startGame = new GameMenuButton("Start", 11, 9);
		GameMenuButton quitGame = new GameMenuButton("Quit", 11, 11);

		addGameMenuButtonToGame(startGame, startAction);
		addGameMenuButtonToGame(quitGame, quitAction);
	}

	//PAUSE MENU
	public void showPause() {

		BodyRenderer background = new BodyRenderer(new Color(0,0,0,95));
		BodyRenderer messagePause = new BodyRenderer(new Color(255,0,0));

		background.addFixture(new BodyFixture(Geometry.createRectangle(100,40)));
		messagePause.addFixture(new BodyFixture(new TextShape(11,7,90,"PAUSE")));

		addObjectToGame(background);
		addObjectToGame(messagePause);
		//buttons
		GameMenuButton continueGame = new GameMenuButton("Continue", 11, 9);
		GameMenuButton quitGame = new GameMenuButton("Quit", 11, 11);

		addGameMenuButtonToGame(continueGame, continueAction);
		addGameMenuButtonToGame(quitGame, quitAction);
	}

	public void clickOnMenu(double x, double y) {

		for(GameMenuButton gmb : menuButtons){

			if(gmb.isTouched(x,y)){
				System.out.println(gmb + " is clicked");
				gmb.execute();
			}
		}
	}

	public void setStartAction (IAction action) { this.startAction = action; }

	public void setContinueAction (IAction action) { this.continueAction = action; }

	public void setQuitAction (IAction action) { this.quitAction = action; }

	public void clear() {

		for (BodyRenderer br : menuObjects) {
			world.removeBody(br);
		}

		menuButtons.clear();
	}
}
