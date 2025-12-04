/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import modelo.Cliente;
import modelo.ClienteDAO;

/**
 *
 * @author adm_pfelacio
 */
public class GerenciarCliente extends HttpServlet {

   

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        String mensagem = "";
        
        String acao = request.getParameter("acao");
        String idClienteStr = request.getParameter("idCliente");
        Cliente c = new Cliente();
        try{
            ClienteDAO cDAO = new ClienteDAO();
            int idCliente = 0;
            if(acao.equals("alterar")){
                if(GerenciarLogin.verificarPermissao(request, response)){
                    try{
                        idCliente = Integer.parseInt(idClienteStr);
                        c = cDAO.getCarregaPorID(idCliente);
                        if(c.getIdCliente()>0){
                            RequestDispatcher disp = getServletContext().getRequestDispatcher("/form_cliente.jsp");
                            request.setAttribute("cliente", c);
                            disp.forward(request, response);
                        }else{
                            exibirMensagem("Cliente não encontrado", false, "",response);
                        }

                    }catch(NumberFormatException e){
                        exibirMensagem("ID de cliente inválido", false, "",response);
                    }
                }else{
                       exibirMensagem("Acesso Negado", false, "",response);
                }
            }
            if(acao.equals("desativar")){
                if(GerenciarLogin.verificarPermissao(request, response)){
                    try{
                        idCliente = Integer.parseInt(idClienteStr);
                        if(idCliente!=0){
                            c.setIdCliente(idCliente);
                            if(cDAO.desativar(c)){
                                exibirMensagem("Cliente desativado com sucesso", true, "gerenciar_cliente.do?acao=listarTodos", response);
                            }else{
                                exibirMensagem("Erro ao desativar", false, "", response);
                            }
                        }
                    }catch(NumberFormatException e){
                       exibirMensagem("id de cliente invalido", false, "", response); 
                    }
                 }else{
                       exibirMensagem("Acesso Negado", false, "",response);
                }
            }
            
            if(acao.equals("listarTodos")){
                
                ArrayList<Cliente> listarTodos = new ArrayList<>();
                listarTodos = cDAO.getLista();
                
                    RequestDispatcher disp = getServletContext().getRequestDispatcher("/listar_cliente.jsp");
                    request.setAttribute("listarTodos", listarTodos);
                    disp.forward(request, response);
                
            
            }
        
        }catch(Exception e){
            out.println(e);
            exibirMensagem("Erro ao acessar o banco", false, "", response);
        }
        
       
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        //configurar o response
        response.setContentType("text/html");
        ArrayList<String> erros = new ArrayList<String>();
        //Recebe os parametros
            String nome = request.getParameter("nome");
            String telefone = request.getParameter("telefone");
            String statusStr = request.getParameter("status");
            String idClienteStr = request.getParameter("idCliente");
            
        try{
            
            
            int status = 0;
            int idCliente=0;
            
            
            if(idClienteStr!= null && !idClienteStr.isEmpty()){
                try{
                    idCliente = Integer.parseInt(idClienteStr);
                }catch(NumberFormatException e){
                    erros.add("ID de cliente inválido");
                }
            }
            if(statusStr!= null && !statusStr.isEmpty()){
                try{
                    status = Integer.parseInt(statusStr);
                }catch(NumberFormatException e){
                    erros.add("status do menu inválido");
                }
            }
            
            if(nome == null || nome.trim().isEmpty())
                erros.add("O campo nome é obrigatório");
            
            if(telefone == null || telefone.trim().isEmpty())
                erros.add("O campo telefone é obrigatório");
            
            
            if(erros.size()>0){
                String campos="";
                for(String erro:erros){
                    campos+= "\\n - "+erro;
                }
                exibirMensagem("Preencha o (s) campo(s)"+campos, false,"", response);
            }else{
                //Cria o objeto Cliente com os dadois recebidos
                Cliente c = new Cliente();
                c.setIdCliente(idCliente);
                // System.out.println(p.getIdPerfil());
                c.setNome(nome);
               // System.out.println(p.getNome());
                c.setStatus(status);
               // System.out.println(p.getStatus());
               c.setTelefone(telefone);
                
                //Usa DAO para gravar no banco
                ClienteDAO dao = new ClienteDAO();
                boolean sucesso = dao.gravar(c);
                
                if(sucesso){
                    exibirMensagem("Cliente gravado com sucesso", true, "gerenciar_cliente.do?acao=listarTodos", response);
                }else{
                    exibirMensagem("Erro ao gravar os dados do cliente", false, "", response);
                }
            }
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GerenciarCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
 private static void exibirMensagem(String mensagem, boolean resposta, String link, HttpServletResponse response){
            
        try{
            PrintWriter out = response.getWriter();
            out.println("<script type='text/javascript'>");
            out.println("alert('"+mensagem+"')");
            if(resposta){
                out.println("location.href='"+link+"'");
            }else{
                out.println("history.back();");
            }
            out.println("</script>");
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
