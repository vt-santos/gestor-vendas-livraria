/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import modelo.Cliente;
import modelo.ClienteDAO;
import modelo.Produto;
import modelo.ProdutoDAO;
import modelo.Usuario;
import modelo.Venda;
import modelo.VendaDAO;
import modelo.VendaProduto;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author adm_pfelacio
 */
public class GerenciarVenda extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet GerenciarVenda</title>");
            out.println("</head>");
            out.println("<body>");

            HttpSession sessao = request.getSession();
            Usuario ulogado = (Usuario) request.getSession().getAttribute("ulogado");
            Venda v = new Venda();
            Cliente c = new Cliente();
            try {
                ClienteDAO cDAO = new ClienteDAO();
                ProdutoDAO pDAO = new ProdutoDAO();
                VendaDAO vDAO = new VendaDAO();
                String acao = request.getParameter("acao");
                ArrayList<VendaProduto> carrinho = new ArrayList<>();
                String idVendaStr = request.getParameter("idVenda");
                int idVenda = 0;

                if (acao.equals("alterar")) {
                    if (GerenciarLogin.verificarPermissao(request, response)) {
                    try {
                        idVenda = Integer.parseInt(idVendaStr);
                        v = vDAO.getCarregaPorID(idVenda);
                        if (v.getIdVenda() > 0) {
                            RequestDispatcher disp = getServletContext().getRequestDispatcher("/form_venda.jsp");
                            request.setAttribute("venda", v);
                            disp.forward(request, response);
                        } else {
                            exibirMensagem("Venda não encontrada", false, "", response);
                        }

                    } catch (NumberFormatException e) {
                        exibirMensagem("ID de venda inválida", false, "", response);
                    }
                } else {
                exibirMensagem("Acesso Negado", false, "", response);
                 }
                }
                if (acao.equals("visualizar")) {
                    if (GerenciarLogin.verificarPermissao(request, response)) {
                    try {
                        idVenda = Integer.parseInt(idVendaStr);
                        v = vDAO.getCarregaPorID2(idVenda);
                        if (v.getIdVenda() > 0) {
                            RequestDispatcher disp = getServletContext().getRequestDispatcher("/visualizar_venda.jsp");
                            request.setAttribute("venda", v);
                            disp.forward(request, response);
                        } else {
                            exibirMensagem("Venda não encontrada", false, "", response);
                        }

                    } catch (NumberFormatException e) {
                        exibirMensagem("ID de venda inválida", false, "", response);
                    }
                } else {
                exibirMensagem("Acesso Negado", false, "", response);
                 }
                }
                if (acao.equals("novaVenda")) {
                    String idCLienteStr = request.getParameter("idCliente");
                    int idCliente = Integer.parseInt(idCLienteStr);
                    c = cDAO.getCarregaPorID(idCliente);
                    v.setCliente(c);
                    v.setVendedor(ulogado);
                    v.setStatus(1);
                    v.setCarrinho(new ArrayList<VendaProduto>());
                    sessao.setAttribute("venda", v);
                    response.sendRedirect("form_venda.jsp");
                }
              
                if(acao.equals("cancelar")){
                    try{
                        idVenda = Integer.parseInt(idVendaStr);
                        if(idVenda!=0){
                            v.setIdVenda(idVenda);
                            if(vDAO.cancelar(v)){
                                exibirMensagem("Venda  cancelada com sucesso", true, "gerenciar_venda.do?acao=listarTodos", response);
                            }else{
                                exibirMensagem("Erro ao cancelar venda", false, "", response);
                            }
                        }
                    }catch(NumberFormatException e){
                       exibirMensagem("id da venda invalida", false, "", response); 
                    }
            }


                if (acao.equals("AdicionarProduto")) {
                    v = (Venda) sessao.getAttribute("venda");
                    Produto p = new Produto();
                    int idProduto = Integer.parseInt(request.getParameter("idProduto"));
                    p = pDAO.getCarregaPorID(idProduto);
                    int qtd = Integer.parseInt(request.getParameter("qtd"));
                    carrinho = v.getCarrinho();
                    VendaProduto vp = new VendaProduto();
                    vp.setProduto(p);
                    vp.setQtdVendida(qtd);
                    vp.setValorVendido(p.getValor());
                    carrinho.add(vp);
                    v.setCarrinho(carrinho);
                    sessao.setAttribute("venda", v);
                    response.sendRedirect("form_venda.jsp");
                }
                if (acao.equals("retirarProduto")) {
                    v = (Venda) sessao.getAttribute("venda");
                    int index = Integer.parseInt(request.getParameter("index"));
                    carrinho = v.getCarrinho();
                    carrinho.remove(index);
                    v.setCarrinho(carrinho);
                    sessao.setAttribute("venda", v);
                    response.sendRedirect("form_visualizar_carrinho.jsp");
                }
                if (acao.equals("gravar")) {
                    try {
                        v = (Venda) sessao.getAttribute("venda");
                        if (vDAO.gravar(v)) {
                            exibirMensagem("Venda gravada com sucesso!", true, "gerenciar_venda.do?acao=listarTodos", response);
                        } else {
                            exibirMensagem("Erro ao gravar a venda!", false, "", response);
                        }

                    } catch (Exception e) {
                        out.print("Erro" + e);
                    }
                }
	                if (acao.equals("listarTodos")) {
		                    ArrayList<Venda> listarTodos = new ArrayList<>();
		                    listarTodos = vDAO.getLista();
		                    RequestDispatcher disp = getServletContext().getRequestDispatcher("/listar_venda.jsp");
		                    request.setAttribute("listarTodos", listarTodos);
		                    disp.forward(request, response);
		                }
		                
		                if (acao.equals("visualizarRelatorio")) {
		                    ArrayList<Venda> listarTodos = new ArrayList<>();
		                    listarTodos = vDAO.getLista();
		                    
		                    // Calcular estatísticas
		                    int totalVendas = listarTodos.size();
		                    int vendasAtivas = 0;
		                    int vendasCanceladas = 0;
		                    
		                    for (Venda venda : listarTodos) {
		                        if (venda.getStatus() == 1) {
		                            vendasAtivas++;
		                        } else if (venda.getStatus() == 2) {
		                            vendasCanceladas++;
		                        }
		                    }
		                    
		                    // Enviar dados para a página JSP
		                    request.setAttribute("listarTodos", listarTodos);
		                    request.setAttribute("totalVendas", totalVendas);
		                    request.setAttribute("vendasAtivas", vendasAtivas);
		                    request.setAttribute("vendasCanceladas", vendasCanceladas);
		                    
		                    RequestDispatcher disp = getServletContext().getRequestDispatcher("/relatorio_vendas.jsp");
		                    disp.forward(request, response);
		                }
		                
		                
            } catch (Exception e) {
                out.print("erro" + e);
            }
            out.println("</body>");
            out.println("</html>");
        }
    }

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
        processRequest(request, response);
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
        processRequest(request, response);
    }

    private static void exibirMensagem(String mensagem, boolean resposta, String link, HttpServletResponse response) {

        try {
            PrintWriter out = response.getWriter();
            out.println("<script type='text/javascript'>");
            out.println("alert('" + mensagem + "')");
            if (resposta) {
                out.println("location.href='" + link + "'");
            } else {
                out.println("history.back();");
            }
            out.println("</script>");
            out.close();
        } catch (Exception e) {
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
