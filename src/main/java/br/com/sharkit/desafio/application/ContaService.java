package br.com.sharkit.desafio.application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.com.sharkit.desafio.domain.model.Conta;
import br.com.sharkit.desafio.domain.repository.ContaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContaService {

	 private final ContaRepository contaRepository;
	 
	 public void importarContas(MultipartFile file) throws Exception {
	        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
	            List<Conta> contas = reader.lines()
	                .map(line -> {
	                    String[] fields = line.split(",");
	                    Conta conta = new Conta();
	                    conta.setDataVencimento(parseDate(fields[0]));
	                    conta.setDataPagamento(parseDate(fields[1]));
	                    conta.setValor(new BigDecimal(fields[2]));
	                    conta.setDescricao(fields[3]);
	                    conta.setSituacao(fields[4]);
	                    return conta;
	                }).collect(Collectors.toList());

	            contaRepository.saveAll(contas);
	        }
	    }
	 
	 

	public Conta criarConta(Conta conta) {
		 return contaRepository.save(conta);
	}

	public Conta atualizarConta(Long id, Conta conta) {
        conta.setId(id);
        return contaRepository.save(conta);
	}

	public Conta alterarSituacao(Long id, String situacao) {
		Conta conta = contaRepository.findById(id).orElseThrow();
        conta.setSituacao(situacao);
        return contaRepository.save(conta);
	}

	public Double obterValorTotalPagoPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
		return contaRepository.findTotalPagoByPeriodo(dataInicio, dataFim);
	}
	
	private LocalDate parseDate(String dateString) {
        if (dateString == null || dateString.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            log.error("Data no formato inválido");
            throw e;
        }
    }
}
