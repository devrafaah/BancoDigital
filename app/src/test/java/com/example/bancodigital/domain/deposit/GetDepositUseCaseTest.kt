package com.example.bancodigital.domain.deposit

import com.example.bancodigital.data.model.Deposit
import com.example.bancodigital.data.repository.deposit.DepositDataSource
import com.example.bancodigital.data.repository.deposit.DepositDataSourceImp
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetDepositUseCaseTest {

    private val repository: DepositDataSourceImp = mockk()

    private lateinit var useCase: GetDepositUseCase

    @Before
    fun setUp() {
        useCase = GetDepositUseCase(repository)
    }

    @Test
    fun testInvoke_shouldReturnDeposit(): Unit = runTest {
        // PREPARAÇÃO
        val expectedDeposit = Deposit(id = "123", amount = 500.0f)

        // Ensinamos o Mock: "Quando chamarem getDepositById com '123', retorne o nosso expectedDeposit"
        // Usamos 'coEvery' porque a função dentro dela é 'suspend'
        coEvery { repository.getDeposit("123") } returns expectedDeposit

        // AÇÃO
        val result = useCase("123")

        // VERIFICAÇÃO
        assertNotNull(result)
        assertEquals(expectedDeposit.amount, result.amount)

        // Bônus do MockK: Podemos garantir que o método do repositório foi chamado exatamente 1 vez!
        coVerify(exactly = 1) { repository.getDeposit("123") }
    }

}