package com.github.alfabravo2013.marvelcharacters.domain.characters

import com.github.alfabravo2013.marvelcharacters.CoroutinesTestExtension
import com.github.alfabravo2013.marvelcharacters.InstantExecutorExtension
import com.github.alfabravo2013.marvelcharacters.networking.MarvelApi
import com.github.alfabravo2013.marvelcharacters.networking.model.CharacterDataContainer
import com.github.alfabravo2013.marvelcharacters.networking.model.CharacterDataWrapper
import com.github.alfabravo2013.marvelcharacters.networking.model.ImageUrl
import com.github.alfabravo2013.marvelcharacters.networking.model.MarvelCharacter
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.fail

@ExperimentalCoroutinesApi
@ExtendWith(InstantExecutorExtension::class, CoroutinesTestExtension::class)
@DisplayName("Characters Remote Data Source Tests")
internal class CharactersLocalDataSourceTest {

    private val marvelApi = mockk<MarvelApi>()
    private val charactersRemoteDataSource = CharactersRemoteDataSource(marvelApi)

    private val pageSize = 2
    private val total = 4
    private val marvelCharacter = MarvelCharacter(
        id = 1,
        name = "name",
        description = "description",
        thumbnail = ImageUrl(path = "path", extension = "extension")
    )

    private val pageResults = listOf(marvelCharacter, marvelCharacter)
    private val partialResults = listOf(marvelCharacter)

    private val firstPageContainer = CharacterDataContainer(
        offset = 0,
        limit = pageSize,
        total = total,
        count = pageResults.size,
        results = pageResults
    )
    private val firstPageWrapper = CharacterDataWrapper(
        code = 200,
        status = "Ok",
        data = firstPageContainer
    )

    @Nested
    @DisplayName("When request any page")
    inner class ApiMethodCallsTests {
        @Test
        @DisplayName("Given queryText is empty, then api called without query")
        fun requestPageNoQuery() = runBlocking {
            coEvery { marvelApi.getCharactersPage(0, 20) } returns firstPageWrapper

            charactersRemoteDataSource.getFirstPage(20)

            coVerify(exactly = 1) {
                marvelApi.getCharactersPage(0, 20)
            }
        }

        @Test
        @DisplayName("Given queryText is not empty, then api called with query")
        fun requestPageWithQuery() = runBlocking {
            coEvery {
                marvelApi.getCharactersPage(0, 20, "query")
            } returns firstPageWrapper

            charactersRemoteDataSource.updateQueryText("query")
            charactersRemoteDataSource.getFirstPage(20)

            coVerify(exactly = 1) {
                marvelApi.getCharactersPage(0, 20, "query")
            }
        }
    }

    @Nested
    @DisplayName("When requesting first page")
    inner class FirstPageTests {

        @Test
        @DisplayName("Given pageSize == 2, then return list of two")
        fun requestFirstPageListSize() = runBlocking {
            coEvery {
                marvelApi.getCharactersPage(0, pageSize)
            } returns firstPageWrapper

            val result = charactersRemoteDataSource.getFirstPage(pageSize)

            assertEquals(pageResults.size, result.characters.size)

            coVerify(exactly = 1) {
                marvelApi.getCharactersPage(0, pageSize)
            }
        }

        @Test
        @DisplayName("Given any offset, then prevOffset is null")
        fun requestFirstPagePrevOffset() = runBlocking {
            coEvery {
                marvelApi.getCharactersPage(0, pageSize)
            } returns firstPageWrapper

            val result = charactersRemoteDataSource.getFirstPage(pageSize)

            assertNull(result.prevOffset)

            coVerify(exactly = 1) {
                marvelApi.getCharactersPage(0, pageSize)
            }
        }

        @Test
        @DisplayName("Given any offset, then nextOffset == pageSize")
        fun requestFirstPageNextOffset() = runBlocking {
            coEvery {
                marvelApi.getCharactersPage(0, pageSize)
            } returns firstPageWrapper

            val result = charactersRemoteDataSource.getFirstPage(pageSize)

            assertEquals(pageSize, result.nextOffset)

            coVerify(exactly = 1) {
                marvelApi.getCharactersPage(0, pageSize)
            }
        }
    }

