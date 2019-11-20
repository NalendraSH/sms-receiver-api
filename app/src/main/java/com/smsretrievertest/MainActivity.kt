package com.smsretrievertest

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.phone.SmsRetriever
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startSMSListener()

        val signatures = AppSignatureHelper(this).appSignatures
        for (sign in signatures){
            Log.d("sign", sign)
        }
    }

    private fun startSMSListener() {
        SmsRetriever.getClient(this).startSmsRetriever()
            .addOnSuccessListener {
                text_verif.text = "Waiting for OTP"
                Toast.makeText(this, "SMS Retriever starts", Toast.LENGTH_LONG).show()
            }.addOnFailureListener {
                text_verif.text = "Cannot Start SMS Retriever"
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            }
    }
}
