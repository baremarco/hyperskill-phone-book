package phonebook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class Main {
    public static void main(String[] args) {
        File directory = new File("/home/marco/IdeaProjects/tests/directory.txt");
        File find = new File("/home/marco/IdeaProjects/tests/find.txt");
        try (FileReader fileReaderDirectory = new FileReader(directory);
             BufferedReader bufferedReaderDirectory = new BufferedReader(fileReaderDirectory);
             FileReader fileReaderFind = new FileReader(find);
             BufferedReader bufferedReaderFind = new BufferedReader(fileReaderFind)) {

            String line;
            int counterFound = 0;
            int counterEntries = 0;

            List<Contact> directoryList = new ArrayList<>();
            while ((line = bufferedReaderDirectory.readLine()) != null) {
                String[] arrayLine = line.split("\\s+", 2);
                directoryList.add(new Contact(arrayLine[1], arrayLine[0]));
            }

            List<String> findList = new ArrayList<>();
            while ((line = bufferedReaderFind.readLine()) != null) {
                findList.add(line);
            }
            //Linear Search
            System.out.println("Start searching (linear search)...");

            Result result = findByLinearSearch(directoryList, findList);
            String outputLinear = String.format("Found %d/%d entries. Time taken: %3$tM min. %3$tS "
                            + "sec. %3$tL ms.%n", result.getFoundQuantity(), result.getEntryQuantity(),
                    result.getSearchingTime() + result.getSortingTime());
            System.out.println(outputLinear);

            //bubble sort + jump search
            System.out.println("Start searching (bubble sort + jump search)...");
            Result result2 = findByJumpSearch(directoryList, findList, result.getSearchingTime());
            if (result2.getExcededTime()) {
                System.out.printf("Found %d/%d entries. Time taken: %3$tM min. %3$tS "
                                + "sec. %3$tL ms.%n", result2.getFoundQuantity(), result2.getEntryQuantity(),
                        result2.getSearchingTime() + result2.getSortingTime());
                System.out.printf("Sorting time: %1$tM min. %1$tS sec. %1$tL ms. - " +
                        "STOPPED, moved to linear search%n", result2.getSortingTime());
                System.out.printf("Searching time: %1$tM min. %1$tS sec. %1$tL ms.",
                        result2.getSearchingTime());
            } else {
                System.out.printf("Found %d/%d entries. Time taken: %3$tM min. %3$tS "
                                + "sec. %3$tL ms.%n", result2.getFoundQuantity(),
                        result2.getEntryQuantity(),
                        result2.getSearchingTime() + result2.getSortingTime());
                System.out.printf("Sorting time: %1$tM min. %1$tS sec. %1$tL ms.%n",
                        result2.getSortingTime());
                System.out.printf("Searching time: %1$tM min. %1$tS sec. %1$tL ms.",
                        result2.getSearchingTime());

            }
            System.out.println("\n");

            //quick sort + binary search
            System.out.println("Start searching (quick sort + binary search)...");
            Result result3 = findByBinarySearch(directoryList, findList);

            System.out.printf("Found %d/%d entries. Time taken: %3$tM min. %3$tS "
                            + "sec. %3$tL ms.%n", result3.getFoundQuantity(),
                    result3.getEntryQuantity(),
                    result3.getSearchingTime() + result3.getSortingTime());
            System.out.printf("Sorting time: %1$tM min. %1$tS sec. %1$tL ms.%n",
                    result3.getSortingTime());
            System.out.printf("Searching time: %1$tM min. %1$tS sec. %1$tL ms.",
                    result3.getSearchingTime());
            System.out.println("\n");

            // hash table
            System.out.println("Start searching (hash table)...");
            Result result4 = findByHashTable(directoryList, findList);

            System.out.printf("Found %d/%d entries. Time taken: %3$tM min. %3$tS "
                            + "sec. %3$tL ms.%n", result4.getFoundQuantity(),
                    result4.getEntryQuantity(),
                    result4.getSearchingTime() + result4.getSortingTime());
            System.out.printf("Creating time: %1$tM min. %1$tS sec. %1$tL ms.%n",
                    result4.getSortingTime());
            System.out.printf("Searching time: %1$tM min. %1$tS sec. %1$tL ms.",
                    result4.getSearchingTime());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Search for an argument in a list using simplest search method
     *
     * @param list   List to search
     * @param target Argument to find
     * @return true if founded, else return false
     */
    static boolean isFoundByLinearSearch(List<Contact> list, String target) {
        boolean isFound = false;
        for (Contact listElement : list) {
            if (listElement.getFullName().equalsIgnoreCase(target)) {
                isFound = true;
                break;
            }
        }
        return isFound;
    }


    static boolean isFoundByjumpSearch(List<Contact> list, String target) {
        int currentRight = 0; // right border of the current block
        int prevRight = 0; // right border of the previous block

        /* If array is empty, the element is not found */
        if (list.size() == 0) {
            return false;
        }

        /* Check the first element */
        if (list.get(currentRight).getFullName() == target) {
            return true;
        }

        /* Calculating the jump length over array elements */
        int jumpLength = (int) Math.sqrt(list.size());

        /* Finding a block where the element may be present */
        while (currentRight < list.size() - 1) {

            /* Calculating the right border of the following block */
            currentRight = Math.min(list.size() - 1, currentRight + jumpLength);

            if (list.get(currentRight).getFullName().compareToIgnoreCase(target) > 0) {
                break; // Found a block that may contain the target element
            }

            prevRight = currentRight; // update the previous right block border
        }

        /* If the last block is reached and it cannot contain the target value => not found */
        if ((currentRight == list.size() - 1)
                && target.compareToIgnoreCase(list.get(currentRight).getFullName()) > 0) {
            return false;
        }

        /* Doing linear search in the found block */
        return backwardSearch(list, target, prevRight, currentRight);
    }

    public static boolean backwardSearch(List<Contact> list, String target,
                                         int leftExcl, int rightIncl) {
        for (int i = rightIncl; i > leftExcl; i--) {
            if (list.get(i).getFullName().compareToIgnoreCase(target) == 0) {
                return true;
            }
        }
        return false;
    }

    static Result findByLinearSearch(List<Contact> directoryList, List<String> argumentList) {
        Result result = new Result();
        long start = System.currentTimeMillis();
        int counterFound = 0;

        for (String argument : argumentList) {
            if (isFoundByLinearSearch(directoryList, argument)) {
                counterFound++;
            }
        }

        long end = System.currentTimeMillis();
        long timeConsumedByLinearSearch = end - start;

        result.setFoundQuantity(counterFound);
        result.setEntryQuantity(argumentList.size());
        result.setSearchingTime(timeConsumedByLinearSearch);
        result.setSortingTime(0l);
        result.setExcededTime(false);

        return result;
    }

    public static List<Contact> getBubbleSortedList(List<Contact> list,
                                                    Long timeConsumedByLinearSearch)
            throws TimeoutException {

        List<Contact> clonedList = new ArrayList<>(list);
        long start = System.currentTimeMillis();
        for (int i = 0; i < clonedList.size() - 1; i++) {
            for (int j = 0; j < clonedList.size() - i - 1; j++) {
                /* if a pair of adjacent elements has the wrong order it swaps them */
                if (clonedList.get(j).getFullName()
                        .compareToIgnoreCase(clonedList.get(j + 1).getFullName())
                        > 0) {
                    Contact temp = clonedList.get(j);
                    clonedList.set(j, clonedList.get(j + 1));
                    clonedList.set(j + 1, temp);
                }
            }
            if (System.currentTimeMillis() - start > timeConsumedByLinearSearch + 10000l) {
                throw new TimeoutException();
            }
        }

        return clonedList;
    }

    static Result findByJumpSearch(List<Contact> directoryList, List<String> argumentList,
                                   long timeConsumedByLinearSearch) {

        Result result = new Result();
        long startProcess = System.currentTimeMillis();
        try {

            List<Contact> sortedDirectoryList = getBubbleSortedList(directoryList,
                    timeConsumedByLinearSearch);
            long sortingTime = System.currentTimeMillis() - startProcess;
            int counterFound = 0;

            for (String argument : argumentList) {
                if (isFoundByjumpSearch(sortedDirectoryList, argument)) {
                    counterFound++;
                }
            }
            long endProcessTime = System.currentTimeMillis() - startProcess;

            result.setExcededTime(false);
            result.setSortingTime(sortingTime);
            result.setSearchingTime(endProcessTime - sortingTime);
            result.setFoundQuantity(counterFound);
            result.setEntryQuantity(argumentList.size());

        } catch (TimeoutException t) {
            Result result2 = findByLinearSearch(directoryList, argumentList);
            long endProcessTime = System.currentTimeMillis() - startProcess;
            result2.setExcededTime(true);
            result2.setSortingTime(endProcessTime - result2.getSearchingTime());
            return result2;
        }

        return result;
    }

    static Result findByBinarySearch(List<Contact> directoryList, List<String> argumentList) {
        Result result = new Result();

        long startProcess = System.currentTimeMillis();

        quickSort(directoryList, 0, directoryList.size() - 1);

        long sortingTime = System.currentTimeMillis() - startProcess;
        int counterFound = 0;

        for (String argument : argumentList) {
            if (isFoundByBinarySearch(directoryList, argument, 0, directoryList.size() - 1)) {
                counterFound++;
            }
        }
        long endProcessTime = System.currentTimeMillis() - startProcess;

        result.setExcededTime(false);
        result.setSortingTime(sortingTime);
        result.setSearchingTime(endProcessTime - sortingTime);
        result.setFoundQuantity(counterFound);
        result.setEntryQuantity(argumentList.size());

        return result;
    }

    static void quickSort(List<Contact> contacts, int left, int right) {
        if (left < right) {
            int pivotIndex = partition(contacts, left, right); // the pivot is already on its place
            quickSort(contacts, left, pivotIndex - 1);  // sort the left subarray
            quickSort(contacts, pivotIndex + 1, right); // sort the right subarray
        }
    }

    private static int partition(List<Contact> contacts, int left, int right) {
        Contact pivot = contacts.get(right);  // choose the rightmost element as the pivot
        int partitionIndex = left; // the first element greater than the pivot

        /* move large values into the right side of the array */
        for (int i = left; i < right; i++) {
            if (contacts.get(i).getFullName().compareToIgnoreCase(pivot.getFullName()) < 0) { // may be used '<' as well
                swap(contacts, i, partitionIndex);
                partitionIndex++;
            }
        }

        swap(contacts, partitionIndex, right); // put the pivot on a suitable position

        return partitionIndex;
    }

    private static void swap(List<Contact> contacts, int i, int j) {
        Contact temp = contacts.get(j);
        contacts.set(j, contacts.get(i));
        contacts.set(i, temp);
    }

    public static boolean isFoundByBinarySearch(
            List<Contact> contacts,
            String target,
            int left,
            int right) {

        if (left > right) {
            return false; // search interval is empty, the element is not found
        }

        int mid = left + (right - left) / 2; // the index of the middle element

        if (target.equalsIgnoreCase(contacts.get(mid).getFullName())) {
            return true; // the element is found, return its index
        } else if (target.compareToIgnoreCase(contacts.get(mid).getFullName()) < 0) {
            return isFoundByBinarySearch(contacts, target, left, mid - 1); // go to the left subarray
        } else {
            return isFoundByBinarySearch(contacts, target, mid + 1, right); // go the the right subarray
        }
    }

    static Result findByHashTable(List<Contact> directoryList, List<String> argumentList) {
        Result result = new Result();

        long startProcess = System.currentTimeMillis();

        HashTable<String> populatedTable = populateTable(directoryList);

        long creatingTime = System.currentTimeMillis() - startProcess;
        int counterFound = 0;

        for (String argument : argumentList) {
            if (isFoundByHashTable(populatedTable, argument)) {
                counterFound++;
            }
        }
        long endProcessTime = System.currentTimeMillis() - startProcess;

        result.setExcededTime(false);
        result.setSortingTime(creatingTime);
        result.setSearchingTime(endProcessTime - creatingTime);
        result.setFoundQuantity(counterFound);
        result.setEntryQuantity(argumentList.size());

        return result;
    }

    private static boolean isFoundByHashTable(
            HashTable<String> populatedTable,
            String argument) {

        return populatedTable.get(Math.abs(argument.hashCode())) != null;
    }

    private static HashTable<String> populateTable(List<Contact> directoryList) {
        int size = directoryList.size()  * 2;
        HashTable<String> table = new HashTable<>(size);

        for (Contact contact : directoryList) {
            table.put(Math.abs(contact.getFullName().hashCode()), contact.getPhoneNumber());
        }

        return table;
    }

}
