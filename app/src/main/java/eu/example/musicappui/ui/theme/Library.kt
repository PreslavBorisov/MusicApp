package eu.example.musicappui.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import eu.example.musicappui.Lib
import eu.example.musicappui.libraries

@Composable
fun Library(){
    LazyColumn{
        items(libraries){
            lib->
            LibItem(lib = lib)
        }
    }
}


@Composable
fun LibItem(lib: Lib){
    Column {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
            ) {
            Row {
                Icon(painter = painterResource(id = lib.icon),
                    contentDescription =lib.name,
                    modifier = Modifier.padding(horizontal = 8.dp),
                    tint = Color.Black
                )
                Text(text = lib.name,
                    color = Color.Black)
            }
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription ="Arrow Right",
                tint = Color.Black
            )
        }
        Divider(color = Color.Black, thickness = 2.dp)
    }
}