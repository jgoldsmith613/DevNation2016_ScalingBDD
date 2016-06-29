'use strict';

angular.module('aceConcerts.components.venues.add.controller', [
  'aceConcerts.core.concert.service'
])
  .controller('AddVenueCtrl',
  function (
    $scope,
    $uibModalInstance,
    concertService
  ) {

    var vm = this;

    vm.showAlert = false;

    vm.venue = {
     name: null,
     city: null,
     capacity: null,
     accomodations: []
    };

    vm.selectedAccomodations = [];


    concertService.getPerformanceTypes()
      .then(function(types) {
        vm.venueTypes = types;
      });

    vm.create = function () {
      var v = vm.venue;
      if (v.name && v.city && v.capacity && v.accomodations.length > 0) {
        $uibModalInstance.close(vm.venue);
      } else {
        vm.showAlert = true;
      }
    };

    vm.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    vm.setAccomodations = function() {
      vm.venue.accomodations = R.map(function(type){
        return type.value;
      }, vm.selectedAccomodations);
    };


});
