angular
    .module("storeApp")
    .controller("FacturasController", [
        "$scope",
        "$http",
        FacturasController
    ]);

function FacturasController($scope, $http) {
    $scope.var = {
        columns: [
            {name: 'Id', property: 'id', visible: true, sortable: false, searchable: false},
            {name: 'fecha', property: 'fecha', visible: true, sortable: true, searchable: true},
            {name: 'monto', property: 'monto', visible: true, sortable: true, searchable: true}
        ],
        URL: 'http://localhost:8080/tp3/service/facturas',
        globalSearch: true,
        title: 'Lista de facturas',
        detailViewTitle: 'Detalles de Facturas'
    };


    $scope.estadoFacturacion = function () {

        $http.get('http://localhost:8080/tp3/service/facturas/isRun').then(
            function (response) {
                $scope.mensaje = response.data;
                $('#estado')
                    .modal('show');
            }
        );
    };

    $scope.lanzarFacturacion = function() {

        $http.get('http://localhost:8080/tp3/service/facturas/facturar');
    }

    $scope.detenerFacturacion = function () {

        $http.get('http://localhost:8080/tp3/service/facturas/detener');

    }

    $scope.iniciarFacturacion = function () {
        $('#inicio')
            .modal('show');
    }


}