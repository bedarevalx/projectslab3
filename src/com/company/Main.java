package com.company;
import java.io.*;
import java.util.*;
import javax.swing.WindowConstants;

class Main {
    private static Set<String> Ways = new TreeSet<>();
    static File inFile = new File("E:\\Projects Idea\\projectslab3\\src\\com\\company\\fileIn.txt");
    static File outFile = new File("E:\\Projects Idea\\projectslab3\\src\\com\\company\\fileOut.txt");
    static File dataDiagramFile = new File("E:\\Projects Idea\\projectslab3\\src\\com\\company\\dataDiagramFile.txt");

    //вывод таблицы
    static void printlist(ArrayList event1, ArrayList event2, ArrayList event3) {
        for (int i = 0; i < event1.size(); i++) {
            System.out.println(i + 1 + ") " + event1.get(i) + " " + event2.get(i) + " " + event3.get(i));
        }
    }

    static void printDiagrammGhant() throws IOException {
        GanttChart example = new GanttChart();
        example.setSize(800, 400);
        example.setLocationRelativeTo(null);
        example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        example.setVisible(true);
    }

    static void redactWorks(ArrayList<Integer> eventA, ArrayList<Integer> eventB, ArrayList<Integer> workTime) throws IOException {

        int nextWork = 1;
        while (nextWork==1) {
            printlist(eventA, eventB, workTime);
            System.out.print("\n1 - редактировать/удалить работу, 2 - добавить работу: ");
            Scanner in = new Scanner(System.in);
            int numberOfWork;
            nextWork = in.nextInt();
            switch (nextWork) {
                case 1:
                    System.out.print("\n1 - редактировать, 2 - удалить: ");
                    int redactOrDelete = in.nextInt();
                    System.out.print("\nВведите номер работы: ");
                    numberOfWork = in.nextInt();
                    //если редактируем
                    if (redactOrDelete == 1) {
                        System.out.print("\nВведите новое время работы: ");
                        ArrayList<Integer> bufList = new ArrayList<>();
                        for (int i = 0; i < workTime.size(); i++) {
                            if (i == numberOfWork - 1) {
                                bufList.add(in.nextInt());
                                continue;
                            }
                            bufList.add(workTime.get(i));
                        }
                        workTime.removeAll(workTime);
                        workTime.addAll(bufList);
                        System.out.print("\nПовторить редактирование?(1-д/2-Н)");
                        nextWork = in.nextInt();
                        break;
                    }
                    //если удаляем
                    else {
                        ArrayList<Integer> bufEventA = new ArrayList<>();
                        ArrayList<Integer> bufEventB = new ArrayList<>();
                        ArrayList<Integer> bufWorkTime = new ArrayList<>();
                        for (int i = 0; i < eventA.size(); i++) {
                            if (i == numberOfWork - 1) {
                                continue;
                            }
                            bufEventA.add(eventA.get(i));
                            bufEventB.add(eventB.get(i));
                            bufWorkTime.add(workTime.get(i));
                        }

                        eventA.removeAll(eventA);
                        eventB.removeAll(eventB);
                        workTime.removeAll(workTime);

                        eventA.addAll(bufEventA);
                        eventB.addAll(bufEventB);
                        workTime.addAll(bufWorkTime);
                        System.out.print("Повторить редактирование?(1-д/2-Н)");
                        nextWork = in.nextInt();
                        break;
                    }
                case 2:
                    int eventI, eventJ, time;
                    do {
                        System.out.print("\nВведите номер события I: ");
                        eventI = in.nextInt();
                    } while (!eventA.contains(eventI) && !eventB.contains(eventI));
                    do {
                        System.out.print("\nВведите номер события J: ");
                        eventJ = in.nextInt();
                    } while (!eventA.contains(eventI) && !eventB.contains(eventI));
                    System.out.print("\nВведите время работы: ");
                    time = in.nextInt();
                    eventA.add(eventI);
                    eventB.add(eventJ);
                    workTime.add(time);
                    System.out.print("\nПовторить редактирование?(1-д/2-Н)");
                    nextWork = in.nextInt();
            }
        }
        outFile.createNewFile();
        FileWriter fw = new FileWriter(outFile);
        for (int i=0;i<eventA.size();i++) {
            fw.write(eventA.get(i) + " " + eventB.get(i) + " " + workTime.get(i) + "\n");
        }
        fw.close();
    }

