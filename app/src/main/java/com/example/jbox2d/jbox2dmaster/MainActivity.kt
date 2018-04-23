package com.example.jbox2d.jbox2dmaster

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var mSensorManager: SensorManager
    lateinit var mSensor: Sensor
    private val mlistener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            if (Sensor.TYPE_ACCELEROMETER == event?.sensor?.type) {
                val x = event.values[0]
                val y = event.values[1]
                jbColView.onSensorChanged(-x, y)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initBall()
        initSensor()
        button.setOnClickListener { jbColView.onRandomChanged() }
    }


    private fun initSensor() {
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    }


    private fun initBall() {
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT)
                .apply { gravity = Gravity.CENTER }

        val imgs = arrayListOf(R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher,
                R.mipmap.ic_launcher, R.mipmap.ic_launcher)
        imgs.forEach {
            jbColView.addView(ImageView(this@MainActivity)
                    .apply { setImageResource(it) }, params)
        }
    }


    override fun onResume() {
        super.onResume()
        mSensorManager.registerListener(mlistener, mSensor, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(mlistener)
    }

/* override fun onCreateOptionsMenu(menu: Menu?): Boolean {
     menuInflater.inflate(R.menu.jb_menu, menu)
     return super.onCreateOptionsMenu(menu)
 }

 override fun onOptionsItemSelected(item: MenuItem?): Boolean {
     when (item?.itemId) {
         R.id.enable -> {

         }

         R.id.editParams -> {

         }

         R.id.sensor -> {

         }
     }
     return super.onOptionsItemSelected(item)
 }*/
}