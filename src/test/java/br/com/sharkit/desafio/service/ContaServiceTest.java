package br.com.sharkit.desafio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import br.com.sharkit.desafio.application.ContaService;
import br.com.sharkit.desafio.domain.model.Conta;
import br.com.sharkit.desafio.domain.repository.ContaRepository;

public class ContaServiceTest {

	@Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCriarConta() {
        Conta conta = new Conta();
        conta.setDescricao("Teste Conta");
        conta.setValor(new BigDecimal("100.00"));
        conta.setDataVencimento(LocalDate.now());

        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Conta savedConta = contaService.criarConta(conta);

        assertEquals("Teste Conta", savedConta.getDescricao());
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    public void testAtualizarConta() {
        Conta conta = new Conta();
        conta.setId(1L);
        conta.setDescricao("Teste Conta Atualizada");

        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Conta updatedConta = contaService.atualizarConta(1L, conta);

        assertEquals("Teste Conta Atualizada", updatedConta.getDescricao());
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    public void testAlterarSituacao() {
        Conta conta = new Conta();
        conta.setId(1L);
        conta.setSituacao("Paga");

        when(contaRepository.findById(1L)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        Conta updatedConta = contaService.alterarSituacao(1L, "Pendente");

        assertEquals("Pendente", updatedConta.getSituacao());
        verify(contaRepository, times(1)).save(conta);
    }

    @Test
    public void testObterValorTotalPagoPorPeriodo() {
        when(contaRepository.findTotalPagoByPeriodo(any(), any())).thenReturn(100.00);

        Double total = contaService.obterValorTotalPagoPorPeriodo(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));

        assertEquals(Double.valueOf("100.00"), total);
    }

}
