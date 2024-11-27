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
    // Criando a entidade Pedido a partir do DTO
    Pedido pedido = new Pedido();
    pedido.setStatus(pedidoDTO.getStatus());
    pedido.setValorTotal(pedidoDTO.getValorTotal());

    // Salvando o pedido uma vez, gerando seu ID
    pedido = pedidoRepository.save(pedido);

    // Inicializando a lista de produtos, caso esteja nula
    List<Produto> produtos = new ArrayList<>();

    // Processando os produtos
    for (ProdutoDTO produtoDTO : pedidoDTO.getProdutos()) {
        Produto produto = new Produto();
        produto.setPreco(produtoDTO.getPreco());
        produto.setQuantidade(produtoDTO.getQuantidade());

        // Associando o pedido já persistido ao produto
        produto.setPedido(pedido);  // Associando o pedido (já com ID) ao produto

        // Salvando o produto
        produtoRepository.save(produto);  // Agora salva o produto corretamente associado ao pedido

        // Adicionando o produto à lista de produtos
        produtos.add(produto);
    }

    // Associando a lista de produtos ao pedido
    pedido.setProdutos(produtos);

    // Atualizando o valor total do pedido
    pedido.setValorTotal(calcularValorTotal(pedidoDTO.getProdutos()));

    // Atualizando o status do pedido para "PROCESSADO"
    pedido.setStatus("PROCESSADO");

    // Atualizando o pedido com os produtos e o novo status (não há necessidade de chamá-lo duas vezes)
    pedidoRepository.save(pedido);  // Esse `save` atualiza o pedido, não cria um novo

    // Retornando o DTO do pedido
    return mapPedidoEntityToDTO(pedido);

    }

    public void enviarPedidoParaFilaRecebidos(PedidoDTO pedidoDTO) {
        rabbitTemplate.convertAndSend("pedidos", pedidoDTO);
    }


    private Produto persistirProduto(ProdutoDTO produtoDTO) {
        // Criar o Produto a partir do DTO
        Produto produto = new Produto();
        produto.setQuantidade(produtoDTO.getQuantidade());
        produto.setPreco(produtoDTO.getPreco());
        
        // Salvar o produto no banco de dados
        return produtoRepository.save(produto);  // Salva o produto separadamente no banco
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
