package com.salvador.towers;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.salvador.towers.Tool.Move;
import com.salvador.towers.Tool.Values;

import java.util.ArrayList;

import static com.salvador.towers.Tool.Values.SCREEN_HIGHT;

public class Main extends ApplicationAdapter {

	private Stage stage;
	private ExtendViewport viewport;

	private StageUI stageUI;
	private StageBase stageBase;

	private OrthographicCamera cam;

	private ArrayList<Move> moves;

	private int nMoves;
	private int nDisc;

	private boolean isPlaying = false;

	@Override
	public void create() {

		//Setup UI
		cam = new OrthographicCamera(Values.SCREEN_WIDTH, SCREEN_HIGHT);
		viewport = new ExtendViewport(Values.SCREEN_WIDTH, SCREEN_HIGHT, cam);

		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		cam.update();

		moves = new ArrayList<>();
		stage = new Stage(viewport);
		stage.setDebugAll(true);
		Gdx.input.setInputProcessor(stage);

		stageUI = new StageUI(this, stage);
		stageBase = new StageBase(this,stage);

		//Setup Default Tower of Hanoi
		nDisc = 3;
		stageBase.createDiscs(nDisc);
		generateMoves(nDisc);
	}

	public void generateMoves(int n) {
		moves.clear();
		System.out.println("Generando movimientos : ");
		towerOfHanoi(n, 1, 3, 2);
		System.out.println("Movimientos generados.");

		nMoves = moves.size();
	}

	void towerOfHanoi(int n, int from, int to, int aux) {
		if (n == 1) {
			System.out.println("Mover disco 1 de la pila " + from + " a la pila " + to);
			moves.add(new Move(from, to));
			return;
		}
		towerOfHanoi(n - 1, from, aux, to);
		System.out.println("Mover disco " + n + " de la pila " + from + " a la pila " + to);
		moves.add(new Move(from, to));
		towerOfHanoi(n - 1, aux, to, from);
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		stage.getViewport().update(width, height, true);
	}

	int i = 0;

	@Override
	public void render() {
		stage.act();
		stage.draw();

		if (Gdx.input.isKeyJustPressed(Input.Keys.A)) {

			nextMove();
		}
	}

	public void nextMove() {
		if (i < nMoves) {
			Move move = moves.get(i);
			stageBase.makeMove(move.from, move.to);
			System.out.println("Moviendo : " + move.from + " - " + move.to);
			i++;
		}else {
			if(isPlaying){
				isPlaying = false;
			}
		}

	}

	public void prevMove() {
		if (i > 0) {
			i--;
			Move move = moves.get(i);
			stageBase.makeMove(move.to, move.from);
			System.out.println("Moviendo : " + move.from + " - " + move.to);
		}
	}

	public void addDisc() {
		if (nDisc < 10) {
			clearDiscs();
			nDisc++;
			stageBase.createDiscs(nDisc);
			generateMoves(nDisc);
			isPlaying = false;
		}

	}

	public void removeDisc() {
		if (nDisc > 1) {
			clearDiscs();
			nDisc--;
			stageBase.createDiscs(nDisc);
			generateMoves(nDisc);
			isPlaying = false;
		}

	}

	public void clearDiscs() {
		stageBase.removeDiscs();
	}

	@Override
	public void dispose() {
		stage.dispose();
	}


	public void nextMovePlay(){
		if(isPlaying ){
			nextMove();
		}
	}

	public void startPlay(){
		if(!isPlaying){
			nextMove();
			isPlaying=true;
		}else{
			isPlaying = false;
		}
	}
}
