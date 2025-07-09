package com.example.thefinaldedication.Registrationpages

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.airbnb.lottie.compose.*
import com.example.thefinaldedication.R
import com.example.thefinaldedication.navigation.ROUT_OTP
import com.example.thefinaldedication.navigation.ROUT_SIGNUP
import com.example.thefinaldedication.ui.theme.LaundryBlue
import com.example.thefinaldedication.ui.theme.LaundryLightBlue
import com.example.thefinaldedication.data.AuthViewModel
import com.example.thefinaldedication.models.Users
import kotlinx.coroutines.launch

@Composable
fun LoginForm(navController: NavController) {
    val viewModel: AuthViewModel = viewModel()
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Form Card with Lottie background
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
                // Lottie Animation
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

                // Login Form Content
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(LaundryLightBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        WashingMachineIcon(
                            modifier = Modifier.size(48.dp),
                            tint = LaundryBlue
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Title
                    Text(
                        text = "Welcome Back",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = "Sign in to your MamaFua Laundry account",
                        fontSize = 16.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Phone Number field
                    Text(
                        text = "Phone Number",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        placeholder = { Text("0712345689") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        textStyle = TextStyle(fontWeight = FontWeight.Bold, color = Color.Black)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password field with forgot password link
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Password",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )

                        TextButton(onClick = { /* TODO: handle forgot password */ }) {
                            Text("Forgot password?", color = LaundryBlue)
                        }
                    }

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        placeholder = { Text("••••••••") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        textStyle = TextStyle(fontWeight = FontWeight.Bold, color = Color.Black),
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sign in button
                    Button(
                        onClick = {
                            isLoading = true
                            scope.launch {
                                try {
                                    // Validate user data against the database
                                    if (viewModel.login(phoneNumber, password)) {
                                        isLoading = false
                                        viewModel.startPhoneNumberVerification(phoneNumber)
                                    } else {
                                        isLoading = false
                                        viewModel.showSnackbarMessage("Invalid phone number or password")
                                    }
                                } catch (e: Exception) {
                                    isLoading = false
                                    viewModel.showSnackbarMessage("An error occurred: ${e.message}")
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LaundryBlue
                        ),
                        enabled = !isLoading
                    ) {
                        Text(if (isLoading) "Signing in..." else "Sign In")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Sign up link
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Don't have an account? ",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )

                        TextButton(onClick = { navController.navigate(ROUT_SIGNUP) }) {
                            Text(
                                text = "Create account",
                                color = LaundryBlue,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Snackbar for notifications
        val snackbarHostState = remember { SnackbarHostState() }
        LaunchedEffect(viewModel.snackbarMessage) {
            viewModel.snackbarMessage.collect { message ->
                snackbarHostState.showSnackbar(message)
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
fun PreviewLoginForm() {
    LoginForm(navController = rememberNavController())
}
