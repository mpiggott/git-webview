(function () {
	'use strict';
	
	var module = angular.module('git', []);

	function SelectedRepositoryService() {
		var selectedRepository;
		return {
			get: function () {
				return selectedRepository;
			},
			set: function (repository) {
				selectedRepository = repository;
			}
		};
	}
	module.service("selected.repository", SelectedRepositoryService);

	function RepositoriesController($http, RepositoriesService) {
		var vm = this;

		vm.setRepository = function(repository) {
			vm.selectedRepository = repository;
			RepositoriesService.set(repository);
		};

		$http.get("/rest/repositories").then(function(response) {
			vm.repositories = response.data;
		});
	}
	RepositoriesController.$inject = ["$http", "selected.repository"];

	module.controller("repositories.controller", RepositoriesController);

	function FileTreeController($scope, $http, SelectedRepository) {
		var vm = this;

		vm.file = "asdf";

		$scope.$watch(function () {
			return SelectedRepository.get();
		}, function (repository) {
			if (repository) {
				vm.files = null;
				$http.get("/rest/repositories/" + encodeURIComponent(repository) + "/master/" + encodeURIComponent(vm.file)).then(function(response) {
					vm.files = response.data;
				});
			}
		});
	}
	FileTreeController.$inject = ["$scope", "$http", "selected.repository"];
	module.controller("file.tree.controller", FileTreeController);
}());