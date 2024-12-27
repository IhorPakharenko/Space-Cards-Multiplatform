package com.isao.spacecards.utils

//import android.util.Log
//import io.kotest.core.listeners.AfterSpecListener
//import io.kotest.core.listeners.BeforeSpecListener
//import io.kotest.core.spec.Spec
//import org.intellij.lang.annotations.Language
//import timber.log.Timber
//import java.time.Instant
//import java.time.ZoneId
//import java.time.format.DateTimeFormatter
//import java.util.regex.Pattern
//
//class TimberConsoleExtension : BeforeSpecListener, AfterSpecListener {
//
//    private val tree = ConsoleTree()
//
//    override suspend fun beforeSpec(spec: Spec) {
//        Timber.plant(tree)
//    }
//
//    override suspend fun afterSpec(spec: Spec) {
//        Timber.uprootAll()
//    }
//}
//
//private class ConsoleTree : Timber.DebugTree() {
//
//    @Language("RegExp")
//    private val anonymousClassPattern = Pattern.compile("""(\$\d+)+$""")
//
//    private val dateTimeFormatter = DateTimeFormatter
//        .ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
//        .withZone(ZoneId.systemDefault())
//
//    override fun log(priority: Int, tag: String?, message: String, t: Throwable?) {
//        val dateTime = dateTimeFormatter.format(Instant.now())
//        val priorityChar = when (priority) {
//            Log.VERBOSE -> 'V'
//            Log.DEBUG -> 'D'
//            Log.INFO -> 'I'
//            Log.WARN -> 'W'
//            Log.ERROR -> 'E'
//            Log.ASSERT -> 'A'
//            else -> '?'
//        }
//
//        println("$dateTime $priorityChar/$tag: $message")
//    }
//
//    override fun createStackElementTag(element: StackTraceElement): String {
//        val matcher = anonymousClassPattern.matcher(element.className)
//        val tag = when {
//            matcher.find() -> matcher.replaceAll("")
//            else -> element.className
//        }
//        return tag.substringAfterLast('.')
//    }
//}