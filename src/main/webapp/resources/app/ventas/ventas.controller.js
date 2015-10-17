/**
 * Created by alex on 28/09/15.
 */
angular
    .module("storeApp")
    .controller("VentasController", [
        "$scope",
        "$http",
        VentasController
    ]);

function VentasController($scope, $http) {
    $scope.var = {
        columns: [
            {name: 'Id', property: "id", visible: false, sortable: true, searchable: true},
            {name: 'Cliente Id', property: "cliente.nombre", visible: true, sortable: true, searchable: true},
            {name: 'Fecha', property: 'fecha', visible: true, sortable: true, searchable: true},
            {name: 'Factura Id', property: 'factura.id', visible: true, sortable: true, searchable: false},
            {name: 'Monto', property: 'monto', visible: true, sortable: true, searchable: true}
        ],
        URL: 'http://localhost:8080/tp3/service/ventas',
        globalSearch: true,
        title: 'Lista de ventas',
        detailViewTitle: 'Detalles de Ventas',
        file: 'ventas.json'

    };

    $scope.clienteSeleccionado = 0;
    $scope.productoSeleccionado = {};
    $scope.productosSeleccionados = [];

    $scope.agregarAlCarro = function () {
        $scope.productosSeleccionados.push(
            {
                id: $scope.productoSeleccionado.id,
                descripcion: $scope.productoSeleccionado.descripcion,
                cantidad: $scope.productoSeleccionado.cantidad
            }
        );
        console.log($scope.productosSeleccionados);
    };
    $scope.removerProducto = function (data) {
        var idx = $scope.productosSeleccionados.indexOf(data);
        if (idx !== -1) {
            $scope.productosSeleccionados.splice(idx, 1);
        }
        console.log($scope.productosSeleccionados);
    };

    $http.get('http://localhost:8080/tp3/service/clientes/all').then(
        function (clientes) {
            $scope.clientes = clientes.data;
        }
    );

    $http.get('http://localhost:8080/tp3/service/productos/all').then(
        function (productos) {
            $scope.productos = productos.data;
        }
    );


    $scope.confirmarVenta = function () {
        var detalles = [];
        for (indice in $scope.productosSeleccionados) {
            detalles.push({
                productoId: parseInt($scope.productosSeleccionados[indice].id),
                cantidad: parseInt($scope.productosSeleccionados[indice].cantidad)
            })
        }

        var data = {
            clienteId: $scope.clienteSeleccionado,
            fecha: "25/25/25",
            detalles: detalles
        };
        $http.post('http://localhost:8080/tp3/service/ventas', data).then(successCallback, errorCallback);

        function successCallback(response) {
            $scope.clienteSeleccionado = 0;
            $scope.productoSeleccionado = {};
            $scope.productosSeleccionados = [];
            console.log(1);
        }

        function errorCallback(response) {
            console.log(2);
        }
    }

}