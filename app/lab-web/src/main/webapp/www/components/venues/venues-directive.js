'use strict';

angular.module('aceConcerts.components.venues.directive', [
    'aceConcerts.components.venues.controller'
  ])
  .directive(
    'venues',
    function () {
      return {
        restrict: 'E',
        replace: true,
        templateUrl: 'www/components/venues/venues.tpl.html',
        controller: 'VenuesCtrl as venues',
        bindToController: true
      };
    }
  );