    @Nested
    @DisplayName("When requesting next page")
    inner class NextPageTests {
        private val secondPageContainer = firstPageContainer.copy(offset = pageSize)
        private val secondPageWrapper = firstPageWrapper.copy(data = secondPageContainer)

        @Test
        @DisplayName("Given pageSize == 2, then return page of two")
        fun requestSecondPageListSize() = runBlocking {
            coEvery {
                marvelApi.getCharactersPage(pageSize, pageSize)
            } returns secondPageWrapper

            val result = charactersRemoteDataSource.getNextPage(pageSize)

            assertEquals(pageResults.size, result.characters.size)

            coVerify(exactly = 1) {
                marvelApi.getCharactersPage(pageSize, pageSize)
            }
        }

        @Test
        @DisplayName("Given offset == 0, then prevOffset == 0")
        fun requestSecondPagePrevOffset() = runBlocking {
            coEvery {
                marvelApi.getCharactersPage(pageSize, pageSize)
            } returns secondPageWrapper

            val result = charactersRemoteDataSource.getNextPage(pageSize)

            assertEquals(0, result.prevOffset)

            coVerify(exactly = 1) {
                marvelApi.getCharactersPage(pageSize, pageSize)
            }
        }

        @Test
        @DisplayName("Given offset == 0, then nextOffset == pageSize * 2")
        fun requestSecondPageNextOffset() = runBlocking {
            coEvery {
                marvelApi.getCharactersPage(pageSize, pageSize)
            } returns secondPageWrapper

            val result = charactersRemoteDataSource.getNextPage(pageSize)

            assertEquals(pageSize * 2, result.nextOffset)

            coVerify(exactly = 1) {
                marvelApi.getCharactersPage(pageSize, pageSize)
            }
        }
    }

    @Nested
    @DisplayName("When requesting previous page")
    inner class PrevPageTests {
        private val secondPageContainer = firstPageContainer.copy(offset = pageSize)
        private val secondPageWrapper = firstPageWrapper.copy(data = secondPageContainer)
        private val lastPageContainer = firstPageContainer.copy(offset = total)
        private val lastPageWrapper = firstPageWrapper.copy(data = lastPageContainer)

        @Test
        @DisplayName("Given offset == 0, return empty list")
        fun test() {
            fail("not implemented")
        }

        @Test
        @DisplayName("Given offset == 1 and pageSize == 2, return list of one")
        fun test2() {
            fail("not implemented")
        }

        @Test
        @DisplayName("Given offset == 2 and pageSize == 2, return list of two")
        fun test3() {
            fail("not implemented")
        }
    }

    @Nested
    @DisplayName("When requesting last page")
    inner class LastPageTests {
        // FIXME: 01.09.2021 carefully check api responses for edge cases
        private val lastPageContainer = firstPageContainer.copy(offset = total - pageSize)
        private val lastPageWrapper = firstPageWrapper.copy(data = lastPageContainer)

        @Test
        @DisplayName("Given pageSize == 2 and offset == total - pageSize, then return list of two")
        fun requestLastPage() = runBlocking {
            coEvery {
                marvelApi.getCharactersPage(any(), pageSize)
            } returns lastPageWrapper

            val result = charactersRemoteDataSource.getNextPage(pageSize)

            assertEquals(pageResults.size, result.characters.size)

            coVerify {
                marvelApi.getCharactersPage(any(), pageSize)
            }
        }

        @Test
        @DisplayName("Given offset == total, then return empty list")
        fun test() {
            fail("not implemented")
        }

        @Test
        @DisplayName("Given offset == total - 1 and pageSize == 2, then return list of one")
        fun test2() {
            fail("not implemented")
        }

        @Test
        @DisplayName("Given offset == total - pageSize, then prevOffset == total - pageSize")
        fun requestLastPagePrevOffset() = runBlocking {
            coEvery {
                marvelApi.getCharactersPage(any(), pageSize)
            } returns lastPageWrapper

            val result = charactersRemoteDataSource.getNextPage(pageSize)

            assertEquals(pageResults.size, result.characters.size)
            assertEquals(total - pageSize, result.prevOffset)

            coVerify {
                marvelApi.getCharactersPage(any(), pageSize)
            }
        }

        @Test
        @DisplayName("Given offset == total - pageSize, then nextOffset is null")
        fun requestLastPageNextOffset() = runBlocking {
            coEvery {
                marvelApi.getCharactersPage(any(), pageSize)
            } returns lastPageWrapper

            val result = charactersRemoteDataSource.getNextPage(pageSize)

            assertNull(result.nextOffset)

            coVerify {
                marvelApi.getCharactersPage(any(), pageSize)
            }
        }

        @Test
        @DisplayName("Given result.size < pageSize, then nextOffset is null")
        fun requestLastPageNearEnd() = runBlocking {
            val results = listOf(marvelCharacter)
            val partialListContainer = firstPageContainer.copy(
                offset = total - pageSize + 1,
                count = results.size,
                results = results
            )
            val partialListWrapper = firstPageWrapper.copy(data = partialListContainer)

            coEvery {
                marvelApi.getCharactersPage(any(), pageSize)
            } returns partialListWrapper

            val result = charactersRemoteDataSource.getNextPage(pageSize)

            assertNull(result.nextOffset)

            coVerify {
                marvelApi.getCharactersPage(any(), pageSize)
            }
        }
    }
}
