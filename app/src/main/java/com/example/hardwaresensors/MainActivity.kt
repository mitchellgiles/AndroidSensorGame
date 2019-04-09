package com.example.hardwaresensors

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

class MainActivity : AppCompatActivity() {

    var light = 0f
    var firstTarget = 0f
    var secondTarget = 0f
    lateinit var sensorManager: SensorManager
    lateinit var proximitySensor: Sensor
    lateinit var targetUtils: TargetUtils
    lateinit var vm: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        targetUtils = TargetUtils()

        vm = ViewModelProviders.of(this)[(MainViewModel::class.java)]
        vm.restart()
        vm.time.observe(this, Observer {
            clock.text = it.toString()
            if(it != null && it == 0) {
                unregister()
                if(!vm.pass) test.setTextColor(getColor(R.color.red))
            }
        })
    }

    var proximitySensorEventListener: SensorEventListener = object : SensorEventListener {
        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
            // TODO Auto-generated method stub
        }

        override fun onSensorChanged(event: SensorEvent) {
            light = event.values[0]
            test.text = light.toString()
            if(targetUtils.isInTarget(firstTarget, secondTarget, light)){
                test.setTextColor(getColor(R.color.green))
                vm.pass = true
                unregister()
            } else {
                test.setTextColor(getColor(R.color.black))
            }
        }
    }

    fun unregister() {
        sensorManager.unregisterListener(proximitySensorEventListener)
    }

    fun reset() {
        sensorManager.registerListener(
            proximitySensorEventListener,
            proximitySensor,
            SensorManager.SENSOR_DELAY_FASTEST
        )
        val list = targetUtils.generateTargets()
        if (list[0] < list[1]) {
            firstTarget = list[0]
            secondTarget = list[1]
        } else {
            firstTarget = list[1]
            secondTarget = list[0]
        }

        first_target.text = firstTarget.toString()
        second_target.text = secondTarget.toString()

        vm.pass = false
        countDown()
    }

    fun countDown() {
        vm.restart()
    }

    override fun onStop() {
        super.onStop()
        unregister()
    }

    override fun onResume() {
        super.onResume()
        reset()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.reset, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_reset -> {
                reset()
            }
            else -> {

            }
        }

        return true
    }

}
