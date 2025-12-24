package com.simats.aspirebridge.data.repository

import com.simats.aspirebridge.R
import com.simats.aspirebridge.data.api.ApiService
import com.simats.aspirebridge.data.model.*

class ExamRepository(
    private val apiService: ApiService
) {

    fun getAllExamCategories(): List<ExamCategory> {
        return listOf(
            getUpscCategory(),
            getSscCategory(),
            getRailwaysCategory(),
            getBankingCategory(),
            getDefenceCategory(),
            getPsuCategory(),
            getStateGovernmentCategory()
        )
    }

    fun getExamCategoryById(id: String): ExamCategory? {
        return getAllExamCategories().find { it.id == id }
    }

    fun getExamSubcategoryById(categoryId: String, subcategoryId: String): ExamSubcategory? {
        return getExamCategoryById(categoryId)?.subcategories?.find { it.id == subcategoryId }
    }

    fun searchExamCategories(query: String): List<ExamCategory> {
        return getAllExamCategories().filter { category ->
            category.name.contains(query, ignoreCase = true) ||
            category.fullName.contains(query, ignoreCase = true) ||
            category.subcategories.any { 
                it.name.contains(query, ignoreCase = true) || 
                it.fullName.contains(query, ignoreCase = true) 
            }
        }
    }

    private fun getUpscCategory(): ExamCategory {
        return ExamCategory(
            id = "upsc",
            name = "UPSC",
            fullName = "Union Public Service Commission",
            description = "Premier civil services examination for Group A and Group B services",
            iconRes = R.drawable.ic_upsc,
            colorRes = R.color.warning,
            subcategories = listOf(
                ExamSubcategory(
                    id = "upsc_civil_services",
                    name = "Civil Services",
                    fullName = "Civil Services Examination (IAS/IPS/IFS)",
                    parentCategoryId = "upsc",
                    description = "Premier examination for IAS, IPS, IFS and other Group A services",
                    eligibility = "Graduate from recognized university, Age: 21-32 years",
                    examPattern = "Prelims (MCQ) + Mains (Descriptive) + Interview",
                    examFrequency = "Annual",
                    officialWebsite = "https://upsc.gov.in"
                ),
                ExamSubcategory(
                    id = "upsc_engineering_services",
                    name = "Engineering Services",
                    fullName = "Engineering Services Examination (IES)",
                    parentCategoryId = "upsc",
                    description = "For recruitment to Group A engineering services",
                    eligibility = "Engineering degree, Age: 21-30 years",
                    examPattern = "Prelims + Mains + Interview",
                    examFrequency = "Annual",
                    officialWebsite = "https://upsc.gov.in"
                ),
                ExamSubcategory(
                    id = "upsc_forest_services",
                    name = "Forest Services",
                    fullName = "Indian Forest Service Examination",
                    parentCategoryId = "upsc",
                    description = "For recruitment to Indian Forest Service",
                    eligibility = "Graduate with science subjects, Age: 21-32 years",
                    examPattern = "Prelims + Mains + Interview",
                    examFrequency = "Annual",
                    officialWebsite = "https://upsc.gov.in"
                ),
                ExamSubcategory(
                    id = "upsc_medical_services",
                    name = "Medical Services",
                    fullName = "Combined Medical Services Examination",
                    parentCategoryId = "upsc",
                    description = "For various medical services under Government of India",
                    eligibility = "MBBS degree, Age: 21-32 years",
                    examPattern = "Prelims + Interview",
                    examFrequency = "Annual",
                    officialWebsite = "https://upsc.gov.in"
                )
            )
        )
    }

    private fun getSscCategory(): ExamCategory {
        return ExamCategory(
            id = "ssc",
            name = "SSC",
            fullName = "Staff Selection Commission",
            description = "Recruitment for Group B and Group C posts in Government of India",
            iconRes = R.drawable.ic_ssc,
            colorRes = R.color.success,
            subcategories = listOf(
                ExamSubcategory(
                    id = "ssc_cgl",
                    name = "SSC CGL",
                    fullName = "Combined Graduate Level Examination",
                    parentCategoryId = "ssc",
                    description = "For Group B and Group C posts requiring graduate qualification",
                    eligibility = "Graduate, Age: 18-32 years (varies by post)",
                    examPattern = "Tier I + Tier II + Tier III + Tier IV",
                    examFrequency = "Annual",
                    officialWebsite = "https://ssc.nic.in"
                ),
                ExamSubcategory(
                    id = "ssc_chsl",
                    name = "SSC CHSL",
                    fullName = "Combined Higher Secondary Level Examination",
                    parentCategoryId = "ssc",
                    description = "For posts requiring 12th pass qualification",
                    eligibility = "12th pass, Age: 18-27 years",
                    examPattern = "Tier I + Tier II + Tier III",
                    examFrequency = "Annual",
                    officialWebsite = "https://ssc.nic.in"
                ),
                ExamSubcategory(
                    id = "ssc_mts",
                    name = "SSC MTS",
                    fullName = "Multi Tasking Staff Examination",
                    parentCategoryId = "ssc",
                    description = "For Multi Tasking Staff posts",
                    eligibility = "10th pass, Age: 18-25 years",
                    examPattern = "Paper I + Paper II",
                    examFrequency = "Annual",
                    officialWebsite = "https://ssc.nic.in"
                ),
                ExamSubcategory(
                    id = "ssc_stenographer",
                    name = "SSC Stenographer",
                    fullName = "Stenographer Grade C & D Examination",
                    parentCategoryId = "ssc",
                    description = "For Stenographer posts",
                    eligibility = "12th pass, Age: 18-27 years",
                    examPattern = "Computer Based Test + Skill Test",
                    examFrequency = "Annual",
                    officialWebsite = "https://ssc.nic.in"
                ),
                ExamSubcategory(
                    id = "ssc_je",
                    name = "SSC JE",
                    fullName = "Junior Engineer Examination",
                    parentCategoryId = "ssc",
                    description = "For Junior Engineer posts",
                    eligibility = "Engineering Diploma/Degree, Age: 18-32 years",
                    examPattern = "Paper I + Paper II",
                    examFrequency = "Annual",
                    officialWebsite = "https://ssc.nic.in"
                )
            )
        )
    }

    private fun getRailwaysCategory(): ExamCategory {
        return ExamCategory(
            id = "railways",
            name = "Railways",
            fullName = "Indian Railways",
            description = "Recruitment for various posts in Indian Railways",
            iconRes = R.drawable.ic_train,
            colorRes = R.color.info,
            subcategories = listOf(
                ExamSubcategory(
                    id = "rrb_ntpc",
                    name = "RRB NTPC",
                    fullName = "Non-Technical Popular Categories",
                    parentCategoryId = "railways",
                    description = "For non-technical posts in Railways",
                    eligibility = "Graduate/12th pass (varies by post), Age: 18-33 years",
                    examPattern = "CBT 1 + CBT 2 + Typing/Computer Test + Document Verification",
                    examFrequency = "As per requirement",
                    officialWebsite = "https://www.rrbcdg.gov.in"
                ),
                ExamSubcategory(
                    id = "rrb_group_d",
                    name = "RRB Group D",
                    fullName = "Railway Recruitment Board Group D",
                    parentCategoryId = "railways",
                    description = "For Group D posts in Railways",
                    eligibility = "10th pass, Age: 18-33 years",
                    examPattern = "CBT + Physical Efficiency Test + Document Verification",
                    examFrequency = "As per requirement",
                    officialWebsite = "https://www.rrbcdg.gov.in"
                ),
                ExamSubcategory(
                    id = "rrb_je",
                    name = "RRB JE",
                    fullName = "Junior Engineer Examination",
                    parentCategoryId = "railways",
                    description = "For Junior Engineer posts in Railways",
                    eligibility = "Engineering Diploma/Degree, Age: 18-33 years",
                    examPattern = "CBT 1 + CBT 2 + Document Verification + Medical Examination",
                    examFrequency = "As per requirement",
                    officialWebsite = "https://www.rrbcdg.gov.in"
                ),
                ExamSubcategory(
                    id = "rrb_alp",
                    name = "RRB ALP",
                    fullName = "Assistant Loco Pilot Examination",
                    parentCategoryId = "railways",
                    description = "For Assistant Loco Pilot posts",
                    eligibility = "ITI/Diploma, Age: 18-28 years",
                    examPattern = "CBT 1 + CBT 2 + Computer Based Aptitude Test + Document Verification",
                    examFrequency = "As per requirement",
                    officialWebsite = "https://www.rrbcdg.gov.in"
                )
            )
        )
    }

    private fun getBankingCategory(): ExamCategory {
        return ExamCategory(
            id = "banking",
            name = "Banking",
            fullName = "Banking Sector Examinations",
            description = "Recruitment for various posts in banking sector",
            iconRes = R.drawable.ic_bank,
            colorRes = R.color.achiever_primary,
            subcategories = listOf(
                ExamSubcategory(
                    id = "sbi_po",
                    name = "SBI PO",
                    fullName = "State Bank of India Probationary Officer",
                    parentCategoryId = "banking",
                    description = "For Probationary Officer posts in SBI",
                    eligibility = "Graduate, Age: 21-30 years",
                    examPattern = "Prelims + Mains + Group Exercise & Interview",
                    examFrequency = "Annual",
                    officialWebsite = "https://sbi.co.in/careers"
                ),
                ExamSubcategory(
                    id = "ibps_po",
                    name = "IBPS PO",
                    fullName = "Institute of Banking Personnel Selection PO",
                    parentCategoryId = "banking",
                    description = "For Probationary Officer posts in public sector banks",
                    eligibility = "Graduate, Age: 20-30 years",
                    examPattern = "Prelims + Mains + Interview",
                    examFrequency = "Annual",
                    officialWebsite = "https://www.ibps.in"
                ),
                ExamSubcategory(
                    id = "rbi_grade_b",
                    name = "RBI Grade B",
                    fullName = "Reserve Bank of India Grade B Officer",
                    parentCategoryId = "banking",
                    description = "For Grade B Officer posts in RBI",
                    eligibility = "Graduate with 60% marks, Age: 21-30 years",
                    examPattern = "Prelims + Mains + Interview",
                    examFrequency = "Annual",
                    officialWebsite = "https://www.rbi.org.in"
                ),
                ExamSubcategory(
                    id = "nabard_grade_a",
                    name = "NABARD Grade A",
                    fullName = "National Bank for Agriculture and Rural Development",
                    parentCategoryId = "banking",
                    description = "For Grade A Officer posts in NABARD",
                    eligibility = "Graduate with 60% marks, Age: 21-30 years",
                    examPattern = "Prelims + Mains + Interview",
                    examFrequency = "Annual",
                    officialWebsite = "https://www.nabard.org"
                )
            )
        )
    }

    private fun getDefenceCategory(): ExamCategory {
        return ExamCategory(
            id = "defence",
            name = "Defence",
            fullName = "Defence Services",
            description = "Recruitment for Indian Armed Forces and paramilitary forces",
            iconRes = R.drawable.ic_shield,
            colorRes = R.color.error,
            subcategories = listOf(
                ExamSubcategory(
                    id = "nda",
                    name = "NDA",
                    fullName = "National Defence Academy",
                    parentCategoryId = "defence",
                    description = "For entry into Army, Navy and Air Force",
                    eligibility = "12th pass (unmarried male), Age: 16.5-19.5 years",
                    examPattern = "Written Exam + SSB Interview",
                    examFrequency = "Bi-annual",
                    officialWebsite = "https://upsc.gov.in"
                ),
                ExamSubcategory(
                    id = "cds",
                    name = "CDS",
                    fullName = "Combined Defence Services",
                    parentCategoryId = "defence",
                    description = "For officer entry in Army, Navy and Air Force",
                    eligibility = "Graduate, Age: 19-25 years (varies by service)",
                    examPattern = "Written Exam + SSB Interview",
                    examFrequency = "Bi-annual",
                    officialWebsite = "https://upsc.gov.in"
                ),
                ExamSubcategory(
                    id = "afcat",
                    name = "AFCAT",
                    fullName = "Air Force Common Admission Test",
                    parentCategoryId = "defence",
                    description = "For officer entry in Indian Air Force",
                    eligibility = "Graduate, Age: 20-24 years",
                    examPattern = "Written Exam + AFSB Interview",
                    examFrequency = "Bi-annual",
                    officialWebsite = "https://afcat.cdac.in"
                ),
                ExamSubcategory(
                    id = "capf",
                    name = "CAPF",
                    fullName = "Central Armed Police Forces",
                    parentCategoryId = "defence",
                    description = "For Assistant Commandant posts in CAPF",
                    eligibility = "Graduate, Age: 20-25 years",
                    examPattern = "Written Exam + Physical & Medical Test + Interview",
                    examFrequency = "Annual",
                    officialWebsite = "https://upsc.gov.in"
                )
            )
        )
    }

    private fun getPsuCategory(): ExamCategory {
        return ExamCategory(
            id = "psu",
            name = "PSU",
            fullName = "Public Sector Undertakings",
            description = "Recruitment in various Public Sector Undertakings",
            iconRes = R.drawable.ic_factory,
            colorRes = R.color.secondary,
            subcategories = listOf(
                ExamSubcategory(
                    id = "ongc",
                    name = "ONGC",
                    fullName = "Oil and Natural Gas Corporation",
                    parentCategoryId = "psu",
                    description = "For various technical and non-technical posts",
                    eligibility = "Graduate/Diploma (varies by post), Age: 18-30 years",
                    examPattern = "Written Test + Interview",
                    examFrequency = "As per requirement",
                    officialWebsite = "https://www.ongcindia.com"
                ),
                ExamSubcategory(
                    id = "ntpc",
                    name = "NTPC",
                    fullName = "National Thermal Power Corporation",
                    parentCategoryId = "psu",
                    description = "For engineering and executive posts",
                    eligibility = "Engineering degree, Age: 18-27 years",
                    examPattern = "Written Test + Interview",
                    examFrequency = "As per requirement",
                    officialWebsite = "https://www.ntpc.co.in"
                ),
                ExamSubcategory(
                    id = "bhel",
                    name = "BHEL",
                    fullName = "Bharat Heavy Electricals Limited",
                    parentCategoryId = "psu",
                    description = "For engineering trainee posts",
                    eligibility = "Engineering degree with 65% marks, Age: 18-25 years",
                    examPattern = "Written Test + Interview",
                    examFrequency = "Annual",
                    officialWebsite = "https://www.bhel.com"
                ),
                ExamSubcategory(
                    id = "gail",
                    name = "GAIL",
                    fullName = "Gas Authority of India Limited",
                    parentCategoryId = "psu",
                    description = "For executive and non-executive posts",
                    eligibility = "Graduate/Engineering degree, Age: 18-30 years",
                    examPattern = "Written Test + Interview",
                    examFrequency = "As per requirement",
                    officialWebsite = "https://www.gail.co.in"
                )
            )
        )
    }

    private fun getStateGovernmentCategory(): ExamCategory {
        return ExamCategory(
            id = "state_government",
            name = "State Govt",
            fullName = "State Government Jobs",
            description = "Recruitment for various state government departments",
            iconRes = R.drawable.ic_location_city,
            colorRes = R.color.aspirant_primary,
            subcategories = listOf(
                ExamSubcategory(
                    id = "state_police",
                    name = "State Police",
                    fullName = "State Police Recruitment",
                    parentCategoryId = "state_government",
                    description = "For Constable, SI, ASI posts in state police",
                    eligibility = "10th/12th/Graduate (varies by post), Age: 18-25 years",
                    examPattern = "Written Test + Physical Test + Medical Test",
                    examFrequency = "As per requirement",
                    officialWebsite = "Varies by state"
                ),
                ExamSubcategory(
                    id = "state_teacher",
                    name = "State Teacher",
                    fullName = "State Teacher Recruitment",
                    parentCategoryId = "state_government",
                    description = "For teaching posts in government schools",
                    eligibility = "Graduate with B.Ed, Age: 18-35 years",
                    examPattern = "Written Test + Interview",
                    examFrequency = "As per requirement",
                    officialWebsite = "Varies by state"
                ),
                ExamSubcategory(
                    id = "state_clerk",
                    name = "State Clerk",
                    fullName = "State Government Clerk",
                    parentCategoryId = "state_government",
                    description = "For clerical posts in state government",
                    eligibility = "12th pass, Age: 18-28 years",
                    examPattern = "Written Test + Typing Test",
                    examFrequency = "As per requirement",
                    officialWebsite = "Varies by state"
                )
            )
        )
    }
}