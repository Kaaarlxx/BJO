package com.example.bjo.gameobject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import com.example.bjo.GameLoop;
import com.example.bjo.R;

public class Bullet extends GameObject {

    public static final double SPEED_PIXELS_PER_SECOND = 600.0;
    private static final double MAX_SPEED = SPEED_PIXELS_PER_SECOND / GameLoop.MAX_UPS;
    private Context context;
    private final Player player;

    public Bullet(Context context,Player player, double height, double width) {
        super(player.getPositionX(),player.getPositionY());
        this.context = context;
        this.player = player;
        this.height = height;
        this.width = width;
        velocityX = player.getDirectionX()*MAX_SPEED;
        velocityY = player.getDirectionY()*MAX_SPEED;
    }

    @Override
    public void draw(Canvas canvas) {
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.bullet);
        bitmap = Bitmap.createScaledBitmap(bitmap,(int)width,(int) height,false);
        canvas.drawBitmap(bitmap,positionX,positionY,null);
    }

    public void update() {
        positionX += velocityX;
        positionY += velocityY;

    }
}
