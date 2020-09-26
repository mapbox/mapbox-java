
.DEFAULT_GOAL := default

default:
	npm install
	validator download-all

clean:
	rm package-lock.json
	rm -rf node_modules
	rm -rf releases

.PHONY: all test clean
test:
	npm test
