package com.hcruzp.pushingvips;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
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

    private Context ctx;
    private Bundle bundle = new Bundle();
    private List<String> vips;
    private List<String> pushedVips;
    private int widthScreen;
    private int heightScreen;
    private float x;

    public ResultsView(Context context) {
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

        DisplayMetrics displayMetrics = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        widthScreen = displayMetrics.widthPixels;
        heightScreen = displayMetrics.heightPixels;
        int screenLayout = ((Activity) getContext()).getResources().getConfiguration().screenLayout;
        boolean isTablet = (screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;

        int rows = isTablet ? 5 : 4;
        boolean colsArePair = vips.size() % rows > 0;
        float cols = (vips.size() / rows) + (colsArePair ? 1 : 0);
        x = (cols / 2) * (-100) + (widthScreen /2) - (colsArePair ? 50 : 0) - 32 / (colsArePair ? 1 : 2);       /* 50: space between items, 32: item size */
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        String pushedVipsArray[] = bundle.getStringArray("pushedVipsArray");
        List<String> pushedVips = Arrays.asList(pushedVipsArray);
        int[] location = new int[2];
        this.getLocationOnScreen(location);
//        x = 100;
        float y = -100;     /* location[1] is the point where the view starts */
        float increment = 150;

        float toCheckIfArrivingTheEdge = location[1] + increment * 2;
        for (String o : vips) {
            String vipName = o;
            if (pushedVips.contains(vipName.substring(0, vipName.length() - 2))) {
                vipName = vipName.substring(0, vipName.length() - 2);
            }
            int resId = getResources().getIdentifier(vipName, "drawable", ctx.getPackageName());
            canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), resId), x , y += increment, null);
            if ((y + toCheckIfArrivingTheEdge) >= heightScreen) {
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
