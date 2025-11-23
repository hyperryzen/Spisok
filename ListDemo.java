import java.util.*;
import java.util.function.Consumer;

// 1. Интерфейс списка
interface List<T> extends Iterable<T> {
    /**
     * Добавляет элемент в конец списка
     * @param element элемент для добавления
     */
    void add(T element);
    
    /**
     * Добавляет элемент по указанному индексу
     * @param index индекс
     * @param element элемент для добавления
     * @throws IndexOutOfBoundsException если индекс невалидный
     */
    void add(int index, T element);
    
    /**
     * Возвращает элемент по указанному индексу
     * @param index индекс
     * @return элемент по указанному индексу
     * @throws IndexOutOfBoundsException если индекс невалидный
     */
    T get(int index);
    
    /**
     * Заменяет элемент по указанному индексу
     * @param index индекс
     * @param element новый элемент
     * @return старый элемент
     * @throws IndexOutOfBoundsException если индекс невалидный
     */
    T set(int index, T element);
    
    /**
     * Удаляет элемент по указанному индексу
     * @param index индекс
     * @return удаленный элемент
     * @throws IndexOutOfBoundsException если индекс невалидный
     */
    T remove(int index);
    
    /**
     * Удаляет первое вхождение указанного элемента
     * @param element элемент для удаления
     * @return true если элемент был удален, false если элемент не найден
     */
    boolean remove(T element);
    
    /**
     * Проверяет, содержится ли элемент в списке
     * @param element искомый элемент
     * @return true если элемент найден, false в противном случае
     */
    boolean contains(T element);
    
    /**
     * Возвращает индекс первого вхождения элемента
     * @param element искомый элемент
     * @return индекс элемента или -1 если элемент не найден
     */
    int indexOf(T element);
    
    /**
     * Возвращает индекс последнего вхождения элемента
     * @param element искомый элемент
     * @return индекс элемента или -1 если элемент не найден
     */
    int lastIndexOf(T element);
    
    /**
     * Проверяет, пуст ли список
     * @return true если список пуст, false в противном случае
     */
    boolean isEmpty();
    
    /**
     * Возвращает количество элементов в списке
     * @return количество элементов
     */
    int size();
    
    /**
     * Очищает список
     */
    void clear();
    
    /**
     * Возвращает массив содержащий все элементы списка
     * @return массив элементов
     */
    Object[] toArray();
    
    /**
     * Возвращает подсписок от fromIndex (включительно) до toIndex (исключительно)
     * @param fromIndex начальный индекс (включительно)
     * @param toIndex конечный индекс (исключительно)
     * @return новый список содержащий указанный диапазон
     * @throws IndexOutOfBoundsException если индексы невалидные
     */
    List<T> subList(int fromIndex, int toIndex);
}

