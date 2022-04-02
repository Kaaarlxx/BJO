package com.example.bjo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.example.bjo.object.Bullet;
import com.example.bjo.object.Enemy;
import com.example.bjo.object.GameObject;
import com.example.bjo.object.Player;

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

    public Game(Context context) {
        super(context);
        SurfaceHolder surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);
        gameLoop = new GameLoop(this,surfaceHolder);
        joystick = new Joystick(2300,1000,100,40);
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
    }

    public void drawUPS(Canvas canvas){
        String averageUPS = Double.toString(gameLoop.getAverageUPS());
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.white));
        paint.setTextSize(50);
        canvas.drawText("UPS " + averageUPS , 100,100,paint);
    }
    public void drawFPS(Canvas canvas){
        String averageFPS = Double.toString(gameLoop.getAverageFPS());
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.white));
        paint.setTextSize(50);
        canvas.drawText("FPS " + averageFPS , 100,200,paint);
    }

    public void update() {

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