    //удаление начального/конечного события
    static void removeEvents(ArrayList<Integer> point, ArrayList<Integer> firstEvent, ArrayList<Integer> secondEvent,
                             ArrayList<Integer> worktime) {
        int startCount = point.size();
        Scanner in = new Scanner(System.in);
        for (int i = 0; i < point.size(); i++) {
            System.out.println("Удалить событие(y/N): " + point.get(i));
            String key = in.nextLine();
            boolean letRemove = true;
            if (key.equals("y") || key.equals("Y")) {
                for (int j = 0; j < firstEvent.size(); j++) {
                    int countDupl = -1;
                    if (firstEvent.get(j).equals(point.get(i))) {
                        int eventToRemove = secondEvent.get(j);
                        for (Integer event : secondEvent) {
                            if (event.equals(eventToRemove)) countDupl++;
                        }
                        if (countDupl <= 0) letRemove = false;
                    }
                }
                if (letRemove) {

                    secondEvent.remove(firstEvent.indexOf(point.get(i)));
                    worktime.remove(firstEvent.indexOf(point.get(i)));
                    firstEvent.remove(firstEvent.indexOf(point.get(i)));
                    point.remove(i);
                    i--;
                    startCount--;
                } else {
                    System.out.println("Ошибка появления новых начальных/конечных событий! Удаление невозможно\n");
                    i--;
                }
            }
            if (startCount == 1) {
                System.out.println("Осталось только одно начальное событие\n");
                break;
            }
        }
    }

    //удаление повторов
    static void recursWays(String outWay, ArrayList<Integer> eventA,
                           ArrayList<Integer> eventB, int event,
                           int endPoint) throws IOException {
        outWay += event + " ";
        if (event == endPoint) {
            Ways.add(outWay.strip());
        }
        int checkend = 0;
        for (int j = 0; j < eventA.size(); j++) {
            if (eventA.get(j) == event) {
                int nextStep = eventB.get(j);
                checkend = nextStep;
                for (int i = 0; i < eventA.size(); i++) {
                    if (eventA.get(i) == nextStep) {
                        recursWays(outWay, eventA, eventB, nextStep, endPoint);
                    }
                }
            }
        }
        if (checkend == endPoint) {
            outWay += endPoint;
            Ways.add(outWay);
            return;
        }
    }

    static int timeOfWay(String way, ArrayList<Integer> eventA, ArrayList<Integer> eventB, ArrayList<Integer> workTime) {
        int time = 0;
        int[] eventsInWay = Arrays.stream(way.split(" ")).mapToInt(Integer::parseInt).toArray();
        if (eventsInWay.length == 1) {
            return 0;
        }
        for (int i = 0; i < eventsInWay.length - 1; i++) {
            for (int j = 0; j < eventA.size(); j++) {
                if (eventsInWay[i] == eventA.get(j) && eventsInWay[i + 1] == eventB.get(j)) {
                    time += workTime.get(j);
                }
            }
        }
        return time;
    }

