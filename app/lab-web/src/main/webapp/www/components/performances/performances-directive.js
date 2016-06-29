'use strict';

angular.module('aceConcerts.components.performances.directive', [
    'aceConcerts.components.performances.controller'
  ])
  .directive(
    'performances',
    function () {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: 'www/components/performances/performances.tpl.html',
        controller: 'PerformancesCtrl as performances',
        bindToController: true
      };
    }
  );
