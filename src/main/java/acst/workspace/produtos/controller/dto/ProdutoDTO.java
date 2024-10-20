package acst.workspace.produtos.controller.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProdutoDTO {
    
    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    private String nome;

    @Size(min = 1, max = 200)
    private String descricao;

    @NotNull
    private Double preco;

    @NotNull
    private Integer quantidadeEstoque;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime dataCriacao;
    
}
