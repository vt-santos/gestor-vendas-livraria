/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adm_pfelacio
 * VERSÃO MODIFICADA COM INTEGRAÇÃO DE ESTOQUE
 */
public class VendaDAO extends DataBaseDAO{

    public VendaDAO() throws ClassNotFoundException {
    }
    
    /**
     * Grava uma venda e atualiza o estoque automaticamente
     * @param v Venda a ser gravada
     * @return true se a operação foi bem-sucedida
     */
    public boolean gravar(Venda v){
        
        boolean sucesso = false;
        boolean sucesso_item = false;
        boolean sucesso_estoque = false;
        boolean retorno = false;
        String sql;
        int cont = 0;
        int cont_estoque = 0;
        
        sql = "INSERT INTO venda (usuario_idUsuario, cliente_idCliente, status, dataVenda) VALUES (?,?,?,now())";
        
        try{
            MovimentacaoEstoqueDAO mDAO = new MovimentacaoEstoqueDAO();
            
            this.conectar();
            // Desabilita auto-commit para transação
            conn.setAutoCommit(false);
            
            try {
                // VALIDAÇÃO: Verifica se há estoque suficiente para todos os produtos
                for(VendaProduto vp: v.getCarrinho()){
                    if(!mDAO.verificarEstoqueSuficiente(vp.getProduto().getIdProduto(), vp.getQtdVendida())){
                        throw new SQLException("Estoque insuficiente para o produto: " + vp.getProduto().getNome() + 
                                             ". Disponível: " + mDAO.getEstoqueAtual(vp.getProduto().getIdProduto()) + 
                                             ", Solicitado: " + vp.getQtdVendida());
                    }
                }
                
                // Insere a venda
                try(PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                    pstm.setInt(1, v.getVendedor().getIdUsuario());
                    pstm.setInt(2, v.getCliente().getIdCliente());
                    pstm.setDouble(3, v.getStatus());
                    int rowsAffected = pstm.executeUpdate();
                    
                    if(rowsAffected > 0){
                        sucesso = true;
                        ResultSet rs = pstm.getGeneratedKeys();
                        if(rs.next()){
                            v.setIdVenda(rs.getInt(1));
                        }
                        
                        // Insere os itens da venda e registra movimentações de estoque
                        for(VendaProduto vp: v.getCarrinho()){
                            // Insere item da venda
                            String sql_item = "INSERT INTO venda_produto (venda_idVenda, produto_idProduto, qtdVendida, valorVendido) "
                                    + "VALUES (?,?,?,?)";
                            try(PreparedStatement pstm_item = conn.prepareStatement(sql_item)){
                                pstm_item.setInt(1, v.getIdVenda());
                                pstm_item.setInt(2, vp.getProduto().getIdProduto());
                                pstm_item.setInt(3, vp.getQtdVendida());
                                pstm_item.setDouble(4, vp.getValorVendido());
                                int rowsAffected_item = pstm_item.executeUpdate();
                                if(rowsAffected_item > 0) cont++;
                            }
                            
                            // Registra movimentação de estoque (baixa automática)
                            MovimentacaoEstoque m = new MovimentacaoEstoque();
                            m.setProduto(vp.getProduto());
                            m.setUsuario(v.getVendedor());
                            m.setTipoMovimentacao("VENDA");
                            m.setQuantidade(vp.getQtdVendida());
                            m.setMotivo("Venda ID: " + v.getIdVenda() + " - Cliente: " + v.getCliente().getNome());
                            
                            // Busca quantidade atual
                            int qtdAtual = mDAO.getEstoqueAtual(vp.getProduto().getIdProduto());
                            m.setQuantidadeAnterior(qtdAtual);
                            m.setQuantidadeAtual(qtdAtual - vp.getQtdVendida());
                            
                            // Registra a movimentação
                            String sql_mov = "INSERT INTO movimentacao_estoque (produto_idProduto, usuario_idUsuario, " +
                                           "tipoMovimentacao, quantidade, quantidadeAnterior, quantidadeAtual, motivo, dataMovimentacao) " +
                                           "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
                            try(PreparedStatement pstm_mov = conn.prepareStatement(sql_mov)){
                                pstm_mov.setInt(1, m.getProduto().getIdProduto());
                                pstm_mov.setInt(2, m.getUsuario().getIdUsuario());
                                pstm_mov.setString(3, m.getTipoMovimentacao());
                                pstm_mov.setInt(4, m.getQuantidade());
                                pstm_mov.setInt(5, m.getQuantidadeAnterior());
                                pstm_mov.setInt(6, m.getQuantidadeAtual());
                                pstm_mov.setString(7, m.getMotivo());
                                int rowsAffected_mov = pstm_mov.executeUpdate();
                                if(rowsAffected_mov > 0) cont_estoque++;
                            }
                            
                            // Atualiza quantidade no produto
                            String sql_update_produto = "UPDATE produto SET qtd = ? WHERE idProduto = ?";
                            try(PreparedStatement pstm_update = conn.prepareStatement(sql_update_produto)){
                                pstm_update.setInt(1, m.getQuantidadeAtual());
                                pstm_update.setInt(2, vp.getProduto().getIdProduto());
                                pstm_update.executeUpdate();
                            }
                        }
                        
                        if(cont == v.getCarrinho().size()) sucesso_item = true;
                        if(cont_estoque == v.getCarrinho().size()) sucesso_estoque = true;
                    }
                }
                
                // Commit apenas se tudo foi bem-sucedido
                if(sucesso && sucesso_item && sucesso_estoque){
                    conn.commit();
                    retorno = true;
                } else {
                    conn.rollback();
                }
                
            } catch (SQLException e) {
                // Rollback em caso de erro
                conn.rollback();
                Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, e);
                throw e;
            }
            
        }catch(SQLException e){
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, e);
            }
            this.desconectar();
        }
        return retorno;
    
    }
    
     public ArrayList<Venda> getLista(){
        
        ArrayList<Venda> lista = new ArrayList<>();
        String sql = "SELECT * FROM venda";
        
        try{
            ClienteDAO cDAO = new ClienteDAO();
            UsuarioDAO uDAO = new UsuarioDAO();
            //abre a conexao com o banco
            this.conectar();
            //Usando try-with-resources para PreparedStatement e ResultSet
            try(PreparedStatement pstm = conn.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()){
                
                while(rs.next()){
                    Venda v =new Venda();
                    v.setIdVenda(rs.getInt("idVenda"));
                    v.setDataVenda(rs.getDate("dataVenda"));
                    v.setStatus(rs.getInt("status"));
                    v.setCliente(cDAO.getCarregaPorID(rs.getInt("cliente_idCliente")));
                    v.setVendedor(uDAO.getCarregaPorID(rs.getInt("usuario_idUsuario")));
                    
                    lista.add(v);
                }
            }
        }catch(SQLException e){
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null,e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            this.desconectar();
        }
    
     return lista;
    }
     
     /**
      * Cancela uma venda e DEVOLVE o estoque
      * @param v Venda a ser cancelada
      * @return true se a operação foi bem-sucedida
      */
     public boolean cancelar(Venda v){
    
        boolean sucesso = false;
        try{
            MovimentacaoEstoqueDAO mDAO = new MovimentacaoEstoqueDAO();
            
            this.conectar();
            conn.setAutoCommit(false);
            
            try {
                // Busca os itens da venda para devolver ao estoque
                Venda vendaCompleta = this.getCarregaPorID2(v.getIdVenda());
                
                // Cancela a venda
                String SQL = "UPDATE venda SET status=2 WHERE idVenda=?";
                try(PreparedStatement pstm = conn.prepareStatement(SQL)){
                    pstm.setInt(1, v.getIdVenda());
                    int rowsAffected = pstm.executeUpdate();
                    if(rowsAffected > 0){
                        sucesso = true;
                        
                        // Devolve os produtos ao estoque
                        for(VendaProduto vp : vendaCompleta.getCarrinho()){
                            MovimentacaoEstoque m = new MovimentacaoEstoque();
                            m.setProduto(vp.getProduto());
                            m.setUsuario(vendaCompleta.getVendedor());
                            m.setTipoMovimentacao("ENTRADA");
                            m.setQuantidade(vp.getQtdVendida());
                            m.setMotivo("Devolução - Cancelamento da Venda ID: " + v.getIdVenda());
                            
                            // Busca quantidade atual
                            int qtdAtual = mDAO.getEstoqueAtual(vp.getProduto().getIdProduto());
                            m.setQuantidadeAnterior(qtdAtual);
                            m.setQuantidadeAtual(qtdAtual + vp.getQtdVendida());
                            
                            // Registra a movimentação de devolução
                            String sql_mov = "INSERT INTO movimentacao_estoque (produto_idProduto, usuario_idUsuario, " +
                                           "tipoMovimentacao, quantidade, quantidadeAnterior, quantidadeAtual, motivo, dataMovimentacao) " +
                                           "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";
                            try(PreparedStatement pstm_mov = conn.prepareStatement(sql_mov)){
                                pstm_mov.setInt(1, m.getProduto().getIdProduto());
                                pstm_mov.setInt(2, m.getUsuario().getIdUsuario());
                                pstm_mov.setString(3, m.getTipoMovimentacao());
                                pstm_mov.setInt(4, m.getQuantidade());
                                pstm_mov.setInt(5, m.getQuantidadeAnterior());
                                pstm_mov.setInt(6, m.getQuantidadeAtual());
                                pstm_mov.setString(7, m.getMotivo());
                                pstm_mov.executeUpdate();
                            }
                            
                            // Atualiza quantidade no produto
                            String sql_update = "UPDATE produto SET qtd = ? WHERE idProduto = ?";
                            try(PreparedStatement pstm_update = conn.prepareStatement(sql_update)){
                                pstm_update.setInt(1, m.getQuantidadeAtual());
                                pstm_update.setInt(2, vp.getProduto().getIdProduto());
                                pstm_update.executeUpdate();
                            }
                        }
                    }
                }
                
                conn.commit();
                
            } catch (SQLException e) {
                conn.rollback();
                Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, e);
                throw e;
            }
        
        }catch(SQLException e){
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally{
            try {
                conn.setAutoCommit(true);
            } catch (SQLException e) {
                Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, e);
            }
            this.desconectar();
        }
        return sucesso;
    }

    
     public Venda getCarregaPorID(int idVenda){
        
        Venda v = null;
        String sql = "SELECT * FROM venda WHERE idVenda = ?";
        try{
            ClienteDAO cDAO = new ClienteDAO();
            UsuarioDAO uDAO = new UsuarioDAO();
            this.conectar();
            try(PreparedStatement pstm = conn.prepareStatement(sql)){
                pstm.setInt(1, idVenda);
                
                try(ResultSet rs = pstm.executeQuery()){
                    if(rs.next()){
                        v = new Venda();
                        v.setIdVenda(rs.getInt("idVenda"));
                        v.setDataVenda(rs.getDate("dataVenda"));
                        v.setStatus(rs.getInt("status"));
                        v.setCliente(cDAO.getCarregaPorID(rs.getInt("cliente_idCliente")));
                        v.setVendedor(uDAO.getCarregaPorID(rs.getInt("usuario_idUsuario")));
                        v.setCarrinho(this.itensVenda(idVenda));
                        
                    }
                }
                
            }
            
        }catch(SQLException e){
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            this.desconectar();
        }    
        return v;
    }
     public Venda getCarregaPorID2(int idVenda){
        
        Venda v = null;
        String sql = "SELECT * FROM venda WHERE idVenda = ?";
        try{
            ClienteDAO cDAO = new ClienteDAO();
            UsuarioDAO uDAO = new UsuarioDAO();
            this.conectar();
            try(PreparedStatement pstm = conn.prepareStatement(sql)){
                pstm.setInt(1, idVenda);
                
                try(ResultSet rs = pstm.executeQuery()){
                    if(rs.next()){
                        v = new Venda();
                        v.setIdVenda(rs.getInt("idVenda"));
                        v.setDataVenda(rs.getDate("dataVenda"));
                        v.setStatus(rs.getInt("status"));
                        v.setCliente(cDAO.getCarregaPorID(rs.getInt("cliente_idCliente")));
                        v.setVendedor(uDAO.getCarregaPorID(rs.getInt("usuario_idUsuario")));
                        v.setCarrinho(this.itensVenda2(idVenda));
                        
                    }
                }
                
            }
            
        }catch(SQLException e){
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            this.desconectar();
        }    
        return v;
    }
     
      public ArrayList<VendaProduto> itensVenda(int idVenda){
        
        ArrayList<VendaProduto> lista = new ArrayList<>();
        String sql = "SELECT * FROM venda_produto WHERE venda_idVenda = ? ";
        
        try{
            ProdutoDAO pDAO = new ProdutoDAO();
            
            //abre a conexao com o banco
            this.conectar();
            //Usando try-with-resources para PreparedStatement e ResultSet
            try(PreparedStatement pstm = conn.prepareStatement(sql);
                
                ResultSet rs = pstm.executeQuery()){
                
                while(rs.next()){
                    VendaProduto vp =new VendaProduto();
                    vp.setIdVendaProduto(rs.getInt("venda_idVenda"));
                    vp.setQtdVendida(rs.getInt("qtdVendida"));
                    vp.setValorVendido(rs.getDouble("valorVendido"));
                    vp.setProduto(pDAO.getCarregaPorID(rs.getInt("produto_idProduto")));
                                        
                    lista.add(vp);
                }
            }
        }catch(SQLException e){
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null,e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            this.desconectar();
        }
    
     return lista;
    }
      public ArrayList<VendaProduto> itensVenda2(int idVenda){
        
        ArrayList<VendaProduto> lista = new ArrayList<>();
        String sql = "SELECT * FROM venda_produto WHERE venda_idVenda = ? ";
        
        try{
            ProdutoDAO pDAO = new ProdutoDAO();
            
            //abre a conexao com o banco
            this.conectar();
            //Usando try-with-resources para PreparedStatement e ResultSet
            try(PreparedStatement pstm = conn.prepareStatement(sql)){
                pstm.setInt(1, idVenda);
                
                try(ResultSet rs = pstm.executeQuery()){
                
                while(rs.next()){
                    VendaProduto vp =new VendaProduto();
                    vp.setIdVendaProduto(rs.getInt("venda_idVenda"));
                    vp.setQtdVendida(rs.getInt("qtdVendida"));
                    vp.setValorVendido(rs.getDouble("valorVendido"));
                    vp.setProduto(pDAO.getCarregaPorID(rs.getInt("produto_idProduto")));
                                        
                    lista.add(vp);
                }
            }
        }}catch(SQLException e){
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, e);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            this.desconectar();
        }
        
     return lista;
      }
}
