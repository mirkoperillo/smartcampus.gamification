var app = angular.module('gamificationEngine', ['ngRoute', 'ui.bootstrap', 'jm.i18next', 'toggle-switch']);

app.config(function ($i18nextProvider) {
  'use strict';
  $i18nextProvider.options = {
    useCookie: false,
    useLocalStorage: false,
    fallbackLng: 'default',
    resGetPath: '../locales/__lng__/__ns__.json'
  };
});

app.config(
  function ($routeProvider) {
    $routeProvider.when('/login', {
      templateUrl: 'templates/login.html',
      controller: 'LoginCtrl'
    })

    .when('/home', {
      templateUrl: 'templates/home.html',
      controller: 'HomeCtrl'
    })

    .when('/game/:id', {
      templateUrl: 'templates/game.html',
      controller: 'GameCtrl'
    })

    .when('/game/:id/points/:idPoints', {
      templateUrl: 'templates/game_points.html',
      controller: 'GamePointsCtrl'
    })

    .when('/game/:id/badges_collections/:idBadgesCollection', {
      templateUrl: 'templates/game_badges_collection.html',
      controller: 'GameBadgesCollectionCtrl'
    })

    .when('/game/:id/leaderboards/:idLeaderboard', {
      templateUrl: 'templates/game_leaderboard.html',
      controller: 'GameLeaderboardCtrl'
    })

    .when('/game/:id/actions', {
      templateUrl: 'templates/actions.html',
      controller: 'ActionsCtrl'
    })

    .otherwise({
      redirectTo: '/login'
    });
  }
);
