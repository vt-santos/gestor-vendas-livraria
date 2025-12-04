/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.sql.Date;
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
public class ClienteDAO extends DataBaseDAO{

    public ClienteDAO() throws ClassNotFoundException {
    }
    
    public ArrayList<Cliente> getLista(){
        
        ArrayList<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM cliente";
        
        try{
            //abre a conexao com o banco
            this.conectar();
            //Usando try-with-resources para PreparedStatement e ResultSet
            try(PreparedStatement pstm = conn.prepareStatement(sql);
                ResultSet rs = pstm.executeQuery()){
                
                while(rs.next()){
                    Cliente c =new Cliente();
                    c.setIdCliente(rs.getInt("idCliente"));
                    c.setNome(rs.getString("nome"));
                    c.setTelefone(rs.getString("telefone"));
                    c.setStatus(rs.getInt("status"));
                    
                    lista.add(c);
                }
            }
        }catch(SQLException e){
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null,e);
        }finally{
            this.desconectar();
        }
    
     return lista;
    }
    
    public boolean gravar(Cliente c){
        
        boolean sucesso = false;
        String sql;
        
        //define a query dependendo do IdMenu
        if(c.getIdCliente()==0){
            sql = "INSERT INTO cliente (nome, telefone, status) VALUES (?,?,?)";
        }else{
            sql = "UPDATE cliente SET nome=?, telefone=?, status=? WHERE idCliente=?";
        }
        try{
            this.conectar();
            //Usando try-with-resources para garantir fechamento do PreparedStatement
            try(PreparedStatement pstm = conn.prepareStatement(sql)){
                pstm.setString(1, c.getNome());
                pstm.setString(2, c.getTelefone());
                pstm.setInt(3, c.getStatus());
                //verifica se o idCliente é maior que zero, se for realiza o UPDaTE
                if(c.getIdCliente()>0){
                    pstm.setInt(4, c.getIdCliente());
                }
                int rowsAffected = pstm.executeUpdate();
                if(rowsAffected > 0){
                    sucesso = true;// a operação foi bem sucedida
                }
                
            }
        }catch(SQLException e){
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            this.desconectar();
        }
        return sucesso;
    
    }
    
    public Cliente getCarregaPorID(int idCliente){
        
        Cliente c = null;
        String sql = "SELECT * FROM cliente WHERE idCliente = ?";
        try{
            this.conectar();
            try(PreparedStatement pstm = conn.prepareStatement(sql)){
                pstm.setInt(1, idCliente);
                
                try(ResultSet rs = pstm.executeQuery()){
                    if(rs.next()){
                        c = new Cliente();
                        c.setIdCliente(rs.getInt("idCliente"));
                        c.setNome(rs.getString("nome"));
                        c.setTelefone(rs.getString("telefone"));
                        c.setStatus(rs.getInt("status"));
                        
                        
                    }
                }
                
            }
            
        }catch(SQLException e){
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            this.desconectar();
        }    
        return c;
    }
    
    public boolean desativar(Cliente c){
    
        boolean sucesso = false;
        try{
            this.conectar();
            String SQL = "UPDATE cliente SET status=2 WHERE idCliente=?";
            try(PreparedStatement pstm = conn.prepareStatement(SQL)){
                pstm.setInt(1, c.getIdCliente());
                int rowsffected = pstm.executeUpdate();
                if(rowsffected > 0){
                    sucesso = true;
                }
            }
        
        }catch(SQLException e){
            Logger.getLogger(ClienteDAO.class.getName()).log(Level.SEVERE, null, e);
        }finally{
            this.desconectar();
        }
        return sucesso;
    }
    
}
