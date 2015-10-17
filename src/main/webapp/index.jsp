<html data-ng-app="storeApp">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" type="text/css" href="resources/static/css/table.css">
<link rel="stylesheet" type="text/css" href="resources/static/css/semantic.min.css">

<script type="text/javascript" src="resources/static/js/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="resources/static/js/angular.js"></script>
<script type="text/javascript" src="resources/static/js/angular-resource.js"></script>
<script type="text/javascript" src="resources/static/js/angular-route.js"></script>
<script type="text/javascript" src="resources/static/js/semantic.min.js"></script>

<script type="text/javascript" src="resources/app/list-directive/list.directive.js"></script>
<script type="text/javascript" src="resources/app/list-directive/list.services.js"></script>
<script type="text/javascript" src="resources/app/list-directive/list.controller.js"></script>

<script type="text/javascript" src="resources/app/storeApp.js"></script>
<script type="text/javascript" src="resources/app/config/store.config.js"></script>
<script type="text/javascript" src="resources/app/clientes/clientes.controller.js"></script>
<script type="text/javascript" src="resources/app/compras/compras.controller.js"></script>
<script type="text/javascript" src="resources/app/productos/productos.controller.js"></script>
<script type="text/javascript" src="resources/app/proveedores/proveedores.controller.js"></script>
<script type="text/javascript" src="resources/app/ventas/ventas.controller.js"></script>
<script type="text/javascript" src="resources/app/facturas/facturas.controller.js"></script>


<body>

<h2 class="ui header">
    <div class="content">
        Sistema Compra-Venta
    </div>
</h2>

<div class="ui grid">
    <div class="two wide column">
        <div class="ui vertical menu inverted">
            <div class="item">
                <div class="header">Ventas</div>
                <div class="menu">
                    <a class="item" data-ng-href="#/ventas/alta">Alta</a>
                    <a class="item" data-ng-href="#/ventas/file">Carga Masiva</a>
                    <a class="item" data-ng-href="#/ventas">Todas las ventas</a>
                </div>
            </div>
            <div class="item">
                <div class="header">Clientes</div>
                <div class="menu">
                    <a class="item" data-ng-href="#/clientes/alta">Alta</a>
                    <a class="item" data-ng-href="#/clientes/file">Carga Masiva</a>
                    <a class="item" data-ng-href="#/clientes/baja">Baja</a>
                    <a class="item" data-ng-href="#/clientes">Todos los clientes</a>
                </div>
            </div>
            <div class="item">
                <div class="header">Proveedores</div>
                <div class="menu">
                    <a class="item" data-ng-href="#/proveedores/alta">Alta</a>
                    <a class="item" data-ng-href="#/proveedores/baja">Baja</a>
                    <a class="item" data-ng-href="#/proveedores">Todos los proveedores</a>
                </div>
            </div>
            <div class="item">
                <div class="header">Productos</div>
                <div class="menu">
                    <a class="item" data-ng-href="#/productos/alta">Alta</a>
                    <a class="item" data-ng-href="#/productos/baja">Baja</a>
                    <a class="item" data-ng-href="#/productos">Todos los productos</a>
                </div>
            </div>
            <div class="item">
                <div class="header">Compras</div>
                <div class="menu">
                    <a class="item" data-ng-href="#/compras/alta">Alta</a>
                    <a class="item" data-ng-href="#/compras/file">Carga Masiva</a>
                    <a class="item" data-ng-href="#/compras/solicitudes">Solicitudes</a>
                    <a class="item" data-ng-href="#/compras">Todas los compras</a>
                </div>
            </div>
            <div class="item">
                <div class="header">Facturas</div>
                <div class="menu">
                    <a class="item" data-ng-href="#/facturas/">Gestion de Facturas</a>
                </div>
            </div>
        </div>
    </div>
    <div class="thirteen wide column">
        <div class="ui segment" data-ng-view=" ">
        </div>
    </div>
</div>

<div class="ui bottom attached label">
    Alexis Ojeda - Saul Zalimben - Nabil Chamas 2015
</div>
</body>
</html>