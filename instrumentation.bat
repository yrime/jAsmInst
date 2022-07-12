COPY /B /Y %1%2 ModifyClasses

java -jar target\jAsmInst-1.0-jar-with-dependencies.jar %1%2 %3 

cd ModifyClasses

jar uf %2 * 

cd ../
