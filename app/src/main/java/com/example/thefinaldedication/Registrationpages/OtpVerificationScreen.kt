package com.example.thefinaldedication.Registrationpages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.*
import com.example.thefinaldedication.R
import com.example.thefinaldedication.data.AuthViewModel
import com.example.thefinaldedication.models.Users
import com.example.thefinaldedication.ui.theme.LaundryBlue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OTPVerificationForm(
    navController: NavController,
    verificationId: String,
    phoneNumber: String,
    user: Users
) {
    val viewModel: AuthViewModel = viewModel()
    var otp by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val timer = remember { mutableStateOf(60) }
    val isResendEnabled = remember { mutableStateOf(false) }

    // Listen for navigation events
    LaunchedEffect(Unit) {
        for (route in viewModel.navigationChannel) {
            if (route == "home") {
                navController.navigate("home") {
                    popUpTo("otp_verification") { inclusive = true }
                }
            }
        }
    }

    // Listen for snackbar messages
    LaunchedEffect(Unit) {
        viewModel.snackbarMessage.collect { message ->
            snackbarHostState.showSnackbar(message)
        }
    }

    // Timer for resend OTP
    LaunchedEffect(key1 = timer.value) {
        if (timer.value > 0) {
            delay(1000)
            timer.value--
        } else {
            isResendEnabled.value = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 400.dp)
                .padding(16.dp)
                .align(Alignment.Center),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.laundryform2))
                val progress by animateLottieCompositionAsState(
                    composition = composition,
                    iterations = LottieConstants.IterateForever
                )

                LottieAnimation(
                    composition = composition,
                    progress = progress,
                    modifier = Modifier
                        .background(Color.White)
                        .align(Alignment.Center),
                    contentScale = ContentScale.Fit
                )

                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Verify Your Phone Number",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = "Enter the 6-digit code sent to your phone",
                        fontSize = 16.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    OutlinedTextField(
                        value = otp,
                        onValueChange = { if (it.length <= 6) otp = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        placeholder = { Text("123456") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        textStyle = TextStyle(fontWeight = FontWeight.Bold, color = Color.Black)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    TextButton(
                        onClick = {
                            if (isResendEnabled.value) {
                                isResendEnabled.value = false
                                timer.value = 60
                                viewModel.resendVerificationCode(phoneNumber)
                                scope.launch {
                                    snackbarHostState.showSnackbar("OTP resent")
                                }
                            }
                        },
                        enabled = isResendEnabled.value
                    ) {
                        Text(
                            text = if (timer.value > 0) "Resend OTP in ${timer.value}s" else "Resend OTP",
                            color = if (isResendEnabled.value) LaundryBlue else Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            isLoading = true
                            viewModel.verifyOtpCode(
                                verificationId = verificationId,
                                otp = otp,
                                onSuccess = {
                                    viewModel.writeUserData(
                                        user = user,
                                        onSuccess = {
                                            isLoading = false
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Verification successful!")
                                            }
                                        },
                                        onError = { errorMsg ->
                                            isLoading = false
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Failed to save user: $errorMsg")
                                            }
                                        }
                                    )
                                },
                                onError = { errorMsg ->
                                    isLoading = false
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Verification failed: $errorMsg")
                                    }
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LaundryBlue
                        ),
                        enabled = !isLoading && otp.length == 6
                    ) {
                        Text(if (isLoading) "Verifying..." else "Verify", color = Color.White)
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Preview
@Composable
fun PreviewOTPVerificationForm() {
    OTPVerificationForm(
        navController = rememberNavController(),
        verificationId = "dummyId",
        phoneNumber = "712345678",
        user = Users(
            phoneNumber = "712345678",
            password = "password",
            // Add other required fields for Users
        )
    )
}