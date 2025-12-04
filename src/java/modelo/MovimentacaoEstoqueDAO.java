/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sistema de Estoque
 */
public class MovimentacaoEstoqueDAO extends DataBaseDAO {

    public MovimentacaoEstoqueDAO() throws ClassNotFoundException {
    }
    
    /**
     * Registra uma movimentação de estoque e atualiza a quantidade do produto
     * @param m MovimentacaoEstoque a ser registrada
     * @return true se a operação foi bem-sucedida
     */
    public boolean registrarMovimentacao(MovimentacaoEstoque m) {
        boolean sucesso = false;
        String sqlMovimentacao = "INSERT INTO movimentacao_estoque (produto_idProduto, usuario_idUsuario, " +
                                 "tipoMovimentacao, quantidade, quantidadeAnterior, quantidadeAtual, motivo, dataMovimentacao) " +
                                 "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
        String sqlAtualizarEstoque = "UPDATE produto SET qtd = ? WHERE idProduto = ?";
        
        try {
            this.conectar();
            // Desabilita auto-commit para transação
            conn.setAutoCommit(false);
            
            try {
                // Busca quantidade atual do produto
                int qtdAtual = this.getEstoqueAtual(m.getProduto().getIdProduto());
                m.setQuantidadeAnterior(qtdAtual);
                
                // Calcula nova quantidade baseada no tipo de movimentação
                int novaQuantidade = qtdAtual;
                if (m.getTipoMovimentacao().equals("ENTRADA")) {
                    novaQuantidade = qtdAtual + m.getQuantidade();
                } else if (m.getTipoMovimentacao().equals("SAIDA") || m.getTipoMovimentacao().equals("VENDA")) {
                    novaQuantidade = qtdAtual - m.getQuantidade();
                } else if (m.getTipoMovimentacao().equals("AJUSTE")) {
                    novaQuantidade = m.getQuantidade(); // No ajuste, a quantidade é o valor final
                    m.setQuantidade(novaQuantidade - qtdAtual); // Ajusta a diferença
                }
                
                // Valida se a quantidade não ficará negativa
                if (novaQuantidade < 0) {
                    throw new SQLException("Estoque insuficiente. Quantidade atual: " + qtdAtual);
                }
                
                m.setQuantidadeAtual(novaQuantidade);
                
                // Registra a movimentação
                try (PreparedStatement pstmMov = conn.prepareStatement(sqlMovimentacao)) {
                    pstmMov.setInt(1, m.getProduto().getIdProduto());
                    pstmMov.setInt(2, m.getUsuario().getIdUsuario());
                    pstmMov.setString(3, m.getTipoMovimentacao());
                    pstmMov.setInt(4, m.getQuantidade());
                    pstmMov.setInt(5, m.getQuantidadeAnterior());
                    pstmMov.setInt(6, m.getQuantidadeAtual());
                    pstmMov.setString(7, m.getMotivo());
                    pstmMov.executeUpdate();
                }
                
                // Atualiza o estoque do produto
                try (PreparedStatement pstmProd = conn.prepareStatement(sqlAtualizarEstoque)) {
                    pstmProd.setInt(1, novaQuantidade);
                    pstmProd.setInt(2, m.getProduto().getIdProduto());
                    pstmProd.executeUpdate();
                }
                
                // Commit da transação
                conn.commit();
                sucesso = true;
                
            } catch (SQLException e) {
                // Rollback em caso de erro
                conn.rollback();
                Logger.getLogger(MovimentacaoEstoqueDAO.class.getName()).log(Level.SEVERE, null, e);
                throw e;
            }
            
        } catch (SQLException e) {
            Logger.getLogger(MovimentacaoEstoqueDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                Logger.getLogger(MovimentacaoEstoqueDAO.class.getName()).log(Level.SEVERE, null, e);
            }
            this.desconectar();
        }
        
        return sucesso;
    }
    
