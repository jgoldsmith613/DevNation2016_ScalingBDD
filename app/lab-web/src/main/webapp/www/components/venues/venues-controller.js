'use strict';

angular.module('aceConcerts.components.venues.controller', [
  'aceConcerts.core.concert.service'
])
  .controller('VenuesCtrl', function (
    $log,
    $uibModal,
    concertService,
    $interval,
    usSpinnerService
  ) {

    var vm = this;

    vm.venues = [];

    activate();

    $interval(activate, 3000);

    function activate() {
      return concertService.getVenues()
        .then(function(venues) {
          vm.venues = venues;
          return;
        });
    }

    vm.openAddVenueModal = function() {
      var modalInstance = $uibModal.open(
        {
          animation: true,
          templateUrl: 'www/components/venues/add-modal/add.html',
          controller: 'AddVenueCtrl as add',
          size: 'lg'
        }
      );

      modalInstance.result
        .then(addVenue, function () {
          $log.info('Modal dismissed at: ' + new Date());
        });

    };

    function addVenue(newVenue) {
      usSpinnerService.spin('spinner-venues');
      return concertService.addVenue(newVenue)
        .then(activate)
        .then(function() {
          return usSpinnerService.stop('spinner-venues');
        });
    }

    console.log('Venues Controller Created');

  });
