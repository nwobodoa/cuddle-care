package com.ebony.cuddlecare.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ebony.cuddlecare.R
import com.ebony.cuddlecare.ui.components.mTopBar

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showBackground = true)
fun Add_vaccination_record() {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .background(color = colorResource(id = R.color.orange))
    ) {
        mTopBar()
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                .background(color = colorResource(id = R.color.backcolor))
                .padding(16.dp)
        )
        {

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vac_logo),
                    contentDescription = null,
                    modifier = Modifier
                        .size(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Text(text = "Vaccination", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Text(text = "Last: Never")
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(20.dp))
                    .background(color = Color.White)
                    .padding(8.dp)
            )

            {
                Row {
                    Icon(Icons.Default.AccessTime, contentDescription = "Timer")
                    Text(text = "Time")
                    Text(text = "9 Nov")
                    Text(text = "2:44 AM")
                }
                Row {

                    val groups = listOf("Disposable", "Cloth")
                    var selectedIndex by remember { mutableStateOf(0) }

                    Icon(Icons.Outlined.Folder, contentDescription = "Folder icon",modifier=Modifier.weight(2f))
                    ExposedSelectionMenu(
                        title = "Select group",
                        options = groups,
                        onSelected = { index ->
                            selectedIndex = index
                        })
                }
            }

                AttachmentRow()
                SaveButton()
                //TODO disable save button if nothing is entered

            }
        }
    }





