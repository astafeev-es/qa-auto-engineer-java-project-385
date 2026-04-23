.PHONY: start test build lint check

start:
	docker run --rm -p 5173:5173 hexletprojects/qa_auto_java_testing_kanban_board_project_ru_app

test:
	./gradlew test

build:
	./gradlew clean build

lint:
	./gradlew checkstyleMain checkstyleTest

check:
	./gradlew check
