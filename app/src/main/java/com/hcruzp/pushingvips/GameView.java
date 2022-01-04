package com.hcruzp.pushingvips;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import androidx.core.app.ActivityCompat;

import com.hcruzp.pushingvips.SoundManager.SoundFX;

public class GameView extends SurfaceView {

    private static String HARD_VIP_HIGH = "vips_eso_tilin_32_g";
    private static String HARD_VIP_MIDDLE = "vips_ni_merga_32_g";
    private static String HARD_VIP_LOW = "vips_ami_meencanta_32_g";
    private static int hardVipHighLimit = 4;
    private static int hardVipMiddleLimit = 3;
    private static int hardVipLowLimit = 2;

    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private List<Sprite> sprites = new ArrayList<Sprite>();
    private List<TempSprite> temps = new ArrayList<TempSprite>();
    private long lastClick;
    private Bitmap bmpDrop;
    private Bitmap bmpTilin;
    private SoundManager sm;
    private int playedFondoId;
    private Bitmap bmpFondo;
    private int tiempo = 20;
    private int killedBad = 0;
    private int killedGood = 0;
    private int killedTilin = 0;
    private Context ctx;
    private List<String> vips;
    private Paint paint = new Paint();
    private List<String> pushedVips;
    private Vibrator vibrator;
    private int hardVipHighTimes = 0;
    private int hardVipMiddleTimes = 0;
    private int hardVipLowTimes = 0;

    public GameView(Context context) {
        super(context);
        ctx = context;
        sm = new SoundManager(context);
        gameLoopThread = new GameLoopThread(this);
        holder = getHolder();
        vibrator = (Vibrator) ctx.getSystemService(Context.VIBRATOR_SERVICE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        //getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        ((Activity) getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        bmpFondo = BitmapFactory.decodeResource(getResources(), R.drawable.fondo);
        bmpFondo = Bitmap.createScaledBitmap(bmpFondo, width, height, false);

        paint.setAntiAlias(true);
        paint.setTextSize(30);
        paint.setTypeface(Typeface.DEFAULT_BOLD);
        paint.setColor(Color.WHITE);

        holder.addCallback(new Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                System.out.println("Llega al surfaceDestroyed");
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                createSprites();
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
                playedFondoId = sm.playSound(SoundFX.FONDO, 1, 1, -1);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }
        });
        bmpDrop = BitmapFactory.decodeResource(getResources(), R.drawable.drop2_32);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            // @Override
            public void run() {
                tiempo--;
            }
        }, 1, 1000);

        /* Gets the name of all the vips in the folder */
        vips = new ArrayList<>();
        Class<?> clz = R.drawable.class;
        final Field[] fields = clz.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            if (name.startsWith("vips_") && name.endsWith("_g")) {
                vips.add(name);
            }
        }
        pushedVips = new ArrayList<>();
    }

    private void createSprites() {
        for (int i = 0; i < 7; i++) {
            addAllBadSprites();
        }
        for (int i = 0; i < 3; i++) {
            addAllGoodSprites();
        }
    }

/*    private Sprite createSprite(int resource, boolean isMan) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
        return new Sprite(this, bmp, isMan);
    }*/

    private Sprite createSprite(int resource, int srpiteType) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
        return new Sprite(this, bmp, srpiteType, null);
    }

    private Sprite createSprite(int resource, int srpiteType, String tilinFileName) {
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), resource);
        return new Sprite(this, bmp, srpiteType, tilinFileName);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
