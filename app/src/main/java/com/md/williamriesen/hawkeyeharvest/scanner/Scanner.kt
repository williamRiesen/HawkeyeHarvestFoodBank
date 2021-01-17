package com.md.williamriesen.hawkeyeharvest.scanner

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage


private class YourImageAnalyzer : ImageAnalysis.Analyzer {

    override fun analyze(imageProxy: ImageProxy) {
//        val mediaImage = imageProxy.image
//        if (mediaImage != null) {
//            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
//            // Pass image to an ML Kit Vision API
//            val options = BarcodeScannerOptions.Builder()
//                .setBarcodeFormats(
//                    Barcode.FORMAT_PDF417
//                )
//                .build()
//
//            val scanner = BarcodeScanning.getClient(options)
//
//            val result = scanner.process(image)
//                .addOnSuccessListener { barcodes ->
//                    // Task completed successfully
//                    // ...
//                    processBarcodes(barcodes)
//                }
//                .addOnFailureListener {
//                    // Task failed with an exception
//                    // ...
//                }
//        }
//    }
}


fun processBarcodes(barcodes: MutableList<Barcode>) {
    for (barcode in barcodes) {
        val bounds = barcode.boundingBox
        val corners = barcode.cornerPoints

        val rawValue = barcode.rawValue

        val valueType = barcode.valueType
        // See API reference for complete list of supported types

        if (valueType == Barcode.TYPE_DRIVER_LICENSE) {
            val lastName = barcode.driverLicense!!.lastName
            val firstName = barcode.driverLicense!!.firstName
            val middleName = barcode.driverLicense!!.middleName
            val city = barcode.driverLicense!!.addressCity

        }
    }
}
}



