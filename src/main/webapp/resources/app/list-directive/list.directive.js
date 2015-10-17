
angular
    .module('GenericList', [])
    .directive('listDirective', [
        ListDirective
    ]);

function ListDirective() {
    return {
        templateUrl: 'resources/app/list-directive/list-directive.html',
        restrict: 'E',
        controller: 'ListController',
        scope: {
            config: '='
        }
    };
}