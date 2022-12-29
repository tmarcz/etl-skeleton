# docker infrastructure setup
docker-compose -f docker-compose-it.yml up -d --wait

$dockerHost = "127.0.0.1"
$dockerPort = 55100

do {
    Write-Host "waiting for docker port to be opened..."
    Start-Sleep -Seconds 1
} until(Test-NetConnection $dockerHost -Port $dockerPort | ? { $_.TcpTestSucceeded } )

# database initializiation
$container = "db-scheduler-test"

docker exec -it $container psql -U postgres -c "SELECT VERSION();"

docker exec -it $container psql -U postgres -c "DROP DATABASE example;"
docker exec -it $container psql -U postgres -c "CREATE DATABASE example;"

docker exec -it $container psql -U postgres -d example -c "CREATE SCHEMA demo;"

