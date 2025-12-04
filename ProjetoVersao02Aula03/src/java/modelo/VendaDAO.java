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
 */
public class VendaDAO extends DataBaseDAO{

    public VendaDAO() throws ClassNotFoundException {
    }
    
    public boolean gravar(Venda v){
        
        boolean sucesso = false;
        boolean sucesso_item = false;
        boolean retorno = false;
        String sql;
        int cont =0;
        
        sql = "INSERT INTO venda (usuario_idUsuario, cliente_idCliente, status, dataVenda) VALUES (?,?,?,now())";
        
        try{
            this.conectar();
            //Usando try-with-resources para garantir fechamento do PreparedStatement
            try(PreparedStatement pstm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
                pstm.setInt(1, v.getVendedor().getIdUsuario());
                pstm.setInt(2, v.getCliente().getIdCliente());
                pstm.setDouble(3, v.getStatus());
                int rowsAffected = pstm.executeUpdate();
                if(rowsAffected > 0){
                    sucesso = true;// a operação foi bem sucedida
                    ResultSet rs= pstm.getGeneratedKeys();
                    if(rs.next()){
                        v.setIdVenda(rs.getInt(1));
                    }
                    for(VendaProduto vp: v.getCarrinho()){
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
                    }
                    if(cont == v.getCarrinho().size()) sucesso_item = true;
                }
                
            }
            if(sucesso && sucesso_item) retorno = true;
            
        }catch(SQLException e){
            Logger.getLogger(VendaDAO.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            this.desconectar();
        }
        return sucesso;
    
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
     
      public ArrayList<VendaProduto> itensVenda(int idVenda){
        
        ArrayList<VendaProduto> lista = new ArrayList<>();
        String sql = "SELECT * FROM venda_produto WHERE idVenda = ? ";
        
        try{
            ProdutoDAO pDAO = new ProdutoDAO();
            
            //abre a conexao com o banco
            this.conectar();
            //Usando try-with-resources para PreparedStatement e ResultSet
            try(PreparedStatement pstm = conn.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()){
                
                while(rs.next()){
                    VendaProduto vp =new VendaProduto();
                    vp.setIdVendaProduto(rs.getInt("idVendaProduto"));
                    vp.setQtdVendida(rs.getInt("qtdVendida"));
                    vp.setValorVendido(rs.getDouble("valorVendido"));
                    vp.setProduto(pDAO.getCarregaPorID(rs.getInt("idProduto")));
                                        
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
    
}
