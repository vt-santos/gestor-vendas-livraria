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
import modelo.Menu;
import modelo.MenuDAO;
import modelo.Perfil;
import modelo.PerfilDAO;

/**
 *
 * @author adm_pfelacio
 */
public class GerenciarMenu extends HttpServlet {

    

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
        String idMenuStr = request.getParameter("idMenu");
        Menu m = new Menu();
        try{
            MenuDAO mDAO = new MenuDAO();
            int idMenu = 0;
            if(acao.equals("alterar")){
                if(GerenciarLogin.verificarPermissao(request, response)){
                    try{
                        idMenu = Integer.parseInt(idMenuStr);
                        m = mDAO.getCarregaPorID(idMenu);
                        if(m.getIdMenu()>0){
                            RequestDispatcher disp = getServletContext().getRequestDispatcher("/form_menu.jsp");
                            request.setAttribute("menu", m);
                            disp.forward(request, response);
                        }else{
                            exibirMensagem("Menu não encontrado", false, "",response);
                        }

                    }catch(NumberFormatException e){
                        exibirMensagem("ID de menu inválido", false, "",response);
                    }
                }else{
                       exibirMensagem("Acesso Negado", false, "",response);
                }
            }
            if(acao.equals("excluir")){
                if(GerenciarLogin.verificarPermissao(request, response)){
                    try{
                        idMenu = Integer.parseInt(idMenuStr);
                        if(idMenu!=0){
                            m.setIdMenu(idMenu);
                            if(mDAO.excluir(m)){
                                exibirMensagem("Menu excluido com sucesso", true, "gerenciar_menu.do?acao=listarTodos", response);
                            }else{
                                exibirMensagem("Erro ao excluir", false, "", response);
                            }
                        }
                    }catch(NumberFormatException e){
                       exibirMensagem("id de menu invalido", false, "", response); 
                    }
                 }else{
                       exibirMensagem("Acesso Negado", false, "",response);
                }
            }
            
            if(acao.equals("listarTodos")){
                
                ArrayList<Menu> listarTodos = new ArrayList<>();
                listarTodos = mDAO.getLista();
                
                    RequestDispatcher disp = getServletContext().getRequestDispatcher("/listar_menu.jsp");
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
            String link = request.getParameter("link");
            String icone = request.getParameter("icone");
            String exibirStr = request.getParameter("exibir");
            String idMenuStr = request.getParameter("idMenu");
            
        try{
            
            
            int exibir = 0;
            int idMenu=0;
            
            
            if(idMenuStr!= null && !idMenuStr.isEmpty()){
                try{
                    idMenu = Integer.parseInt(idMenuStr);
                }catch(NumberFormatException e){
                    erros.add("ID de menu inválido");
                }
            }
            if(exibirStr!= null && !exibirStr.isEmpty()){
                try{
                    exibir = Integer.parseInt(exibirStr);
                }catch(NumberFormatException e){
                    erros.add("exibir do menu inválido");
                }
            }
            
            if(nome == null || nome.trim().isEmpty())
                erros.add("O campo nome é obrigatório");
            
            if(link == null || link.trim().isEmpty())
                erros.add("O campo link é obrigatório");
            
            
            if(erros.size()>0){
                String campos="";
                for(String erro:erros){
                    campos+= "\\n - "+erro;
                }
                exibirMensagem("Preencha o (s) campo(s)"+campos, false,"", response);
            }else{
                //Cria o objeto Menu com os dadois recebidos
                Menu m = new Menu();
                m.setIdMenu(idMenu);
                // System.out.println(p.getIdPerfil());
                m.setNome(nome);
               // System.out.println(p.getNome());
                m.setExibir(exibir);
               // System.out.println(p.getStatus());
               m.setLink(link);
               m.setIcone(icone);
                
                //Usa DAO para gravar no banco
                MenuDAO dao = new MenuDAO();
                boolean sucesso = dao.gravar(m);
                
                if(sucesso){
                    exibirMensagem("Menu gravado com sucesso", true, "gerenciar_menu.do?acao=listarTodos", response);
                }else{
                    exibirMensagem("Erro ao gravar os dados do menu", false, "", response);
                }
            }
            
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(GerenciarMenu.class.getName()).log(Level.SEVERE, null, ex);
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
