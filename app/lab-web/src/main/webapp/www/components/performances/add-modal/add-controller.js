'use strict';

angular.module('aceConcerts.components.performances.add.controller', [
  'aceConcerts.core.concert.service'
])
  .controller('AddPerformanceCtrl',
  function (
    $scope,
    $uibModalInstance,
    concertService
  ) {

    var vm = this;

    vm.performance = {
       venueName: null,
       performer: {
         name: null,
         description: null,
         type: null
       },
       open: null,
       close: null
    };

    vm.selectedVenue = {};
    vm.selectedType = {};
    vm.showAlert = false;

    var dateOptions = {
      dateDisabled: false,
      formatYear: 'yy',
      maxDate: new Date(2020, 5, 22),
      minDate: new Date(),
      startingDay: 1
    };


    vm.openDatePop = {
      opened: false,
      open: function() {
        vm.openDatePop.opened = true;
        vm.openDatePop.dateOptions.maxDate = vm.performance.close || new Date(2020, 5, 22);
      },
      dateOptions: R.clone(dateOptions)
    };

    vm.closeDatePop = {
      opened: false,
      open: function() {
        vm.closeDatePop.opened = true;
        vm.closeDatePop.dateOptions.minDate = vm.performance.open || new Date();
      },
      dateOptions: R.clone(dateOptions)
    };


    concertService.getVenues()
      .then(function(venues) {
        vm.venues = venues;
      });

    vm.getPerformanceTypes = function() {
      return concertService.getPerformanceTypes()
        .then(function(types) {
          vm.accomodations = R.filter(selectedVenueHasType, types);
          vm.performance.venueName = vm.selectedVenue.name;
          return;
        });
    };

    vm.setPerformanceType = function() {
      vm.performance.performer.type = vm.selectedType.value;
    };

    vm.request = function () {
      var p = vm.performance;
      if (p.venueName && p.performer.name && p.performer.type && p.open && p.close) {
        $uibModalInstance.close(vm.performance);
      } else {
        vm.showAlert = true;
      }
    };

    vm.cancel = function () {
      $uibModalInstance.dismiss('cancel');
    };

    var selectedVenueHasType = function(type) {
      return R.contains(type.value, vm.selectedVenue.accomodations);
    }
});
