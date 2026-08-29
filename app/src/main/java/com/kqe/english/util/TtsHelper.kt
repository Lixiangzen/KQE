package com.kqe.english.util

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

/**
 * 单词发音：封装系统 TTS，美式发音朗读英文单词与例句。
 */
class TtsHelper(context: Context) : TextToSpeech.OnInitListener {

    private val tts = TextToSpeech(context.applicationContext, this)

    @Volatile
    private var ready = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)
            ready = result != TextToSpeech.LANG_MISSING_DATA &&
                result != TextToSpeech.LANG_NOT_SUPPORTED
        }
    }

    fun speak(text: String) {
        if (ready && text.isNotBlank()) {
            tts.setSpeechRate(0.9f)
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "kqe_tts")
        }
    }

    fun shutdown() {
        runCatching {
            tts.stop()
            tts.shutdown()
        }
    }
}
