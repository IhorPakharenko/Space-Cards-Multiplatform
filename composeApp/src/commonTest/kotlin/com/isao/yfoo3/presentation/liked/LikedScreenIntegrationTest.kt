package com.isao.yfoo3.presentation.liked
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
//import com.isao.yfoo3.R
//import com.isao.yfoo3.core.di.appModule
//import com.isao.yfoo3.core.utils.robolectricContextModule
//import com.isao.yfoo3.data.dao.LikedImageDao
//import com.isao.yfoo3.data.local.mapper.toEntityModel
//import com.isao.yfoo3.domain.model.LikedImage
//import com.isao.yfoo3.domain.model.dummy.LikedImageDummies
//import com.isao.yfoo3.presentation.liked.composable.LikedRoute
//import com.isao.yfoo3.presentation.liked.mapper.toPresentationModel
//import com.isao.yfoo3.utils.KoinRule
//import com.isao.yfoo3.utils.getString
//import com.isao.yfoo3.utils.hasNoText
//import com.isao.yfoo3.utils.testsModule
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