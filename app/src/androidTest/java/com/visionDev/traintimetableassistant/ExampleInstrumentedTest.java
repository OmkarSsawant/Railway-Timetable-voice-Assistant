package com.visionDev.traintimetableassistant;

import android.content.Context;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.visionDev.traintimetableassistant.data.TrainTimeTableDB;
import com.visionDev.traintimetableassistant.data.doa.TrainDAO;
import com.visionDev.traintimetableassistant.data.models.Train;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.visionDev.traintimetableassistant", appContext.getPackageName());
    }


}