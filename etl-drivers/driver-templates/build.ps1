## downloading openapi cli
Write-Host "# Driver builder [start] #" -ForegroundColor DarkBlue
Write-Host "# Preparing openapi cli..." -ForegroundColor DarkGreen
$cli = 'openapi-generator-cli.jar'
$is_cli_downloaded = Test-Path -path ./cli/openapi-generator-cli.jar
if(!$is_cli_downloaded){
    mkdir cli
    Invoke-WebRequest -OutFile ./cli/openapi-generator-cli.jar https://repo1.maven.org/maven2/org/openapitools/openapi-generator-cli/6.2.1/openapi-generator-cli-6.2.1.jar
}

## execute cli to build code
Write-Host "# Building template driver based on common api..." -ForegroundColor DarkGreen
Remove-Item driver-example -Force  -Recurse -ErrorAction SilentlyContinue
java -jar ./cli/openapi-generator-cli.jar generate `
    -g java-micronaut-server `
    -i api.yml `
    -o ./driver-example `
    -p controllerPackage=com.example.controller `
    -p modelPackage=com.example.model `
    -p build=maven `
    -p test=junit `
    --additional-properties=hideGenerationTimestamp=true
Write-Host "# Driver builder [end] #" -ForegroundColor DarkBlue
