package com.example.jbox2d.jbox2dmaster

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

/**
 *  jbox2d 实现
 */
class MainActivity : AppCompatActivity() {
    lateinit var mSensorManager: SensorManager
    lateinit var mSensor: Sensor
    private val mSensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        override fun onSensorChanged(event: SensorEvent?) {
            if (Sensor.TYPE_ACCELEROMETER == event?.sensor?.type) {
                val x = event.values[0]
                val y = event.values[1] * 2.0f
                jbColView.onSensorChanged(-x, y)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSensor()
        initChildView()
        initListener()
    }
    private fun initSensor() {
        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) // 加速度传感器
    }
    private fun initChildView() {
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT).apply { gravity = Gravity.CENTER }
        val imgs = arrayListOf(R.mipmap.ic_football, R.mipmap.ic_football1,
                R.mipmap.ic_football2, R.mipmap.ic_football3,
                R.mipmap.ic_football4, R.mipmap.ic_football5, R.mipmap.ic_football6)
        imgs.forEachIndexed { index, it ->
            jbColView.addView(ImageView(this@MainActivity)
                    .apply {
                        setImageResource(it)
                        setTag(R.id.jb_view_circle_tag, index != imgs.lastIndex)// 是圆形就设置true  否则是矩形
                    }, params)
        }
    }
    private fun initListener() {
        bt_jump.setOnClickListener { jbColView.onRandomChanged() }
        bt_sensor.setOnClickListener {
            val tag = bt_sensor.tag as? Boolean
            if (null != tag && tag) {
                bt_sensor.tag = false
                bt_sensor.text = "传感器：关闭"
                mSensorManager.unregisterListener(mSensorEventListener)
            } else {
                bt_sensor.tag = true
                bt_sensor.text = "传感器：开启"
                mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_UI)
            }
        }
    }
    override fun onResume() {
        super.onResume()
        val tag = bt_sensor.tag as? Boolean
        if (null != tag && tag) {
            mSensorManager.registerListener(mSensorEventListener, mSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }
    override fun onPause() {
        super.onPause()
        val tag = bt_sensor.tag as? Boolean
        if (null != tag && tag) {
            mSensorManager.unregisterListener(mSensorEventListener)
        }
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