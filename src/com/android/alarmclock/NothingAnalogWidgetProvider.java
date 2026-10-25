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

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.android.deskclock.DeskClock;
import com.android.deskclock.R;
import com.android.deskclock.Utils;

/**
 * Nothing-style analog clock: a ring of dots with dotted hands and a red second hand.
 * The dial and hands are plain drawables, so the system's AnalogClock does all the ticking.
 */
public class NothingAnalogWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager wm, int[] widgetIds) {
        super.onUpdate(context, wm, widgetIds);

        for (int widgetId : widgetIds) {
            final RemoteViews widget =
                    new RemoteViews(context.getPackageName(), R.layout.nothing_analog_widget);

            // Tapping on the widget opens the app (if not on the lock screen).
            if (Utils.isWidgetClickable(wm, widgetId)) {
                final Intent openApp = new Intent(context, DeskClock.class);
                final PendingIntent pi = PendingIntent.getActivity(context, 0, openApp,
                        PendingIntent.FLAG_IMMUTABLE);
                widget.setOnClickPendingIntent(R.id.nothing_analog_widget, pi);
            }

            wm.updateAppWidget(widgetId, widget);
        }
    }
}
