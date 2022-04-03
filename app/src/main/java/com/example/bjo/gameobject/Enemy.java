package com.example.bjo.gameobject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.bjo.GameLoop;
import com.example.bjo.R;

public class Enemy  extends GameObject {
    private static final double SPEED_PIXELS_PER_SECOND = Player.SPEED_PIXELS_PER_SECOND/2;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private static final double SPAWNS_PER_MINUTE = 20;
    private static final double SPAWNS_PER_SECOND = SPAWNS_PER_MINUTE/60;
    private static final double UPDATES_PER_SPAWN = GameLoop.MAX_UPS/SPAWNS_PER_SECOND;
    private static double updatesUntilNextSpawn = UPDATES_PER_SPAWN;
    Context context;
    private Player player;

    public Enemy(Context context,float positionX,float positionY,Player player,double height,double width) {
        super(positionX,positionY);
        this.context = context;
        this.player = player;
        this.height = height;
        this.width = width;
    }

    public Enemy(Context context, Player player,double height,double width) {
        super(Math.random()*1000+500,Math.random()*1000);
        this.context = context;
        this.player = player;
        this.height = height;
        this.width = width;
    }

    public static boolean readyToSpawn() {
        if(updatesUntilNextSpawn <= 0 ){
            updatesUntilNextSpawn =+ UPDATES_PER_SPAWN;
            return true;
        } else{
            updatesUntilNextSpawn --;
            return false;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    public void draw(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.enemy);
        bitmap = Bitmap.createScaledBitmap(bitmap,(int)width,(int) height,false);
        canvas.drawBitmap(bitmap,positionX,positionY,null);
    }


    @Override
    public void update() {
        double distanceToPlayerX = player.getPositionX() - positionX;
        double distanceToPlayerY = player.getPositionY() - positionY;

        double distanceToPlayer = GameObject.getDistanceBetweenObjects(this,player);

        double directionX = distanceToPlayerX/distanceToPlayer;
        double directionY = distanceToPlayerY/distanceToPlayer;

        if(distanceToPlayer > 0){
            velocityX = directionX*MAX_SPEED;
            velocityY = directionY*MAX_SPEED;
        }else{
            velocityX = 0;
            velocityY = 0;
        }

        positionX += velocityX;
        positionY += velocityY;





    }
}
