package acst.workspace.produtos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import acst.workspace.produtos.controller.dto.ProdutoDTO;
import acst.workspace.produtos.controller.exception.ProdutoNotFoundException;
import acst.workspace.produtos.controller.mapper.ProdutoMapper;
import acst.workspace.produtos.model.Produto;
import acst.workspace.produtos.repository.ProdutosRepository;

@Service
public class ProdutosService {

    private final ProdutosRepository produtosRepository;
    private final ProdutoMapper produtoMapper;

    public ProdutosService(ProdutosRepository produtosRepository, ProdutoMapper produtoMapper) {
        this.produtosRepository = produtosRepository;
        this.produtoMapper = produtoMapper;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Produto> findAll(Sort sort) {
        return produtosRepository.findAll(sort);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Produto> findbyNome(String nome, Sort sort) {
        return produtosRepository.findByNomeContainingIgnoreCase(nome, sort);
    }

    @Transactional(readOnly = true)
    public Optional<Produto> findById(Long id) {
        return produtosRepository.findById(id);
    }
    
    @Transactional
    public ProdutoDTO create(Produto produto) {
        Produto produtoSalvo = produtosRepository.save(produto);
        return produtoMapper.toDTO(produtoSalvo);
    }

    @Transactional
    public void delete(Long id) {
        produtosRepository.deleteById(id);
    }

    @Transactional
    public Produto update(Long id, Produto produtoCreate) {
        Optional<Produto> optionalProduto = produtosRepository.findById(id);

        if (optionalProduto.isPresent()) {
            Produto produtoExistente = optionalProduto.get();
            produtoExistente.setNome(produtoCreate.getNome());
            produtoExistente.setPreco(produtoCreate.getPreco());
            produtoExistente.setDescricao(produtoCreate.getDescricao());
            produtoExistente.setQuantidadeEstoque(produtoCreate.getQuantidadeEstoque());
            return produtosRepository.save(produtoExistente); 
        } else {
            throw new ProdutoNotFoundException("Produto não encontrado com id: " + id);
        }
    }


}
