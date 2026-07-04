package com.example.bancodigital.domain.profile

import com.example.bancodigital.data.repository.profile.ProfileDataSourceImp
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SaveProfileImageUseCaseTest {

    private val repository: ProfileDataSourceImp = mockk()

    private lateinit var useCase : SaveProfileImageUseCase

    @Before
    fun setUp() {
        useCase = SaveProfileImageUseCase(repository)
    }

    @Test
    fun saveProfileImageTest() = runTest {

        val expectSaveProfile = "dasdasdasdasdaa"

        coEvery { repository.saveImage(expectSaveProfile) } returns expectSaveProfile

        val result = useCase.invoke(expectSaveProfile)

        assertEquals(expectSaveProfile,result)

        coVerify(exactly = 1) { repository.saveImage(expectSaveProfile) }


    }

}