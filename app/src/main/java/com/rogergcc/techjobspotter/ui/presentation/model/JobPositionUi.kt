package com.rogergcc.techjobspotter.ui.presentation.model


/**
 * Created on enero.
 * year 2025 .
 */
data class JobPositionUi(
  val candidateRequiredLocation: String? = "",
  val category: String? = "",
  val companyLogo: String? = "",
  val companyLogoUrl: String? = "",
  val companyName: String? = "",
  val description: String? = "",
  val id: Int? = 0,
  val jobType: String? = "",
  val publicationDate: String? = "",
  val salary: String? = "",
  val tags: List<String?>? = listOf(),
  val title: String? = "",
  val url: String? = "",
  var isMarked: Boolean = false,
)