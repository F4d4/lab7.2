package com.f4d4.server.rulers;

import com.f4d4.general.facility.Ticket;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класс Руководителя коллекцией
 */

public class CollectionRuler {
    private DatabaseRuler databaseRuler;

    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private PriorityQueue<Ticket> collection = new PriorityQueue<>();

    public CollectionRuler(DatabaseRuler databaseRuler){
        this.databaseRuler = databaseRuler;
        this.lastInitTime=null;
        this.lastSaveTime=null;
    }

    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }


    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }


    public PriorityQueue<Ticket> getCollection() {
        return collection;
    }


    /**
     * Ищет элемент коллекции по id
     */
    public synchronized Ticket byId(Long id){
        for (Ticket element:collection){
            if (element.getId() == id) return element;
        }
        return null;
    }

    /**
     * Проверяет содержит ли коллекция элемент
     *
     * @return возвращает true если элемент существует в коллекции
     */
    public boolean isContain(Ticket t){
        return t == null || byId(t.getId()) != null;
    }


    /**
     * Сортировка коллекции
     */
    public void update() {
        List<Ticket> tempList = new ArrayList<>(collection);
        Collections.sort(tempList);
        collection = new PriorityQueue<>(tempList);
    }
    /**
     * Инициализация коллекции из файла
     *
     *@return возвращает сообщение о  успешности выполнения метода
     */

    public boolean init() {
        collection.clear();
        collection = databaseRuler.loadCollection();
        lastInitTime = LocalDateTime.now();
        update();
        return true;
    }

    /**
     * Добавление элемента в коллекцию
     *
     * @return возвращает сообщение о  успешности выполнения метода
     */
    public boolean add(Ticket a){
        if(isContain(a)){
            return false;
        }
        collection.add(a);
        update();
        return true;
    }

    public synchronized int getUserid(String login) throws SQLException {
        return databaseRuler.getUserID(login);
    }

    public void insertTICKET(Ticket ticket , String login) throws SQLException{
        databaseRuler.insertTicket(ticket , login);
    }

    public Ticket getLastTicket() throws SQLException{
        return databaseRuler.getLastTicket();
    }

    public synchronized void addTOcollection(Ticket ticket,String login) throws SQLException{
        insertTICKET(ticket , login);
        add(getLastTicket());
    }

    public synchronized int isCorrectID(long id) throws SQLException{
        return databaseRuler.isCorrectID(id);
    }

    public synchronized void updateTicketDB(Ticket ticket , long id) throws SQLException{
        databaseRuler.updateTicket(ticket,id);
    }

    public synchronized void updateCollection(Ticket deletable , Ticket newTicket,long id , String login ){
        newTicket.setId(id);
        remove(deletable);
        newTicket.setLogin(login);
        add(newTicket);
    }
    /**
     * Удаляет элемент из коллекции
     */
    public synchronized void remove(Ticket t){
        collection.remove(t);
        update();
    }
    /**
     * Удаляет все элементы в коллекции
     */
    public synchronized void removeAll( int id) throws SQLException{
        databaseRuler.clear(id);
        collection = clearUsersTicket(id);
        update();
    }
    /**
     * удаляет первый элемент коллекции
     */
    public synchronized void removefirst() throws SQLException{
        databaseRuler.removeFirstTicket();
        collection.poll();
        update();
    }

    public synchronized void removeTicketByIdDB(long id) throws SQLException{
        databaseRuler.removeTicketById(id);
    }


    public boolean collectionIsEmpty(){
        return collection.isEmpty();
    }

    public synchronized Ticket getFirstTicketToRemove(){
        return collection.peek();
    }

    public PriorityQueue<Ticket> clearUsersTicket(int id){
        PriorityQueue<Ticket> filteredCollection = collection.stream()
                .filter(ticket -> ticket.getUser_id() != id)
                .collect(Collectors.toCollection(PriorityQueue::new));
        return filteredCollection;
    }

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";

        StringBuilder info = new StringBuilder();
        for (var Ticket : collection) {
            info.append(Ticket+"\n\n");
        }
        return info.toString().trim();
    }



}
