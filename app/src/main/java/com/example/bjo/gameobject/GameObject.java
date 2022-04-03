package com.example.bjo.gameobject;

import android.graphics.Canvas;

public abstract class GameObject {

    protected float positionX;
    protected float positionY;
    protected double velocityX;
    protected double velocityY;
    public double width;
    protected double height;
    protected double directionX;
    protected double directionY;

    public GameObject(double x, double y){
        this.positionX = (float) x;
        this.positionY = (float) y;

    }

    public static double getDistanceBetweenObjects(GameObject obj1, GameObject obj2) {
        return Math.sqrt(
                Math.pow(obj2.getPositionX() - obj1.getPositionX(), 2) +
                        Math.pow(obj2.getPositionY() - obj1.getPositionY(), 2)
        );
    }

    public static boolean isColliding(GameObject obj1, GameObject obj2) {
        double distance  =getDistanceBetweenObjects(obj1,obj2);
        double pitagoras = Math.sqrt(
                Math.pow(obj2.height-obj1.height,2)+Math.pow(obj2.width-obj1.width,2));
        if(distance <= pitagoras) return true;
        else return false;
    }

    public abstract void draw(Canvas canvas);
    public abstract void update();

    public double getPositionX(){return positionX;};

    public double getPositionY(){return positionY;};

    public double getDirectionX() {
        return directionX;
    }
    public double getDirectionY() {
        return directionY;
    }
}
