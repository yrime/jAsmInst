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



