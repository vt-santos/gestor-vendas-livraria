<%@page import="modelo.Cliente"%>
<%@page import="modelo.Venda"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="css/estilo.css"/>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
    <title>Visualizar Venda | Livraria Aurora</title>
</head>
<body>

<header class="banner">
    <%@include file="banner.jsp" %>
    <div class="banner-text">
        <h1>Livraria Aurora</h1>
        <p>Visualizar Venda</p>
    </div>
</header>

<nav class="navbar">
    <span class="menu-icon" onclick="toggleMenu()">☰</span>
    <%@include file="menu.jsp" %>
</nav>

<section class="content">
    <h2>Detalhes da Venda</h2>
    <p><strong>Vendedor:</strong> ${venda.vendedor.nome}</p>
    <p><strong>Cliente:</strong> ${venda.cliente.nome}</p>
    <p><strong>Data da Venda:</strong> <fmt:formatDate value="${venda.dataVenda}" pattern="dd/MM/yyyy"/></p>

    <!-- Calcular total da venda -->
    <c:set var="total" value="0"/>
    <c:forEach var="p" items="${venda.carrinho}">
        <c:set var="subtotal" value="${p.qtdVendida * p.valorVendido}"/>
        <c:set var="total" value="${total + subtotal}"/>
    </c:forEach>

    <h3 class="mt-4">Valor total da compra: 
        <span class="badge badge-success">
            <fmt:formatNumber value="${total}" type="currency" currencySymbol="R$" groupingUsed="true"/>
        </span>
    </h3>

    <h3 class="mt-4">Itens da Venda</h3>

    <div class="cards">
        <c:forEach var="p" items="${venda.carrinho}">
            <div class="card">
                <img src="fotos/${p.produto.foto}" alt="Foto do Produto ${p.produto.nome}" style="width:80px; height:auto; margin-bottom:10px; border-radius:4px;">
                <h3>${p.produto.nome}</h3>
                <p><strong>Quantidade:</strong> ${p.qtdVendida}</p>
                <p><strong>Valor Unitário:</strong> 
                    <fmt:formatNumber value="${p.valorVendido}" type="currency" currencySymbol="R$" groupingUsed="true"/>
                </p>
                <p><strong>Total:</strong> 
                    <fmt:formatNumber value="${p.qtdVendida * p.valorVendido}" type="currency" currencySymbol="R$" groupingUsed="true"/>
                </p>
            </div>
        </c:forEach>
    </div>

    <div class="btn-group-acao mt-4">
        <a href="gerenciar_venda.do?acao=listarTodos" class="btn btn-warning">Voltar</a>
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
