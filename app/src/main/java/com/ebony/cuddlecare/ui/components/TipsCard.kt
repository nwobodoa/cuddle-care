package com.ebony.cuddlecare.ui.components

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ebony.cuddlecare.R
import java.io.FileNotFoundException
import kotlin.random.Random

data class TipItem(val tipHeader: String, val tipBody: String)

@Composable

fun TipsCard(onDismiss: () -> Unit) {
    val random = remember { Random.Default }
    val tipsList = readHealthTips(context = LocalContext.current)
    val randomIndex = remember { random.nextInt(tipsList.size) }
    val randomTip = remember { tipsList[randomIndex] }
    ElevatedCard(
        colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.tipBack)),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,

            ),
        modifier = Modifier
            .wrapContentSize()
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = randomTip.tipHeader, fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )
            Text(
                fontSize = 14.sp,
                textAlign = TextAlign.Justify,
                text = randomTip.tipBody
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onDismiss, colors = ButtonDefaults
                        .buttonColors(containerColor = colorResource(id = R.color.tipBtn))
                ) {
                    Text(text = "Dismiss")
                }
            }
        }
    }


}

fun readHealthTips(context: Context): List<TipItem> {
    val tipsList = mutableListOf<TipItem>()
    try {
        val inputStream = context.resources.openRawResource(R.raw.tips)
        inputStream.bufferedReader().useLines { lines ->
            lines.forEach {
                val (tipHeader, tipBody) = it.split(':', ignoreCase = false, limit = 2)
                tipsList.add(TipItem(tipHeader.trim(), tipBody.trim()))
            }
        }
    } catch (e: FileNotFoundException) {
        println("File not found.")
    }
    return tipsList
}
