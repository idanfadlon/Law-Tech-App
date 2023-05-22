package com.example.law_tech_app.activities


import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.fragments.LawyerCalendarFragment
import com.example.law_tech_app.models.Event
import com.example.law_tech_app.utils.Constants
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar


class AddEventActivity : BaseActivity() {
    lateinit var tie_subject:TextInputEditText
    lateinit var tie_participants:TextInputEditText
    lateinit var tie_description:TextInputEditText
    lateinit var tie_preparing:TextInputEditText
    lateinit var typesSpinner:Spinner
    lateinit var prioritySpinner:Spinner
    lateinit var addBtn:ImageButton
    lateinit var currentDayAndMonth:ArrayList<Int>
    lateinit var pickTimeEnd:Button
    lateinit var pickTimeStart:Button
    lateinit var calendarEnd:Calendar
    lateinit var calendarStart:Calendar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        if (intent.hasExtra(Constants.EXTRA_EVENT_DETAILS)){
            currentDayAndMonth = (intent.extras?.get(Constants.EXTRA_EVENT_DETAILS) as ArrayList<Int>)
        }
        tie_subject = findViewById(R.id.tie_addEvent_subject)
        tie_participants = findViewById(R.id.tie_addEvent_participants)
        tie_description = findViewById(R.id.tie_addEvent_description)
        tie_preparing = findViewById(R.id.tie_addEvent_preparing)
        typesSpinner = findViewById(R.id.spinner_addEvent_type)
        prioritySpinner = findViewById(R.id.spinner_addEvent_priority)
        addBtn = findViewById(R.id.ib_addEvent_add)
        addBtn.setOnClickListener {
            if(validateInput()){
                addEventToFirestore()
            }
        }
        pickTimeStart = findViewById(R.id.btn_addEvent_pickTime_start)
        pickTimeStart.setOnClickListener {
            calendarStart = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker,hour,minute ->
                calendarStart.set(Calendar.HOUR_OF_DAY,hour)
                calendarStart.set(Calendar.MINUTE,minute)
                pickTimeStart.text = SimpleDateFormat("HH:mm").format(calendarStart.time)
            }
            TimePickerDialog(this,timeSetListener,calendarStart.get(Calendar.HOUR_OF_DAY),calendarStart.get(Calendar.MINUTE),true).show()
        }
        pickTimeEnd = findViewById(R.id.btn_addEvent_pickTime_end)
        pickTimeEnd.setOnClickListener {
            calendarEnd = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener{timePicker,hour,minute ->
                calendarEnd.set(Calendar.HOUR_OF_DAY,hour)
                calendarEnd.set(Calendar.MINUTE,minute)
                pickTimeEnd.text = SimpleDateFormat("HH:mm").format(calendarEnd.time)
            }
            TimePickerDialog(this,timeSetListener,calendarEnd.get(Calendar.HOUR_OF_DAY),calendarEnd.get(Calendar.MINUTE),true).show()
        }

        ArrayAdapter.createFromResource(
            this,
            R.array.types_spinner_items,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.select_dialog_item)
            typesSpinner.adapter = adapter
        }
        ArrayAdapter.createFromResource(
            this,
            R.array.priority_spinner_items,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.select_dialog_item)
            prioritySpinner.adapter = adapter
        }
    }

    private fun addEventToFirestore(){
        showProgressDialog(resources.getString(R.string.loading))
        val duration = ((calendarEnd.get(Calendar.HOUR_OF_DAY) - calendarStart.get(Calendar.HOUR_OF_DAY)) * 60) + (calendarEnd.get(Calendar.MINUTE) - calendarStart.get(Calendar.MINUTE))
        val event = Event(
            "",
            FirestoreClass().getCurrentUserID(),
            tie_participants.text.toString(),
            tie_subject.text.toString(),
            currentDayAndMonth[0],
            currentDayAndMonth[1],
            prioritySpinner.selectedItem.toString(),
            duration.toDouble(),
            tie_description.text.toString(),
            typesSpinner.selectedItem.toString(),
            tie_preparing.text.toString().toDouble(),
            SimpleDateFormat("HH:mm").format(calendarStart.time) + "-" + SimpleDateFormat("HH:mm").format(calendarEnd.time)
        )
        FirestoreClass().addEventToFirestore(event,this)

    }
    fun addEventSuccess(){
        hideProgressDialog()
        val intent = Intent(this,LawyerMainActivity::class.java)
        intent.putExtra(Constants.IS_EVENT_ADDED,true)
        startActivity(intent)
        finish()
    }
    private fun validateInput():Boolean {
        return when {
            TextUtils.isEmpty(tie_subject.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter subject !", true)
                false
            }
            TextUtils.isEmpty(tie_participants.text.toString().trim { it <= ' ' })->{
                showErrorSnackBar("Please enter participants !",true)
                false
            }
            TextUtils.isEmpty(tie_description.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter a little description !", true)
                false
            }
            else -> {
                true
            }
        }

    }
}