    static ArrayList<Integer> findEarlyOrLaterTime(boolean findEarly, ArrayList<Integer> eventA, ArrayList<Integer>
            eventB, ArrayList<Integer> Events,
                                                   ArrayList<Integer> LaterTime,
                                                   ArrayList<Integer> EarlyTime, ArrayList<Integer> workTime) throws IOException {
        for (Integer events : Events) {
            if (!findEarly) {
                if (events == eventA.get(0)) {
                    LaterTime.add(0);
                    continue;
                }
                if (events == eventB.get(eventB.size() - 1)) {

                    LaterTime.add(EarlyTime.get(Events.indexOf(eventB.get(eventB.size() - 1))));
                    continue;
                }
            }
            String outWay = "";
            if (findEarly) recursWays(outWay, eventA, eventB, eventA.get(0), events);
            else
                recursWays(outWay, eventA, eventB, events, eventB.get(eventB.size() - 1));
            int maxTime = 0;
            for (String way : Ways) {
                if (timeOfWay(way, eventA, eventB, workTime) >= maxTime) {
                    maxTime = timeOfWay(way, eventA, eventB, workTime);
                }
            }
            Ways.removeAll(Ways);
            if (findEarly) {
                EarlyTime.add(maxTime);
            } else {

                LaterTime.add(EarlyTime.get(Events.indexOf(eventB.get(eventB.size() - 1))) - maxTime);
            }
        }
        if (findEarly) return EarlyTime;
        else
            return LaterTime;
    }

    static void deleteDuplicates(ArrayList list) {
        for (int i = 0; i < list.size(); i++) {
            int repeatCount = -1;
            for (int j = 0; j < list.size(); j++) {
                if (list.get(i).equals(list.get(j))) {
                    repeatCount++;
                    if (repeatCount > 0) {
                        list.remove(j);
                        repeatCount = 0;
                    }
                }
            }
        }
    }
    static int[][] makeSortedGraph(ArrayList<Integer> eventA_in,  ArrayList<Integer> eventB_in,  ArrayList<Integer> workTime_in) throws IOException {
        ArrayList<Integer> endPoint = new ArrayList<>();
        ArrayList<Integer> startPoint = new ArrayList<>();
        ArrayList<Integer> eventA = new ArrayList<>();
        ArrayList<Integer> eventB = new ArrayList<>();
        ArrayList<Integer> workTime = new ArrayList<>();


        for (Integer nextEvent : eventA_in) {
            if (!eventB_in.contains(nextEvent)) startPoint.add(nextEvent);
        }
        deleteDuplicates(startPoint);
        if (startPoint.size() > 1) {
            System.out.println("Обнаружено несколько начальных событий\n");
        }
        System.out.println(startPoint);
        removeEvents(startPoint, eventA_in, eventB_in, workTime_in);
        if (startPoint.size() > 1) {
            System.out.println("Создано фиктивное событие '-100' для событий: " + startPoint);
            for (Integer event : startPoint) {
                eventA.add(-100);
                eventB.add(event);
                workTime.add(0);
            }
        }
        for (Integer startEvent : startPoint) {
            while (eventA_in.contains(startEvent)) {
                int index = eventA_in.indexOf(startEvent);
                eventA.add(startEvent);
                eventB.add(eventB_in.get(index));
                workTime.add(workTime_in.get(index));
                eventB_in.remove(index);
                workTime_in.remove(index);
                eventA_in.remove(index);
            }
        }

        for (int i = 0; i < eventA.size(); i++) {
            while (eventA_in.contains(eventB.get(i))) {
                int indexAdd = eventA_in.indexOf(eventB.get(i));
                eventA.add(eventA_in.get(indexAdd));
                eventB.add(eventB_in.get(indexAdd));
                workTime.add(workTime_in.get(indexAdd));
                eventA_in.remove(indexAdd);
                eventB_in.remove(indexAdd);
                workTime_in.remove(indexAdd);
            }
        }

        for (int i = 0; i < eventA.size(); i++) {
            if (eventA.get(i).equals(eventB.get(i))) {
                eventA.remove(i);
                eventB.remove(i);
                workTime.remove(i);
                i--;
            }
        }
        for (int i = 0; i < eventA.size(); i++) {
            int countDuplicates = -1;
            for (int j = 0; j < eventA.size(); j++) {

                boolean equalInA = eventA.get(i).equals(eventA.get(j));
                boolean equalInB = eventB.get(i).equals(eventB.get(j));
                if (equalInA && equalInB) {
                    countDuplicates++;
                }
            }
            if (countDuplicates > 0) {
                eventA.remove(i);
                eventB.remove(i);
                workTime.remove(i);
                i--;
            }
        }
        for (Integer nextEvent : eventB) {
            if (!eventA.contains(nextEvent)) endPoint.add(nextEvent);
        }
        System.out.println(endPoint);
        deleteDuplicates(endPoint);
        if (endPoint.size() > 1)
            System.out.println("\nОбнаружено несколько конечных точек: " + endPoint.size());
        removeEvents(endPoint, eventB, eventA, workTime);
        if (endPoint.size() > 1) {
            System.out.println("Создано фиктивное событие '-200' для событий: " + endPoint);
            for (Integer event : endPoint) {
                eventB.add(-200);
                eventA.add(event);
                workTime.add(0);
            }
        }
        outFile.createNewFile();
        FileWriter fw = new FileWriter(outFile);
        for (int i = 0; i < eventA.size(); i++) {
            fw.write(eventA.get(i) + " " + eventB.get(i) + " " + workTime.get(i) + "\n");
        }
        fw.close();
        int[][] outFullGraph = new int[3][eventA.size()];
        outFullGraph[0] = eventA.stream().mapToInt(i -> i).toArray();
        outFullGraph[1] = eventB.stream().mapToInt(i -> i).toArray();
        outFullGraph[2] = workTime.stream().mapToInt(i -> i).toArray();
        return outFullGraph;
    }
    static int[][] readGraphFromFile(File fileName) throws IOException {
        ArrayList<Integer> eventA = new ArrayList();
        ArrayList<Integer> eventB = new ArrayList();
        ArrayList<Integer> workTime = new ArrayList();

        FileReader fr = new FileReader(fileName);
        Scanner sc = new Scanner(fr);
        while (sc.hasNext()) {
            eventA.add(sc.nextInt());
            eventB.add(sc.nextInt());
            workTime.add(sc.nextInt());
        }
        fr.close();
        int[][] outFullGraph = new int[3][eventA.size()];
        outFullGraph[0] = eventA.stream().mapToInt(i -> i).toArray();
        outFullGraph[1] = eventB.stream().mapToInt(i -> i).toArray();
        outFullGraph[2] = workTime.stream().mapToInt(i -> i).toArray();
        return outFullGraph;
    }
    static ArrayList<Integer> getArrayListFromArray(ArrayList<Integer> eventList, int[] array){
        for(int num: array){
            eventList.add(num);
        }
        return eventList;
    }

