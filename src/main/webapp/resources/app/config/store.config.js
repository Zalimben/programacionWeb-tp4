
angular
    .module("storeApp")
    .config(function storeConfig($routeProvider) {
        $routeProvider
            .when('/ventas', {
                templateUrl: 'resources/app/ventas/ventas.html',
                controller: 'VentasController'
            })
            .when('/ventas/alta', {
                templateUrl: 'resources/app/ventas/ventas_form.html',
                controller: 'VentasController'
            })
            .when('/ventas/file', {
                templateUrl: 'resources/app/ventas/ventas_carga_masiva.html',
                controller: 'VentasController'
            })
            .when('/clientes', {
                templateUrl: 'resources/app/clientes/clientes.html',
                controller: 'ClientesController'
            })
            .when('/clientes/baja', {
                templateUrl: 'resources/app/clientes/clientes_baja.html',
                controller: 'ClientesController'
            })
            .when('/clientes/alta', {
                templateUrl: 'resources/app/clientes/clientes_form.html',
                controller: 'ClientesController'
            })
            .when('/clientes/file', {
                templateUrl: 'resources/app/clientes/clientes_carga_masiva.html',
                controller: 'ClientesController'
            })
            .when('/productos', {
                templateUrl: 'resources/app/productos/productos.html',
                controller: 'ProductosController'
            })
            .when('/productos/baja', {
                templateUrl: 'resources/app/productos/productos_baja.html',
                controller: 'ProductosController'
            })
            .when('/productos/alta', {
                templateUrl: 'resources/app/productos/productos_form.html',
                controller: 'ProductosController'
            })
            .when('/compras', {
                templateUrl: 'resources/app/compras/compras.html',
                controller: 'ComprasController'
            })
            .when('/compras/alta', {
                templateUrl: 'resources/app/compras/compras_form.html',
                controller: 'ComprasController'
            })
            .when('/compras/file', {
                templateUrl: 'resources/app/compras/compras_carga_masiva.html',
                controller: 'ComprasController'
            })
            .when('/compras/solicitudes', {
                templateUrl: 'resources/app/compras/compras_solicitudes.html',
                controller: 'ComprasController'
            })
            .when('/home', {
                templateUrl: 'home.html',
                controller: 'HomeController'
            })
            .when('/facturas', {
                templateUrl: 'resources/app/facturas/facturas.html',
                controller: 'FacturasController'
            })
            .when('/proveedores', {
                templateUrl: 'resources/app/proveedores/proveedores.html',
                controller: 'ProveedoresController'
            })
            .when('/proveedores/baja', {
                templateUrl: 'resources/app/proveedores/proveedores_baja.html',
                controller: 'ProveedoresController'
            })
            .when('/proveedores/alta', {
                templateUrl: 'resources/app/proveedores/proveedores_form.html',
                controller: 'ProveedoresController'
            });
    });

//angular
//    .module('storeApp')
//    .config(function($routeProvider){
//        $routeProvider
//            .when("/home",{
//                controller: "HomeController",
//                controllerAs: "Home",
//                templateUrl: "/templates/home/home.html"
//            })
//            .when("/forecast/list", {
//                controller: "ListController",
//                controllerAs:"List",
//                templateUrl: "/templates/list/list.html"
//            })
//            .when("/forecast/worksheet", {
//                controller: "WorksheetController",
//                controllerAs:"Worksheet",
//                templateUrl: "/templates/worksheet/worksheet.html"
//            })
//            .when("/forecast/worksheet/:id", {
//                controller: "WorksheetEditController",
//                controllerAs:"WorksheetEdit",
//                templateUrl: "/templates/worksheet/worksheet_forecast.html"
//            })
//            .otherwise({
//                redirectTo: "/"
//            });
//    });