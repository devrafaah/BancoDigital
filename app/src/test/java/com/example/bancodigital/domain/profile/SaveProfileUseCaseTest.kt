package com.example.bancodigital.domain.profile

import com.example.bancodigital.data.model.User
import com.example.bancodigital.data.repository.profile.ProfileDataSourceImp
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SaveProfileUseCaseTest {

    private var repository : ProfileDataSourceImp = mockk()

    private lateinit var useCase : SaveProfileUseCase

    @Before
    fun setUp() {
        useCase = SaveProfileUseCase(repository)
    }

    @Test
    fun invoke() = runTest {

        val profileTest = User(
            id = "testIdUser",
            name = "testando",
            email = "test@gmail.com",
            phone = "11900000000",
            image = "imagem.png",
            senha = "1234567"
        )

        coEvery { repository.saveprofile(profileTest) } returns Unit

        useCase(profileTest)
        coVerify(exactly = 1) { repository.saveprofile(profileTest) }

    }

}