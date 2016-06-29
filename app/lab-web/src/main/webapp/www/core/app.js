'use strict';
/* global */

// Sets up the name spaces for your app and initializes all of your dependancies.
angular.module('aceConcerts', [
    'ngResource',
    'ui.router',
    'ui.bootstrap',
    'ui.multiselect',
    'angularSpinner',
    'aceConcerts.sections.login',
    'aceConcerts.sections.home',
    'aceConcerts.components.performances',
    'aceConcerts.components.venues',
    'aceConcerts.core.user',
    'aceConcerts.core.concert'
  ])
  .run(
    function ($state) {
      $state.go('login');
      console.log('Starting Up the App');
    });
