package vn.ghn.library.app

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

abstract class BaseApp : Application(){
    abstract fun shareRef() : SharedPreferences

}