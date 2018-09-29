package com.congphuong.sensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** SensorsPlugin */
public class SensorsPlugin implements EventChannel.StreamHandler {
  private static final String ROTATION_CHANNEL_NAME =
          "sensors.congphuong.com/rotation";
  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final EventChannel rotationEventChannel = new EventChannel(registrar.messenger(), ROTATION_CHANNEL_NAME);
    rotationEventChannel.setStreamHandler(new SensorsPlugin(registrar.context(), Sensor.TYPE_ROTATION_VECTOR));

  }

  private SensorEventListener sensorEventListener;
  private final SensorManager sensorManager;
  private final Sensor sensor;

  public SensorsPlugin(Context context, int sensorType) {
    sensorManager = (SensorManager) context.getSystemService(context.SENSOR_SERVICE);
    sensor = sensorManager.getDefaultSensor(sensorType);
  }

  @Override
  public void onListen(Object o, EventChannel.EventSink eventSink) {
      sensorEventListener = createSensorEventListener(eventSink);
      sensorManager.registerListener(sensorEventListener, sensor, sensorManager.SENSOR_DELAY_NORMAL);
  }

  @Override
  public void onCancel(Object o) {
    sensorManager.unregisterListener(sensorEventListener);
  }

  SensorEventListener createSensorEventListener(final EventChannel.EventSink events) {
    return new SensorEventListener() {
      @Override
      public void onAccuracyChanged(Sensor sensor, int accuracy) {}

      @Override
      public void onSensorChanged(SensorEvent event) {
        float[] rotationMatrixFromVector = new float[16];
        SensorManager.getRotationMatrixFromVector(rotationMatrixFromVector, event.values);
        events.success(rotationMatrixFromVector);
      }
    };
  }
}
