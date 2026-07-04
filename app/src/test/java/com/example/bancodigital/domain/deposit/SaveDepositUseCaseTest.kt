package com.example.bancodigital.domain.deposit

import com.example.bancodigital.data.model.Deposit
import com.example.bancodigital.data.repository.deposit.DepositDataSourceImp
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class SaveDepositUseCaseTest {

    private val repository: DepositDataSourceImp = mockk()

    private lateinit var useCase: SaveDepositUseCase

    @Before
    fun setUp() {
        useCase = SaveDepositUseCase(repository)
    }

    @Test
    fun invoke_shouldSaveDepositSuccessfully() = runTest {
        val testDeposit = Deposit(
            id = "id_teste_123",
            date = 16700000000L,
            amount = 150.0f
        )

        coEvery { repository.saveDeposit(testDeposit) } returns testDeposit

        val result = useCase(testDeposit)

        assertNotNull(result)
        assertEquals(testDeposit.id, result.id)
        assertEquals(testDeposit.amount, result.amount)
        assertEquals(testDeposit.date, result.date)

        coVerify(exactly = 1) { repository.saveDeposit(testDeposit) }
    }
}