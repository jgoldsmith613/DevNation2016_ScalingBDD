'use strict';

angular.module('aceConcerts.core.concert.service', [])
  .factory(
    'concertService',
    function userService(
      $q,
      $resource
    ) {

      var camel = 'http://localhost:8081';
      var bookingsEndpoint = $resource(camel + '/bookings');
      var venuesEndpoint = $resource(camel + '/venues');
      var typesEndpoint = $resource(camel + '/performancetypes');


      function addBooking(newBooking) {
        return bookingsEndpoint.save(newBooking).$promise
          .then(function(res) {
            return res;
          });
      }

      function deleteBooking(newBooking) {
        return $q.when({status: 'success'});
      }

      function getBookings(){
        return bookingsEndpoint.query().$promise
          .then(function(res) {
            return res;
          });
      }


      function addVenue(newVenue) {
        return venuesEndpoint.save(newVenue).$promise
          .then(function(res) {
            return res;
          });
      }

      function deleteVenue(newBooking) {
        return $q.when({status: 'success'});
      }

      function getVenues() {
        return venuesEndpoint.query().$promise
          .then(function(res) {
            return res;
          });
      }

      function getPerformanceTypes() {
        return typesEndpoint.get().$promise
          .then(function(res) {
            delete res.$promise;
            delete res.$resolved;
            return objectToViewArray(res);
          });
      }

      var objectToViewArray = R.compose(R.map(R.zipObj(['value', 'label'])), R.toPairs);

      return {
        addBooking: addBooking,
        deleteBooking: deleteBooking,
        getBookings: getBookings,
        addVenue: addVenue,
        deleteVenue: deleteVenue,
        getVenues: getVenues,
        getPerformanceTypes: getPerformanceTypes
      };
    }
  );
