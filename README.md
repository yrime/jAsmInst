# Fuzzer jAsmInst for winafl

## О Фаззере.
Инструментатор jAsmInst предназначен для проведения фаззинг тестирования на Windows в режиме dump-mode. Модификация исходного jar файла производится путем добавления вызова статического метода 
>__afl_maybe_log(int id)

в статические поля, методы, инструкции переходов и т.д инструментируемого класса.
Для запупуска фаззинг-тестирования требуется утилита DumpWinAFL(https://github.com/yrime/DumbWinAFL), которая эмулирует работу форк-сервера Afl.

## Build
### native build
> g++ -I"%JAVA_HOME%/include" -I"%JAVA_HOME%/include/win32" -fPIC src\main\java\natif\natif_natifUpd.cpp -shared -o natif_natifUpd.so
### java build
'''
 mkdir ModifyClasses\natif
 mvn package && mvn compile assembly:single && COPY /B /Y target\classes\natif\upd.class ModifyClasses\natif && COPY /B /Y target\classes\natif\natifUpd.class ModifyClasses\natif
'''
