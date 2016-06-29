'use strict';

angular.module('aceConcerts.sections.home.controller', [
  'aceConcerts.core.user.service'
])
  .controller(
    'HomeCtrl',
    function HomeCtrl(userService, $state) {

      var vm = this;

      vm.venues = {
        class: '',
        show: function() {
          vm.venues.class = 'active';
          vm.perfs.class = '';
        }
      }

      vm.perfs = {
        class: 'active',
        show: function() {
          vm.perfs.class = 'active';
          vm.venues.class = '';
        }
      }

      console.log('Home Controller Created');
    }
  );
