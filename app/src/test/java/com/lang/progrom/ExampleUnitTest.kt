package com.lang.progrom

import com.lang.ktn.bean.PartBean
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    lateinit var dasd: String
    lateinit var currentPart: PartBean

    @Test
    fun addition_isCorrect() {




        if (::currentPart.isInitialized) {
            println("currentPart this::currentPart.isInitialized")
        }else{
            println("currentPart 未初始化")
        }

//        println(UUID.nameUUIDFromBytes("微博分期".toByteArray()).toString().replace("-",""))

//        assertEquals(4, 2 + 2)
// 调用高阶函数 4362923d-9944-38c2-bfc2-2b73cbae4280
//        kotlinDSL {
//            // 这个 lambda 的接收者类型为StringBuilder
//            append(" DSL ")
//            append("thisa")
//            println(this)
//        }
    }

    // 声明接收者
    fun kotlinDSL(block:StringBuilder.(Int)->Unit){
//        block(StringBuilder("Kotlin"))
//        block(StringBuilder("Kotlin"),1,3)
        StringBuilder().block(1)
        block(StringBuilder(),1)
    }


//    public inline fun <T, R : Comparable<R>> Iterable<T>.maxBy(selector: (T) -> R): T? {
//        val iterator = iterator()
//        if (!iterator.hasNext()) return null
//        var maxElem = iterator.next()
//        if (!iterator.hasNext()) return maxElem
//        var maxValue = selector(maxElem)
//        do {
//            val e = iterator.next()
//            val v = selector(e)
//            if (maxValue < v) {
//                maxElem = e
//                maxValue = v
//            }
//        } while (iterator.hasNext())
//        return maxElem
//    }
}

private fun java.lang.StringBuilder.block(stringBuilder: StringBuilder) {

}
