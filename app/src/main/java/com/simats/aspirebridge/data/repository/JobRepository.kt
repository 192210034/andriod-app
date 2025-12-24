package com.simats.aspirebridge.data.repository

import com.simats.aspirebridge.R
import com.simats.aspirebridge.data.api.ApiService
import com.simats.aspirebridge.data.model.*

class JobRepository(
    private val apiService: ApiService
) {

    fun getCentralGovernmentCategories(): List<JobCategory> {
        return listOf(
            JobCategory(
                id = "upsc",
                name = "UPSC Group A Services",
                description = "IAS, IPS, IFS, IRS, IAAS, IDAS, IPoS",
                iconRes = R.drawable.ic_upsc,
                colorRes = R.color.warning,
                jobCount = 500
            ),
            JobCategory(
                id = "ssc",
                name = "SSC (Staff Selection Commission)",
                description = "CGL, CHSL, MTS, Stenographer, Constable",
                iconRes = R.drawable.ic_ssc,
                colorRes = R.color.success,
                jobCount = 15000
            ),
            JobCategory(
                id = "railways",
                name = "Indian Railways",
                description = "RRB NTPC, Group D, JE, SSE, Technician",
                iconRes = R.drawable.ic_train,
                colorRes = R.color.info,
                jobCount = 20000
            ),
            JobCategory(
                id = "banking",
                name = "Banking Sector",
                description = "SBI, IBPS, RBI, NABARD, SEBI",
                iconRes = R.drawable.ic_bank,
                colorRes = R.color.achiever_primary,
                jobCount = 8000
            ),
            JobCategory(
                id = "defence",
                name = "Defence Services",
                description = "Army, Navy, Air Force, CAPF, BSF, CRPF",
                iconRes = R.drawable.ic_shield,
                colorRes = R.color.error,
                jobCount = 12000
            ),
            JobCategory(
                id = "psus",
                name = "Public Sector Units (PSUs)",
                description = "ONGC, BHEL, GAIL, IOCL, NTPC, SAIL",
                iconRes = R.drawable.ic_factory,
                colorRes = R.color.secondary,
                jobCount = 5000
            )
        )
    }

    fun getStateGovernmentCategories(): List<JobCategory> {
        return listOf(
            JobCategory(
                id = "police",
                name = "Police Department",
                description = "Constable, SI, ASI, Head Constable, Driver",
                iconRes = R.drawable.ic_shield,
                colorRes = R.color.error,
                jobCount = 8000
            ),
            JobCategory(
                id = "health",
                name = "Health & Medical Services",
                description = "ANM, Staff Nurse, Pharmacist, Lab Technician",
                iconRes = R.drawable.ic_add,
                colorRes = R.color.success,
                jobCount = 6000
            ),
            JobCategory(
                id = "education",
                name = "Education Department",
                description = "Teacher, Principal, Lecturer, Clerk, Peon",
                iconRes = R.drawable.ic_graduation_cap,
                colorRes = R.color.info,
                jobCount = 10000
            ),
            JobCategory(
                id = "courts",
                name = "Courts & Judiciary",
                description = "Clerk, Stenographer, Peon, Attender, Driver",
                iconRes = R.drawable.ic_government_building,
                colorRes = R.color.warning,
                jobCount = 2000
            ),
            JobCategory(
                id = "sachivalayam",
                name = "Sachivalayam (Village/Ward)",
                description = "Village Secretary, Digital Assistant, ANM",
                iconRes = R.drawable.ic_home,
                colorRes = R.color.achiever_primary,
                jobCount = 5000
            ),
            JobCategory(
                id = "revenue",
                name = "Revenue Department",
                description = "VRO, VRA, Tahsildar, Surveyor, Clerk",
                iconRes = R.drawable.ic_work,
                colorRes = R.color.secondary,
                jobCount = 3000
            ),
            JobCategory(
                id = "agriculture",
                name = "Agriculture Department",
                description = "AEO, ADA, Field Assistant, Horticulture Officer",
                iconRes = R.drawable.ic_work,
                colorRes = R.color.success,
                jobCount = 2000
            ),
            JobCategory(
                id = "forest",
                name = "Forest Department",
                description = "Forest Guard, Range Officer, Beat Officer",
                iconRes = R.drawable.ic_work,
                colorRes = R.color.aspirant_primary,
                jobCount = 1500
            ),
            JobCategory(
                id = "electricity",
                name = "Electricity Department",
                description = "Lineman, Junior Assistant, Meter Reader",
                iconRes = R.drawable.ic_factory,
                colorRes = R.color.warning,
                jobCount = 2500
            ),
            JobCategory(
                id = "rtc",
                name = "RTC (Road Transport)",
                description = "Driver, Conductor, Mechanic, Clerk",
                iconRes = R.drawable.ic_train,
                colorRes = R.color.info,
                jobCount = 1000
            )
        )
    }

    fun getPopularCategories(): List<PopularCategory> {
        return listOf(
            PopularCategory(
                id = "teaching",
                name = "Teaching Jobs",
                jobCount = 2500,
                iconRes = R.drawable.ic_graduation_cap,
                colorRes = R.color.info
            ),
            PopularCategory(
                id = "police",
                name = "Police Recruitment",
                jobCount = 1800,
                iconRes = R.drawable.ic_shield,
                colorRes = R.color.error
            ),
            PopularCategory(
                id = "banking",
                name = "Banking Jobs",
                jobCount = 1200,
                iconRes = R.drawable.ic_bank,
                colorRes = R.color.achiever_primary
            ),
            PopularCategory(
                id = "railways",
                name = "Railway Jobs",
                jobCount = 3000,
                iconRes = R.drawable.ic_train,
                colorRes = R.color.info
            )
        )
    }

    fun getSampleJobDetails(): JobListing {
        return JobListing(
            id = "aso_central_secretariat",
            title = "Assistant Section Officer (ASO)",
            department = "Central Secretariat Service",
            organization = "Department of Personnel & Training",
            salaryRange = SalaryRange(25500, 81100),
            vacancies = 500,
            educationRequirement = "Graduate",
            ageLimit = "18-27 years",
            experience = "Fresher can apply",
            jobType = JobType.PERMANENT,
            location = "All India",
            applicationDeadline = "31st March 2024",
            responsibilities = listOf(
                "Assist in administrative functions of the department",
                "Handle correspondence and file management",
                "Coordinate with various sections and departments",
                "Prepare reports and maintain records",
                "Support senior officers in policy implementation"
            ),
            eligibilityCriteria = EligibilityCriteria(
                education = "Bachelor's degree from recognized university",
                ageLimit = "18-27 years (Relaxation as per rules)",
                experience = "Fresher can apply",
                additionalRequirements = listOf(
                    "Computer knowledge essential",
                    "Good communication skills"
                )
            ),
            selectionProcess = listOf(
                SelectionStep(1, "Computer Based Examination (Tier-I)", "Objective type questions"),
                SelectionStep(2, "Descriptive Paper (Tier-II)", "Essay and letter writing"),
                SelectionStep(3, "Document Verification & Medical Examination", "Final verification")
            ),
            departmentHierarchy = "Central Government → Ministry of Personnel → Department of Personnel & Training → Central Secretariat Service"
        )
    }
}