package com.ms.order.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.order.dtos.PedidoDTO;
import com.ms.order.dtos.ProdutoDTO;
import com.ms.order.model.Pedido;
import com.ms.order.model.Produto;
import com.ms.order.repository.PedidoRepository;
import com.ms.order.repository.ProdutoRepository;

import jakarta.transaction.Transactional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional
    public PedidoDTO criarPedido(PedidoDTO pedidoDTO) {
   
    Pedido pedido = new Pedido();
    pedido.setStatus(pedidoDTO.getStatus());
    pedido.setValorTotal(pedidoDTO.getValorTotal());
    
    pedido = pedidoRepository.save(pedido);
  
    List<Produto> produtos = new ArrayList<>();

    // Processando os produtos
    for (ProdutoDTO produtoDTO : pedidoDTO.getProdutos()) {
        Produto produto = new Produto();
        produto.setPreco(produtoDTO.getPreco());
        produto.setQuantidade(produtoDTO.getQuantidade());
        produto.setPedido(pedido);       
        produtoRepository.save(produto);       
        produtos.add(produto);
    }

    pedido.setProdutos(produtos);

    pedido.setValorTotal(calcularValorTotal(pedidoDTO.getProdutos()));

    pedido.setStatus("PROCESSADO");

    pedidoRepository.save(pedido); 
    
    return mapPedidoEntityToDTO(pedido);

    }

    public void enviarPedidoParaFilaRecebidos(PedidoDTO pedidoDTO) {
        rabbitTemplate.convertAndSend("pedidos", pedidoDTO);
    }
    

    public List<PedidoDTO> listarPedidos(String status) {
        List<Pedido> pedidos = (status != null) ? 
            pedidoRepository.findByStatus(status) : 
            pedidoRepository.findAll();

        return pedidos.stream().map(this::mapPedidoEntityToDTO).collect(Collectors.toList());
    }

    public void atualizarStatusPedido(Long id, String status) {
        Pedido pedido = pedidoRepository.findById(id).orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedido.setStatus(status);
        pedidoRepository.save(pedido);
    }

    private PedidoDTO mapPedidoEntityToDTO(Pedido pedido) {
        PedidoDTO dto = new PedidoDTO();
        dto.setId(pedido.getId());
        dto.setStatus(pedido.getStatus());
        dto.setValorTotal(pedido.getValorTotal());
        dto.setProdutos(pedido.getProdutos().stream().map(this::mapProdutoEntityToDTO).collect(Collectors.toList()));
        return dto;
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

    private Double calcularValorTotal(List<ProdutoDTO> produtos) {
        return produtos.stream().mapToDouble(p -> p.getPreco() * p.getQuantidade()).sum();
    }
}
