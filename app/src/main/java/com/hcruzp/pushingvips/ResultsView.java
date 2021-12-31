package com.hcruzp.pushingvips;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultsView extends View {

    private Context cntx;
    private Bundle bundle = new Bundle();
    private List<String> vips;
    private List<String> pushedVips;

    public ResultsView(Context context) {
        super(context);
        cntx = context;
/*        for (int i = 0; i < pushedVips.size(); i++) {
            System.out.println("Results pushedVips.get(i) = " + pushedVips.get(i));
        }*/
        vips = new ArrayList<>();
        Class<?> clz = R.drawable.class;
        final Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            if (name.startsWith("vips_") && name.endsWith("_g")) {
                vips.add(name);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        String pushedVipsArray[] = bundle.getStringArray("pushedVipsArray");
        List<String> pushedVips = Arrays.asList(pushedVipsArray);
        int[] location = new int[2];
        this.getLocationOnScreen(location);
        float x = 100;
        float y = -100;     /* location[1] is the point where the view starts */
        float increment = 150;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        float toCheckIfArrivingTheEdge = location[1] + increment * 2;
        for (String o : vips) {
            String vipName = o;
            if (pushedVips.contains(vipName.substring(0, vipName.length() - 2))) {
                vipName = vipName.substring(0, vipName.length() - 2);
            }
            int resId = getResources().getIdentifier(vipName, "drawable", cntx.getPackageName());
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), resId), x , y += increment, null);
            if ((y + toCheckIfArrivingTheEdge) >= height) {
                y = -100;
                x += increment;
            }
        }
    }

    public Bundle getBundle() {
        return bundle;
    }

    public void setBundle(Bundle bundle) {
        this.bundle = bundle;
    }
}
