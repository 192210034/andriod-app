package com.simats.aspirebridge.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JobCategory(
    val id: String,
    val name: String,
    val description: String,
    val iconRes: Int,
    val colorRes: Int,
    val jobCount: Int,
    val subCategories: List<JobSubCategory> = emptyList()
) : Parcelable

@Parcelize
data class JobSubCategory(
    val id: String,
    val name: String,
    val description: String,
    val jobCount: Int,
    val jobs: List<JobListing> = emptyList()
) : Parcelable

@Parcelize
data class JobListing(
    val id: String,
    val title: String,
    val department: String,
    val organization: String,
    val salaryRange: SalaryRange,
    val vacancies: Int,
    val educationRequirement: String,
    val ageLimit: String,
    val experience: String,
    val jobType: JobType,
    val location: String,
    val applicationDeadline: String,
    val responsibilities: List<String>,
    val eligibilityCriteria: EligibilityCriteria,
    val selectionProcess: List<SelectionStep>,
    val departmentHierarchy: String,
    val isBookmarked: Boolean = false,
    val applicationUrl: String? = null
) : Parcelable

@Parcelize
data class SalaryRange(
    val min: Int,
    val max: Int,
    val currency: String = "INR"
) : Parcelable {
    fun getFormattedRange(): String {
        return "₹${formatAmount(min)} - ${formatAmount(max)}"
    }
    
    private fun formatAmount(amount: Int): String {
        return when {
            amount >= 100000 -> "${amount / 100000}L"
            amount >= 1000 -> "${amount / 1000}K"
            else -> amount.toString()
        }
    }
}

@Parcelize
data class EligibilityCriteria(
    val education: String,
    val ageLimit: String,
    val experience: String,
    val nationality: String = "Indian",
    val additionalRequirements: List<String> = emptyList()
) : Parcelable

@Parcelize
data class SelectionStep(
    val stepNumber: Int,
    val title: String,
    val description: String,
    val isOptional: Boolean = false
) : Parcelable

enum class JobType {
    PERMANENT,
    TEMPORARY,
    CONTRACT,
    APPRENTICESHIP
}

enum class GovernmentLevel {
    CENTRAL,
    STATE,
    LOCAL
}

// Popular job categories for home screen
data class PopularCategory(
    val id: String,
    val name: String,
    val jobCount: Int,
    val iconRes: Int,
    val colorRes: Int
)