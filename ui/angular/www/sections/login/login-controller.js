'use strict';

angular.module('aceConcerts.sections.login.controller', [
  'aceConcerts.core.user.service'
])
  .controller(
    'LoginCtrl',
    function LoginCtrl(userService, $state) {

      // Dummy default data for login
      var vm = this;
      vm.email = 'shadowMan@redhat.com';
      vm.password = 123456;

      vm.submit = function() {
        if (vm.email && vm.password) {
          userService.login(vm.email, vm.password)
          .then(function() {
            return $state.go('home');
          });
        }
      };

      console.log('Login Controller Created');
    }
  );
