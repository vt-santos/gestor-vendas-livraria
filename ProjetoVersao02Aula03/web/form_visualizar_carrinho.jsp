<%-- 
    Document   : index
    Created on : 12/08/2025, 21:29:37
    Author     : adm_pfelacio
--%>

<%@page import="modelo.VendaProduto"%>
<%@page import="modelo.Venda"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
        <link rel="stylesheet" href="css/estilo.css"/>
        <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
        <link rel="stylesheet" href="bootstrap/css/bootstrap-theme.min.css"/>
        <link rel="stylesheet" href="datatables/jquery.dataTables.min.css"/>
        <script type="text/javascript">
            function retirarProduto(index, item){
                
                if(confirm('Deseja realmente retirar o item '+item+'?')){
                    window.open("gerenciar_venda.do?acao=retirarProduto&index="+index,"_self");
                }
                
            }
        </script>    
        <title>Visualizar Carrinho</title>
    </head>
    <body>
        <div class="banner">
            <%@include file="banner.jsp" %>
        </div>
        <!--Menu de navegação -->
        <%@include file="menu.jsp" %>
        
        <div class="content">
            <h2>Meu Carrinho</h2>
            <%
              Venda v = new Venda();
              try{
              
                  v = (Venda) session.getAttribute("venda");
                  
              }catch(Exception e){
                  out.print("Erro"+e);
              }
            %>
            <br><br>
            Vendedor: <%=v.getVendedor().getNome()%>
            Cliente: <%=v.getCliente().getNome()%>
            <table class="table table-hover table-striped table-bordered display" id="listarProduto">
            
                <thead>
                    <tr>
                        <th>ITEM</th>
                        <th>PRODUTO</th>
                        <th>QTD</th>
                        <th>VALOR</th>
                        <th>TOTAL</th>
                        <th>REMOVER</th>
                    </tr>
                </thead>
                 <tfoot>
                    <tr>
                        <th>ITEM</th>
                        <th>PRODUTO</th>
                        <th>QTD</th>
                        <th>VALOR</th>
                        <th>TOTAL</th>
                        <th>REMOVER</th>
                    </tr>
                </tfoot>
                <tbody>
                    <%
                     
                        double total = 0;
                        int cont = 0;
                        for(VendaProduto vp: v.getCarrinho()){
                    %>        
                        <tr>
                            <td align="center"><%=cont+1%></td> 
                            <td><%=vp.getProduto().getNome()%></td>
                            <td><%=vp.getQtdVendida()%></td>
                            <td>R$ <%=vp.getValorVendido()%></td>
                            <td>R$ <%=vp.getQtdVendida()*vp.getValorVendido()%></td>
                            <td align="center">
                                <button class="btn btn-danger" onclick="retirarProduto(<%=cont%>,<%=cont+1%>)">
                                    <i class="glyphicon glyphicon-trash"></i>
                                </button>
                            </td>
                        </tr>
                        <%
                            total = total + (vp.getQtdVendida()*vp.getValorVendido());
                            cont++;
                        }
                        
                        %>
                       </tbody>
                </table>
               </div>
                       Valor Total ====================> R$ <%=total%>
                <br />
                <a href="gerenciar_cliente.do?acao=listarTodos" class="btn btn-danger">
                    <i class="glyphicon glyphicon">Cancelar</i>
                </a>
                <a href="form_venda.jsp" class="btn btn-warning">
                    <i class="glyphicon glyphicon">Continuar Vendendo</i>
                </a>
                <a href="gerenciar_venda.do?acao=gravar" class="btn btn-success">
                    <i class="glyphicon glyphicon">Confirmar Venda</i>
                </a>    
                    
        <script type="text/javascript" src="datatables/jquery.js"></script>
        <script type="text/javascript" src="datatables/jquery.dataTables.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                $("#listarProduto").DataTable({
                    "language":{
                    "url": "datatables/portugues.json"     
                    }
                });
            });
        
        <!-- ativar menu responsivo-->
        
            function toggleMenu(){
                var menu = document.getElementById("nav-links");
                menu.classList.toggle("show");
            }
        </script>    
        
    </body>
</html>


