package com.example.login_project

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import com.google.common.util.concurrent.ListenableFuture
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors



class MainActivity4 : AppCompatActivity() {

    private lateinit var previewView: PreviewView
    private lateinit var cameraSelector: CameraSelector
    private lateinit var processCameraProvider: ProcessCameraProvider
    private lateinit var cameraProviderFuture:ListenableFuture<ProcessCameraProvider>
    private lateinit var cameraPreview:Preview
    private lateinit var imageAnalysis: ImageAnalysis

    private fun camera_permission(){
        val requestCameraPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                startCamera()
            }
        }

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED) {
            startCamera()
        } else {
            requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main4)
        previewView = findViewById(R.id.previewView)
        camera_permission()
    }
    private fun startCamera(){
        cameraSelector=CameraSelector.DEFAULT_BACK_CAMERA
        cameraProviderFuture=ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(
            {
                processCameraProvider=cameraProviderFuture.get()
                bindCameraPreview()
                bindInputAnalyser()
            },ContextCompat.getMainExecutor(this)
        )
    }

    private fun bindInputAnalyser() {
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build()
            val barcodeScanner = BarcodeScanning.getClient(options)
       imageAnalysis = ImageAnalysis.Builder()
                .setTargetRotation(previewView.display.rotation)
                .build()
        val cameraExecutor=Executors.newSingleThreadExecutor()
        imageAnalysis.setAnalyzer(cameraExecutor) { imageProxy ->
                processImageProxy(barcodeScanner, imageProxy)
            }
       processCameraProvider.bindToLifecycle(this,cameraSelector,imageAnalysis)
    }

    @Suppress("UnsafeOptInUsageError")
    private fun processImageProxy(barcodeScanner: BarcodeScanner, imageProxy: ImageProxy) {
        val inputImage=InputImage.fromMediaImage(imageProxy.image!!,imageProxy.imageInfo.rotationDegrees)
        barcodeScanner.process(inputImage)
            .addOnSuccessListener { barcodes->
                if(barcodes.isNotEmpty()){
                    onScan?.invoke(barcodes)
                    onScan=null
                    finish()
                }
            }.addOnFailureListener {
                it.printStackTrace()
            }.addOnCompleteListener {
                imageProxy.close()
            }
    }

    private fun bindCameraPreview(){
        cameraPreview=Preview.Builder()
            .setTargetRotation(previewView.display.rotation)
            .build()
        cameraPreview.setSurfaceProvider(previewView.surfaceProvider)
        processCameraProvider.bindToLifecycle(this,cameraSelector,cameraPreview)
    }

    companion object{
        private var onScan:((barcodes:List<Barcode>)->Unit)?=null
        fun startScanner(context: Context,onScan:(barcodes:List<Barcode>)->Unit){
            this.onScan=onScan
            Intent(context,MainActivity4::class.java).also{
                context.startActivity(it)
            }
        }
    }
}


