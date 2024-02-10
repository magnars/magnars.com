test:
	clojure -M:dev -m kaocha.runner

build:
	clojure -X:dev:build

.PHONY: test build
