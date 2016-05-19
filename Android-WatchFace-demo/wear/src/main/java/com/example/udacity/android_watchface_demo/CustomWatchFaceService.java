package com.example.udacity.android_watchface_demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.wearable.watchface.CanvasWatchFaceService;
import android.support.wearable.watchface.WatchFaceStyle;
import android.text.format.Time;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.WindowInsets;

import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class CustomWatchFaceService extends CanvasWatchFaceService {
    private static final String TAG = CustomWatchFaceService.class.getSimpleName();

    @Override
    public Engine onCreateEngine() {
        return new WatchFaceEngine();
    }

    private class WatchFaceEngine extends Engine {

        //Member variables
        private Typeface WATCH_TEXT_TYPEFACE = Typeface.create(Typeface.SERIF, Typeface.NORMAL);

        private static final int MSG_UPDATE_TIME_ID = 42;
        private static final long DEFAULT_UPDATE_RATE_MS = 1000;
        private long mUpdateRateMs = 1000;

        private Time mDisplayTime;

        private Paint mBackgroundColorPaint;
        private Paint mTimePaint;
        private Paint mDatePaint;
        private Paint mGraphicsPaint;

        private boolean mHasTimeZoneReceiverBeenRegistered = false;
        private boolean mIsInMuteMode;
        private boolean mIsLowBitAmbient;

        private float mXOffset;
        private float mYOffset;

        private int mBackgroundColor = Color.parseColor("#0000ff");
        private int mTextColor = Color.parseColor("#ffffff");

        final BroadcastReceiver mTimeZoneBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mDisplayTime.clear(intent.getStringExtra("time-zone"));
                mDisplayTime.setToNow();
            }
        };

        private final Handler mTimeHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_UPDATE_TIME_ID: {
                        invalidate();
                        if (isVisible() && !isInAmbientMode()) {
                            long currentTimeMillis = System.currentTimeMillis();
                            long delay = mUpdateRateMs - (currentTimeMillis % mUpdateRateMs);
                            sendEmptyMessageDelayed(MSG_UPDATE_TIME_ID, delay);
                        }
                        break;
                    }
                }
            }
        };

        //Overridden methods
        @Override
        public void onCreate(SurfaceHolder holder) {
            super.onCreate(holder);

            setWatchFaceStyle(new WatchFaceStyle.Builder(CustomWatchFaceService.this)
                            .setBackgroundVisibility(WatchFaceStyle.BACKGROUND_VISIBILITY_INTERRUPTIVE)
                            .setCardPeekMode(WatchFaceStyle.PEEK_MODE_VARIABLE)
                            .setShowSystemUiTime(false)
                            .build()
            );

            initBackground();
            mTimePaint = createTextPaint(Color.WHITE, R.dimen.time_text_size);
            mDatePaint = createTextPaint(Color.WHITE, R.dimen.date_text_size);
            mGraphicsPaint = createGraphicsPaint();

            mDisplayTime = new Time();
        }

        @Override
        public void onDestroy() {
            mTimeHandler.removeMessages(MSG_UPDATE_TIME_ID);
            super.onDestroy();
        }

        @Override
        public void onVisibilityChanged(boolean visible) {
            super.onVisibilityChanged(visible);

            if (visible) {
                if (!mHasTimeZoneReceiverBeenRegistered) {

                    IntentFilter filter = new IntentFilter(Intent.ACTION_TIMEZONE_CHANGED);
                    CustomWatchFaceService.this.registerReceiver(mTimeZoneBroadcastReceiver, filter);

                    mHasTimeZoneReceiverBeenRegistered = true;
                }

                mDisplayTime.clear(TimeZone.getDefault().getID());
                mDisplayTime.setToNow();
            } else {
                if (mHasTimeZoneReceiverBeenRegistered) {
                    CustomWatchFaceService.this.unregisterReceiver(mTimeZoneBroadcastReceiver);
                    mHasTimeZoneReceiverBeenRegistered = false;
                }
            }

            updateTimer();
        }

        @Override
        public void onApplyWindowInsets(WindowInsets insets) {
            super.onApplyWindowInsets(insets);

            mYOffset = getResources().getDimension(R.dimen.y_offset);

            if (insets.isRound()) {
                mXOffset = getResources().getDimension(R.dimen.x_offset_round);
            } else {
                mXOffset = getResources().getDimension(R.dimen.x_offset_square);
            }
        }

        @Override
        public void onPropertiesChanged(Bundle properties) {
            super.onPropertiesChanged(properties);

            if (properties.getBoolean(PROPERTY_BURN_IN_PROTECTION, false)) {
                mIsLowBitAmbient = properties.getBoolean(PROPERTY_LOW_BIT_AMBIENT, false);
            }
        }

        @Override
        public void onTimeTick() {
            super.onTimeTick();

            invalidate();
        }

        @Override
        public void onAmbientModeChanged(boolean inAmbientMode) {
            super.onAmbientModeChanged(inAmbientMode);

            if (inAmbientMode) {
                mTimePaint.setColor(Color.parseColor("white"));
            } else {
                mTimePaint.setColor(Color.parseColor("red"));
            }

            if (mIsLowBitAmbient) {
                mTimePaint.setAntiAlias(!inAmbientMode);
            }

            invalidate();
            updateTimer();
        }

        @Override
        public void onInterruptionFilterChanged(int interruptionFilter) {
            super.onInterruptionFilterChanged(interruptionFilter);

            boolean isDeviceMuted = (interruptionFilter == android.support.wearable.watchface.WatchFaceService.INTERRUPTION_FILTER_NONE);
            if (isDeviceMuted) {
                mUpdateRateMs = TimeUnit.MINUTES.toMillis(1);

            } else {
                mUpdateRateMs = DEFAULT_UPDATE_RATE_MS;
            }

            if (mIsInMuteMode != isDeviceMuted) {
                mIsInMuteMode = isDeviceMuted;
                int alpha = (isDeviceMuted) ? 100 : 255;
                mTimePaint.setAlpha(alpha);
                invalidate();
            }

            updateTimer();
        }

        @Override
        public void onDraw(Canvas canvas, Rect bounds) {
            super.onDraw(canvas, bounds);

            mDisplayTime.setToNow();

            drawBackground(canvas, bounds);
            drawTimeText(canvas);

            drawIcon(canvas, bounds);
        }

        private void drawIcon(Canvas canvas, Rect bounds) {
            CustomWatchFaceApplication application = (CustomWatchFaceApplication) getApplication();
            Map<String, Object> forecastDataMap = application.getForecastDataMap();
            Bitmap bitmap = (Bitmap) forecastDataMap.get(WearableConstants.ICON_BITMAP_KEY);

            if (bitmap == null) {
                Log.i(TAG, "no icon found");
                return;
            }

            Log.i(TAG, "drawing the icon");
            Rect rect = new Rect();
            int x = (int) mXOffset - 25;
            int y = (int) mYOffset + 20;

            rect.set(x, y, x + 40, y + 40);
            canvas.drawBitmap(bitmap, null, rect, mGraphicsPaint);
        }

        //Utility methods
        private void initBackground() {
            mBackgroundColorPaint = new Paint();
            mBackgroundColorPaint.setColor(mBackgroundColor);
        }

        private Paint createTextPaint(int textColor, int dimension) {
            Paint paint = new Paint();
            paint.setColor(textColor);
            paint.setTypeface(WATCH_TEXT_TYPEFACE);
            paint.setAntiAlias(true);
            paint.setTextSize(getResources().getDimension(dimension));

            return paint;
        }

        private Paint createGraphicsPaint() {
            Paint paint = new Paint();
            paint.setAntiAlias(true);

            return paint;
        }

        private void updateTimer() {
            mTimeHandler.removeMessages(MSG_UPDATE_TIME_ID);
            if (isVisible() && !isInAmbientMode()) {
                mTimeHandler.sendEmptyMessage(MSG_UPDATE_TIME_ID);
            }
        }

        private void drawBackground(Canvas canvas, Rect bounds) {
            canvas.drawRect(0, 0, bounds.width(), bounds.height(), mBackgroundColorPaint);
        }

        private void drawTimeText(Canvas canvas) {
            String timeText = mDisplayTime.hour + ":" + String.format("%02d", mDisplayTime.minute);
            String dateText = DateFormatUtil.generateDateString();
            canvas.drawText(timeText, mXOffset, mYOffset, mTimePaint);
            canvas.drawText(dateText, mXOffset + 20, mYOffset + 45, mDatePaint);
        }
    }
}
