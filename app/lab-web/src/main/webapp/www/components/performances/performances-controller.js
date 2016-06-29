'use strict';

angular.module('aceConcerts.components.performances.controller', [
  'aceConcerts.core.concert.service'
])
  .controller('PerformancesCtrl', function (
    $log,
    $uibModal,
    concertService,
    $interval,
    usSpinnerService
  ) {
    var vm = this;

    vm.bookings = [];

    activate();

    $interval(activate, 3000);

    function activate() {
      return concertService.getBookings()
        .then(function(bookings) {
          vm.bookings = bookings;
        });
    }

    vm.openAddBookingModal = function() {
      var modalInstance = $uibModal.open(
        {
          animation: true,
          templateUrl: 'www/components/performances/add-modal/add.html',
          controller: 'AddPerformanceCtrl as add',
          size: 'lg'
        }
      );

      modalInstance.result
        .then(addBooking, function () {
          $log.info('Modal dismissed at: ' + new Date());
        });

    };

    function addBooking(newPerformance) {
      usSpinnerService.spin('spinner-book');
      return concertService.addBooking(newPerformance)
        .then(activate)
        .then(function() {
          return usSpinnerService.stop('spinner-book');
        });
    }

    console.log('Performances Controller Created');

});
