package id.bangkumis.tradewise.ui.screens.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import java.util.Locale

@Composable
fun TransactionDialog(
    onDismiss: () -> Unit,
    onConfirm: (quantity: String) -> Unit,
    assetName: String,
    assetPrice: Double,
    transactionType: String,
    ownedAmount: Double
){
    var quantity by remember { mutableStateOf("") }
    val quantityAsDouble = quantity.toDoubleOrNull()?: 0.0
    val totalCost = quantity.toDoubleOrNull()?.let { it*assetPrice }?: 0.0

    val isConfirmEnable = when {
        quantity.isBlank() || quantityAsDouble <= 0 -> false
        transactionType == "SELL" && quantityAsDouble > ownedAmount -> false
        else -> true
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "${transactionType.replaceFirstChar { it.titlecase(Locale.ROOT) }} $assetName")},
        text = {
            Column {
                Text(
                    text = "Current Price: $${"%.2f".format(assetPrice)}",
                    fontSize = 14.sp
                )
                OutlinedTextField(
                    value = quantity,
                    onValueChange = { quantity = it.filter {char -> char.isDigit() || char == '.'}},
                    label = { Text(text = "Quantity")},
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Text(
                    text = "Total Cost: $${"%.2f".format(totalCost)}",
                    fontSize = 14.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(quantity) },
                enabled = isConfirmEnable
            ) {
                Text(text = "Confirm")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text(text = "Cancel")
            }
        }
    )
}