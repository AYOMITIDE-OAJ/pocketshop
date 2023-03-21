
package com.oajstudios.pocketshop.utils.rangeBar;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;

import java.util.ArrayList;

/**
 * Class representing the blue connecting line between the two thumbs.
 */
public class ConnectingLine {


    private final int[] colors;
    private final float[] positions;
    private final Paint paint = new Paint();

    private final float mY;


    /**
     * Constructor for connecting line
     *
     * @param y                    the y co-ordinate for the line
     * @param connectingLineWeight the weight of the line
     * @param connectingLineColors the color of the line
     */
    public ConnectingLine(float y, float connectingLineWeight,
                          ArrayList<Integer> connectingLineColors) {

        if(connectingLineColors.size() == 1){
            connectingLineColors.add(connectingLineColors.get(0));
        }

        colors = new int[connectingLineColors.size()];
        positions = new float[connectingLineColors.size()];
        for(int index = 0; index < connectingLineColors.size(); index++){
            colors[index] = connectingLineColors.get(index);

            positions[index] = (float) index / (connectingLineColors.size() - 1);
        }

        paint.setStrokeWidth(connectingLineWeight);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);

        mY = y;
    }

    private LinearGradient getLinearGradient(float startX, float endX, float height){

        return new LinearGradient(startX, height, endX, height,
                colors,
                positions,
                Shader.TileMode.REPEAT);
    }


    /**
     * Draw the connecting line between the two thumbs in rangebar.
     *
     * @param canvas     the Canvas to draw to
     * @param leftThumb  the left thumb
     * @param rightThumb the right thumb
     */
    public void draw(Canvas canvas, PinView leftThumb, PinView rightThumb) {
        paint.setShader(getLinearGradient(0, canvas.getWidth(), mY));

        canvas.drawLine(leftThumb.getX(), mY, rightThumb.getX(), mY, paint);

    }

    /**
     * Draw the connecting line between for single slider.
     *
     * @param canvas     the Canvas to draw to
     * @param rightThumb the right thumb
     * @param leftMargin the left margin
     */
    public void draw(Canvas canvas, float leftMargin, PinView rightThumb) {
        paint.setShader(getLinearGradient(0, canvas.getWidth(), mY));

        canvas.drawLine(leftMargin, mY, rightThumb.getX(), mY, paint);
    }
}
