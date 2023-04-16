package com.example.law_tech_app.activities


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.Toast
import com.example.law_tech_app.Firestore.FirestoreClass
import com.example.law_tech_app.R
import com.example.law_tech_app.fragments.LawyerCalendarFragment
import com.example.law_tech_app.models.Event
import com.example.law_tech_app.utils.Constants
import com.google.android.material.textfield.TextInputEditText


class AddEventActivity : BaseActivity() {
    lateinit var tie_subject:TextInputEditText
    lateinit var tie_participants:TextInputEditText
    lateinit var tie_description:TextInputEditText
    lateinit var tie_duration:TextInputEditText
    lateinit var typesSpinner:Spinner
    lateinit var prioritySpinner:Spinner
    lateinit var freqsSpinner:Spinner
    lateinit var addBtn:ImageButton
    lateinit var currentDayAndMonth:ArrayList<Int>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        if (intent.hasExtra(Constants.EXTRA_EVENT_DETAILS)){
            currentDayAndMonth = (intent.extras?.get(Constants.EXTRA_EVENT_DETAILS) as ArrayList<Int>)
        }
        tie_subject = findViewById(R.id.tie_addEvent_subject)
        tie_participants = findViewById(R.id.tie_addEvent_participants)
        tie_description = findViewById(R.id.tie_addEvent_description)
        tie_duration = findViewById(R.id.tie_addEvent_duration)
        typesSpinner = findViewById(R.id.spinner_addEvent_type)
        prioritySpinner = findViewById(R.id.spinner_addEvent_priority)
        freqsSpinner = findViewById(R.id.spinner_addEvent_freq)
        addBtn = findViewById(R.id.ib_addEvent_add)
        addBtn.setOnClickListener {
            if(validateInput()){
                addEventToFirestore()
            }
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
            R.array.freqs_spinner_items,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.select_dialog_item)
            freqsSpinner.adapter = adapter
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
        val event = Event(
            "",
            FirestoreClass().getCurrentUserID(),
            tie_participants.text.toString(),
            tie_subject.text.toString(),
            currentDayAndMonth[0],
            currentDayAndMonth[1],
            prioritySpinner.selectedItem.toString(),
            tie_duration.text.toString().toDouble(),
            tie_description.text.toString(),
            typesSpinner.selectedItem.toString(),
            freqsSpinner.selectedItem.toString()
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
            TextUtils.isEmpty(tie_duration.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar("Please enter duration !", true)
                false
            }
            else -> {
                true
            }
        }

    }
}