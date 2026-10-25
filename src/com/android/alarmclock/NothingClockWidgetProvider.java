/*
 * Copyright (C) 2026 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.android.deskclock.DeskClock;
import com.android.deskclock.R;
import com.android.deskclock.Utils;

/**
 * Nothing-style digital clock: big dot-matrix time, a red dot, the date and the next alarm.
 * The time and date are TextClocks, so the system keeps them current without any timers of ours.
 */
public class NothingClockWidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        // The next alarm line is the only thing we have to push ourselves.
        if (AlarmManager.ACTION_NEXT_ALARM_CLOCK_CHANGED.equals(intent.getAction())) {
            final AppWidgetManager wm = AppWidgetManager.getInstance(context);
            if (wm != null) {
                final int[] ids = wm.getAppWidgetIds(new ComponentName(context, getClass()));
                if (ids.length > 0) {
                    onUpdate(context, wm, ids);
                }
            }
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager wm, int[] widgetIds) {
        super.onUpdate(context, wm, widgetIds);

        final String nextAlarm = Utils.getNextAlarm(context);

        for (int widgetId : widgetIds) {
            final RemoteViews widget =
                    new RemoteViews(context.getPackageName(), R.layout.nothing_digital_widget);

            if (TextUtils.isEmpty(nextAlarm)) {
                widget.setViewVisibility(R.id.nothing_next_alarm, View.GONE);
            } else {
                widget.setTextViewText(R.id.nothing_next_alarm, nextAlarm);
                widget.setViewVisibility(R.id.nothing_next_alarm, View.VISIBLE);
            }

            // Tapping on the widget opens the app (if not on the lock screen).
            if (Utils.isWidgetClickable(wm, widgetId)) {
                final Intent openApp = new Intent(context, DeskClock.class);
                final PendingIntent pi = PendingIntent.getActivity(context, 0, openApp,
                        PendingIntent.FLAG_IMMUTABLE);
                widget.setOnClickPendingIntent(R.id.nothing_digital_widget, pi);
            }

            wm.updateAppWidget(widgetId, widget);
        }
    }
}
