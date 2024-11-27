package     com.ms.order.dtos;      

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PedidoDTO {

    private Long id;
    private List<ProdutoDTO> produtos;
    private Double valorTotal;
    private String status;

    public PedidoDTO() {
    
    }

    @JsonCreator
    public PedidoDTO(@JsonProperty("id") Long id, 
                 @JsonProperty("produtos") List<ProdutoDTO> produtos,
                 @JsonProperty("valorTotal") Double valorTotal,
                 @JsonProperty("status") String status) {
                this.id = id;
                this.produtos = produtos;
                this.valorTotal = valorTotal;
                this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ProdutoDTO> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<ProdutoDTO> produtos) {
        this.produtos = produtos;
    }

    public Double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
