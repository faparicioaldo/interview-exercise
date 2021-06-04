# Ejercicio Bunsan | Analisis Fundamental

##	INDICACIONES
-	El archivo de entrada debera colocarse en la ruta "C:\tmp\tests.txt"
-	El archivo de salida se aparecera en la ruta "C:\tmp\result.txt"
-	Compilar y ejecutar con Java 8
-	JUnit para pruebas unitarias
-	Desarrollo en windows

Analisis de ubicacion de segmentos en entradas de archivo:

- Linea 1 segmento A en posiciones 2
- Linea 2 segmentos F, G, B en posiciones 1, 2 y 3 respectivamente
- Linea 3 segmentos E, D, C en posiciones 1, 2 y 3 respectivamente
- Linea 4 sin segmentos

Observaciones:
-	Se colocan metodos como publicos para hacer pruebas unitarias
-	El proceso elimina entradas duplicadas para la salida
 
Algoritmo:

- Leer grupos de 3 lineas separadas por 1 linea vacia (entradas) y almacenar en un Map<Integer, List<Char>>
- Iterar sobre el map cada item sera una entrada
  > Armar binario de cada numero guardar binarios en List<String> concatenar a cada string su binario
  - primera linea para obtener segmento A
  - segnda linea para obtener segmentos B, F y G
  - tercera linea para obtener segmentos C, D y E
  - al final se obtendra 9 numeros binarios
- validar lista de linarios
  - en busca de ilegales
  - en busca de invalidos

clasificar numeros por

- OK
- ERR
- ILL

Segmentos Validos del 1 al 10

- A B C D E F
- B C
- A B D E G
- A B C D G
- B C F G
- A C D F G
- A C D E F G
- A B C
- A B C D E F G
- A B C D F G

Binarios Validos del 1 al 10

- 0000000
- 0000001
- 0000010
- 0000011
- 0000100
- 0000101
- 0000110
- 0000111
- 0001000
- 0001001
- 0001010
