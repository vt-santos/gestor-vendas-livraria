<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="css/estilo.css">
    <link rel="stylesheet" href="bootstrap/css/bootstrap.min.css">
    <title>Relatório de Vendas | Livraria Aurora</title>
    
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html2canvas/1.4.1/html2canvas.min.js"></script>
    <script>
        function toggleMenu(){
            var menu = document.getElementById("nav-links");
            if (menu) {
                menu.classList.toggle("show");
            }
        }
        
        function imprimirRelatorio() {
            window.print();
        }
        
        function gerarPDF() {
            const { jsPDF } = window.jspdf;
            const element = document.querySelector('.relatorio-container'); // Elemento que contém o relatório
            
            // Oculta elementos que não devem aparecer no PDF (botões, menus, etc.)
            const buttons = element.querySelectorAll('.btn-imprimir, .btn-default');
            buttons.forEach(btn => btn.style.display = 'none');
            
            html2canvas(element, { scale: 2 }).then(canvas => {
                const imgData = canvas.toDataURL('image/png');
                const pdf = new jsPDF('p', 'mm', 'a4');
                const imgProps= pdf.getImageProperties(imgData);
                const pdfWidth = pdf.internal.pageSize.getWidth();
                const pdfHeight = (imgProps.height * pdfWidth) / imgProps.width;
                
                pdf.addImage(imgData, 'PNG', 0, 0, pdfWidth, pdfHeight);
                pdf.save("relatorio_vendas.pdf");
                
                // Restaura a exibição dos botões
                buttons.forEach(btn => btn.style.display = 'block');
            });
        }
    </script>
</head>
<body>

<header class="banner">
    <%@include file="banner.jsp"%>
    <div class="banner-text">
        <h1>Livraria Aurora</h1>
        <p>Relatório de Vendas</p>
    </div>
</header>

<nav class="navbar">
    <span class="menu-icon" onclick="toggleMenu()">☰</span>
    <%@include file="menu.jsp"%>
</nav>

<section class="content">
    <div class="relatorio-container">
        <div class="relatorio-header">
            <h1>Relatório de Vendas</h1>
            <p>Livraria Aurora - Sistema de Gestão</p>
            <p><strong>Data de Geração:</strong> <fmt:formatDate value="<%= new java.util.Date() %>" pattern="dd/MM/yyyy HH:mm:ss"/></p>
        </div>
        
        <div class="btn-imprimir text-center">
            <div class="row">
                <div class="col-md-6 col-md-offset-3">
                    <button onclick="imprimirRelatorio()" class="btn btn-primary btn-lg btn-block" style="margin-bottom: 10px;">
                        <i class="glyphicon glyphicon-print"></i> Imprimir Relatório
                    </button>
                    <button onclick="gerarPDF()" class="btn btn-danger btn-lg btn-block">
                        <i class="glyphicon glyphicon-file"></i> Gerar PDF
                    </button>
                </div>
            </div>
        </div>
        
        <div class="resumo-vendas">
            <h2>Resumo Geral</h2>
            <div class="row">
                <div class="col-md-4">
                    <div class="card-resumo card-resumo-total">
                        <h3>${totalVendas}</h3>
                        <p>Total de Vendas</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card-resumo card-resumo-ativas">
                        <h3>${vendasAtivas}</h3>
                        <p>Vendas Ativas</p>
                    </div>
                </div>
                <div class="col-md-4">
                    <div class="card-resumo card-resumo-canceladas">
                        <h3>${vendasCanceladas}</h3>
                        <p>Vendas Canceladas</p>
                    </div>
                </div>
            </div>
        </div>
        
        <div class="tabela-vendas">
            <h2>Detalhamento de Vendas</h2>
            <div class="table-responsive">
                <table class="table table-bordered table-striped">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Vendedor</th>
                            <th>Cliente</th>
                            <th>Data</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="v" items="${listarTodos}">
                            <tr>
                                <td>${v.idVenda}</td>
                                <td>${v.vendedor.nome}</td>
                                <td>${v.cliente.nome}</td>
                                <td><fmt:formatDate value="${v.dataVenda}" pattern="dd/MM/yyyy"/></td>
                                <td>
                                    <c:if test="${v.status==1}">
                                        <span class="status-ativa">Ativa</span>
                                    </c:if>
                                    <c:if test="${v.status==2}">
                                        <span class="status-cancelada">Cancelada</span>
                                    </c:if>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
        
        
        <div class="text-center" style="margin-top: 30px;">
            <a href="gerenciar_venda.do?acao=listarTodos" class="btn btn-default btn-lg">
                <i class="glyphicon glyphicon-arrow-left"></i> Voltar
            </a>
        </div>
    </div>
</section>

</body>
</html>
