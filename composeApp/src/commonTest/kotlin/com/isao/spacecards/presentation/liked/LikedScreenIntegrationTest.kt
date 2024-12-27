package com.isao.spacecards.presentation.liked
//
//import android.content.Intent
//import androidx.compose.runtime.CompositionLocalProvider
//import androidx.compose.ui.platform.LocalUriHandler
//import androidx.compose.ui.test.filter
//import androidx.compose.ui.test.hasClickAction
//import androidx.compose.ui.test.hasText
//import androidx.compose.ui.test.junit4.ComposeContentTestRule
//import androidx.compose.ui.test.junit4.createComposeRule
//import androidx.compose.ui.test.longClick
//import androidx.compose.ui.test.performClick
//import androidx.compose.ui.test.performTouchInput
//import androidx.test.espresso.intent.Intents
//import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
//import androidx.test.espresso.intent.matcher.IntentMatchers.hasData
//import androidx.test.espresso.intent.rule.IntentsRule
//import com.isao.spacecards.R
//import com.isao.spacecards.core.di.appModule
//import com.isao.spacecards.core.utils.robolectricContextModule
//import com.isao.spacecards.data.dao.LikedImageDao
//import com.isao.spacecards.data.local.mapper.toEntityModel
//import com.isao.spacecards.domain.model.LikedImage
//import com.isao.spacecards.domain.model.dummy.LikedImageDummies
//import com.isao.spacecards.presentation.liked.composable.LikedRoute
//import com.isao.spacecards.presentation.liked.mapper.toPresentationModel
//import com.isao.spacecards.utils.KoinRule
//import com.isao.spacecards.utils.getString
//import com.isao.spacecards.utils.hasNoText
//import com.isao.spacecards.utils.testsModule
//import io.kotest.matchers.shouldBe
//import kotlinx.coroutines.test.runTest
//import org.hamcrest.core.AllOf
//import org.junit.Rule
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.koin.androidx.compose.KoinAndroidContext
//import org.koin.core.annotation.KoinExperimentalAPI
//import org.koin.test.KoinTest
//import org.koin.test.get
//import org.robolectric.RobolectricTestRunner
//
//@RunWith(RobolectricTestRunner::class)
//class LikedScreenIntegrationTest : KoinTest {
//    @get:Rule
//    val testRule = createComposeRule()
//
//    @get:Rule
//    val koinRule = KoinRule(appModule + testsModule + robolectricContextModule)
//
//    @get:Rule
//    val intentsRule = IntentsRule()
//
//    private val images = listOf(
//        LikedImageDummies.LikedImage2,
//        LikedImageDummies.LikedImage1,
//    )
//
//    @Test
//    fun `when clicking item, open browser with image`() = runTest {
//        get<LikedImageDao>().saveImages(images)
//
//        testRule.setUpComposable()
//        testRule.onAllNodes(hasClickAction()).filter(hasNoText())[0].performClick()
//
//        Intents.intended(
//            AllOf(
//                listOf(
//                    hasAction(Intent.ACTION_VIEW),
//                    hasData(images.first().toPresentationModel().imageUrl)
//                )
//            )
//        )
//    }
//
//    @Test
//    fun `given dropdown open, when 'image by' clicked, open browser with image source`() = runTest {
//        get<LikedImageDao>().saveImages(images)
//
//        testRule.setUpComposable()
//        testRule.onAllNodes(hasClickAction() and hasNoText())[0].performTouchInput { longClick() }
//        testRule.onNode(
//            hasText(
//                getString(
//                    R.string.image_by,
//                    images.first().toPresentationModel().source.websiteName
//                )
//            )
//        ).performClick()
//
//
//
//        Intents.intended(
//            AllOf(
//                listOf(
//                    hasAction(Intent.ACTION_VIEW),
//                    hasData(images.first().toPresentationModel().source.websiteUrl)
//                )
//            )
//        )
//    }
//
//    @Test
//    fun `given dropdown open, when delete clicked, delete item`() = runTest {
//        get<LikedImageDao>().saveImages(images)
//
//        testRule.setUpComposable()
//        val displayedImageNodes = testRule.onAllNodes(hasClickAction() and hasNoText())
//        val originalImageCount = displayedImageNodes.fetchSemanticsNodes().size
//        displayedImageNodes[0].performTouchInput { longClick() }
//        testRule.onNode(hasText(getString(R.string.delete))).performClick()
//
//        val newImageCount =
//            testRule.onAllNodes(hasClickAction() and hasNoText()).fetchSemanticsNodes().size
//        newImageCount shouldBe originalImageCount - 1
//    }
//
//    @OptIn(KoinExperimentalAPI::class)
//    private fun ComposeContentTestRule.setUpComposable() = setContent {
//        // Needed as a workaround for some crashes (mostly in tests)
//        // https://github.com/InsertKoinIO/koin/issues/1557
//        CompositionLocalProvider(androidx.compose.ui.platform.LocalUriHandler provides)
//        KoinAndroidContext {
//            LikedRoute()
//        }
//    }
//
//    private suspend fun LikedImageDao.saveImages(images: List<LikedImage>) = images.forEach {
//        saveLikedImage(it.toEntityModel())
//    }
//}