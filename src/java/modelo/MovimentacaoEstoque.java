/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.Date;

/**
 *
 * @author Sistema de Estoque
 */
public class MovimentacaoEstoque {
    
    private int idMovimentacao;
    private Produto produto;
    private Usuario usuario;
    private String tipoMovimentacao; // ENTRADA, SAIDA, AJUSTE, VENDA
    private int quantidade;
    private int quantidadeAnterior;
    private int quantidadeAtual;
    private String motivo;
    private Date dataMovimentacao;

    public MovimentacaoEstoque() {
    }

    public MovimentacaoEstoque(int idMovimentacao, Produto produto, Usuario usuario, 
                               String tipoMovimentacao, int quantidade, int quantidadeAnterior, 
                               int quantidadeAtual, String motivo, Date dataMovimentacao) {
        this.idMovimentacao = idMovimentacao;
        this.produto = produto;
        this.usuario = usuario;
        this.tipoMovimentacao = tipoMovimentacao;
        this.quantidade = quantidade;
        this.quantidadeAnterior = quantidadeAnterior;
        this.quantidadeAtual = quantidadeAtual;
        this.motivo = motivo;
        this.dataMovimentacao = dataMovimentacao;
    }

    public int getIdMovimentacao() {
        return idMovimentacao;
    }

    public void setIdMovimentacao(int idMovimentacao) {
        this.idMovimentacao = idMovimentacao;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getTipoMovimentacao() {
        return tipoMovimentacao;
    }

    public void setTipoMovimentacao(String tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getQuantidadeAnterior() {
        return quantidadeAnterior;
    }

    public void setQuantidadeAnterior(int quantidadeAnterior) {
        this.quantidadeAnterior = quantidadeAnterior;
    }

    public int getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(int quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Date getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(Date dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    @Override
    public String toString() {
        return "MovimentacaoEstoque{" + 
               "idMovimentacao=" + idMovimentacao + 
               ", produto=" + produto.getNome() + 
               ", tipoMovimentacao=" + tipoMovimentacao + 
               ", quantidade=" + quantidade + 
               ", quantidadeAnterior=" + quantidadeAnterior + 
               ", quantidadeAtual=" + quantidadeAtual + 
               ", motivo=" + motivo + 
               ", dataMovimentacao=" + dataMovimentacao + '}';
    }
}
