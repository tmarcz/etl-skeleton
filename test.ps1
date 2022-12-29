# mkdir /x


Copy-item -Force -Recurse -Verbose .\etl-scheduler\* -Destination .\x\scheduler\
Copy-item -Force -Recurse -Verbose .\etl-commons\java-commons\* -Destination .\x\commons\

