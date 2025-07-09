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
import com.example.thefinaldedication.navigation.ROUT_LOGIN
import com.example.thefinaldedication.navigation.ROUT_SIGNUP
import com.example.thefinaldedication.ui.theme.LaundryBlue
import com.example.thefinaldedication.ui.theme.LaundryLightBlue
import com.example.thefinaldedication.data.AuthViewModel
import com.example.thefinaldedication.models.Users
import kotlinx.coroutines.launch

@Composable
fun SignUpForm(navController: NavController) {
    val viewModel: AuthViewModel = viewModel()
    var name by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val nameError = remember { mutableStateOf("") }
    val phoneNumberError = remember { mutableStateOf("") }
    val passwordError = remember { mutableStateOf("") }

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
                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.signup))
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
                        text = "Hello, There 😊👋",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Text(
                        text = "Welcome to MamaFua Laundry app",
                        fontSize = 16.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Full Name field
                    Text(
                        text = "Full Name",
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.align(Alignment.Start),
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier
                            .fillMaxWidth(),
                        placeholder = { Text("John Sam") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        textStyle = TextStyle(fontWeight = FontWeight.Bold, color = Color.Black),
                        isError = nameError.value.isNotEmpty(),
                        supportingText = { if (nameError.value.isNotEmpty()) Text(nameError.value, color = Color.Red) }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Phone Number field
                    Text(
                        text = "Phone Number",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.Start)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        modifier = Modifier
                            .fillMaxWidth(),
                        placeholder = { Text("0712345689") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        textStyle = TextStyle(fontWeight = FontWeight.Bold, color = Color.Black),
                        isError = phoneNumberError.value.isNotEmpty(),
                        supportingText = { if (phoneNumberError.value.isNotEmpty()) Text(phoneNumberError.value, color = Color.Red) }
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
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                        )
                    }

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        modifier = Modifier
                            .fillMaxWidth(),
                        placeholder = { Text("••••••••") },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        textStyle = TextStyle(fontWeight = FontWeight.Bold, color = Color.Black),
                        isError = passwordError.value.isNotEmpty(),
                        supportingText = { if (passwordError.value.isNotEmpty()) Text(passwordError.value, color = Color.Red) }
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Remember me checkbox
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = false, // Remember me functionality not implemented yet
                            onCheckedChange = { /* Handle remember me */true }
                        )

                        Text(
                            text = "Remember me",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Sign up button
                    Button(
                        onClick = {
                            var isValid = true
                            if (name.isBlank()) {
                                nameError.value = "Name is required"
                                isValid = false
                            } else {
                                nameError.value = ""
                            }
                            if (phoneNumber.isBlank()) {
                                phoneNumberError.value = "Phone number is required"
                                isValid = false
                            } else {
                                phoneNumberError.value = ""
                            }
                            if (password.isBlank()) {
                                passwordError.value = "Password is required"
                                isValid = false
                            } else {
                                passwordError.value = ""
                            }

                            if (isValid) {
                                isLoading = true
                                scope.launch {
                                    viewModel.signUp(Users(name, phoneNumber, password))
                                    isLoading = false
                                    navController.navigate(ROUT_LOGIN)
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LaundryBlue
                        ),
                        enabled = !isLoading && phoneNumber.isNotBlank() && password.isNotBlank()

                    ) {
                        Text(if (isLoading) "Signing in..." else "Sign Up")
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    // Login link
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already have an account? ",
                            color = Color.Gray,
                            fontSize = 14.sp
                        )

                        TextButton(onClick = { navController.navigate(ROUT_LOGIN) }) {
                            Text(
                                text = "Login to account",
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
fun PreviewSignUpForm() {
    SignUpForm(navController = rememberNavController())
}