// 2. Реализация на основе массива (аналог ArrayList)
class ArrayList<T> implements List<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private static final double GROW_FACTOR = 1.5;
    
    private Object[] elements;
    private int size;
    
    public ArrayList() {
        this.elements = new Object[DEFAULT_CAPACITY];
        this.size = 0;
    }
    
    public ArrayList(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("Initial capacity cannot be negative");
        }
        this.elements = new Object[initialCapacity];
        this.size = 0;
    }
    
    @Override
    public void add(T element) {
        ensureCapacity(size + 1);
        elements[size++] = element;
    }
    
    @Override
    public void add(int index, T element) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);
        
        // Сдвигаем элементы вправо
        System.arraycopy(elements, index, elements, index + 1, size - index);
        elements[index] = element;
        size++;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) elements[index];
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public T set(int index, T element) {
        checkIndex(index);
        T oldValue = (T) elements[index];
        elements[index] = element;
        return oldValue;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        checkIndex(index);
        T removedElement = (T) elements[index];
        
        // Сдвигаем элементы влево
        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(elements, index + 1, elements, index, numMoved);
        }
        elements[--size] = null; // Помощь сборщику мусора
        
        return removedElement;
    }
    
    @Override
    public boolean remove(T element) {
        int index = indexOf(element);
        if (index != -1) {
            remove(index);
            return true;
        }
        return false;
    }
    
    @Override
    public boolean contains(T element) {
        return indexOf(element) != -1;
    }
    
    @Override
    public int indexOf(T element) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(element, elements[i])) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(T element) {
        for (int i = size - 1; i >= 0; i--) {
            if (Objects.equals(element, elements[i])) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            elements[i] = null;
        }
        size = 0;
    }
    
    @Override
    public Object[] toArray() {
        return Arrays.copyOf(elements, size);
    }
    
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("Invalid range: fromIndex=" + fromIndex + ", toIndex=" + toIndex + ", size=" + size);
        }
        
        ArrayList<T> subList = new ArrayList<>(toIndex - fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(get(i));
        }
        return subList;
    }
    
    @Override
    public Iterator<T> iterator() {
        return new ArrayListIterator();
    }
    
    // Вспомогательные методы
    private void ensureCapacity(int minCapacity) {
        if (minCapacity > elements.length) {
            int newCapacity = (int) (elements.length * GROW_FACTOR);
            if (newCapacity < minCapacity) {
                newCapacity = minCapacity;
            }
            elements = Arrays.copyOf(elements, newCapacity);
        }
    }
    
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
    
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
    
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(elements[i]);
            if (i < size - 1) {
                sb.append(", ");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    // Итератор для ArrayList
    private class ArrayListIterator implements Iterator<T> {
        private int currentIndex = 0;
        private boolean canRemove = false;
        
        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }
        
        @Override
        @SuppressWarnings("unchecked")
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            canRemove = true;
            return (T) elements[currentIndex++];
        }
        
        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException();
            }
            ArrayList.this.remove(--currentIndex);
            canRemove = false;
        }
    }
}

// 3. Реализация на основе связного списка (аналог LinkedList)
class LinkedList<T> implements List<T> {
    
    private static class Node<T> {
        T data;
        Node<T> next;
        Node<T> prev;
        
        Node(T data) {
            this.data = data;
            this.next = null;
            this.prev = null;
        }
    }
    
    private Node<T> head;
    private Node<T> tail;
    private int size;
    
    public LinkedList() {
        this.head = null;
        this.tail = null;
        this.size = 0;
    }
    
    @Override
    public void add(T element) {
        addLast(element);
    }
    
    @Override
    public void add(int index, T element) {
        checkIndexForAdd(index);
        
        if (index == 0) {
            addFirst(element);
        } else if (index == size) {
            addLast(element);
        } else {
            Node<T> current = getNode(index);
            Node<T> newNode = new Node<>(element);
            
            // Вставляем перед current
            newNode.prev = current.prev;
            newNode.next = current;
            current.prev.next = newNode;
            current.prev = newNode;
            
            size++;
        }
    }
    
    public void addFirst(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        size++;
    }
    
    public void addLast(T element) {
        Node<T> newNode = new Node<>(element);
        if (isEmpty()) {
            head = tail = newNode;
        } else {
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
        size++;
    }
    
    @Override
    public T get(int index) {
        checkIndex(index);
        return getNode(index).data;
    }
    
    public T getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return head.data;
    }
    
