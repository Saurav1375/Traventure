package com.example.tripapplication.auth.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AgreementCheckbox(checked : Boolean, onCheckedChange : (Boolean) -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = {
                onCheckedChange(it)
            },
            colors = CheckboxDefaults.colors(
                checkedColor = Color(0xFF005450)
            )
        )
        Text(
            modifier = Modifier
                .clickable { },
            text = buildAnnotatedString {
                append("I've read and agreed to ")
                withStyle(
                    style = SpanStyle(
                        fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )

                ) {
                    append("Term & Conditions ")
                }
                append(" and ")
                withStyle(
                    style = SpanStyle(
                        fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                        fontWeight = FontWeight.Normal,
                        color = Color.Black
                    )

                ) {
                    append("Privacy Policy")
                }
            },
            color = Color.Gray,
            style = MaterialTheme.typography.bodyMedium
        )

    }
}

