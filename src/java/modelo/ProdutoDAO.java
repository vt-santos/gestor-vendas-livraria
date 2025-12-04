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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adm_pfelacio
 */
public class ProdutoDAO extends DataBaseDAO{

    public ProdutoDAO() throws ClassNotFoundException {
    }
    
    public ArrayList<Produto> getLista(){
        
        ArrayList<Produto> lista = new ArrayList<>();
        String sql = "SELECT * FROM produto";
        
        try{
            //abre a conexao com o banco
            this.conectar();
            //Usando try-with-resources para PreparedStatement e ResultSet
            try(PreparedStatement pstm = conn.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()){
                
                while(rs.next()){
                    Produto p =new Produto();
                    p.setIdProduto(rs.getInt("idProduto"));
                    p.setNome(rs.getString("nome"));
                    p.setQtd(rs.getInt("qtd"));
                    p.setValor(rs.getDouble("valor"));
                    p.setStatus(rs.getInt("status"));
                    p.setFoto(rs.getString("foto"));
                    
                    lista.add(p);
                }
            }
        }catch(SQLException e){
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null,e);
        }finally{
            this.desconectar();
        }
    
     return lista;
    }
    
    public boolean gravar(Produto p){
        
        boolean sucesso = false;
        String sql;
        
        //define a query dependendo do IdMenu
        if(p.getIdProduto()==0){
            sql = "INSERT INTO produto (nome, qtd, valor, status, foto) VALUES (?,?,?,?,?)";
        }else{
            sql = "UPDATE produto SET nome=?, qtd=?, valor=?, status=?, foto=? WHERE idProduto=?";
        }
        try{
            this.conectar();
            //Usando try-with-resources para garantir fechamento do PreparedStatement
            try(PreparedStatement pstm = conn.prepareStatement(sql)){
                pstm.setString(1, p.getNome());
                pstm.setInt(2, p.getQtd());
                pstm.setDouble(3, p.getValor());
                pstm.setInt(4, p.getStatus());
                pstm.setString(5, p.getFoto());
                //verifica se o idProduto é maior que zero, se for realiza o UPDaTE
                if(p.getIdProduto()>0){
                    pstm.setInt(6, p.getIdProduto());
                }
                int rowsAffected = pstm.executeUpdate();
                if(rowsAffected > 0){
                    sucesso = true;// a operação foi bem sucedida
                }
                
            }
        }catch(SQLException e){
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            this.desconectar();
        }
        return sucesso;
    
    }
    
    public Produto getCarregaPorID(int idProduto){
        
        Produto p = null;
        String sql = "SELECT * FROM produto WHERE idProduto = ?";
        try{
            this.conectar();
            try(PreparedStatement pstm = conn.prepareStatement(sql)){
                pstm.setInt(1, idProduto);
                
                try(ResultSet rs = pstm.executeQuery()){
                    if(rs.next()){
                        p = new Produto();
                        p.setIdProduto(rs.getInt("idProduto"));
                        p.setNome(rs.getString("nome"));
                        p.setQtd(rs.getInt("qtd"));
                        p.setValor(rs.getDouble("valor"));
                        p.setStatus(rs.getInt("status"));
                        p.setFoto(rs.getString("foto"));
                        
                        
                    }
                }
                
            }
            
        }catch(SQLException e){
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            this.desconectar();
        }    
        return p;
    }
    
    public boolean desativar(Produto p){
    
        boolean sucesso = false;
        try{
            this.conectar();
            String SQL = "UPDATE produto SET status=2 WHERE idProduto=?";
            try(PreparedStatement pstm = conn.prepareStatement(SQL)){
                pstm.setInt(1, p.getIdProduto());
                int rowsffected = pstm.executeUpdate();
                if(rowsffected > 0){
                    sucesso = true;
                }
            }
        
        }catch(SQLException e){
            Logger.getLogger(ProdutoDAO.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            this.desconectar();
        }
        return sucesso;
    }
}
