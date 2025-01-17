package com.appnexus.opensdk;

import android.app.Activity;

import com.appnexus.opensdk.util.Lock;

import org.junit.After;
import org.junit.Before;
import org.robolectric.Robolectric;
import org.robolectric.shadows.httpclient.FakeHttp;
import org.robolectric.util.Scheduler;

import java.util.Timer;
import java.util.TimerTask;

import static org.robolectric.Shadows.shadowOf;

public class BaseRoboTest {
    public static final int placementID = 1;
    public static final int width = 320;
    public static final int height = 50;
    Activity activity;
    Scheduler uiScheduler, bgScheduler;

    @Before
    public void setup() {
        activity = Robolectric.buildActivity(MockMainActivity.class).create().get();
        shadowOf(activity).grantPermissions("android.permission.INTERNET");
        FakeHttp.getFakeHttpLayer().interceptHttpRequests(true);
        FakeHttp.getFakeHttpLayer().interceptResponseContent(true);
        bgScheduler = Robolectric.getBackgroundThreadScheduler();
        uiScheduler = Robolectric.getForegroundThreadScheduler();
        Robolectric.flushBackgroundThreadScheduler();
        Robolectric.flushForegroundThreadScheduler();
        bgScheduler.pause();
        uiScheduler.pause();
    }


    @After
    public void tearDown() {
        FakeHttp.clearPendingHttpResponses();
        activity.finish();
    }

    public void scheduleTimerToCheckForTasks() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (uiScheduler.areAnyRunnable() || bgScheduler.areAnyRunnable()) {
                    Lock.unpause();
                    this.cancel();
                }
            }
        }, 0, 100);
    }

    public void waitForTasks() {
        scheduleTimerToCheckForTasks();
        Lock.pause();
    }
}