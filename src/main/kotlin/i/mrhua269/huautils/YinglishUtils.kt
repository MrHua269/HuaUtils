package i.mrhua269.i.mrhua269.huautils

import com.hankcs.hanlp.HanLP
import com.hankcs.hanlp.corpus.tag.Nature
import kotlin.random.Random

/**
 * From https://github.com/RimoChan/yinglish
 * Translated with CodeGeex
 * Used libraries: HanLP
 */
object YinglishUtils {
    private fun convert(x1: String, nature: Nature, turbulence: Double): String {
        var x = x1

        if (Random.nextDouble() > turbulence) {
            return x
        }

        if (x in listOf("，", "。","，",".")) {
            return "……"
        }

        if (x in listOf("!", "！")) {
            return "❤"
        }

        if (x.length > 1 && Random.nextDouble() < 0.5) {
            return "${x[0]}……${x}"
        } else {
            if (nature == Nature.n && Random.nextDouble() < 0.5) {
                x = "〇".repeat(x.length)
            }
            return "……${x}"
        }
    }

    fun ch2sy(text: String, turbulence: Double = 0.5): String{
        val terms = HanLP.newSegment().seg(text)
        var result = ""

        for (term in terms){
            result = result.plus(convert(term.word, term.nature, turbulence))
        }

        return result
    }
}