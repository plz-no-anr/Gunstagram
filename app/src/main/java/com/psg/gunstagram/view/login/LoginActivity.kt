package com.psg.gunstagram.view.login

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Base64
import android.util.Log
import com.google.android.gms.auth.api.Auth
import com.psg.gunstagram.R
import com.psg.gunstagram.databinding.ActivityLoginBinding
import com.psg.gunstagram.util.AppLogger
import com.psg.gunstagram.view.base.BaseActivity
import com.psg.gunstagram.view.base.BaseViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException



class LoginActivity : BaseActivity<ActivityLoginBinding,LoginViewModel>(R.layout.activity_login) {
    override val TAG: String = LoginActivity::class.java.simpleName
    override val viewModel: LoginViewModel by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        setEventFlow()
    }

    private fun setEventFlow(){
        CoroutineScope(Dispatchers.IO).launch{
            viewModel.eventFlow.collect { event -> handleEvent(event) }
        }
    }

    private fun handleEvent(event: BaseViewModel.Event) = when (event){
        is BaseViewModel.Event.ShowToast ->
            CoroutineScope(Dispatchers.Main).launch {
                makeToast(event.text)
            }
        is BaseViewModel.Event.SignEmail ->
            CoroutineScope(Dispatchers.Main).launch {
                signEmail(event.user)
            }
        is BaseViewModel.Event.SignGoogle ->
            CoroutineScope(Dispatchers.Main).launch {
                signGoogle(event.intent,event.code)
            }
    }

    override fun initView(){
        binding.activity = this
        binding.vm = viewModel
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.callbackManager?.onActivityResult(requestCode, resultCode, data)
        if (requestCode == viewModel.GOOGLE_LOGIN_CODE){
            val result = data?.let { Auth.GoogleSignInApi.getSignInResultFromIntent(it) }
            if (result!!.isSuccess){
                val account = result.signInAccount
                viewModel.firebaseAuthWithGoogle(account)
            }
        }
    }




}