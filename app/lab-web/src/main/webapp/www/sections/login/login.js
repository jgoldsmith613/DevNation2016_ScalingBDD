'use strict';

angular.module('aceConcerts.sections.login', [
  'aceConcerts.sections.login.controller'
])
.config(function ($stateProvider) {

    // This sets up the URL routing info for the Login Page

      $stateProvider.state(
        'login',
        {
          url: '/login',
          templateUrl: 'www/sections/login/view/login.tpl.html',
          controller: 'LoginCtrl as login'
        }
      );

      console.log('Login Config Created');
  });
