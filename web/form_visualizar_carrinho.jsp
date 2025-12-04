<%@page import="modelo.VendaProduto"%>
<%@page import="modelo.Venda"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/estilo.css">
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <title>Meu Carrinho | Livraria Aurora</title>

    <script>
        function retirarProduto(index){
            if(confirm("Deseja remover este item?")){
                window.location = "gerenciar_venda.do?acao=retirarProduto&index=" + index;
            }
        }
        function toggleMenu(){
            var menu = document.getElementById("nav-links");
            if (menu) {
                menu.classList.toggle("show");
            }
        }
    </script>
</head>
<body>

<header class="banner">
    <%@include file="banner.jsp"%>
    <div class="banner-text">
        <h1>Livraria Aurora</h1>
        <p>Meu Carrinho</p>
    </div>
</header>

<nav class="navbar">
    <span class="menu-icon" onclick="toggleMenu()">☰</span>
    <%@include file="menu.jsp"%>
</nav>

<section class="content">
    <h2>Meu Carrinho</h2>

    <%
        Venda v = (Venda) session.getAttribute("venda");
        double total = 0;
        int cont = 0;
    %>

    <div class="table-responsive">
        <table class="table table-hover table-striped table-bordered">
            <thead class="thead-dark">
                <tr>
                    <th>#</th>
                    <th>Produto</th>
                    <th>Qtd</th>
                    <th>Valor Unitário</th>
                    <th>Subtotal</th>
                    <th>Remover</th>
                </tr>
            </thead>
            <tbody>
            <%
                for(VendaProduto vp : v.getCarrinho()){
                    double subtotal = vp.getQtdVendida() * vp.getValorVendido();
                    total += subtotal;
            %>
                <tr>
                    <td><%=cont+1%></td>
                    <td><%=vp.getProduto().getNome()%></td>
                    <td><%=vp.getQtdVendida()%></td>
                    <td>R$ <%=String.format("%.2f", vp.getValorVendido())%></td>
                    <td>R$ <%=String.format("%.2f", subtotal)%></td>
                    <td>
                        <button onclick="retirarProduto(<%=cont%>)" class="btn btn-danger btn-sm" title="Remover produto">
                            <i class="glyphicon glyphicon-trash"></i>
                        </button>
                    </td>
                </tr>
            <%
                    cont++;
                }
            %>
            </tbody>
        </table>
    </div>

    <h3 class="mt-4">Total da Compra: 
        <span class="badge badge-success">R$ <%=String.format("%.2f", total)%></span>
    </h3>

    <!-- Botões de ação -->
    <div style="margin-top:20px;">
        <a href="gerenciar_cliente.do?acao=listarTodos" class="btn btn-danger">Cancelar</a>
        <a href="form_venda.jsp" class="btn btn-warning">Continuar Vendendo</a>
        <a href="gerenciar_venda.do?acao=gravar" class="btn btn-success">Confirmar Venda</a>
    </div>
</section>

</body>
</html>
