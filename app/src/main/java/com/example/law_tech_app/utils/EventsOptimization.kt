package com.example.law_tech_app.utils

import android.util.Log
import com.example.law_tech_app.fragments.LawyerCalendarFragment
import com.example.law_tech_app.models.Event
import java.lang.Math.abs
import java.util.*
import kotlin.collections.ArrayList


class EventsOptimization {


    fun calculateMaximumCompletedTasks(n: Int, t: Double, events_durations: ArrayList<Double>, events:ArrayList<Event>): Int {
        var time = t
        if (n == 0) return 0
        val len = events_durations.size
        events_durations.sort()
        val sortedEvents = events.sortedWith(compareBy { it.duration })

        val diff = DoubleArray(len)
        for (i in 0 until len - 1) {
            val temp_diff = sortedEvents[i + 1].preparingDuration
            diff[i] = events_durations[i] + temp_diff
        }
        diff[len - 1] = events_durations[len - 1]
        Arrays.sort(diff)
        var tasks = 0
        for (i in 0 until len) {
            time -= diff[i]
            if (time >= 0) ++tasks else break
        }
        return tasks
    }
    fun calculateCombinations(numbers:ArrayList<Double>, target: Double, partial: ArrayList<Double>,combinationLength:Int,fragment: LawyerCalendarFragment) {
        var s = 0.0
        for (x in partial) s += x
        if (s <= target && (partial.size == combinationLength)) {
            Log.e("matan",
                "sum( " + Arrays.toString(partial.toArray()).toString() + " ) <= " + target
            )
            fragment.combinations.add(partial)
        }
        for (i in 0 until numbers.size) {
            val remaining = ArrayList<Double>()
            val n = numbers[i]
            for (j in i + 1 until numbers.size) remaining.add(numbers[j])
            val partial_rec = ArrayList(partial)
            partial_rec.add(n)
            calculateCombinations(remaining, target, partial_rec,combinationLength,fragment)
        }
    }

    fun createEventsOrderedListByDurations(
        listToOrder: ArrayList<Event>,
        durationsList: ArrayList<Double>
    ): ArrayList<Event> {
        val sortedSubList = listToOrder.sortedBy { item ->
            val index = durationsList.indexOf(item.duration)
            if (index != -1) index else Int.MAX_VALUE // Handle IndexOutOfBoundsException
        }.subList(0,durationsList.size)
        val events = ArrayList<Event>()
        sortedSubList.forEach { events.add(it) }
        return events
    }

}