package com.egadwys.gi_employee.scanner

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.egadwys.gi_employee.R
import com.egadwys.gi_employee.dashboard.Dashboard
import com.egadwys.gi_employee.splash.SplashScreen
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class Scanner : AppCompatActivity() {
    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private var camera: Camera? = null
    private var cameraControl: CameraControl? = null
    private var cameraInfo: CameraInfo? = null
    private lateinit var sharedPreferences: SharedPreferences
    private var isProcessing = false
    private var isFlashlightOn = false
    private val CAMERA_REQUEST_CODE = 101
    private lateinit var loading_scanner: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.scanner)

        loading_scanner = findViewById(R.id.loading_scanner)
        loading_scanner.visibility = View.GONE

        sharedPreferences = getSharedPreferences("user", Context.MODE_PRIVATE)
        val flashlightButton: Button = findViewById(R.id.flashlightButton)
        flashlightButton.setOnClickListener {
            isFlashlightOn = !isFlashlightOn
            toggleFlash(isFlashlightOn)
        }
        checkCameraPermission()
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        checkIfCameraPermissionIsGranted()
    }

    private fun checkIfCameraPermissionIsGranted() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Permission granted: start the preview
            startCamera()
        } else {
            // Permission denied
            MaterialAlertDialogBuilder(this)
                .setTitle("Permission required")
                .setMessage("This application needs to access the camera to process barcodes")
                .setPositiveButton("Ok") { _, _ ->
                    // Keep asking for permission until granted
                    checkCameraPermission()
                }
                .setCancelable(false)
                .create()
                .apply {
                    setCanceledOnTouchOutside(false)
                    show()
                }
        }
    }

    private fun checkCameraPermission() {
        try {
            val requiredPermissions = arrayOf(Manifest.permission.CAMERA)
            ActivityCompat.requestPermissions(this, requiredPermissions, 0)
        } catch (e: IllegalArgumentException) {
            checkIfCameraPermissionIsGranted()
        }
    }

    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider?) {
        val preview: Preview = Preview.Builder()
            .build()

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy: ImageProxy ->
            processImageProxy(imageProxy)
        }

        camera = cameraProvider?.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
        cameraControl = camera?.cameraControl
        cameraInfo = camera?.cameraInfo
        preview.setSurfaceProvider(findViewById<PreviewView>(R.id.preview_view).surfaceProvider)
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_CODE_128,
                    Barcode.FORMAT_QR_CODE)
                .build()
            val scanner = BarcodeScanning.getClient(options)

            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()  && !isProcessing) {
                        isProcessing = true
                        vibrate()
                        val barcode = barcodes[0]
                        val user = barcode.rawValue
                        val fadeInAnimationin = AnimationUtils.loadAnimation(this, R.anim.fade_in)
                        loading_scanner.visibility = View.VISIBLE
                        loading_scanner.startAnimation(fadeInAnimationin)
                        Handler(Looper.getMainLooper()).postDelayed({
                            getdata(user.toString())
                        }, 1000)
                    }
                }
                .addOnFailureListener { exception ->
                    finish()
                    Toast.makeText(this, exception.message.toString(), Toast.LENGTH_SHORT).show()
                    Log.d("failur: ", exception.message.toString())
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        }
    }

    private fun toggleFlash(enable:Boolean) {
        cameraControl?.enableTorch(enable)?.addListener({
            isFlashlightOn = enable
            val message = if (enable) "Flashlight turned on" else "Flashlight turned off"
        }, ContextCompat.getMainExecutor(this))
    }

    private fun getdata(user: String) {
        val fadeInAnimationout = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        RetrofitClient_scanner.instance.cekuser(user).enqueue(object :
            Callback<List<DataClass_scanner>> {
            override fun onResponse(call: Call<List<DataClass_scanner>>, response: Response<List<DataClass_scanner>>) {
                if (response.isSuccessful) {
                    response.body()?.let { loginDataList ->
                        if (loginDataList.isNotEmpty()) {
                            val loginData = loginDataList[0]
                            Toast.makeText(this@Scanner, loginData.staffStatus,Toast.LENGTH_SHORT).show()
                            createNotificationChannel(this@Scanner, loginData.name)
                            sendNotification(this@Scanner, loginData.name)
                            sharedPreferences.edit().putString("user", loginData.username).apply()
                            sharedPreferences.edit().putString("nama", loginData.name).apply()
                            val intent = Intent(this@Scanner, SplashScreen::class.java).apply {
                                putExtra("username", loginData.username)
                                putExtra("name", loginData.name)
                            }
                            startActivity(intent)
                            finish()
                        }
                    }
                    Log.d("resonse: ", response.message())
                } else {
                    finish()
                    Toast.makeText(this@Scanner, "Response: ${response.message()}", Toast.LENGTH_SHORT).show()
                    Log.d("response, but not", response.message())
                }
            }

            override fun onFailure(call: Call<List<DataClass_scanner>>, t: Throwable) {
                finish()
                Toast.makeText(this@Scanner, "Request Failed: ${t.message.toString()}", Toast.LENGTH_SHORT).show()
                Log.e("Request Failed: ", t.message.toString())
            }
        })
    }

    private fun vibrate() {
        val vibrator = ContextCompat.getSystemService(this, Vibrator::class.java) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Vibrate for 100 milliseconds
            val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            // Deprecated in API 26
            vibrator.vibrate(500)
        }
    }

    fun createNotificationChannel(context: Context, name: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Login success"
            val descriptionText = "Hello $name"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(context: Context, name: String) {
        val builder = NotificationCompat.Builder(context, "CHANNEL_ID")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle("Login success")
            .setContentText("Hello $name")
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setDefaults(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    this@Scanner,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            notify(1, builder.build())
        }
    }
}