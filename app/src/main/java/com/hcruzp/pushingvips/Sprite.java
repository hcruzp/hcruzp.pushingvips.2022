package com.hcruzp.pushingvips;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

public class Sprite {

    int[] DIRECTION_TO_ANIMATION_MAP = {3, 1, 0, 2};
    private static final int BMP_COLUMNS = 3;
    private static final int BMP_ROWS = 4;
    protected static final int TYPE_BAD = 1;
    protected static final int TYPE_GOOD = 2;
    protected static final int TYPE_TILIN = 3;
    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;
    private GameView gameView;
    private Bitmap bmp;
    private int width;
    private int height;
    private int currentFrame;
    private boolean isMan;
    private int spriteType;
    private String tilinFileName;

/*    public Sprite(GameView gameView, Bitmap bmp, boolean man) {
        this.gameView = gameView;
        this.bmp = bmp;
        this.width = bmp.getWidth() / BMP_COLUMNS;
        this.height = bmp.getHeight() / BMP_ROWS;
        Random rnd = new Random();
        x = rnd.nextInt(gameView.getWidth() - width);
        y = rnd.nextInt(gameView.getHeight() - height);
        xSpeed = rnd.nextInt(10);
        ySpeed = rnd.nextInt(10);
        this.isMan = man;
    }*/

    /**
     * tilinFileName is usefull only to know what sprite need to retrieve to change on colision of a tilin type
     *
     * @param gameView
     * @param bmp
     * @param spriteType
     * @param tilinFileName
     */
    public Sprite(GameView gameView, Bitmap bmp, int spriteType, String tilinFileName) {
        this.gameView = gameView;
        this.bmp = bmp;
        this.tilinFileName = tilinFileName;
        this.width = bmp.getWidth();
        this.height = bmp.getHeight();

        Random rnd = new Random();
        x = rnd.nextInt(gameView.getWidth() - width);
        y = rnd.nextInt(gameView.getHeight() - height);
        xSpeed = rnd.nextInt(10) - 5;
        ySpeed = rnd.nextInt(10) - 5;
        if (spriteType != TYPE_TILIN) {
            this.width /= BMP_COLUMNS;
            this.height /= BMP_ROWS;
        } else {
            System.out.println("bmp = " + this.bmp.toString());
            xSpeed *= 15;
            ySpeed *= 15;
/*            xSpeed *= 10;
            ySpeed *= 10;*/
        }
        this.spriteType = spriteType;
    }


    private void update() {
        if (x > gameView.getWidth() - width - xSpeed || x + xSpeed < 0) {
            xSpeed = -xSpeed;
        }
        x = x + xSpeed;
        if (y > gameView.getHeight() - height - ySpeed || y + ySpeed < 0) {
            ySpeed = -ySpeed;
        }
        y = y + ySpeed;
        if (this.spriteType != TYPE_TILIN) {
            currentFrame = ++currentFrame % BMP_COLUMNS;
        } else {
            currentFrame = 0;
        }
    }

    public void onDraw(Canvas canvas) {
        update();
        int srcX = 0;
        int srcY = 0;
        if (this.spriteType != TYPE_TILIN) {
            srcX = currentFrame * width;
            srcY = getAnimationRow() * height;
        }
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(bmp, src, dst, null);
    }

    // direction = 0 up, 1 left, 2 down, 3 right,
    // animation = 3 back, 1 left, 0 front, 2 right
    private int getAnimationRow() {
        double dirDouble = (Math.atan2(xSpeed, ySpeed) / (Math.PI / 2) + 2);
        int direction = 0;
        if (this.spriteType != TYPE_TILIN) {
            direction = (int) Math.round(dirDouble) % BMP_ROWS;
        }
        return DIRECTION_TO_ANIMATION_MAP[direction];
    }

    public boolean isCollision(float x2, float y2) {
        return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
    }

    public String getTilinDrawableName() {
        return this.tilinFileName.substring(0, this.tilinFileName.length() - 2);
    }

    public String getTilinRawName() {
        return this.tilinFileName.substring(0, this.tilinFileName.length() - 5)/* + ".mp3"*/;
    }

    public boolean getIsMan() {
        return isMan;
    }

    public void setMan(boolean isMan) {
        this.isMan = isMan;
    }

    public int getSpriteType() {
        return spriteType;
    }

    public void setSpriteType(int spriteType) {
        this.spriteType = spriteType;
    }

    public String getTilinFileName() {
        return tilinFileName;
    }

    public void setTilinFileName(String tilinFileName) {
        this.tilinFileName = tilinFileName;
    }
}
