package com.simats.aspirebridge.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Wallet data models for the mentorship platform
 */

@Parcelize
data class Wallet(
    val userId: String,
    val balance: Float,
    val totalEarnings: Float = 0f,
    val totalSpent: Float = 0f,
    val monthlyEarnings: Float = 0f,
    val monthlySpent: Float = 0f,
    val pendingAmount: Float = 0f,
    val lastUpdated: Long = System.currentTimeMillis()
) : Parcelable

@Parcelize
data class Transaction(
    val id: String,
    val userId: String,
    val type: TransactionType,
    val amount: Float,
    val description: String,
    val status: TransactionStatus,
    val sessionId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val completedAt: Long? = null,
    val referenceId: String? = null
) : Parcelable

enum class TransactionType {
    CREDIT,
    DEBIT,
    REFUND,
    WITHDRAWAL,
    DEPOSIT,
    SESSION_PAYMENT,
    SESSION_EARNING
}

enum class TransactionStatus {
    PENDING,
    COMPLETED,
    FAILED,
    CANCELLED,
    PROCESSING
}

@Parcelize
data class PaymentMethod(
    val id: String,
    val userId: String,
    val type: PaymentMethodType,
    val displayName: String,
    val lastFourDigits: String? = null,
    val expiryMonth: Int? = null,
    val expiryYear: Int? = null,
    val isDefault: Boolean = false,
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
) : Parcelable

enum class PaymentMethodType {
    CREDIT_CARD,
    DEBIT_CARD,
    UPI,
    NET_BANKING,
    WALLET
}