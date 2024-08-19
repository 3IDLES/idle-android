package com.idle.designsystem.compose.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.idle.compose.clickable
import com.idle.designresource.R
import com.idle.designsystem.compose.foundation.CareTheme

@Composable
fun CareSnackBar(
    data: SnackbarData,
    modifier: Modifier = Modifier,
    @DrawableRes leftImage: Int = R.drawable.ic_error,
    backgroundColor: Color = CareTheme.colors.red,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
    ) {
        Image(
            painter = painterResource(id = leftImage),
            contentDescription = null,
            modifier = Modifier.padding(end = 8.dp),
        )

        Text(
            text = data.visuals.message,
            style = CareTheme.typography.subtitle4,
            color = CareTheme.colors.white000,
        )

        Spacer(modifier = Modifier.weight(1f))

        Image(
            painter = painterResource(id = R.drawable.ic_cancel),
            contentDescription = null,
            modifier = Modifier.clickable { data.dismiss() }
        )
    }
}