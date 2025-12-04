<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link rel="stylesheet" href="css/estilo.css"/>
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css"/>
    <title>Movimentação de Estoque | Livraria Aurora</title>

    <script>
        function toggleMenu(){
            var menu = document.getElementById("nav-links");
            if (menu) {
                menu.classList.toggle("show");
            }
        }
        
        function atualizarLabel() {
            var tipo = document.querySelector('input[name="tipoMovimentacao"]:checked').value;
            var labelQtd = document.getElementById("labelQuantidade");
            
            if (tipo === "ENTRADA") {
                labelQtd.innerHTML = "Quantidade a Adicionar:";
            } else if (tipo === "SAIDA") {
                labelQtd.innerHTML = "Quantidade a Retirar:";
            } else if (tipo === "AJUSTE") {
                labelQtd.innerHTML = "Quantidade Final (Ajuste):";
            }
        }
        
        function validarFormulario() {
            var idProduto = document.getElementById("idProduto").value;
            var quantidade = document.getElementById("quantidade").value;
            var motivo = document.getElementById("motivo").value;
            
            if (idProduto === "" || idProduto === "0") {
                alert("Por favor, selecione um produto.");
                return false;
            }
            
            if (quantidade === "" || quantidade <= 0) {
                alert("Por favor, informe uma quantidade válida.");
                return false;
            }
            
            if (motivo.trim() === "") {
                alert("Por favor, informe o motivo da movimentação.");
                return false;
            }
            
            return true;
        }
    </script>
</head>
<body>

<header class="banner">
    <%@include file="banner.jsp" %>
    <div class="banner-text">
        <h1>Livraria Aurora</h1>
        <p>Movimentação de Estoque</p>
    </div>
</header>

<nav class="navbar">
    <span class="menu-icon" onclick="toggleMenu()">☰</span>
    <%@include file="menu.jsp" %>
</nav>

<section class="content">
    <h2>Registrar Movimentação de Estoque</h2>
    <p>Preencha os dados para registrar entrada, saída ou ajuste de estoque.</p>
    
    <div class="form-container">
        <form action="gerenciar_estoque.do" method="POST" onsubmit="return validarFormulario()">
            <input type="hidden" name="acao" value="registrarMovimentacao"/>
            
            <div class="form-group">
                <label for="idProduto">Produto: *</label>
                <select name="idProduto" id="idProduto" class="form-control" required>
                    <option value="0">Selecione um produto</option>
                    <c:forEach var="p" items="${produtos}">
                        <option value="${p.idProduto}">
                            ${p.nome} - Estoque Atual: ${p.qtd} unidades
                        </option>
                    </c:forEach>
                </select>
            </div>
            
            <div class="form-group">
                <label>Tipo de Movimentação: *</label>
                <div class="radio-group">
                    <label class="radio-inline">
                        <input type="radio" name="tipoMovimentacao" value="ENTRADA" 
                               checked onchange="atualizarLabel()"/> Entrada
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="tipoMovimentacao" value="SAIDA" 
                               onchange="atualizarLabel()"/> Saída
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="tipoMovimentacao" value="AJUSTE" 
                               onchange="atualizarLabel()"/> Ajuste
                    </label>
                </div>
            </div>
            
            <div class="form-group">
                <label for="quantidade" id="labelQuantidade">Quantidade a Adicionar: *</label>
                <input type="number" name="quantidade" id="quantidade" 
                       class="form-control" min="1" required/>
            </div>
            
            <div class="form-group">
                <label for="motivo">Motivo: *</label>
                <textarea name="motivo" id="motivo" class="form-control" 
                          rows="3" required placeholder="Descreva o motivo da movimentação"></textarea>
            </div>
            
            <div class="form-group text-center">
                <button type="submit" class="btn btn-success btn-lg">
                    <i class="glyphicon glyphicon-floppy-disk"></i> Registrar Movimentação
                </button>
                <a href="gerenciar_estoque.do?acao=listarMovimentacoes" class="btn btn-default btn-lg">
                    <i class="glyphicon glyphicon-arrow-left"></i> Voltar
                </a>
            </div>
        </form>
    </div>
</section>

<style>
    .form-container {
        max-width: 800px;
        margin: 0 auto;
        background: #fff;
        padding: 30px;
        border-radius: 8px;
        box-shadow: 0 2px 10px rgba(0,0,0,0.1);
    }
    
    .radio-group {
        padding: 10px 0;
    }
    
    .radio-inline {
        margin-right: 20px;
        font-weight: normal;
    }
    
    .radio-inline input[type="radio"] {
        margin-right: 5px;
    }
</style>

</body>
</html>
