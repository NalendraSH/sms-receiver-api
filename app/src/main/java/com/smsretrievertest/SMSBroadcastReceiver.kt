package com.smsretrievertest

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import java.util.regex.Pattern

class SMSBroadcastReceiver: BroadcastReceiver(){

    private var otpReceiver : OTPReceiveListener? = null

    fun initOTPListener(receiver: OTPReceiveListener) {
        this.otpReceiver = receiver
    }

    override fun onReceive(context: Context, intent: Intent) {

        if (SmsRetriever.SMS_RETRIEVED_ACTION == intent.action) {

            val extras = intent.extras
            val status = extras!!.get(SmsRetriever.EXTRA_STATUS) as Status

            when (status.statusCode) {
                CommonStatusCodes.SUCCESS -> {

                    // Get SMS message contents
                    val otp: String = extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String
                    Log.d("otp_message", otp)

                    val pattern = Pattern.compile("(\\d{6})")
                    val matcher = pattern.matcher(otp)

                    // Extract one-time code from the message and complete verification
                    var value = ""
                    if (matcher.find()) {
                        System.out.println(matcher.group(1))
                        value = matcher.group(1)
                    }

                    Log.d("otp message", "message : $value, otp : $otp")
                    otpReceiver?.onOTPReceived(value)
                }

                CommonStatusCodes.TIMEOUT -> {
                    // Waiting for SMS timed out (5 minutes)
                    Log.d("otp_message", "timeout")
                    otpReceiver?.onOTPTimeOut()
                }
            }
        }

    }

    interface OTPReceiveListener {

        fun onOTPReceived(otp: String)

        fun onOTPTimeOut()
    }

}