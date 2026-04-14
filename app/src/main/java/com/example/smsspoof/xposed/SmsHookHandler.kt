package com.example.smsspoof.xposed

import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log

class SmsHookHandler(private val context: Context) {

    fun hookDispatchIntent(classLoader: ClassLoader) {
        // Hooking is handled in SmsSpoofModule for now
        // This class focuses on the modification logic
    }

    fun modifySenderId(intent: Intent, newSenderId: String): Intent {
        // Create a copy of the intent to avoid modifying the original
        val modifiedIntent = Intent(intent)
        
        // Log original intent extras for debugging
        Log.d("SMS_Spoofer", "Original intent extras: ${intent.extras}")
        
        // Based on Android telephony internals, we need to modify specific extras
        // The sender ID is typically stored in the "sender" or "address" extra
        // For incoming SMS, it's often in the "sender" extra of the SMS message
        
        // Try to get the SMS bundle from intent extras
        val smsBundle = intent.getBundleExtra(Telephony.Sms.Intents.GET_MESSAGES_EXTRA)
        if (smsBundle != null) {
            // This is for the GET_MESSAGES_EXTRA which contains an array of SmsMessage
            // We would need to modify each message in the array
            // For simplicity, we're handling the common case where sender is in intent extras
            Log.d("SMS_Spoofer", "Found GET_MESSAGES_EXTRA bundle")
        }
        
        // Common approach: modify the "sender" or "address" extra directly in intent
        // This varies by Android version and device manufacturer
        modifiedIntent.putExtra("sender", newSenderId)
        modifiedIntent.putExtra("address", newSenderId)
        
        // Also try the Telephony.Sms constants
        modifiedIntent.putExtra(Telephony.Sms.Inbox.ADDRESS, newSenderId)
        modifiedIntent.putExtra(Telephony.Sms.SenderDependent.ADDRESS, newSenderId)
        
        Log.d("SMS_Spoofer", "Modified intent with sender ID: $newSenderId")
        Log.d("SMS_Spoofer", "Modified intent extras: ${modifiedIntent.extras}")
        
        return modifiedIntent
    }
    
    // Overload that gets the sender ID from shared preferences or database
    fun modifySenderId(intent: Intent): Intent {
        // In a real implementation, we would get the active sender ID from storage
        // For now, we'll use a default or test value
        val activeSenderId = getActiveSenderId()
        return modifySenderId(intent, activeSenderId)
    }
    
    private fun getActiveSenderId(): String {
        // TODO: Get active sender ID from SharedPreferences or database
        // For now, return a test value
        return "SMS-SPOOF"
    }
}
