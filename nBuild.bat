echo "native build"
g++ -I"%JAVA_HOME%/include" -I"%JAVA_HOME%/include/win32" -fPIC src\main\java\natif\natif_natifUpd.cpp -shared -o natif_natifUpd.so
