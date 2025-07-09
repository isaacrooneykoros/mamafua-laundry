package com.example.thefinaldedication.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.thefinaldedication.models.Users
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume

class AuthViewModel : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> get() = _authState


    private val _snackbarChannel = MutableSharedFlow<String>(extraBufferCapacity = 5)
    val snackbarMessage: SharedFlow<String> get() = _snackbarChannel.asSharedFlow()

    private val _navigationChannel = Channel<String>(Channel.CONFLATED)
    val navigationChannel: Channel<String> get() = _navigationChannel

    private lateinit var verificationId: String
    private lateinit var resendingToken: PhoneAuthProvider.ForceResendingToken

    private val auth = FirebaseAuth.getInstance()
    // Use your custom database URL
    private val database: FirebaseDatabase = FirebaseDatabase.getInstance("https://my-application1-775bd-default-rtdb.firebaseio.com/")
    private val usersRef: DatabaseReference = database.getReference("users")

    fun signUp(user: Users) {
        usersRef.child(user.phoneNumber).setValue(user)
            .addOnSuccessListener {
                Log.d("Database", "User data saved successfully")
                viewModelScope.launch {
                    _snackbarChannel.emit("User data saved successfully")
                }
                _authState.value = AuthState.Success
            }
            .addOnFailureListener {
                Log.e("Database", "Error saving user data", it)
                viewModelScope.launch {
                    _snackbarChannel.emit("Error saving user data: ${it.message}")
                }
                _authState.value = AuthState.Error
            }
    }
    suspend fun login(phoneNumber: String, password: String): Boolean {
        return suspendCancellableCoroutine { continuation ->
            usersRef.child(phoneNumber).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val user = snapshot.getValue(Users::class.java)
                        if (user != null && user.password == password) {
                            continuation.resume(true)
                        } else {
                            continuation.resume(false)
                        }
                    } else {
                        continuation.resume(false)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("Database", "Validation cancelled: ${error.message}", error.toException())
                    continuation.resume(false)
                }
            })
        }
    }

    fun startPhoneNumberVerification(phoneNumber: String) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w("PhoneAuth", "onVerificationFailed", e)
                viewModelScope.launch {
                    _snackbarChannel.emit("Verification failed: ${e.message}")
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("PhoneAuth", "onCodeSent:$verificationId")
                this@AuthViewModel.verificationId = verificationId
                resendingToken = token
                _authState.value = AuthState.VerificationCodeSent
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+254$phoneNumber")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun resendVerificationCode(phoneNumber: String) {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Log.w("PhoneAuth", "onVerificationFailed", e)
                viewModelScope.launch {
                    _snackbarChannel.emit("Resend failed: ${e.message}")
                }
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                Log.d("PhoneAuth", "onCodeSent:$verificationId")
                this@AuthViewModel.verificationId = verificationId
                resendingToken = token
                _authState.value = AuthState.VerificationCodeSent
            }
        }

        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+254$phoneNumber")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setCallbacks(callbacks)
            .setForceResendingToken(resendingToken)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOtpCode(
        verificationId: String,
        otp: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val credential = PhoneAuthProvider.getCredential(verificationId, otp)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success
                    viewModelScope.launch {
                        _navigationChannel.send(ROUT_HOME)
                    }
                    onSuccess()
                } else {
                    val errorMsg = task.exception?.message ?: "Verification failed"
                    _authState.value = AuthState.Error
                    viewModelScope.launch {
                        _snackbarChannel.emit("Sign in failed: $errorMsg")
                    }
                    onError(errorMsg)
                }
            }
    }

    fun verifyPhoneNumberWithCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithPhoneAuthCredential(credential)
    }

    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _authState.value = AuthState.Success
                    viewModelScope.launch {
                        _navigationChannel.send(ROUT_HOME)
                    }
                } else {
                    Log.w("PhoneAuth", "signInWithCredential:failure", task.exception)
                    viewModelScope.launch {
                        _snackbarChannel.emit("Sign in failed: ${task.exception?.message}")
                    }
                    _authState.value = AuthState.Error
                }
            }
    }

    fun writeUserData(user: Users, onSuccess: () -> Unit, onError: (String) -> Unit) {
        usersRef.child(user.phoneNumber).setValue(user)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it.message ?: "Failed to save user data") }
    }

    fun showSnackbarMessage(message: String) {
        viewModelScope.launch {
            _snackbarChannel.emit(message)
        }
    }
}

sealed class AuthState {
    object Success : AuthState()
    object LoggedOut : AuthState()
    object Error : AuthState()
    object VerificationCodeSent : AuthState()
}

const val ROUT_HOME = "home"