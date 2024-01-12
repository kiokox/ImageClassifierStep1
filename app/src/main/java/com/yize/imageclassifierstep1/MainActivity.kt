package com.yize.imageclassifierstep1

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import java.io.IOException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val img: ImageView = findViewById(R.id.imageToLabel)
        // assets folder image file name with extension
        val fileName = "rosa.jpg"
        // get bitmap from assets folder 位图
        val bitmap: Bitmap? = assetsToBitmap(fileName)
        bitmap?.apply {
            img.setImageBitmap(this)
        }
        val txtOutput : TextView = findViewById(R.id.txtOutput)
        val btn: Button = findViewById(R.id.btnTest)

        btn.setOnClickListener {
            // ImageLabeling.getClient实例化标记器，并向其传递ImageLaberOptions。
            // 它附带DEFAULT_OPTIONS属性，可用于快速启动并运行
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            // 使用fromBitmap方法根据输入图片创建输入图片，InputImage是机器学习套件处理图片所需的格式
            val image = InputImage.fromBitmap(bitmap!!, 0)
            // 标签添加者会处理图片，并在成功或失败时提供异步回调。
            // 如果成功，回调将包含一个标签列表，标签文本和置信度
            // 如果执行失败，返回异常
            var outputText = ""
            labeler.process(image)
                .addOnSuccessListener { labels ->
                    // Task completed successfully
                    for (label in labels) {
                        val text = label.text
                        val confidence = label.confidence
                        outputText += "$text : $confidence\n"
                    }
                    txtOutput.text = outputText
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                }
        }
    }
}

// extension function to get bitmap from assets
fun Context.assetsToBitmap(fileName: String): Bitmap? {
    return try {
        with(assets.open(fileName)) {
            BitmapFactory.decodeStream(this)
        }
    } catch (e: IOException) {
        null
    }
}
