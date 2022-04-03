package com.example.bjo.gameobject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.example.bjo.GameLoop;
import com.example.bjo.gamepanel.HealthBar;
import com.example.bjo.gamepanel.Joystick;
import com.example.bjo.R;
import com.example.bjo.Utils;

public class Player extends GameObject {
    public static double SPEED_PIXELS_PER_SECOND = 400.0;
    public static  int MAX_HEALTH_POINTS = 10;
    private static  double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private Joystick joystick;
    Context context;
    private HealthBar healthBar;
    private int healthPoints;


    public Player(Context context,Joystick joystick,float positionX,float positionY,double width,double height) {
        super(positionX,positionY);
        this.joystick = joystick;
        this.context = context;
        this.height = height;
        this.width = width;
        this.healthBar = new HealthBar(context,this);
        this.healthPoints = MAX_HEALTH_POINTS;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void draw(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.hero);
        bitmap = Bitmap.createScaledBitmap(bitmap,(int)width,(int)height,false);
        canvas.drawBitmap(bitmap,positionX,positionY,null);
        healthBar.draw(canvas);
    }

    public void update() {
        velocityX = joystick.getActuatorX()*MAX_SPEED;
        velocityY = joystick.getActuatorY()*MAX_SPEED;
        positionX += velocityX;
        positionY += velocityY;


        if(velocityX !=0 || velocityY != 0){
            double distance = Utils.getDistanceBetweenPoints(0,0,velocityX,velocityY);
            directionX = velocityX/distance;
            directionY = velocityY/distance;
        }
    }

    public void setPosition(float x, float y) {
        this.positionX = x;
        this.positionY = y;
    }

    public int getHealthPoints() {
        return healthPoints;
    }

    public void setHealthPoints(int healthPoints) {
        if(healthPoints >=0) this.healthPoints =  healthPoints;

    }
}
