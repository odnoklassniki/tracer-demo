package ru.ok.tracer.demo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.ok.tracer.crash.report.TracerCrashReport
import ru.ok.tracer.disk.usage.DiskUsage
import ru.ok.tracer.profiler.sampling.SamplingProfiler
import ru.ok.tracer.profiler.systrace.SystraceProfiler
import java.io.File
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.random.Random

class TracerDemoActivity : AppCompatActivity(R.layout.activity_tracer_demo) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.run_oom).setOnClickListener {
            val list = mutableListOf<Any>()
            while (true) {
                list.add(IntArray(1024 * 1024))
            }
        }
        findViewById<View>(R.id.run_disk_usage).setOnClickListener {
            for (i in 1..10) {
                val file = File(filesDir, "test_file_$i.tmp")
                file.writeBytes(Random.nextBytes(1024 * 1024))
            }
            DiskUsage.runNow(this)
        }
        findViewById<View>(R.id.make_crash).setOnClickListener {
            TracerCrashReport.log("Crash button clicked!")
            divideByZero()
        }
        findViewById<View>(R.id.make_native_crash).setOnClickListener {
            TracerCrashReport.log("Native crash button clicked!")
            nativeDereferenceNull()
        }
        findViewById<View>(R.id.make_anr).setOnClickListener {
            TracerCrashReport.log("ANR button clicked!")
            val countDownLatch = CountDownLatch(1)
            val thread = Thread {
                synchronized(this@TracerDemoActivity) {
                    countDownLatch.countDown()
                    infiniteMethod()
                }
            }
            thread.start()
            countDownLatch.await()
            infiniteMethod()

        }
        findViewById<View>(R.id.make_non_fatal_crash).setOnClickListener {
            try {
                divideByZero()
            } catch (e: Exception) {
                TracerCrashReport.report(e, "Test issue key")
            }
        }
        findViewById<View>(R.id.start_sampled_trace).setOnClickListener {
            SamplingProfiler.start(
                context = this,
                tag = "test_sampled_trace",
                duration = 5000,
            )
        }
        findViewById<View>(R.id.abort_sampled_trace).setOnClickListener {
            SamplingProfiler.abort()
        }
        findViewById<View>(R.id.commit_sampled_trace).setOnClickListener {
            SamplingProfiler.commit("commit")
        }
        findViewById<View>(R.id.start_systrace).setOnClickListener {
            SystraceProfiler.start(
                context = this,
                tag = "test_systrace",
                duration = 5000,
            )
        }
        findViewById<View>(R.id.abort_systrace).setOnClickListener {
            SystraceProfiler.abort()
        }
        findViewById<View>(R.id.commit_systrace).setOnClickListener {
            SystraceProfiler.commit("commit")
        }
    }

    private fun divideByZero() {
        "0".toInt() / "0".toInt()
    }

    @Synchronized
    private fun infiniteMethod() {
        @Suppress("ControlFlowWithEmptyBody")
        while (true);
    }

    private external fun nativeDereferenceNull()
}