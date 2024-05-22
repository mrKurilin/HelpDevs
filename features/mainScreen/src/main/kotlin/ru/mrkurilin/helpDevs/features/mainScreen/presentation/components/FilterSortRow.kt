package ru.mrkurilin.helpDevs.features.mainScreen.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.mrkurilin.helpDevs.mainScreen.R

@Composable
fun FilterSortRow(
    onChangeSortDirectionClicked: () -> Unit,
    onSortClicked: () -> Unit,
    onFilterClicked: () -> Unit,
    onClearFilterClicked: () -> Unit,
) {

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ){
        Button(
            onClick = onChangeSortDirectionClicked,
            modifier = Modifier.height(48.dp)
        ) {
            Icon(
                painterResource(id = R.drawable.sort_to_top),
                contentDescription = null
            )
        }

        Button(
            onClick = onSortClicked,
            modifier = Modifier.height(48.dp)
        ) {
            Icon(
                painterResource(id = R.drawable.sort),
                contentDescription = null
            )
        }

        Button(
            onClick = onFilterClicked,
            modifier = Modifier.height(48.dp)
        ) {
            Icon(
                painterResource(id = R.drawable.filter),
                contentDescription = null
            )
        }

        Button(
            onClick = onClearFilterClicked,
            modifier = Modifier.height(48.dp)
        ) {
            Icon(
                painterResource(id = R.drawable.clear_filter),
                contentDescription = null
            )
        }
    }
}

@Preview
@Composable
fun FilterSortRowPreview() {
    FilterSortRow(
        {},
        {},
        {},
        {},
    )
}
