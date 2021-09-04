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
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

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

    private val secondPageContainer = firstPageContainer.copy(offset = pageSize)
    private val secondPageWrapper = firstPageWrapper.copy(data = secondPageContainer)

    private val badResponse = CharacterDataWrapper(
        code = 409,
        status = "bad",
        data = CharacterDataContainer(
            offset = 0,
            limit = pageSize,
            total = total,
            count = 0,
            results = emptyList()
        )
    )

    @BeforeEach
    fun setup() {
        coEvery {
            marvelApi.getCharactersPage(or(less(0), more(total)), any())
        } returns badResponse
    }

    @Nested
    @DisplayName("When requesting any page")
    inner class ApiMethodCallsTests {
        @Test
        @DisplayName("Given queryText is empty, then api called without query")
        fun requestPageNoQuery() = runBlocking {
            coEvery { marvelApi.getCharactersPage(0, pageSize) } returns firstPageWrapper

            charactersRemoteDataSource.getFirstPage(pageSize)

            coVerify(exactly = 1) { marvelApi.getCharactersPage(0, pageSize) }
        }

        @Test
        @DisplayName("Given queryText is not empty, then api called with query")
        fun requestPageWithQuery() = runBlocking {
            coEvery {
                marvelApi.getCharactersPage(0, pageSize, "query")
            } returns firstPageWrapper

            charactersRemoteDataSource.updateQueryText("query")
            charactersRemoteDataSource.getFirstPage(pageSize)

            coVerify(exactly = 1) { marvelApi.getCharactersPage(0, pageSize, "query") }
        }
    }

    @Nested
    @DisplayName("When requesting first page")
    inner class FirstPageTests {

        @Test
        @DisplayName("Given pageSize == 2, then return list.size == 2")
        fun requestFirstPageListSize() = runBlocking {
            coEvery { marvelApi.getCharactersPage(0, pageSize) } returns firstPageWrapper

            val result = charactersRemoteDataSource.getFirstPage(pageSize)

            assertEquals(pageResults.size, result.characters.size)

            coVerify(exactly = 1) { marvelApi.getCharactersPage(0, pageSize) }
        }

        @Test
        @DisplayName("Given any offset, then prevOffset is null")
        fun requestFirstPagePrevOffset() = runBlocking {
            coEvery { marvelApi.getCharactersPage(0, pageSize) } returns firstPageWrapper

            val result = charactersRemoteDataSource.getFirstPage(pageSize)

            assertNull(result.prevOffset)

            coVerify(exactly = 1) { marvelApi.getCharactersPage(0, pageSize) }
        }

        @Test
        @DisplayName("Given any offset, then nextOffset == pageSize")
        fun requestFirstPageNextOffset() = runBlocking {
            coEvery { marvelApi.getCharactersPage(0, pageSize) } returns firstPageWrapper

            val result = charactersRemoteDataSource.getFirstPage(pageSize)

            assertEquals(pageSize, result.nextOffset)

            coVerify(exactly = 1) { marvelApi.getCharactersPage(0, pageSize) }
        }

        @Test
        @DisplayName("Given results.size < pageSize, then nextOffset is null")
        fun requestFirstPagePartialList() = runBlocking {
            coEvery {
                marvelApi.getCharactersPage(0, pageSize)
            } returns firstPageWrapper.copy(
                data = firstPageContainer.copy(
                    count = partialResults.size,
                    total = 3,
                    results = partialResults
                )
            )

            val result = charactersRemoteDataSource.getFirstPage(pageSize)

            assertEquals(partialResults.size, result.characters.size)
            assertNull(result.nextOffset)

            coVerify(exactly = 1) { marvelApi.getCharactersPage(0, pageSize) }
        }

        @Test
        @DisplayName("Given results.size == total, then nextOffset is null")
        fun requestFirstPageAllFetched() = runBlocking {
            coEvery {
                marvelApi.getCharactersPage(0, pageSize * 2)
            } returns firstPageWrapper.copy(
                data = firstPageContainer.copy(results = pageResults + pageResults)
            )

            val result = charactersRemoteDataSource.getFirstPage(pageSize * 2)

            assertEquals(total, result.characters.size)
            assertNull(result.nextOffset)

            coVerify(exactly = 1) { marvelApi.getCharactersPage(0, pageSize * 2) }
        }
    }

    @Nested
    @DisplayName("When requesting next page")
    inner class NextPageTests {

        @Test
        @DisplayName("Given pageSize == 2, then return page.size == 2")
        fun requestSecondPageListSize() = runBlocking {
            coEvery { marvelApi.getCharactersPage(pageSize, pageSize) } returns secondPageWrapper

            val result = charactersRemoteDataSource.getNextPage(pageSize)

            assertEquals(pageSize, result.characters.size)

            coVerify(exactly = 1) { marvelApi.getCharactersPage(pageSize, pageSize) }
        }

        @Test
        @DisplayName("Given offset == 0, then prevOffset == 0")
        fun requestSecondPagePrevOffset() = runBlocking {
            coEvery { marvelApi.getCharactersPage(pageSize, pageSize) } returns secondPageWrapper

            val result = charactersRemoteDataSource.getNextPage(pageSize)

            assertEquals(0, result.prevOffset)

            coVerify(exactly = 1) { marvelApi.getCharactersPage(pageSize, pageSize) }
        }

        @Test
        @DisplayName("Given offset == 0, then nextOffset == pageSize * 2")
        fun requestSecondPageNextOffset() = runBlocking {
            coEvery { marvelApi.getCharactersPage(pageSize, pageSize) } returns secondPageWrapper

            val result = charactersRemoteDataSource.getNextPage(pageSize)

            assertEquals(pageSize * 2, result.nextOffset)

            coVerify(exactly = 1) { marvelApi.getCharactersPage(pageSize, pageSize) }
        }

        @Test
        @DisplayName("Given results.size < pageSize, then nextOffset is null")
        fun requestSecondPagePartialList() = runBlocking {
            coEvery {
                marvelApi.getCharactersPage(pageSize, pageSize)
            } returns secondPageWrapper.copy(
                data = secondPageContainer.copy(
                    count = partialResults.size,
                    total = 3,
                    results = partialResults
                )
            )

            val result = charactersRemoteDataSource.getNextPage(pageSize)

            assertTrue(result.characters.size < pageSize)
            assertEquals(partialResults.size, result.characters.size)
            assertNull(result.nextOffset)

            coVerify(exactly = 1) { marvelApi.getCharactersPage(pageSize, pageSize) }
        }

        @Test
        @DisplayName("Given offset + pageSize > total, then nextOffset is null")
        fun requestSecondPageBeyondTotal() = runBlocking {
            val total = 3
            coEvery {
                marvelApi.getCharactersPage(total - 1, pageSize)
            } returns secondPageWrapper.copy(
                data = secondPageContainer.copy(
                    offset = total,
                    limit = pageSize,
                    total = total,
                    count = partialResults.size,
                    results = partialResults
                )
            )

            val result = charactersRemoteDataSource.getNextPage(pageSize)

            assertEquals(partialResults.size, result.characters.size)
            assertNull(result.nextOffset)
            assertEquals(total - pageSize, result.prevOffset)

            coVerify { marvelApi.getCharactersPage(total - 1, pageSize) }
        }
    }

    @Nested
    @DisplayName("When requesting previous page")
    inner class PrevPageTests {

        @Test
        @DisplayName("Given offset == 0, then return empty list")
        fun requestPrevPageFromOffsetZero() = runBlocking {
            coEvery {
                marvelApi.getCharactersPage(range(0, total), less(1))
            } returns badResponse

            assertThrows<MarvelApi.BadRequestException> {
                charactersRemoteDataSource.getPrevPage(pageSize)
            }

            coVerify { marvelApi.getCharactersPage(0, 0) }
        }

        @Test
        @DisplayName("Given offset == 3 and pageSize == 4, return list.size == 3")
        fun requestPrevPageFromOffsetOne() = runBlocking {
            coEvery { marvelApi.getCharactersPage(1, 1) } returns secondPageWrapper.copy(
                data = CharacterDataContainer(
                    offset = 1,
                    limit = 1,
                    total = 4,
                    count = 1,
                    results = partialResults
                )
            )
            coEvery { marvelApi.getCharactersPage(0, 1) } returns firstPageWrapper.copy(
                data = CharacterDataContainer(
                    offset = 0,
                    limit = 1,
                    total = 4,
                    count = 1,
                    results = partialResults
                )
            )

            charactersRemoteDataSource.getNextPage(1)
            val result = charactersRemoteDataSource.getPrevPage(pageSize)

            assertEquals(partialResults.size, result.characters.size)
            assertNull(result.prevOffset)
            assertEquals(1, result.nextOffset)

            coVerify(exactly = 1) {
                marvelApi.getCharactersPage(1, 1)
                marvelApi.getCharactersPage(0, 1)
            }
        }

        @Test
        @DisplayName("Given offset == 2 and pageSize == 2, return list of two")
        fun requestPreviousPageAfterNextPage() = runBlocking {
            coEvery { marvelApi.getCharactersPage(2, pageSize) } returns secondPageWrapper
            coEvery { marvelApi.getCharactersPage(0, pageSize) } returns firstPageWrapper

            charactersRemoteDataSource.getNextPage(pageSize)
            val result = charactersRemoteDataSource.getPrevPage(pageSize)

            assertEquals(pageSize, result.characters.size)
            assertNull(result.prevOffset)
            assertEquals(result.nextOffset, pageSize)

            coVerify(exactly = 1) {
                marvelApi.getCharactersPage(2, pageSize)
                marvelApi.getCharactersPage(0, pageSize)
            }
        }
    }

    @Nested
    @DisplayName("Bad responses")
    inner class BadResponseTests {

        @Test
        @DisplayName("Given code 409, then throw MarvelApi.BadRequestException")
        fun badRequestTest() = runBlocking {
            coEvery { marvelApi.getCharactersPage(any(), any()) } returns badResponse

            assertThrows<MarvelApi.BadRequestException> {
                charactersRemoteDataSource.getFirstPage(pageSize)
            }

            coVerify { marvelApi.getCharactersPage(0, pageSize) }
        }

        @Test
        @DisplayName("Given code is not 200 or 409, then throw MarvelApi.ApiException")
        fun apiErrorTest() = runBlocking {
            coEvery {
                marvelApi.getCharactersPage(any(), any())
            } returns badResponse.copy(code = 500)

            assertThrows<MarvelApi.ApiException> {
                charactersRemoteDataSource.getFirstPage(pageSize)
            }

            coVerify { marvelApi.getCharactersPage(0, pageSize) }
        }
    }
}
