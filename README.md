# Fuzzer jAsmInst for winafl

## О Фаззере.
Инструментатор jAsmInst предназначен для проведения фаззинг тестирования на Windows в режиме dump-mode. Модификация исходного jar файла производится путем добавления вызова статического метода:
>__afl_maybe_log(int id)

**Реализован javaagent** для проведения динамической инструментации java-приложения. Кодовая дирректория src\main\agent.

в статические поля, методы, инструкции переходов и т.д инструментируемого класса.

Для запупуска фаззинг-тестирования требуется утилита DumpWinAFL(https://github.com/yrime/StaticWinAFL), которая эмулирует работу форк-сервера Afl.

## Build
Сборочные скрипты
### native build
`nBuild.bat`
### java build

`build.bat`

### javaagent build

`cd src\main\agent`

`mvn compile assembly:single`

## Instrumentation
instrumentaion.bat <путь до дирректории с jar> <jar файл> <шаблон пакета>
- Необходимо указывать абсолютный путь до jar файла
- шаблон пакета - пример ru.java.src.main . Если шаблона нет, то указывать не надо.

> instrumentaion.bat  C:\Users\absolut_way\ name_jar_file.jar ru.package.filter.src

## Fuzzing
### Подготовка winafl
в рабочую дирректорию необходимо скопировать файл natif_natifUpd.so

### work
1. необходимо указывать абсолютный путь к инструментированному jar-файлу;
2. режим фаззера - static instrumentaion - `-Y`
3. комманда запуска java должна всегда быть в "кавычках";

Запуск должен производиться следующим образом:

> afl-fuzz.exe -i IN -o OUT -t 5000 -m none -Y -- -- StaticWinAFL.exe "java -jar c:\Users\yrime\IdeaProjects\winafl\bin64\login-1.0-shaded.jar -i @@"

## use javaagent

1. Добавить папку natif в jar: 'jar uf <jar-file> natif\*' 
2. Запуск: Строка java jv = `"java -javaagent:target\jAsmAgent-1.0-jar-with-dependencies.jar -jar <full path to jar> <arguments with @@>"`
  
  `afl-fuzz.exe -i IN -o OUT -t <time> -m none -Y -- -- StaticWinAFL <jv>`
  
### example
  `afl-fuzz.exe -i IN -o OUT -t <time> -m none -Y -- -- StaticWinAFL.exe "java -javaagent:target\jAsmAgent-1.0-jar-with-dependencies.jar -jar c:\parser.jar --input=@@"`

## Logs
1. testlogfork.txt - log file StaticWinAFL;
2. cmdOutput.txt - output from java programm

## How it works
  
  Фаззер основан на фаззере **WinAfl**. Утилита StaticWinAFL предназначена для эмуляции режима fork (в планах перевод функционала утилиты в нутрь инструментатора).
  
  Аргумент WinaAfl -Y предназначен для перевода работы winafl в режим статической инструментации.
  
  Инструментация производится с использованием пакета java ASM, на каждый опкод ветвления, входа и выхода из функции, попадания в статические поля, calls и тд 
  
  внедряется вызов нативного метода `__afl_maybe_log(int id)`. Метод работает следующим образом:
  1. Генерируется псевдослучайное число от имени метода;
  2. Данное число передается в метод `__afl_maybe_log(int id)`;
  3. Метод вызывает преобразование shared memory фаззера winafl: `natifUpd.mapUp(i)`;
  4. Преобразование:
  ```
             HANDLE mem = OpenFileMapping(FILE_MAP_ALL_ACCESS, false, shm);
             areaPtr = MapViewOfFile(mem, FILE_MAP_ALL_ACCESS, 0, 0, 0);
             if(areaPtr == NULL){
                out << "shm value failed" << std::endl;
                out.close();
             }
             __afl_area_ptr = (char*)areaPtr; 
  ```
  


