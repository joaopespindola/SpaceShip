package com.firstgame.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import java.util.Iterator;//LOOP FOR FICA MAIS RAPIDO USANDO ITERATOR
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;

public class SpaceShip extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img, texNave, texMissile, texEnemy;
	private Sprite nave, missile;
	private float xNave, yNave, speed, xMissile, yMissile;
	private boolean attack, gameover;
	private Array<Rectangle> enemies;
	private long lastEnemyTime;
	private int score;
	private int power;
	private FreeTypeFontGenerator generator;
	private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
	private BitmapFont bitmap;
	
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

		lastEnemyTime = 0;
		score = 0;
		power = 3;
		gameover = false;
		generator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
		parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
		parameter.size = 30;
		parameter.borderWidth = 1;
		parameter.borderColor = Color.BLACK;
		parameter.color = Color.WHITE;
		bitmap = generator.generateFont(parameter);

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
		this.moveEnemies();

		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		if (!gameover) {
			if (attack) {
				batch.draw(missile, xMissile + (nave.getWidth() / 2), yMissile + (nave.getHeight() / 2 - 12));
			}
			batch.draw(nave, xNave, yNave);
			for (Rectangle enemy : enemies) {
				batch.draw(texEnemy, enemy.x, enemy.y);
			}

			bitmap.draw(batch, "Score: " + score, 20, Gdx.graphics.getHeight() - 20);
			bitmap.draw(batch, "Power: " + power, Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 20);
		}else {
			bitmap.draw(batch, "Score: " + score, 20, Gdx.graphics.getHeight() - 20);
			bitmap.draw(batch, "GAME OVER ", Gdx.graphics.getWidth() - 150, Gdx.graphics.getHeight() - 20);
			if(Gdx.input.isKeyPressed(Input.Keys.ENTER)){
				score = 0;
				power = 3;
				xNave = 0;
				yNave = 0;
				gameover = false;
				enemies.clear();
			}
		}
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
		Rectangle enemy = new Rectangle(Gdx.graphics.getWidth(), MathUtils.random(0, Gdx.graphics.getHeight() - texEnemy.getHeight()), texEnemy.getWidth(), texEnemy.getHeight());
		enemies.add(enemy);
		lastEnemyTime = TimeUtils.nanoTime();
	}

	private void moveEnemies(){
		if(TimeUtils.nanoTime() - lastEnemyTime > 1000000000){
			this.spawnEmemies();
		}

		//ITERATOR É USADO PARA PERCORRER SEQUENCIALMENTE UMA COLEÇÃO DE ELEMENTOS COMO
		//ARRAY, ARRAYLIST, LIST, MAP...
		for(Iterator<Rectangle > iter = enemies.iterator(); iter.hasNext();){
			Rectangle enemy = iter.next();
			enemy.x -= 400 * Gdx.graphics.getDeltaTime();

			//collide com o míssel
			if(collide(enemy.x, enemy.y, enemy.height, enemy.width, xMissile, yMissile, missile.getWidth(), missile.getHeight()) && attack){
				score++;
				attack = false;
				iter.remove();

				//collide com a nave
				}else if(collide(enemy.x, enemy.y, enemy.height, enemy.width, xNave, yNave, nave.getWidth(), nave.getHeight()) && !gameover){
				power--;
				if (power <= 0){
					gameover = true;
				}
				iter.remove();
			}


			if(enemy.x + texEnemy.getWidth() < 0){
				iter.remove();
			}
		}
	}

	private boolean collide(float x1, float y1, float w1, float h1,float x2, float y2, float w2, float h2){
		if(x1 + w1 > x2 && x1 < x2 + w2 && y1 + h1 > y2 && y1 < y2 + h2){
			return true;
		}else{
			return false;
		}
	}
}