    /**
     * Retorna o estoque atual de um produto
     * @param idProduto ID do produto
     * @return quantidade em estoque
     */
    public int getEstoqueAtual(int idProduto) {
        int quantidade = 0;
        String sql = "SELECT qtd FROM produto WHERE idProduto = ?";
        
        try {
            boolean jaConectado = (conn != null && !conn.isClosed());
            if (!jaConectado) {
                this.conectar();
            }
            
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                pstm.setInt(1, idProduto);
                try (ResultSet rs = pstm.executeQuery()) {
                    if (rs.next()) {
                        quantidade = rs.getInt("qtd");
                    }
                }
            }
            
            if (!jaConectado) {
                this.desconectar();
            }
            
        } catch (SQLException e) {
            Logger.getLogger(MovimentacaoEstoqueDAO.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return quantidade;
    }
    
    /**
     * Lista todas as movimentações de estoque
     * @return ArrayList com todas as movimentações
     */
    public ArrayList<MovimentacaoEstoque> getLista() {
        ArrayList<MovimentacaoEstoque> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimentacao_estoque ORDER BY dataMovimentacao DESC";
        
        try {
            ProdutoDAO pDAO = new ProdutoDAO();
            UsuarioDAO uDAO = new UsuarioDAO();
            
            this.conectar();
            try (PreparedStatement pstm = conn.prepareStatement(sql);
                 ResultSet rs = pstm.executeQuery()) {
                
                while (rs.next()) {
                    MovimentacaoEstoque m = new MovimentacaoEstoque();
                    m.setIdMovimentacao(rs.getInt("idMovimentacao"));
                    m.setProduto(pDAO.getCarregaPorID(rs.getInt("produto_idProduto")));
                    m.setUsuario(uDAO.getCarregaPorID(rs.getInt("usuario_idUsuario")));
                    m.setTipoMovimentacao(rs.getString("tipoMovimentacao"));
                    m.setQuantidade(rs.getInt("quantidade"));
                    m.setQuantidadeAnterior(rs.getInt("quantidadeAnterior"));
                    m.setQuantidadeAtual(rs.getInt("quantidadeAtual"));
                    m.setMotivo(rs.getString("motivo"));
                    m.setDataMovimentacao(rs.getTimestamp("dataMovimentacao"));
                    
                    lista.add(m);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(MovimentacaoEstoqueDAO.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MovimentacaoEstoqueDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.desconectar();
        }
        
        return lista;
    }
    
    /**
     * Lista movimentações de um produto específico
     * @param idProduto ID do produto
     * @return ArrayList com as movimentações do produto
     */
    public ArrayList<MovimentacaoEstoque> getListaPorProduto(int idProduto) {
        ArrayList<MovimentacaoEstoque> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimentacao_estoque WHERE produto_idProduto = ? ORDER BY dataMovimentacao DESC";
        
        try {
            ProdutoDAO pDAO = new ProdutoDAO();
            UsuarioDAO uDAO = new UsuarioDAO();
            
            this.conectar();
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                pstm.setInt(1, idProduto);
                
                try (ResultSet rs = pstm.executeQuery()) {
                    while (rs.next()) {
                        MovimentacaoEstoque m = new MovimentacaoEstoque();
                        m.setIdMovimentacao(rs.getInt("idMovimentacao"));
                        m.setProduto(pDAO.getCarregaPorID(rs.getInt("produto_idProduto")));
                        m.setUsuario(uDAO.getCarregaPorID(rs.getInt("usuario_idUsuario")));
                        m.setTipoMovimentacao(rs.getString("tipoMovimentacao"));
                        m.setQuantidade(rs.getInt("quantidade"));
                        m.setQuantidadeAnterior(rs.getInt("quantidadeAnterior"));
                        m.setQuantidadeAtual(rs.getInt("quantidadeAtual"));
                        m.setMotivo(rs.getString("motivo"));
                        m.setDataMovimentacao(rs.getTimestamp("dataMovimentacao"));
                        
                        lista.add(m);
                    }
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(MovimentacaoEstoqueDAO.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MovimentacaoEstoqueDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            this.desconectar();
        }
        
        return lista;
    }
    
    /**
     * Lista produtos com estoque baixo (abaixo de um limite)
     * @param limiteMinimo quantidade mínima para considerar estoque baixo
     * @return ArrayList com produtos com estoque baixo
     */
    public ArrayList<Produto> getProdutosEstoqueBaixo(int limiteMinimo) {
        ArrayList<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto WHERE qtd <= ? AND status = 1 ORDER BY qtd ASC";
        
        try {
            this.conectar();
            try (PreparedStatement pstm = conn.prepareStatement(sql)) {
                pstm.setInt(1, limiteMinimo);
                
                try (ResultSet rs = pstm.executeQuery()) {
                    while (rs.next()) {
                        Produto p = new Produto();
                        p.setIdProduto(rs.getInt("idProduto"));
                        p.setNome(rs.getString("nome"));
                        p.setQtd(rs.getInt("qtd"));
                        p.setValor(rs.getDouble("valor"));
                        p.setStatus(rs.getInt("status"));
                        p.setFoto(rs.getString("foto"));
                        
                        lista.add(p);
                    }
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(MovimentacaoEstoqueDAO.class.getName()).log(Level.SEVERE, null, e);
        } finally {
            this.desconectar();
        }
        
        return lista;
    }
    
    /**
     * Verifica se há estoque suficiente para uma venda
     * @param idProduto ID do produto
     * @param quantidadeDesejada quantidade que se deseja vender
     * @return true se há estoque suficiente
     */
    public boolean verificarEstoqueSuficiente(int idProduto, int quantidadeDesejada) {
        int estoqueAtual = this.getEstoqueAtual(idProduto);
        return estoqueAtual >= quantidadeDesejada;
    }
}
