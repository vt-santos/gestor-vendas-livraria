<%@page import="modelo.Venda"%>
<%@page import="modelo.Cliente"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/estilo.css">
    <title>Nova Venda | Livraria Aurora</title>
</head>
<body>

<header class="banner">
    <%@include file="banner.jsp"%>
    <div class="banner-text">
        <h1>Livraria Aurora</h1>
        <p>Nova Venda</p>
    </div>
</header>

<nav class="navbar">
    <span class="menu-icon" onclick="toggleMenu()">☰</span>
    <%@include file="menu.jsp"%>
</nav>

<section class="content">
    <h2>Catálogo de Livros</h2>

    <%
        Venda v = (Venda) session.getAttribute("venda");
    %>

    <p><strong>Vendedor:</strong> <%=v.getVendedor().getNome()%></p>
    <p><strong>Cliente:</strong> <%=v.getCliente().getNome()%></p>
    <p><strong>Itens no carrinho:</strong> <%=v.getCarrinho().size()%></p>

    <jsp:useBean id="produto" class="modelo.ProdutoDAO"/>

    <div class="cards">
        <c:forEach var="p" items="${produto.lista}">
            <div class="card">
                <form method="get" action="gerenciar_venda.do">
                    <input type="hidden" name="acao" value="AdicionarProduto">
                    <input type="hidden" name="idProduto" value="${p.idProduto}">

                    <img src="fotos/${p.foto}" alt="Capa do livro ${p.nome}">
                    <h3>${p.nome}</h3>

                    <div class="compra-container">
                        <input type="number" name="qtd" value="1" class="campo-qtd" min="1">
                        <button class="btn btn-primary btn-sm btn-carrinho" title="Adicionar ao carrinho">
                            <img src="imagens/carrinho.png" class="icon-carrinho" alt="Carrinho">
                        </button>
                    </div>
                </form>
            </div>
        </c:forEach>
    </div>

    <!-- Botões de ação -->
    <div style="margin-top:20px;">
        <a href="gerenciar_cliente.do?acao=listarTodos" class="btn btn-danger">Cancelar</a>
        <a href="form_visualizar_carrinho.jsp" class="btn btn-success">Ver Carrinho</a>
    </div>
</section>

<script>
    function toggleMenu(){
        var menu = document.getElementById("nav-links");
        if (menu) {
            menu.classList.toggle("show");
        }
    }
</script>

</body>
</html>
