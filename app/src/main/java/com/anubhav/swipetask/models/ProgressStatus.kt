package com.anubhav.swipetask.models

data class ProgressStatus(
    val message: String,
    val percentage: Int,
    val isIndeterminate: Boolean,
    val isActive: Boolean
)
