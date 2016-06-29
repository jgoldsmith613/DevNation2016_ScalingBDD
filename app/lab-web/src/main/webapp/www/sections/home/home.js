'use strict';

angular.module('aceConcerts.sections.home', [
  'aceConcerts.sections.home.controller'
])
.config(function ($stateProvider) {

  // This sets up the URL routing info for the Home Page

      $stateProvider.state(
        'home',
        {
          url: '/home',
          templateUrl: 'www/sections/home/view/home.tpl.html',
          controller: 'HomeCtrl as home'
        }
      );

      console.log('Home Config Created');
  });
