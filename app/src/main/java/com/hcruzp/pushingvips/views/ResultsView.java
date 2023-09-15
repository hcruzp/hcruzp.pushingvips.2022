package com.hcruzp.pushingvips.views;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

import com.hcruzp.pushingvips.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResultsView extends View {

    private Context ctx;
    private Bundle bundle = new Bundle();
    private List<String> vips;
    private List<String> pushedVips;
    private int widthPixels;
    private int heightPixels;
    private float x;

    public ResultsView(Context context, int widthPixels, int heightPixels) {
        super(context);
        ctx = context;
        vips = new ArrayList<>();
        Class<?> clz = R.drawable.class;
        final Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            if (name.startsWith("vips_") && name.endsWith("_g")) {
                vips.add(name);
            }
        }
        this.widthPixels = widthPixels;
        this.heightPixels = heightPixels;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);

        int screenLayout = ((Activity) getContext()).getResources().getConfiguration().screenLayout;
        boolean isTablet = (screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;

        String pushedVipsArray[] = bundle.getStringArray("pushedVipsArray");
        List<String> pushedVips = Arrays.asList(pushedVipsArray);

        float yInit = -100;
        float y = yInit;     /* location[1] is the point where the view starts */
        float increment = 150;

        int[] location = new int[2];
        this.getLocationOnScreen(location);

        float toCheckIfArrivingTheEdge = location[1] + increment * 2;

        /* knowing the rows */
        int rows = 0;
        for (String o : vips) {
            rows++;
            y += increment;
            if ((y + toCheckIfArrivingTheEdge) >= heightPixels) {
                y = yInit;
                break;
            }
        }

        /*int rows = isTablet ? 5 : 2;
        boolean colsArePair = vips.size() % rows > 0;
        float cols = (vips.size() / rows) + (!colsArePair ? 1 : 0);
        /* 118: space between items(150-32), 32: item size /
        x = (cols / 2) * (-100) + (widthPixels /2) - (colsArePair ? 118 : 0) - 32 / (colsArePair ? 1 : 2);

        String pushedVipsArray[] = bundle.getStringArray("pushedVipsArray");
        List<String> pushedVips = Arrays.asList(pushedVipsArray);
        int[] location = new int[2];
        this.getLocationOnScreen(location);
        System.out.println("x = " + location[0]);
        System.out.println("y = " + location[1]);

        System.out.println("widthPixels = " + widthPixels);
        System.out.println("heightPixels = " + heightPixels);*/

        boolean colsArePair = vips.size() % rows > 0;
        float cols = (vips.size() / rows) + (!colsArePair ? 1 : 0);
        x = (cols / 2) * (-150) + (widthPixels /2) + (!colsArePair ? 75 : 0);

//        x = 100;
        y = yInit;     /* location[1] is the point where the view starts */

        for (String o : vips) {
            String vipName = o;
            if (pushedVips.contains(vipName.substring(0, vipName.length() - 2))) {
                vipName = vipName.substring(0, vipName.length() - 2);
            }
            int resId = getResources().getIdentifier(vipName, "drawable", ctx.getPackageName());
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), resId), x , y += increment, null);
            if ((y + toCheckIfArrivingTheEdge) >= heightPixels) {
                y = yInit;
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
