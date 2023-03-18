package com.tengizmkcorp.flashlight

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.text.HtmlCompat
import com.tengizmkcorp.flashlight.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggleButton: ToggleButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggleButton = binding.toggleButton
        checkPermission()
        listeners()
    }

    private fun listeners() {
        val cameraObject: CameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        var light: Boolean = false
        toggleButton.setOnClickListener {
            if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                light = toggleButton.text == "ON"
                //it only works for android 8.0 or higher
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    cameraObject.setTorchMode(
                        cameraObject.cameraIdList[0],
                        light)
                    playSound()
                } else
                    Toast.makeText(this,
                        HtmlCompat.fromHtml("<font color='#ef1a2a' ><b>" + resources.getString(R.string.this_device_does_not_have_flashlight) + "</b></font>",
                            HtmlCompat.FROM_HTML_MODE_LEGACY),
                        Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun playSound() {
        val mediaPlayer: MediaPlayer = MediaPlayer.create(this, R.raw.click_sound)
        mediaPlayer.start()
    }

    private fun checkPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        ) {
            toggleButton.isEnabled = false
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), 101)
        } else toggleButton.isEnabled = true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        toggleButton.isEnabled =
            requestCode == 101 && grantResults[0] == PackageManager.PERMISSION_GRANTED
    }
}