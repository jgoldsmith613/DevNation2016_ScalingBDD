'use strict';

angular.module('aceConcerts.core.user.service', [])
  .factory(
    'userService',
    function userService($q) {

      var login = function(email, password) {
        return $q.when();
      };

      return {
        login: login
      };
    }
  );
