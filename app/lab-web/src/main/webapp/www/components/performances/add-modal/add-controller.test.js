'use strict';

describe('userService', function () {

  beforeEach(module('aceConcerts.core.user.service'));

  beforeEach(module(function ($provide) {
    $provide.factory('', function () {
      return ;
    });
  });

  beforeEach(function () {
    inject(function ($injector, _$rootScope_) {
      Svc = $injector.get('svc');
      $rootScope = _$rootScope_;
    });
  });

  it('should exist', function () {
    expect(Svc).to.be.an('Object');
    expect().to.be.eql();
  });
});
