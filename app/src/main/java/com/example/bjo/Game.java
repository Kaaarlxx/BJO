package com.example.bjo;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.bjo.gameobject.Bullet;
import com.example.bjo.gameobject.Enemy;
import com.example.bjo.gameobject.GameObject;
import com.example.bjo.gameobject.Player;
import com.example.bjo.gamepanel.GameOver;
import com.example.bjo.gamepanel.Joystick;
import com.example.bjo.gamepanel.Performance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Game extends SurfaceView implements SurfaceHolder.Callback {
    private final Joystick joystick;
    private GameLoop gameLoop;
    private final Player player;
    private List<Enemy> enemyList = new ArrayList<Enemy>();
    private List<Bullet> bulletList = new ArrayList<Bullet>();
    private int joystickPointerId = 0;
    private int numberOfSpellsToCast = 0;
    private GameOver gameOver;
    private Performance performance;

    public Game(Context context) {
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        gameLoop = new GameLoop(this,surfaceHolder);

        //gamePanels
        performance = new Performance(gameLoop,context);
        gameOver = new GameOver(context);
        joystick = new Joystick(2300,1000,100,40);

        //gameObjects
        player = new Player(getContext(),joystick,100,100,200,200);


        setFocusable(true);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {

        gameLoop.startLoop();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){

        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if(joystick.getIsPressed()){
                    numberOfSpellsToCast++;
                }
                else if(joystick.isPressed(event.getX(),event.getY())){
                    joystickPointerId = event.getPointerId(event.getActionIndex());
                    joystick.setIsPressed(true);
                }else{
                    numberOfSpellsToCast++;
                }
                return true;
            case MotionEvent.ACTION_MOVE:
                if(joystick.getIsPressed()) {
                    joystick.setActuator(event.getX(), event.getY());
                }
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if(joystickPointerId == event.getPointerId(event.getActionIndex())){
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                }

                return true;
        }
        return true;
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);
        player.draw(canvas);
        joystick.draw(canvas);
        for(Enemy enemy : enemyList) enemy.draw(canvas);
        for(Bullet bullet : bulletList) bullet.draw(canvas);
    
        if(player.getHealthPoints()<=0){
            gameOver.draw(canvas);
        }
    }
    
    public void update() {

        if(player.getHealthPoints()<=0){
            return;
        }
        player.update();
        joystick.update();
        if(Enemy.readyToSpawn()){
            enemyList.add(new Enemy(getContext(),player,140,190));
        }
        while(numberOfSpellsToCast > 0){
            bulletList.add(new Bullet(getContext(),player,40,60));
            numberOfSpellsToCast--;
        }

        for(Enemy enemy :enemyList) enemy.update();
        for(Bullet bullet :bulletList) bullet.update();
        Iterator<Enemy>iteratorEnemy = enemyList.iterator();


        while (iteratorEnemy.hasNext()){
            GameObject enemy = iteratorEnemy.next();
            if(GameObject.isColliding(enemy,player)){
                        player.setHealthPoints(player.getHealthPoints()-1);
                        iteratorEnemy.remove();
                    }
                Iterator<Bullet> iteratorBullet = bulletList.iterator();
                while(iteratorBullet.hasNext()){
                    GameObject bullet = iteratorBullet.next();
                    if(GameObject.isColliding(bullet,enemy)){
                        iteratorBullet.remove();
                        iteratorEnemy.remove();
                        break;
                    }
                }
        }
    }

}
