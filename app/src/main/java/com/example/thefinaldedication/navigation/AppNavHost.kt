package com.example.thefinaldedication.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.thefinaldedication.Homepage.Homepage
import com.example.thefinaldedication.Homepage.Laundry
import com.example.thefinaldedication.Laundry.Cart
import com.example.thefinaldedication.Payment.PaymentScreen
import com.example.thefinaldedication.Registrationpages.LoginForm
import com.example.thefinaldedication.Registrationpages.OTPVerificationForm
import com.example.thefinaldedication.Registrationpages.SignUpForm
import com.example.thefinaldedication.cart.CartItem
import com.example.thefinaldedication.models.Users

@Composable
fun AppNavHost(navController: NavHostController, startDestination: String = ROUT_PAYMENT) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(ROUT_HOME) {
            Homepage(navController = navController)
        }

        composable(ROUT_SIGNUP) {
            SignUpForm(navController = navController)
        }
        composable(ROUT_LOGIN) {
            LoginForm(navController = navController)
        }
        composable(ROUT_OTP) { backStackEntry ->
            val verificationId = backStackEntry.arguments?.getString("verificationId") ?: ""
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
        composable(ROUT_LAUNDRY) {
            // Placeholder for Laundry screen
             Laundry( navController = navController)
        }

        composable(ROUT_CART) {
            // Placeholder for Cart screen
             Cart(navController = navController, cartItems = listOf())
        }
        composable(ROUT_PAYMENT) { backStackEntry ->
           val cartItems = backStackEntry.arguments?.getParcelableArrayList<android.os.Parcelable>("cartItems")?.filterIsInstance<CartItem>() ?: arrayListOf()
            PaymentScreen(navController = navController, cartItems = cartItems)
        }


    }
}