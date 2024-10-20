package acst.workspace.produtos.controller;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import acst.workspace.produtos.controller.dto.ProdutoCreateDTO;
import acst.workspace.produtos.controller.dto.ProdutoDTO;
import acst.workspace.produtos.controller.exception.ProdutoNotFoundException;
import acst.workspace.produtos.controller.mapper.ProdutoMapper;
import acst.workspace.produtos.model.Produto;
import acst.workspace.produtos.service.ProdutosService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
public class ProdutosController {
    
    private final ProdutosService produtosService;
    private final ProdutoMapper produtoMapper;

    public ProdutosController(ProdutosService produtosService, ProdutoMapper produtoMapper) {
        this.produtosService = produtosService;
        this.produtoMapper = produtoMapper;
    }

    @GetMapping
    @Operation(summary = "Lista todos os produtos", description = "Retorna uma lista de produtos, com opção de filtro por nome e         ordenação pelo preço.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de produtos retornada com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida, verifique os parâmetros."),
        @ApiResponse(responseCode = "404", description = "Nenhum produto encontrado com o filtro fornecido.")
    })
    public ResponseEntity<List<ProdutoDTO>> findAll(
            @Parameter(description = "Nome do produto para filtrar", required = false)
            @RequestParam(required = false) String nome,
            @Parameter(description = "Direção da ordenação: 'asc' ou 'desc'. Padrão é 'asc'.", example = "asc", required = false)
            @RequestParam(defaultValue = "asc") String order) {
        
        Sort.Direction direction = order.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, "preco");

        List<Produto> listaProdutos;
    
        if (nome != null && !nome.isEmpty()) {
            listaProdutos = produtosService.findbyNome(nome, sort);
        } else {
            listaProdutos = produtosService.findAll(sort);
        }
    
        List<ProdutoDTO> result = produtoMapper.toProdutoDTOList(listaProdutos);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Retorna os detalhes de um produto específico com base no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto encontrado"),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    public ResponseEntity<ProdutoDTO> findById(
            @Parameter(description = "ID do produto a ser buscado", required = true)
            @PathVariable Long id) {

        Produto produto = produtosService.findById(id)
            .orElseThrow(() -> new ProdutoNotFoundException("Produto não encontrado com id: " + id));
        ProdutoDTO result = produtoMapper.toDTO(produto);
        return ResponseEntity.ok(result);
    }    

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar produto por ID", description = "Remove um produto do sistema com base no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida.")
    })
    public ResponseEntity<Void> delete(
            @Parameter(description = "ID do produto a ser deletado", required = true)
            @PathVariable Long id) {
        produtosService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    @Operation(summary = "Criar novo produto", description = "Adiciona um novo produto ao sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Produto criado com sucesso."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida.")
    })
    public ResponseEntity<ProdutoDTO> create(
            @Valid
            @Parameter(description = "Dados do produto a ser criado", required = true)
            @RequestBody ProdutoCreateDTO produtoCreateDTO) {
        var produtoCreate = produtoMapper.toModel(produtoCreateDTO);
        ProdutoDTO produto = produtosService.create(produtoCreate);
        return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar produto por ID", description = "Atualiza os dados de um produto existente com base no ID fornecido.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso."),
        @ApiResponse(responseCode = "404", description = "Produto não encontrado."),
        @ApiResponse(responseCode = "400", description = "Requisição inválida.")
    })
    public ResponseEntity<ProdutoDTO> update(
            @Parameter(description = "ID do produto a ser atualizado", required = true)
            @PathVariable Long id, 
            @Parameter(description = "Dados do produto a serem atualizados", required = true)
            @RequestBody ProdutoCreateDTO produtoCreateDTO) {
        var produtoCreate = produtoMapper.toModel(produtoCreateDTO);
        Produto produtoAtualizado = produtosService.update(id, produtoCreate);
        ProdutoDTO result = produtoMapper.toDTO(produtoAtualizado);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
    
}