    static void makeSortedDataFile(ArrayList<Integer> eventA,  ArrayList<Integer> eventB,  ArrayList<Integer> workTime, ArrayList<Integer> allEvents, ArrayList<Integer> earlyTime) throws IOException {
        dataDiagramFile.createNewFile();
        FileWriter fr = new FileWriter(dataDiagramFile);
        for(int i=0;i<eventA.size();i++){
            fr.write(eventA.get(i)+ " " + eventB.get(i) + " " + workTime.get(i) + " " + earlyTime.get(allEvents.indexOf(eventA.get(i)))+ " ");
        }
        fr.close();
    }

    public static void main(String[] args) throws IOException {
        ArrayList<Integer> eventA_in = new ArrayList();
        ArrayList<Integer> eventB_in = new ArrayList();
        ArrayList<Integer> workTime_in = new ArrayList();
        ArrayList<Integer> eventA = new ArrayList<>();
        ArrayList<Integer> eventB = new ArrayList<>();
        ArrayList<Integer> workTime = new ArrayList<>();
        ArrayList<Integer> fullReserv = new ArrayList<>();
        ArrayList<Integer> independentReserv = new ArrayList<>();
        ArrayList<Integer> EarlyTime = new ArrayList<>();
        ArrayList<Integer> LaterTime = new ArrayList<>();
        ArrayList<Integer> ReservTime = new ArrayList<>();


        int[][] bufArray = readGraphFromFile(inFile);
        getArrayListFromArray(eventA_in, bufArray[0]);
        getArrayListFromArray(eventB_in, bufArray[1]);
        getArrayListFromArray(workTime_in, bufArray[2]);

        System.out.println("Исходный список работ:");
        printlist(eventA_in, eventB_in, workTime_in);
        bufArray = makeSortedGraph(eventA_in,eventB_in,workTime_in);
        getArrayListFromArray(eventA, bufArray[0]);
        getArrayListFromArray(eventB, bufArray[1]);
        getArrayListFromArray(workTime, bufArray[2]);
        String outWay = "";

        //2 лаба
        Ways.removeAll(Ways);
        //Создаем список всех событий
        Set<Integer> eventsWOduplicates = new TreeSet<Integer>();
        eventsWOduplicates.addAll(eventA);
        eventsWOduplicates.addAll(eventB);
        ArrayList<Integer> Events = new ArrayList<>(eventsWOduplicates);
        EarlyTime = findEarlyOrLaterTime(true, eventA, eventB, Events, LaterTime, EarlyTime, workTime);
        LaterTime = findEarlyOrLaterTime(false, eventA, eventB, Events, LaterTime, EarlyTime, workTime)
        ;
        for (Integer earlyTime : EarlyTime) {
            ReservTime.add(LaterTime.get(EarlyTime.indexOf(earlyTime)) - earlyTime);
        }
        int counter = 0;
        for (Integer pastEvent : eventB) {
            int neighborEvent = eventA.get(counter);
            int timeOfWork = workTime.get(counter);
            int earlyTimeInPastEvent = EarlyTime.get(Events.indexOf(pastEvent));
            int laterTimeInPastEvent =
                    LaterTime.get(Events.indexOf(pastEvent));
            int laterTimeInPreviousEvent = LaterTime.get(Events.indexOf(neighborEvent));

            int earlyTimeInPreviousEvent = EarlyTime.get(Events.indexOf(neighborEvent));
            fullReserv.add(laterTimeInPastEvent - earlyTimeInPreviousEvent -
                    timeOfWork);
            independentReserv.add(earlyTimeInPastEvent - laterTimeInPreviousEvent - timeOfWork);
            counter++;
        }
        makeSortedDataFile(eventA, eventB, workTime, Events, EarlyTime);
        printDiagrammGhant();
        Scanner in = new Scanner(System.in);
        System.out.print("\nОтредактировать график и вывести новую диаграмму?(y/N): ");
        String key = in.nextLine();
        while(Objects.equals(key, "y")) {
            redactWorks(eventA,eventB,workTime);
            //упорядочиваем работы
            bufArray = readGraphFromFile(outFile);
            getArrayListFromArray(eventA_in, bufArray[0]);
            getArrayListFromArray(eventB_in, bufArray[1]);
            getArrayListFromArray(workTime_in, bufArray[2]);
            System.out.println("Отредактированный список работ:");
            printlist(eventA_in, eventB_in, workTime_in);
            bufArray = makeSortedGraph(eventA_in,eventB_in,workTime_in);
            eventA.removeAll(eventA);
            eventB.removeAll(eventB);
            workTime.removeAll(workTime);
            getArrayListFromArray(eventA, bufArray[0]);
            getArrayListFromArray(eventB, bufArray[1]);
            getArrayListFromArray(workTime, bufArray[2]);


            /*
            сделать сортировку после редактирования
            нахождение earlyTime
             */
            eventsWOduplicates = new TreeSet<Integer>();
            eventsWOduplicates.addAll(eventA);
            eventsWOduplicates.addAll(eventB);
            Events = new ArrayList<>(eventsWOduplicates);

            EarlyTime = new ArrayList<>();
            EarlyTime = findEarlyOrLaterTime(true, eventA, eventB, Events, LaterTime, EarlyTime, workTime);

            makeSortedDataFile(eventA, eventB, workTime, Events, EarlyTime);
            printDiagrammGhant();


            System.out.print("Повторить редактирование?(y/N): ");
            key = in.nextLine();
        }
    }
}