import com.idle.domain.model.auth.UserType
import com.idle.domain.repositorry.profile.ProfileRepository
import com.idle.domain.usecase.profile.UpdateCenterProfileUseCase
import io.mockk.clearMocks
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UpdateCenterProfileUseCaseTest {

    private lateinit var profileRepository: ProfileRepository
    private lateinit var useCase: UpdateCenterProfileUseCase

    private val officeNumber = "12345"
    private val introduce = "테스트 소개"

    @BeforeEach
    fun setUp() {
        profileRepository = mockk(relaxed = true)
        useCase = UpdateCenterProfileUseCase(profileRepository)
    }

    @AfterEach
    fun tearDown() {
        clearMocks(profileRepository)
    }

    @Test
    fun `이미지 파일이 content로 시작하면 프로필과 이미지 업데이트가 호출된다`() = runTest {
        // Given: 이미지 파일이 content://로 시작
        val imageFileUri = "content://media/external/images/media/12345"

        // When
        useCase(officeNumber, introduce, imageFileUri)

        // Then
        coVerify { profileRepository.updateCenterProfile(officeNumber, introduce) }
        coVerify {
            profileRepository.updateProfileImage(
                userType = UserType.CENTER.apiValue,
                imageFileUri = imageFileUri,
                reqWidth = 1340,
                reqHeight = 1016
            )
        }
    }

    @Test
    fun `이미지 파일이 https로 시작하면 이미지 업데이트는 호출되지 않고 프로필만 업데이트된다`() = runTest {
        // Given: 이미지 파일이 https://로 시작
        val imageFileUri = "https://example.com/path/to/image"

        // When
        useCase(officeNumber, introduce, imageFileUri)

        // Then
        coVerify { profileRepository.updateCenterProfile(officeNumber, introduce) }
        coVerify(exactly = 0) {
            profileRepository.updateProfileImage(
                any(),
                any(),
                any(),
                any()
            )
        }
    }

    @Test
    fun `이미지 파일이 null이면 프로필만 업데이트되고 이미지 업데이트는 호출되지 않는다`() = runTest {
        // Given: 이미지 파일이 null
        val imageFileUri = null

        // When
        useCase(officeNumber, introduce, imageFileUri)

        // Then
        coVerify { profileRepository.updateCenterProfile(officeNumber, introduce) }
        coVerify(exactly = 0) {
            profileRepository.updateProfileImage(
                any(),
                any(),
                any(),
                any()
            )
        }
    }
}
