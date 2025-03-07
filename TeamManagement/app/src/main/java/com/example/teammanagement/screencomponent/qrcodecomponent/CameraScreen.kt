package com.example.teammanagement.screencomponent.qrcodecomponent

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.teammanagement.Actions
import com.example.teammanagement.R
import com.example.teammanagement.ui.theme.Blue
import com.example.teammanagement.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    actions: Actions,
) {
    val localContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(localContext)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Scan the QR Code") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            actions.navigateBack()
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "Back",
                            modifier = Modifier.size(28.dp),
                            colorFilter = ColorFilter.tint(Blue)
                        )
                    }
                }
            )
        }
    ) {
        Box(){
            AndroidView(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                factory = { context ->
                    val previewView = PreviewView(context)
                    val preview = Preview.Builder().build()
                    val selector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()

                    preview.setSurfaceProvider(previewView.surfaceProvider)

                    val imageAnalysis = ImageAnalysis.Builder().build()
                    imageAnalysis.setAnalyzer(
                        ContextCompat.getMainExecutor(context),
                        BarcodeAnalyzer(context, actions)
                    )

                    runCatching {
                        cameraProviderFuture.get().bindToLifecycle(
                            lifecycleOwner,
                            selector,
                            preview,
                            imageAnalysis
                        )
                    }.onFailure {
                        Log.e("CAMERA", "Camera bind error ${it.localizedMessage}", it)
                    }
                    previewView
                }
            )
            Image(
                painter = painterResource(id = R.drawable.qr_code_scan_9775),
                contentDescription = "Background",
                modifier = Modifier.fillMaxSize().padding(50.dp),
                colorFilter = ColorFilter.tint(Color.Gray)
            )
        }
    }
}