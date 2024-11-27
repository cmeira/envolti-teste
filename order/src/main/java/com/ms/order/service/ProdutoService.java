package com.ms.order.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.order.dtos.ProdutoDTO;
import com.ms.order.model.Produto;
import com.ms.order.repository.ProdutoRepository;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<ProdutoDTO> listarProdutos() {
        List<Produto> produtos = produtoRepository.findAll();
        return produtos.stream().map(this::mapProdutoEntityToDTO).collect(Collectors.toList());
    }

    public ProdutoDTO obterProdutoPorId(Long id) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return mapProdutoEntityToDTO(produto);
    }

    public void criarProduto(ProdutoDTO produtoDTO) {
        Produto produto = mapProdutoDTOToEntity(produtoDTO);
        produtoRepository.save(produto);
    }

    public void atualizarProduto(Long id, ProdutoDTO produtoDTO) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produto.setQuantidade(produtoDTO.getQuantidade());
        produto.setPreco(produtoDTO.getPreco());
        produtoRepository.save(produto);
    }

    public void deletarProduto(Long id) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        produtoRepository.delete(produto);
    }

    private ProdutoDTO mapProdutoEntityToDTO(Produto produto) {
        ProdutoDTO dto = new ProdutoDTO();
        dto.setId(produto.getId());
        dto.setQuantidade(produto.getQuantidade());
        dto.setPreco(produto.getPreco());
        return dto;
    }

    private Produto mapProdutoDTOToEntity(ProdutoDTO dto) {
        Produto produto = new Produto();
        produto.setId(dto.getId());
        produto.setQuantidade(dto.getQuantidade());
        produto.setPreco(dto.getPreco());
        return produto;
    }
}
