@file:OptIn(ExperimentalTestApi::class)

package com.isao.yfoo3.presentation.liked

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.runComposeUiTest
import com.isao.yfoo3.presentation.liked.composable.LikedScreen
import io.kotest.core.spec.style.FunSpec
import org.jetbrains.compose.resources.getString
import org.koin.test.KoinTest
import yfoomultiplatform.composeapp.generated.resources.Res
import yfoomultiplatform.composeapp.generated.resources.loading

@OptIn(ExperimentalTestApi::class)
class LikedScreenTest : FunSpec({
//    @get:Rule
//    val testRule = createComposeRule()
//
//    @get:Rule
//    val koinRule = KoinRule(emptyList())

//    private val intents = mutableListOf<LikedIntent>()

    test("when loading, show loading placeholder") {
        val loading = getString(Res.string.loading)
        runComposeUiTest {
            setUpComposable(state = LikedUiState(isLoading = true), onIntent = {})
            onNodeWithContentDescription(loading).assertExists()
        }
    }

    // A screenshot test already covers this case, so this test is likely unnecessary
//    @Test
//    fun `when loading, show loading placeholder`() = runComposeUiTest {
//        setUpComposable(state = LikedUiState(isLoading = true), onIntent = {})
//        onNodeWithContentDescription(getString(Res.string.loading)).assertExists()
//    }
//
//    // A screenshot test already covers this case, so this test is likely unnecessary
//    @Test
//    fun `when error, show error bar`() {
//        testRule.setUpComposable(state = LikedUiState(isError = true), onIntent = {})
//        testRule.onNodeWithText(getString(Res.string.something_went_wrong)).assertExists()
//    }
//
//    // A screenshot test already covers this case, so this test is likely unnecessary
//    @Test
//    fun `when content available, show all content`() {
//        val content = generateLikedImageDisplayables(4)
//        testRule.setUpComposable(state = LikedUiState(items = content), onIntent = {})
//
//        testRule.onNode(hasScrollAction())
//            .onChildren()
//            .filter(hasClickAction())
//            // Sorting button
//            .assertAny(hasText(getString(Res.string.added)))
//            // Items + Sorting button
//            .assertCountEquals(content.size + 1)
//    }
//
//    @Test
//    fun `when content available, sorting button is available`() {
//        val content = generateLikedImageDisplayables(4)
//        testRule.setUpComposable(state = LikedUiState(items = content))
//
//        // Sorting button
//        testRule.onNode(hasClickAction() and hasText(getString(Res.string.added))).performClick()
//
//        intents shouldHaveSingleElement { it is LikedIntent.SetSorting }
//    }
//
//    @Test
//    fun `when long clicking item, dropdown appears`() {
//        val content = generateLikedImageDisplayables(4)
//        testRule.setUpComposable(state = LikedUiState(items = content))
//
//        testRule.onAllNodes(hasClickAction() and hasNoText())[0].performTouchInput { longClick() }
//
//        testRule.onNode(
//            hasText(getString(Res.string.image_by, content[0].source.websiteName))
//        ).assertExists()
//        testRule.onNode(
//            hasClickAction()
//                    and hasText(getString(Res.string.delete))
//        ).assertExists()
//    }
//

}), KoinTest

private fun ComposeUiTest.setUpComposable(
    state: LikedUiState = LikedUiState(),
    onIntent: (LikedIntent) -> Unit = {}
) = setContent {
    LikedScreen(
        uiState = state,
        onIntent = {
//                intents += it
            onIntent(it)
        }
    )
}