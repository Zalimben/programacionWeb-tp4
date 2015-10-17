angular
    .module("storeApp")
    .controller("ProductosController", [
        "$scope",
        "$http",
        ProductosController
    ]);

function ProductosController($scope, $http) {
    $scope.var = {
        columns: [
            {name: 'Id', property: 'id', visible: false, sortable: true, searchable: true},
            {name: 'descripcion', property: 'descripcion', visible: true, sortable: true, searchable: true},
            {name: 'proveedor', property: 'proveedor.descripcion', visible: true, sortable: true, searchable: true},
            {name: 'stock', property: 'stock', visible: true, sortable: true, searchable: true},
            {name: 'precio', property: 'precio', visible: true, sortable: true, searchable: true}
        ],
        URL: 'http://localhost:8080/tp3/service/productos',
        globalSearch: true,
        title: 'Lista de productos',
        detailViewTitle: 'Detalles de Productos',
        file: 'productos.json'

    };

    $scope.productoId = "";
    $scope.confirmarEliminarProducto = function () {
        $http.delete('http://localhost:8080/tp3/service/productos/delete/' + $scope.productoId).then(function (response) {
            window.alert(response.data);
        }, function () {
            window.alert("No se puedo eliminar el producto");
        })
    };

    $scope.producto = {};
    $http.get('http://localhost:8080/tp3/service/productos/all').then(
        function (response) {
            $scope.productos = response.data;
        }
    );
    $http.get('http://localhost:8080/tp3/service/proveedores/all').then(
        function (proveedores) {
            $scope.proveedores = proveedores.data;
        }
    );
    $scope.confirmarNuevoProducto = function () {
        var data = {
            proveedorId: $scope.producto.proveedorId,
            precio: $scope.producto.precio,
            descripcion: $scope.producto.descripcion
        };
        $http.post('http://localhost:8080/tp3/service/productos', data).then(successCallback, errorCallback);

        function successCallback(response) {
            $scope.producto = {};
            console.log(1);
        }

        function errorCallback(response) {
            console.log(2);
        }
    }
}