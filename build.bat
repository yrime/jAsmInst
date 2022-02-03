echo "java build"
mkdir ModifyClasses\natif

mvn package && mvn compile assembly:single && COPY /B /Y target\classes\natif\upd.class ModifyClasses\natif && COPY /B /Y target\classes\natif\natifUpd.class ModifyClasses\natif
