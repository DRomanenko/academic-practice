# academic-practice
Analysis of photo, audio and video material for subsequent decryption of the encoded message. Maze generation.

## Задачи
### A. Черное и белое (4 балла)
[Условие задачи A](https://github.com/DRomanenko/academic-practice/blob/master/tasks%201-3/src/main/resources/blandwh.png), представлено в формате png. Изображение разбито на 1369 ячеек размера 100х100 пикселей, в каждой ячейке содержится либо сообщение на английском языке, либо кодировка цвета в цветовой модели RGB, описывающее либо белый, либо чёрный цвет. При раскраске изображения в отведённые цвета становится доступным QR код, содержащий кодовую фразу, являющуюся конечным ответом на задачу A.
### B. Видео и буквы (5 баллов)
[Условие задачи B](https://github.com/DRomanenko/academic-practice/blob/master/tasks%201-3/src/main/resources/task.mp4), представлено в формате MP4. На первом кадре содержится условие: "Найдите количество вхождений количество вхождений всех латинских букв в это видео (без учёта этого кадра). Ответ – это строка из всех найденных букв и их количеств. Например, если в видео 40 букв ‘Z’, 3 ‘A’ и 22 ’E’, тогда ответом будет ‘A3E22Z40’" и все возможные варинты букв. На последующих кадрах по одной изображены подсчитываемые буквы.
### C. Пам-пам (6 баллов)
[Условие задачи C](https://github.com/DRomanenko/academic-practice/blob/master/tasks%201-3/src/main/resources/task.wav), представлено в формате MP3 и содержит последовательность звуков "пум" и "пам". Данная последовательность звуков представляет собой сообщение, зашифрованное при помощи азбуки Морзе. Требуется разобрать аудио запись и преобразовать последовательность азубки Морзе в текст.
### D. Запутанный лабиринт
Необходимо сгенерировать лабиринт из ![100\times100](https://latex.codecogs.com/svg.latex?100\times100) комнат, связанных коридорами. Причём, из любой комнаты в любую другую должен существовать единственный путь. Итоговый результат системы оценки равен значению формулы ![1 + ln(1 + \sum_{i}\frac{1}{|20 - a_i| + 1})](https://latex.codecogs.com/svg.latex?1%20+%20ln(1%20+%20\sum_{i}\frac{1}{|20%20-%20a_i|%20+%201})), где ![a_i](https://latex.codecogs.com/svg.latex?a_i) – число подматриц размера ![2\times2](https://latex.codecogs.com/svg.latex?2\times2) вида i матричного представления сгенерированного лабиринта.
