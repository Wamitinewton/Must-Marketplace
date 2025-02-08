package com.newton.mustmarket.features.merchant.create_store.presentation.view.merchant_input

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.newton.mustmarket.core.sharedComposable.ProductInputFields

@Composable
 fun BasicInfoSection(
    name: String,
    onNameChange: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Basic Information",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        ProductInputFields(
            inputText = name,
            onInputChanged = onNameChange,
            labelName = "Store Name",
            keyboardType = KeyboardType.Text,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
 fun LocationSection(
    location: String,
    onLocationChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Location Details",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        ProductInputFields(
            inputText = location,
            onInputChanged = onLocationChange,
            labelName = "Store Location",
            keyboardType = KeyboardType.Text,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
 fun DescriptionSection(
    description: String,
    onDescriptionChange: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            "Store Description",
            style = MaterialTheme.typography.h5,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))

        ProductInputFields(
            inputText = description,
            onInputChanged = onDescriptionChange,
            labelName = "Store Description",
            keyboardType = KeyboardType.Text,
            modifier = Modifier.fillMaxWidth(),
            isDescription = true
        )
    }
}