    public T getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        return tail.data;
    }
    
    @Override
    public T set(int index, T element) {
        checkIndex(index);
        Node<T> node = getNode(index);
        T oldValue = node.data;
        node.data = element;
        return oldValue;
    }
    
    @Override
    public T remove(int index) {
        checkIndex(index);
        
        if (index == 0) {
            return removeFirst();
        } else if (index == size - 1) {
            return removeLast();
        } else {
            Node<T> nodeToRemove = getNode(index);
            return removeNode(nodeToRemove);
        }
    }
    
    @Override
    public boolean remove(T element) {
        Node<T> current = head;
        while (current != null) {
            if (Objects.equals(element, current.data)) {
                removeNode(current);
                return true;
            }
            current = current.next;
        }
        return false;
    }
    
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        
        T removedData = head.data;
        if (head == tail) {
            head = tail = null;
        } else {
            head = head.next;
            head.prev = null;
        }
        size--;
        return removedData;
    }
    
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("List is empty");
        }
        
        T removedData = tail.data;
        if (head == tail) {
            head = tail = null;
        } else {
            tail = tail.prev;
            tail.next = null;
        }
        size--;
        return removedData;
    }
    
    @Override
    public boolean contains(T element) {
        return indexOf(element) != -1;
    }
    
    @Override
    public int indexOf(T element) {
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            if (Objects.equals(element, current.data)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }
    
    @Override
    public int lastIndexOf(T element) {
        Node<T> current = tail;
        int index = size - 1;
        while (current != null) {
            if (Objects.equals(element, current.data)) {
                return index;
            }
            current = current.prev;
            index--;
        }
        return -1;
    }
    
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    
    @Override
    public int size() {
        return size;
    }
    
    @Override
    public void clear() {
        head = null;
        tail = null;
        size = 0;
    }
    
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            array[index++] = current.data;
            current = current.next;
        }
        return array;
    }
    
    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        if (fromIndex < 0 || toIndex > size || fromIndex > toIndex) {
            throw new IndexOutOfBoundsException("Invalid range: fromIndex=" + fromIndex + ", toIndex=" + toIndex + ", size=" + size);
        }
        
        LinkedList<T> subList = new LinkedList<>();
        Node<T> current = getNode(fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(current.data);
            current = current.next;
        }
        return subList;
    }
    
    @Override
    public Iterator<T> iterator() {
        return new LinkedListIterator();
    }
    
    // Вспомогательные методы
    private Node<T> getNode(int index) {
        checkIndex(index);
        
        Node<T> current;
        if (index < size / 2) {
            // Ищем с начала
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            // Ищем с конца
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }
    
    private T removeNode(Node<T> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }
        
        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
        
        T removedData = node.data;
        size--;
        return removedData;
    }
    
    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
    
    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }
    
    @Override
    public String toString() {
        if (isEmpty()) {
            return "[]";
        }
        
        StringBuilder sb = new StringBuilder("[");
        Node<T> current = head;
        while (current != null) {
            sb.append(current.data);
            if (current.next != null) {
                sb.append(", ");
            }
            current = current.next;
        }
        sb.append("]");
        return sb.toString();
    }
    
    // Итератор для LinkedList
    private class LinkedListIterator implements Iterator<T> {
        private Node<T> current = head;
        private Node<T> lastReturned = null;
        private boolean canRemove = false;
        
        @Override
        public boolean hasNext() {
            return current != null;
        }
        
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastReturned = current;
            current = current.next;
            canRemove = true;
            return lastReturned.data;
        }
        
        @Override
        public void remove() {
            if (!canRemove) {
                throw new IllegalStateException();
            }
            removeNode(lastReturned);
            canRemove = false;
        }
    }
}

// 4. Демонстрация работы
public class ListDemo {
    public static void main(String[] args) {
        System.out.println("=== Демонстрация ArrayList ===");
        demoArrayList();
        
        System.out.println("\n=== Демонстрация LinkedList ===");
        demoLinkedList();
        
        System.out.println("\n=== Демонстрация операций со списками ===");
        demoListOperations();
        
        System.out.println("\n=== Демонстрация итераторов ===");
        demoIterators();
        
        System.out.println("\n=== Сравнение производительности ===");
        comparePerformance();
    }
    
    private static void demoArrayList() {
        List<Integer> list = new ArrayList<>();
        
        System.out.println("Добавляем элементы: 10, 20, 30, 40, 50");
        list.add(10);
        list.add(20);
        list.add(30);
        list.add(40);
        list.add(50);
        
        System.out.println("Список: " + list);
        System.out.println("Размер: " + list.size());
        System.out.println("Пустой ли: " + list.isEmpty());
        
        System.out.println("\nДобавляем элемент 25 на позицию 2:");
        list.add(2, 25);
        System.out.println("Список: " + list);
        
        System.out.println("\nПолучаем и изменяем элементы:");
        System.out.println("Элемент на позиции 3: " + list.get(3));
        System.out.println("Заменяем элемент на позиции 1: " + list.set(1, 15));
        System.out.println("Список: " + list);
        
        System.out.println("\nПоиск элементов:");
        System.out.println("Индекс 30: " + list.indexOf(30));
        System.out.println("Содержит 100: " + list.contains(100));
    }
    
