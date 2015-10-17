/**
 * Created by alex on 28/09/15.
 */
angular
    .module("storeApp")
    .controller("ProveedoresController", [
        "$scope",
        "$http",
        ProveedoresController
    ]);

function ProveedoresController($scope, $http) {
    $scope.var = {
        columns: [
            {name: 'Id', property: 'id', visible: false, sortable: true, searchable: true},
            {name: 'descripcion', property: 'descripcion', visible: true, sortable: true, searchable: true}
        ],
        URL: 'http://localhost:8080/tp3/service/proveedores',
        globalSearch: true,
        title: 'Lista de proveedores',
        detailViewTitle: 'Detalles de Proveedores',
        file: 'proveedores.json'

    };


    $scope.proveedor = {
        descripcion: ""
    };

    $http.get('http://localhost:8080/tp3/service/proveedores/all').then(function (response) {
        console.log(response);
        $scope.proveedores = response.data;
    });

    $scope.proveedorId = "";
    $scope.confirmarEliminarProveedor = function () {
        $http.delete('http://localhost:8080/tp3/service/proveedores/delete/' + $scope.proveedorId).then(function (response) {
            window.alert(response.data);
        }, function () {
            window.alert("No se puedo eliminar el proveedor");
        })
    };

    $scope.confirmarNuevoProveedor = function () {
        var data = {
            descripcion: $scope.proveedor.descripcion
        };
        $http.post('http://localhost:8080/tp3/service/proveedores', data).then(successCallback, errorCallback);

        function successCallback(response) {
            $scope.proveedor = {
                descripcion: ""
            };
            console.log(1);
        }

        function errorCallback(response) {
            console.log(2);
        }
    }

}