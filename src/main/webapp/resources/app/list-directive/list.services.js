/**
 * Created by ALEX on 8/14/2015.
 */
angular
    .module('GenericList')
    .factory('ListServices', [
        '$resource',
        ListServices
    ]);

function ListServices($resource) {
    return {
        salesServices: function (URL) {
            return $resource(URL + "/:operacion", {}, {
                'get': {
                    method: 'GET'
                },
                'exportar': {
                    method: 'GET',
                    params: {operacion: 'exportar'},
                    isArray: true
                }
            })
        }
    };
}




