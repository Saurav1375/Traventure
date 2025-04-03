package com.example.tripapplication.auth.presentation.auth_screen.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun AutoAdvancePager(
    pageItems: List<Int>,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) { // Apply the passed modifier here
        val pagerState = rememberPagerState(pageCount = { pageItems.size })
        val pagerIsDragged by pagerState.interactionSource.collectIsDraggedAsState()

        val pageInteractionSource = remember { MutableInteractionSource() }
        val pageIsPressed by pageInteractionSource.collectIsPressedAsState()

        // Stop auto-advancing when pager is dragged or one of the pages is pressed
        val autoAdvance = !pagerIsDragged && !pageIsPressed

        if (autoAdvance) {
            LaunchedEffect(pagerState, pageInteractionSource) {
                while (true) {
                    delay(2000)
                    val nextPage = (pagerState.currentPage + 1) % pageItems.size
                    pagerState.animateScrollToPage(nextPage)
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth() // Ensure pager fills width
        ) { page ->
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Adjust the height of the image if needed to avoid creating extra space
                Image(
                    painter = painterResource(pageItems[page]),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp) // Slightly reduced height to avoid spacing issues
                )

                TravelAppHeader(
                    "Your Journey",
                    "Perfectly Planned",
                    "Effortlessly create and organize your dream trips. Start exploring now!"
                )
                Spacer(modifier = Modifier.height(8.dp))
                PagerIndicator(pageItems.size, pagerState.currentPage)
            }
        }
    }
}