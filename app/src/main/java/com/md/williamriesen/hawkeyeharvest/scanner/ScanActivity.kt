package com.md.williamriesen.hawkeyeharvest.scanner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import com.md.williamriesen.hawkeyeharvest.R


import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.android.synthetic.main.activity_scan.*

class ScanActivity : AppCompatActivity() {
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var imageAnalysis: ImageAnalysis

    override fun onCreate(savedInstanceState: Bundle?) {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(1280, 720))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)


        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            var preview: Preview = Preview.Builder()
                .build()

            var cameraSelector: CameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
//                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build()

            preview.setSurfaceProvider(previewView.getSurfaceProvider())

            var camera =
                cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview)

            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(applicationContext), ImageAnalysis.Analyzer
            { image ->
                val rotationDegrees = image.imageInfo.rotationDegrees
                // insert your code here.
                analyze(image)
            })

            cameraProvider.bindToLifecycle(
                this as LifecycleOwner,
                cameraSelector,
                imageAnalysis,
                preview
            )
        }, ContextCompat.getMainExecutor(this))
    }

    fun analyze(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            // Pass image to an ML Kit Vision API
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(
                    Barcode.FORMAT_ALL_FORMATS
//                    Barcode.FORMAT_PDF417
                )
                .build()

            val scanner = BarcodeScanning.getClient(options)

            val result = scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    // Task completed successfully
                    // ...
                    processBarcodes(barcodes)
//                    Log.d("TAG","analysis succeeded!")
                }
                .addOnFailureListener {
                    // Task failed with an exception
                    // ...
                    Log.d("TAG","analysis failed with exception $it")
                }
                .addOnCompleteListener {
                    mediaImage.close()
                    imageProxy.close()
                }
        }

    }

    private fun processBarcodes(barcodes: MutableList<Barcode>) {
//        Log.d("TAG","processBarcodes function fired.")
//        Log.d("TAG", "isEmpty ${barcodes.isEmpty()}")
        for (barcode in barcodes) {
            val bounds = barcode.boundingBox
            val corners = barcode.cornerPoints

            val rawValue = barcode.rawValue

            val valueType = barcode.valueType
            // See API reference for complete list of supported type

            if (valueType == Barcode.TYPE_DRIVER_LICENSE) {
                val lastName = barcode.driverLicense!!.lastName
                Log.d("TAG","Last name: $lastName")
                val firstName = barcode.driverLicense!!.firstName
                Log.d("TAG","First name: $firstName")
                val middleName = barcode.driverLicense!!.middleName
                Log.d("TAG","Middle name: $middleName")
                val city = barcode.driverLicense!!.addressCity
                Log.d("TAG","Address City name: $city")

            }
        }
    }
}