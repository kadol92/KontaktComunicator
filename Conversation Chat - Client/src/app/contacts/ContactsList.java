package app.contacts;

import java.text.Collator;
import java.util.*;

/**
 * Created by Kamil on 2017-01-15.
 */
public class ContactsList {


    private static int users = 0;
    private Map<Integer, Contact> contacts;
    private Map<String, Integer> alphabeticContacts;
    private List<Contact> contactList;


    public ContactsList()
    {
        this.contactList = new ArrayList<>();
        //this.contacts = new TreeMap<>();
    }

    public static int getUsersNumber() {
        return users;
    }

    public void addContact(Contact contact) {
        this.contactList.add(contact);

        Collections.sort(contactList, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {

                return o1.getName().compareTo(o2.getName());
            }
        });
        //this.contacts.put(users, contact);
        users++;

    }

    public void sortContacts(){
        Collections.sort(contactList, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {

                return o1.getName().compareTo(o2.getName());
            }
        });
    }

    public Contact getContact(int userID) {
        return this.contactList.get(userID);
        //return this.contacts.get(userID);
    }


    public List<Contact> getAllContacts() {
        //List<Contact> contacts = new ArrayList<>(this.contacts.values());
        //return contacts;
        return this.contactList;
    }



}
