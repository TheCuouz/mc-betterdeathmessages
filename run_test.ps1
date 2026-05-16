$env:JAVA_HOME = "C:\Program Files\Microsoft\jdk-21.0.11.10-hotspot"
$env:PATH = "C:\tools\apache-maven-3.9.6\bin;$($env:JAVA_HOME)\bin;$env:PATH"
Set-Location "C:\Users\craft\Desktop\CRISTIAN\02_PROYECTOS\HUM\minecraft-plugins-suite\repos\mc-betterdeathmessages"
mvn -B test
