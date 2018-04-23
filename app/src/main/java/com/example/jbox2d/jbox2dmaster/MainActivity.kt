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
    lateinit var mSensor: Sensor  // android启动系统的时候，开启的服务之一，启动流程
    private val mlistener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        }

        override fun onSensorChanged(event: SensorEvent?) {
            if (Sensor.TYPE_ACCELEROMETER == event?.sensor?.type) {
                val x = event.values[0]
                val y = event.values[1]*2.0f
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
        // 感应器
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) // 重力传感器类型
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
                    .apply { setImageResource(it)
                        setTag(R.id.jb_view_circle_tag, true)// 是圆形就设置true
                    }, params)
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

    override fun onContentChanged() {
        super.onContentChanged()
//        setContentView，生成View树之后，会回调它
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