//	        canvas.drawColor(Color.GREEN);
            canvas.drawBitmap(bmpFondo, 0, 0, null);
            for (int i = temps.size() - 1; i >= 0; i--) {
                temps.get(i).onDraw(canvas);
            }
            for (Sprite sprite : sprites) {
                sprite.onDraw(canvas);
            }
            canvas.drawText("Time: " + tiempo, 40, 30, paint);
            canvas.drawText("VIPs: " + pushedVips.size() + " / " + vips.size(), 40, 60, paint);
            canvas.drawText(" ✓ : " + killedBad, getWidth() - 110, 30, paint);
            canvas.drawText("✗: " + killedGood, getWidth() - 110, 60, paint);
            if (tiempo == 0) {
                sm.pauseSound(playedFondoId);
                gameLoopThread.setRunning(false);
                gameLoopThread.interrupt();
/*				try {
					gameLoopThread.join();
				} catch (InterruptedException ie) {
					System.out.println("InterruptedException = " + ie);
				}*/
                launchResults();
/*				Intent intent = new Intent(ctx, Results.class);
				intent.putExtra("record", killed + "");
				ctx.startActivity(intent);*/
            }
        } catch (NullPointerException npe) {
            sm.pauseSound(playedFondoId);
            gameLoopThread.setRunning(false);
            gameLoopThread.interrupt();
//            System.out.println("AQUI TERMINA EL JUEGO = " + npe);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (System.currentTimeMillis() - lastClick > 100) {
            lastClick = System.currentTimeMillis();
            synchronized (getHolder()) {
                float x = event.getX();
                float y = event.getY();
                for (int i = sprites.size() - 1; i >= 0; i--) {
                    Sprite sprite = sprites.get(i);
                    if (sprite.isCollision(x, y)) {
                        if (sprite.getSpriteType() == Sprite.TYPE_BAD) {
                            sm.playSound(SoundFX.POP, 1, 1, 0);
                            killedBad++;
                            if (killedBad % 5 == 0) {
                                addVIPSprite();
                            }
                            sprites.remove(sprite);
                            temps.add(new TempSprite(temps, this, x, y, bmpDrop));
                        } else if (sprite.getSpriteType() == Sprite.TYPE_GOOD) {
                            killedGood++;
                            sm.playSound(SoundFX.SCREAM, 1, 1, 0);
                            vibrator.vibrate(5);
                            addAllBadSprites();

                            sprites.remove(sprite);
                            temps.add(new TempSprite(temps, this, x, y, bmpDrop));
                        } else {
                            if (!pushedVips.contains(sprite.getTilinDrawableName())) {
                                pushedVips.add(sprite.getTilinDrawableName());
                            }
                            tiempo++;
                            killedTilin++;
//                            int soundId = getResources().getIdentifier(sprite.getTilinRawName(), "raw", ctx.getPackageName());
/*                            sm.getSoundMap().put(SoundFX.TILIN, sm.getSoundPool().load(ctx, soundId, 1));
                            SoundPool.OnLoadCompleteListener listener = mock(SoundPool.OnLoadCompleteListener.class);
                            sm.getSoundPool().setOnLoadCompleteListener();
                            sm.playSound(SoundFX.TILIN, 1, 1, 0);*/
                            int soundId = getResources().getIdentifier(sprite.getTilinRawName(), "raw", ctx.getPackageName());
                            sm.playTilinSound(soundId, 1, 1, 0, ctx);
                            vibrator.vibrate(10);
                            sprites.remove(sprite);
                            /* Every 2 vips pushed, add more bads */
                            if (killedTilin % 2 == 0) {
                                addAllBadSprites();
                            }

                            int resId = getResources().getIdentifier(sprite.getTilinDrawableName(), "drawable", ctx.getPackageName());
                            bmpTilin = BitmapFactory.decodeResource(getResources(), resId);
                            temps.add(new TempSprite(temps, this, x, y, bmpTilin));
                        }
                        if (killedGood == 5) {
                            launchResults();
                        }
                        break;
                    }
                }
            }
        }
        return true;// super.onTouchEvent(event);
    }

    public void launchResults() {
        String[] pushedVipsArray = pushedVips.toArray(new String[0]);
        sm.pauseSound(playedFondoId);
        gameLoopThread.setRunning(false);
        Intent intent = new Intent(ctx, Results.class);
        intent.putExtra("killedBad", killedBad);
        intent.putExtra("killedGood", killedGood);
        intent.putExtra("pushedVipsArray", pushedVipsArray);
        ctx.startActivity(intent);
        ((Activity) ctx).finish();
    }

    private void addAllBadSprites() {
        sprites.add(createSprite(R.drawable.bad1, Sprite.TYPE_BAD));
        sprites.add(createSprite(R.drawable.bad2, Sprite.TYPE_BAD));
        sprites.add(createSprite(R.drawable.bad3, Sprite.TYPE_BAD));
        sprites.add(createSprite(R.drawable.bad4, Sprite.TYPE_BAD));
        sprites.add(createSprite(R.drawable.bad5, Sprite.TYPE_BAD));
        sprites.add(createSprite(R.drawable.bad6, Sprite.TYPE_BAD));
    }

    private void addAllGoodSprites() {
        sprites.add(createSprite(R.drawable.good1, Sprite.TYPE_GOOD));
        sprites.add(createSprite(R.drawable.good2, Sprite.TYPE_GOOD));
        sprites.add(createSprite(R.drawable.good3, Sprite.TYPE_GOOD));
        sprites.add(createSprite(R.drawable.good4, Sprite.TYPE_GOOD));
        sprites.add(createSprite(R.drawable.good5, Sprite.TYPE_GOOD));
        sprites.add(createSprite(R.drawable.good6, Sprite.TYPE_GOOD));
    }

    private void addVIPSprite() {
        Random random = new Random();
        String vipName = vips.get(random.nextInt(vips.size()));
        if (vipName == HARD_VIP_HIGH) {
            hardVipHighTimes++;
//            System.out.println("*************** VIP H = " + vipName + " " + hardVipHighTimes);
            if (hardVipHighTimes == hardVipHighLimit) {
//                System.out.println("HHHHHHHHHHHHHHHHHHHHH");
                addVIPSpriteByName(vipName, Sprite.TYPE_TILIN);
                hardVipHighTimes = 0;
            } else {
                addVIPSprite();
            }
        } else if (vipName == HARD_VIP_MIDDLE) {
            hardVipMiddleTimes++;
//            System.out.println("*************** VIP M = " + vipName + " " + hardVipMiddleTimes);
            if (hardVipMiddleTimes == hardVipMiddleLimit) {
//                System.out.println("MMMMMMMMMMMMMMMMMMMMM");
                addVIPSpriteByName(vipName, Sprite.TYPE_TILIN);
                hardVipMiddleTimes = 0;
            } else {
                addVIPSprite();
            }
        } else if (vipName == HARD_VIP_LOW) {
            hardVipLowTimes++;
//            System.out.println("*************** VIP L  = " + vipName + " " + hardVipLowTimes);
            if (hardVipLowTimes == hardVipLowLimit) {
//                System.out.println("LLLLLLLLLLLLLLLL");
                addVIPSpriteByName(vipName, Sprite.TYPE_TILIN);
                hardVipLowTimes = 0;
            } else {
                addVIPSprite();
            }
        } else {
            addVIPSpriteByName(vipName, Sprite.TYPE_TILIN);
        }
//        addVIPByName("vips_hacerlade_superpedo_32_g", Sprite.TYPE_TILIN);
/*      int resId = getResources().getIdentifier("vips_hacerlade_superpedo_32_g", "drawable", ctx.getPackageName());
        sprites.add(createSprite(resId, Sprite.TYPE_TILIN, "vips_hacerlade_superpedo_32_g"));*/
    }

    private void addVIPSpriteByName(String vipName, int spriteType) {
        int resId = getResources().getIdentifier(vipName, "drawable", ctx.getPackageName());
        sprites.add(createSprite(resId, spriteType, vipName));
    }
}