    private static void demoLinkedList() {
        List<String> list = new LinkedList<>();
        
        System.out.println("Добавляем строки:");
        list.add("Apple");
        list.add("Banana");
        list.add("Orange");
        
        System.out.println("Список: " + list);
        System.out.println("Размер: " + list.size());
        
        // Используем дополнительные методы LinkedList
        LinkedList<String> linkedList = (LinkedList<String>) list;
        System.out.println("\nИспользуем методы LinkedList:");
        linkedList.addFirst("First");
        linkedList.addLast("Last");
        System.out.println("После добавления в начало и конец: " + list);
        
        System.out.println("Первый элемент: " + linkedList.getFirst());
        System.out.println("Последний элемент: " + linkedList.getLast());
        
        System.out.println("Удаляем первый: " + linkedList.removeFirst());
        System.out.println("Удаляем последний: " + linkedList.removeLast());
        System.out.println("Список: " + list);
    }
    
    private static void demoListOperations() {
        List<Character> list = new ArrayList<>();
        
        System.out.println("Создаем список символов:");
        for (char c = 'A'; c <= 'F'; c++) {
            list.add(c);
        }
        System.out.println("Исходный список: " + list);
        
        System.out.println("\nУдаляем элементы:");
        System.out.println("Удален элемент на позиции 2: " + list.remove(2));
        System.out.println("Список: " + list);
        
        System.out.println("Удаляем элемент 'E': " + list.remove((Character) 'E'));
        System.out.println("Список: " + list);
        
        System.out.println("\nСоздаем подсписок (1-3):");
        List<Character> subList = list.subList(1, 3);
        System.out.println("Подсписок: " + subList);
        
        System.out.println("\nОчищаем список:");
        list.clear();
        System.out.println("Пустой ли: " + list.isEmpty());
        System.out.println("Размер: " + list.size());
    }
    
    private static void demoIterators() {
        List<Integer> list = new LinkedList<>();
        
        System.out.println("Создаем список и используем итератор:");
        for (int i = 1; i <= 5; i++) {
            list.add(i * 10);
        }
        System.out.println("Список: " + list);
        
        System.out.println("Обход с помощью итератора:");
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            System.out.print(iterator.next() + " ");
        }
        System.out.println();
        
        System.out.println("Удаляем четные числа с помощью итератора:");
        iterator = list.iterator();
        while (iterator.hasNext()) {
            int value = iterator.next();
            if (value % 20 == 0) {
                iterator.remove();
            }
        }
        System.out.println("Список после удаления: " + list);
    }
    
    private static void comparePerformance() {
        final int ELEMENT_COUNT = 10000;
        
        // Тест добавления в конец
        long startTime = System.nanoTime();
        List<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            arrayList.add(i);
        }
        long arrayAddTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        List<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            linkedList.add(i);
        }
        long linkedAddTime = System.nanoTime() - startTime;
        
        // Тест доступа по индексу
        startTime = System.nanoTime();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            arrayList.get(i);
        }
        long arrayGetTime = System.nanoTime() - startTime;
        
        startTime = System.nanoTime();
        for (int i = 0; i < ELEMENT_COUNT; i++) {
            linkedList.get(i);
        }
        long linkedGetTime = System.nanoTime() - startTime;
        
        System.out.println("Производительность для " + ELEMENT_COUNT + " элементов:");
        System.out.printf("Добавление в конец - ArrayList: %.3f ms, LinkedList: %.3f ms\n", 
                         arrayAddTime / 1000000.0, linkedAddTime / 1000000.0);
        System.out.printf("Доступ по индексу  - ArrayList: %.3f ms, LinkedList: %.3f ms\n", 
                         arrayGetTime / 1000000.0, linkedGetTime / 1000000.0);
    }
}