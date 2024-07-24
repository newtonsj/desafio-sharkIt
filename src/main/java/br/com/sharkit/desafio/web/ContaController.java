package br.com.sharkit.desafio.web;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.sharkit.desafio.application.ContaService;
import br.com.sharkit.desafio.domain.model.Conta;
import br.com.sharkit.desafio.domain.repository.ContaRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/contas")
@RequiredArgsConstructor
@Tag(name = "Conta", description = "API para gerenciamento de contas a pagar")
public class ContaController {

	private final ContaRepository contaRepository;
	
	 private final ContaService contaService;
	
	
	@PostMapping
	@Operation(summary = "Criar nova conta", description = "Cria uma nova conta a pagar")
    public Conta criarConta(@RequestBody Conta conta) {
        return contaService.criarConta(conta);
    }

    @PutMapping("/{id}")
    public Conta atualizarConta(@PathVariable Long id, @RequestBody Conta conta) {
        return contaService.atualizarConta(id, conta);
    }

    @PatchMapping("/{id}/situacao")
    public Conta alterarSituacao(@PathVariable Long id, @RequestParam String situacao) {
        return contaService.alterarSituacao(id, situacao);
    }

    @GetMapping
    public Page<Conta> listarContas(Pageable pageable,
                                    @RequestParam(required = false) String descricao,
                                    @RequestParam(required = false) LocalDate dataVencimento) {
        return contaRepository.findAll(pageable);
    }

    @GetMapping("/{id}")
    public Conta obterConta(@PathVariable Long id) {
        return contaRepository.findById(id).orElseThrow();
    }

    @GetMapping("/valor-periodo")
    public Double getTotalPago(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {

        return contaService.obterValorTotalPagoPorPeriodo(dataInicio, dataFim);
    }
    
    @PostMapping(value = "/importar", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    @Operation(summary = "Importar contas", description = "Importa contas em lote através de arquivo .CSV")
    public void importarContas(@RequestParam("file") MultipartFile file) throws Exception {
        contaService.importarContas(file);
    }
}
