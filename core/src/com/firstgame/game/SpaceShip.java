package com.firstgame.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;//LOOP FOR FICA MAIS RAPIDO USANDO ITERATOR

public class SpaceShip extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, texNave, texMissile, texEnemy;
	private Sprite nave, missile;
	private float xNave, yNave, speed, xMissile, yMissile;
	private boolean attack;
	private Array<Rectangle> enemies;
	
	@Override
	public void create () {
		batch = new SpriteBatch();

		//LIBGDX JA PEGA DIRETO NA RAIZ DO "ASSETS"
		img = new Texture("bg.png");
		texNave = new Texture("spaceship.png");
		texMissile = new Texture("missile.png");
		texEnemy = new Texture("enemy.png");

		nave = new Sprite(texNave);
		missile = new Sprite(texMissile);

		enemies = new Array<Rectangle>();

		xNave = 0;
		yNave = 0;
		speed = 10;
		xMissile = xNave;
		yMissile = yNave;
		attack = false;
	}

	@Override
	public void render () {
		this.moveMissile();
		this.moveNave();
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		if(attack) {
			batch.draw(missile, xMissile + (nave.getWidth() / 2), yMissile + (nave.getHeight() / 2 - 12));
		}
		batch.draw(nave, xNave, yNave);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
		texNave.dispose();
	}

	private void moveNave(){
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || (Gdx.input.isKeyPressed(Input.Keys.D))){
			//LARGURA DA TELA - TAMANHO DA NAVE PRA NAO SAIR DOS LIMITES DA TELA
			if(xNave < Gdx.graphics.getWidth() - nave.getWidth()){
				xNave += speed;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || (Gdx.input.isKeyPressed(Input.Keys.A))){
			if(xNave > 0){
				xNave -= speed;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || (Gdx.input.isKeyPressed(Input.Keys.S))){
			if(yNave > 0) {
				yNave -= speed;
			}
		}
		if(Gdx.input.isKeyPressed(Input.Keys.UP) || (Gdx.input.isKeyPressed(Input.Keys.W))){
			if(yNave < Gdx.graphics.getHeight()-nave.getHeight()) {
				yNave += speed;
			}
		}
	}
	private void moveMissile(){
		if(Gdx.input.isKeyPressed(Input.Keys.SPACE) && attack==false){
			attack = true;
			yMissile = yNave;
		}
		if (attack) {
			if(xMissile < Gdx.graphics.getWidth()) {
				xMissile += 25;
			}else{
				xMissile = xNave;
				//ATTACK RETORNA A SER FALSO P/ VOLTAR PARA CONDIÇÃO INICIAL
				attack = false;
			}
		}
		else {
			xMissile = xNave;
			yMissile = yNave;
		}
	}

	private void spawnEmemies(){
		Rectangle enemy = new Rectangle(200, 200, texEnemy.getWidth(), texEnemy.getHeight());

		enemies.add(enemy);
	}
}
