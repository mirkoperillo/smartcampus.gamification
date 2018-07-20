angular.module('gamificationEngine.rules', [])
	.controller('RulesCtrl', function ($scope, $rootScope, $timeout, $window, $stateParams, $uibModal, gamesFactory) {
		$rootScope.currentNav = 'rules';
		$rootScope.currentGameId = $stateParams.id;

		$scope.list = true;

		$scope.hideRule = true;
		$scope.title = "labels:title_add_rule";
		$scope.action = "labels:btn_publish";

		$scope.ruleContent = '';
		$scope.input = {};
		$scope.alerts = {
			'nameError': false,
			'contentError': false,
			'ruleError': '',
			'ruleEdited': false,
			'ruleDeleted': false
		};
		var previousName = '';

		var rule;
		var game = $scope.game;

		$scope.save = function () {
			$scope.alerts.nameError = false;
			$scope.alerts.contentError = false;
			$scope.alerts.ruleError = '';
			$scope.alerts.ruleValidation = '';
			var valid = true;

			if (!$scope.input.ruleContent || $scope.input.ruleContent.length == 0) {
				$scope.alerts.contentError = true;
				valid = false;
			}
			if (!$scope.input.name || $scope.input.name.length == 0) {
				$scope.alerts.nameError = true;
				valid = false;
			}

			if (valid) {
				//check if already exist
				var found = false;
				if (game.rules && (!rule || rule.name != $scope.input.name)) {
					for (var i = 0; i < game.rules.length && !found; i++) {
						found = game.rules[i].name == $scope.input.name;
					}
				}


				if (!found) {
					$scope.disabled = true;

					var r = (rule) ? rule : {};
					r.name = $scope.input.name;
					r.content = $scope.input.ruleContent;

					if ($scope.input.name != 'constants') {
						gamesFactory.validateRule($scope.input.ruleContent).then(function (data) {
							if (data.length > 0) {
								$scope.alerts.ruleValidation = data;
								$scope.disabled = false;
							}
							else {
								addRuleAPI(game, r);
							}
						}, function (msg) {
							r.name = previousName;
							$scope.alerts.ruleError = 'messages:' + msg;
							$scope.disabled = false;
						});
					}
					else {
						addRuleAPI(game, r);
					}
				} else {
					$scope.alerts.ruleError = 'messages:msg_error_exist';
				}
			}
		};

		function addRuleAPI(game, r) {
			gamesFactory.addRule(game, r).then(
				function (data) {
					if (!game.rules) {
						game.rules = [];
					}
					if (!rule) {
						game.rules.unshift(data);
					}
					$scope.disabled = false;
					$scope.list = true;
					$scope.alerts.ruleEdited = true;
				},
				function (message) {
					r.name = previousName;
					// Show given error alert
					$scope.alerts.ruleError = 'messages:' + message;
					$scope.disabled = false;
				});
		}

		$scope.cancel = function () {
			$scope.list = true;
			$scope.alerts.nameError = false;
			$scope.alerts.contentError = false;
			$scope.alerts.ruleError = '';
			$scope.alerts.ruleValidation = '';
		}

		$scope.addRule = function () {
			$scope.input = {};
			rule = null;
			$scope.alerts.ruleEdited = false;
			$scope.list = false;
			$scope.title = "labels:title_add_rule";
			$scope.action = "labels:btn_publish";
		}

		$scope.editRule = function (editingRule) {
			rule = editingRule;
			$scope.alerts.ruleEdited = false;
			$scope.list = false;
			$scope.title = "labels:title_edit_rule";
			$scope.action = "labels:btn_save";

			gamesFactory.getRule(game, rule.id).then(
				function (data) {
					if (data) {
						$scope.input.name = data.name;
						$scope.input.ruleContent = data.content;

						previousName = data.name;
						
						$window.scrollTo(0, 0);
					}
				},
				function (message) {
					// Show given error alert
					$scope.input = {};
					$scope.alerts.ruleError = 'messages:' + message;
				});
		}

		$scope.rules = [];
	
		$scope.showRule = function (index) {
			var rule = game.rules[index];
				gamesFactory.getRule(game, rule.id).then(
					function (data) {
						if (data) {
							$scope.rules[index] = data.content;
						}
					},
					function (message) {
						// Show given error alert
						$scope.rules[index] = '';
						$scope.alerts.ruleError = 'messages:' + message;
					});
			}

		$scope.deleteRule = function (rule) {
			// Delete a game
			var modalInstance = $uibModal.open({
				templateUrl: 'modals/modal_delete_confirm.html',
				controller: 'DeleteRuleModalInstanceCtrl',
				backdrop: "static",
				resolve: {
					game: function () {
						return $scope.game;
					},
					rule: function () {
						return rule;
					}
				}
			});
			
			modalInstance.result.then(function () {
				$scope.alerts.ruleDeleted = true;
				
				$timeout(function () {
					$scope.alerts.ruleDeleted = false;
				}, 4000);
			});
		};

		// Load game
		gamesFactory.getGameById($stateParams.id).then(function (game) {
			$scope.game = game;
		}, function () {
			// Show error alert
			$scope.alerts.loadGameError = true;
		});
	});

// Delete rule modal
modals
	.controller('DeleteRuleModalInstanceCtrl', function ($scope, $uibModalInstance, gamesFactory, game, rule) {
	$scope.alerts = {
		'deleteError': '',
	};
	$scope.argument = rule.name;

	$scope.delete = function () {
		gamesFactory.deleteRule(game, rule.id).then(
			function (data) {
				if (data) {
					var idx = 0;
					for (idx = 0; idx < game.rules.length; idx++) {
						if (game.rules[idx].id == rule.id) {
							break;
						}
					}
					game.rules.splice(idx, 1);
					$uibModalInstance.close();
				}
			},
			function (message) {
				$scope.alerts.deleteError = message;
			})
	};

	$scope.cancel = function () {
		$uibModalInstance.dismiss('cancel');
	};
});
