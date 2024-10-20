package acst.workspace.produtos.controller.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import acst.workspace.produtos.controller.dto.ProdutoCreateDTO;
import acst.workspace.produtos.controller.dto.ProdutoDTO;
import acst.workspace.produtos.model.Produto;

@Mapper(componentModel = "spring")
public interface ProdutoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dataCriacao", ignore = true)
    Produto toModel(ProdutoCreateDTO produtoDTO);

    ProdutoDTO toDTO(Produto produto);   

    List<ProdutoDTO> toProdutoDTOList(List<Produto> produtos); 
}
