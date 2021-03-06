package com.psg.gunstagram.util

import android.util.Log

class AppLogger {
    companion object{
        fun d(tag:String,msg:String){
            if (Constants.DEBUG){
                Log.d(tag, msg)
            }
        }

        fun i(tag:String,msg:String){
            if (Constants.DEBUG){
                Log.i(tag, msg)
            }
        }

        fun e(tag:String,msg:String,e:Exception){
            if (Constants.DEBUG){
                Log.e(tag, msg, e)
            }
        }

        fun p(msg:String){
            if (Constants.DEBUG){
                println(msg)
            }
        }

    }
}