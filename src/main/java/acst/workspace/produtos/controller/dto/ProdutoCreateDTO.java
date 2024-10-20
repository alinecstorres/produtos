package acst.workspace.produtos.controller.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoCreateDTO {

    @NotNull(message = "O nome do produto não pode ser nulo")
    @Size(min = 1, max = 200, message = "O nome deve ter entre 1 e 200 caracteres")
    private String nome;

    @Size(max = 200, message = "A descrição deve ter no máximo 200 caracteres")
    private String descricao;

    @NotNull(message = "O preço do produto não pode ser nulo")
    @Positive(message = "O preço deve ser maior que zero")
    private Double preco;

    @NotNull(message = "A quantidade em estoque não pode ser nula")
    @Positive(message = "A quantidade em estoque deve ser um número inteiro positivo")
    private Integer quantidadeEstoque;
    
}
