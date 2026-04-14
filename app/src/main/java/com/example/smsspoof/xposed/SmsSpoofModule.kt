package com.example.smsspoof.xposed

import android.content.Context
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam

class SmsSpoofModule : IXposedHookLoadPackage {
    private lateinit var context: Context
    private lateinit var hookHandler: SmsHookHandler

    override fun handleLoadPackage(param: LoadPackageParam) {
        // Initialize context and hook handler
        context = param.classLoader.loadClass("android.app.ActivityThread")
            .getMethod("currentApplication")
            .invoke(null) as Context
        hookHandler = SmsHookHandler(context)

        // Hook into InboundSmsHandler.dispatchIntent
        try {
            val clazz = XposedHelpers.findClass(
                "com.android.internal.telephony.InboundSmsHandler",
                param.classLoader
            )
            
            XposedBridge.hookMethod(
                clazz.getDeclaredMethod("dispatchIntent", android.content.Intent::class.java),
                object : XC_MethodHook() {
                    override fun beforeHookedMethod(param: XC_MethodHook.MethodHookParam) {
                        val intent = param.args[0] as android.content.Intent
                        val modifiedIntent = hookHandler.modifySenderId(intent)
                        param.args[0] = modifiedIntent
                    }
                }
            )
        } catch (e: Exception) {
            XposedBridge.log("SMS Spoofer: Error hooking InboundSmsHandler: ${e.message}")
        }
    }
}
