package com.example.law_tech_app.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import com.example.law_tech_app.R
import com.example.law_tech_app.models.Event
import com.example.law_tech_app.utils.Constants
import com.google.android.material.textfield.TextInputEditText

class EventUpdateDetailsActivity : AppCompatActivity() {

    lateinit var subject: TextInputEditText
    lateinit var participants: TextInputEditText
    lateinit var duration: TextInputEditText
    lateinit var notes: TextInputEditText
    lateinit var event:Event
    lateinit var cancel:ImageButton
    lateinit var save:ImageButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_event_details)
        if (intent.hasExtra(Constants.EXTRA_EVENT_DETAILS)){
            event = intent.extras?.get(Constants.EXTRA_EVENT_DETAILS)!! as Event
        }
        subject = findViewById(R.id.tie_updateDetails_subject)
        participants = findViewById(R.id.tie_updateDetails_participants)
        duration = findViewById(R.id.tie_updateDetails_duration)
        notes = findViewById(R.id.tie_updateDetails_notes)
        cancel = findViewById(R.id.ib_updateEvent_cancel)
        cancel.setOnClickListener {
            onBackPressed()
        }
        save = findViewById(R.id.ib_updateEvent_save)
        save.setOnClickListener {

        }
    }
    private fun loadCurrentEventDetails(){
        subject.setText(event.subject)
        for(participant in event.participants){
            participants.setText(participant + " ")
        }
        duration.setText(event.date)

    }

    override fun onResume() {
        super.onResume()
        loadCurrentEventDetails()
    }
}
