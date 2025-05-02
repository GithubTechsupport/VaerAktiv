package no.uio.ifi.in2000.vaeraktiv.ui.home

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DiscouragedApi")
@Composable
fun LoadAllActivities(modifier : Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ){
        LoadingActivityCard()
        LoadingActivityCard()
        LoadingActivityCard()
    }
}