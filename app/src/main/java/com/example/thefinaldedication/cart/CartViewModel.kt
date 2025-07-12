// CartViewModel.kt
package com.example.thefinaldedication.Laundry

import androidx.lifecycle.ViewModel
import com.example.thefinaldedication.cart.CartItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems

    fun addItem(item: CartItem) {
        _cartItems.update { currentItems ->
            currentItems.map { cartItem ->
                if (cartItem.name == item.name) {
                    cartItem.copy(quantity = cartItem.quantity + 1)
                } else {
                    cartItem
                }
            }.ifEmpty { currentItems + item }
        }
    }

    fun removeItem(item: CartItem) {
        _cartItems.update { currentItems ->
            currentItems.mapNotNull { cartItem ->
                if (cartItem.name == item.name) {
                    if (cartItem.quantity > 1) {
                        cartItem.copy(quantity = cartItem.quantity - 1)
                    } else {
                        null // Remove the item if quantity is 1
                    }
                } else {
                    cartItem
                }
            }
        }
    }

    fun getTotalPrice(): Double {
        return _cartItems.value.sumOf { it.price * it.quantity }
    }
}
