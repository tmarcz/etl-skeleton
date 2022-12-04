Write-Host "# Connector Nexus publishing [start] #" -ForegroundColor DarkBlue
Write-Host "# Preparing package..." -ForegroundColor DarkGreen
$cli = 'openapi-generator-cli.jar'

mvn deploy:deploy-file `
    -DgroupId=com.example.test `
    -DartifactId=test-module `
    -Dversion=1.0.0 `
    -DgeneratePom=true `
    -Dpackaging=jar `
    -DrepositoryId=sample-rel `
    -Durl=http://nexus.private.net/Your_Nexus_Repository_Path `
    -Dfile=./PATH_TO_JAR_FILE
