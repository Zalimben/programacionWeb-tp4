
angular
    .module("storeApp")
    .controller("ComprasController", [
        "$scope",
        "$http",
        ComprasController
    ]);

function ComprasController($scope, $http) {
    $scope.var = {
        columns: [
            {name: 'proveedorId', property: 'proveedor.descripcion', visible: true, sortable: true, searchable: true},
            {name: 'fecha', property: 'fecha', visible: true, sortable: true, searchable: true},
            {name: 'monto', property: 'monto', visible: true, sortable: true, searchable: true}
        ],
        URL: 'http://localhost:8080/tp3/service/compras',
        globalSearch: true,
        title: 'Lista de compras',
        detailViewTitle: 'Detalles de Compras',
        file: 'compras.json'

    };

    $scope.var2 = {
        columns: [
            {name: 'producto', property: 'producto.descripcion', visible: true, sortable: true, searchable: true},
            {name: 'fecha', property: 'fecha', visible: true, sortable: true, searchable: true},
            {name: 'Atendido', property: 'atendido', visible: true, sortable: false, searchable: false}
        ],
        URL: 'http://localhost:8080/tp3/service/compras/solicitudes',
        globalSearch: true,
        title: 'Lista de solicitudes de compras',
        detailViewTitle: 'Detalles de Solicitudes de Compra',
        file: 'solicitudes.json'

    };

    $scope.proveedorSeleccionado = 0;
    $scope.productoSeleccionado = {};
    $scope.productosSeleccionados = [];


    $http.get('http://localhost:8080/tp3/service/proveedores/all').then(
        function (proveedores) {
            $scope.proveedores = proveedores.data;
        }
    );

    $scope.productosByProveedor = function () {
        $http.get('http://localhost:8080/tp3/service/productos/proveedor/' + $scope.proveedorSeleccionado).then(
            function (productos) {
                $scope.productos = productos.data;
                console.log($scope.productos);
                if ($scope.productos.length == 0) {
                    window.alert("El proveedor seleccionado no posee productos");
                }
            }
        );
    };

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

    $scope.confirmarCompra = function () {
        var detalles = [];
        for (indice in $scope.productosSeleccionados) {
            detalles.push({
                productoId: $scope.productosSeleccionados[indice].id,
                cantidad: parseInt($scope.productosSeleccionados[indice].cantidad)
            })
        }

        var data = {
            proveedorId: $scope.proveedorSeleccionado,
            fecha: "25/25/25",
            detalles: detalles
        };
        $http.post('http://localhost:8080/tp3/service/compras', data).then(successCallback, errorCallback);

        function successCallback(response) {
            $scope.proveedorSeleccionado = 0;
            $scope.productoSeleccionado = {};
            $scope.productosSeleccionados = [];
            console.log(1);
        }

        function errorCallback(response) {
            console.log(2);
        }
    }

}