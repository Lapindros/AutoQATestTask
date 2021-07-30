# clone project
https://github.com/Lapindros/AutoQATestTask.git

# install the package for use as a dependency in other projects locally
mvn install

# run tests and generate Allure report
mvn clean test site

# open Allure report
mvn io.qameta.allure:allure-maven:serve