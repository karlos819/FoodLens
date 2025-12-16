package com.example.foodlens

import android.content.Context
import android.graphics.Bitmap
import org.tensorflow.lite.DataType
import org.tensorflow.lite.Interpreter
import org.tensorflow.lite.support.common.FileUtil
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp

class FoodClassifier(context: Context) {


    private val modelName = "food_model.tflite"
    private val labelName = "labels.txt"

    private var interpreter: Interpreter
    private var labels: List<String>


    private val modelInputType = DataType.UINT8

    init {

        val model = FileUtil.loadMappedFile(context, modelName)
        labels = FileUtil.loadLabels(context, labelName)


        val options = Interpreter.Options()
        interpreter = Interpreter(model, options)
    }

    fun classify(bitmap: Bitmap): Pair<String, Float> {

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))
            .build()


        var tensorImage = TensorImage(modelInputType)
        tensorImage.load(bitmap)
        tensorImage = imageProcessor.process(tensorImage)


        val outputBuffer = Array(1) { FloatArray(labels.size) }

        interpreter.run(tensorImage.buffer, outputBuffer)

        val probabilities = outputBuffer[0]


        val maxIndex = probabilities.indices.maxByOrNull { probabilities[it] } ?: -1

        if (maxIndex != -1) {
            val foodName = labels[maxIndex]


            val score = probabilities[maxIndex]

            return Pair(foodName, score)
        } else {
            return Pair("분석 실패", 0f)
        }
    }


    fun close() {
        interpreter.close()
    }
}