package com.example.bjo.object;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Build;
import androidx.annotation.RequiresApi;

import com.example.bjo.GameLoop;
import com.example.bjo.Joystick;
import com.example.bjo.R;
import com.example.bjo.Utils;

public class Player extends GameObject {
    public static final double SPEED_PIXELS_PER_SECOND = 400.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private Joystick joystick;
    Context context;


    public Player(Context context,Joystick joystick,float positionX,float positionY,double width,double height) {
        super(positionX,positionY);
        this.joystick = joystick;
        this.context = context;
        this.height = height;
        this.width = width;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void draw(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.hero);
        bitmap = Bitmap.createScaledBitmap(bitmap,(int)width,(int)height,false);
        canvas.drawBitmap(bitmap,positionX,positionY,null);
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
}
