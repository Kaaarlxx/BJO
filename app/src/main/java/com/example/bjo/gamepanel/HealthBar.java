package com.example.bjo.gamepanel;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import androidx.core.content.ContextCompat;

import com.example.bjo.R;
import com.example.bjo.gameobject.Player;

public class HealthBar {


    private Player player;
    private int width,height,margin;
    private Paint borderPaint;
    private Paint healthPaint;

    public HealthBar(Context context,Player player) {
        this.player = player;
        this.width = (int)player.width;
        this.height = 30;
        this.margin = 2;
        this.borderPaint = new Paint();
        int borderColor = ContextCompat.getColor(context, R.color.white);
        borderPaint.setColor(borderColor);
        this.healthPaint = new Paint();
        int healthColor = ContextCompat.getColor(context, R.color.health);
        healthPaint.setColor(healthColor);
    }


    public void draw(Canvas canvas) {
        float x = (float) player.getPositionX();
        float y = (float) player.getPositionY();
        float distanceToPlayer = 30;
        float healthPointsPercentage = (float) player.getHealthPoints()/player.MAX_HEALTH_POINTS;
        float borderLeft,borderTop,borderRight,borderBottom;
        borderLeft = x;
        borderRight = x + width;
        borderBottom = y - distanceToPlayer;
        borderTop = borderBottom - height;
        canvas.drawRect(borderLeft,borderTop,borderRight,borderBottom,borderPaint);

        float healthLeft,healthTop,healthRight,healthBottom,healthWidth,healthHeight;
        healthWidth = width - 2*margin;
        healthHeight = height - 2 * margin;
        healthLeft = borderLeft + margin;
        healthRight = healthLeft+healthWidth*healthPointsPercentage;
        healthBottom = borderBottom - margin;
        healthTop = healthBottom - healthHeight;
        canvas.drawRect(healthLeft,healthTop,healthRight,healthBottom,healthPaint);
    }
}
