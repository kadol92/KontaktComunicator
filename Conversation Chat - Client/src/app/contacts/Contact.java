package app.contacts;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Kamil on 2017-01-15.
 */
public class Contact {

    private String name;
    private String city;
    private int userID;

    public boolean isUserWriting() {
        return userWriting;
    }

    public void setUserWriting(boolean userWriting) {
        this.userWriting = userWriting;
    }

    private boolean userWriting;
    private int unreadMessages;
    private List<ContactMessage> currentCorversation;
    //private List<String> currentCorversation;
    private List<String> historyConversation;

    private Date date;


    public Contact(String name, String city, int userID) {
        this.name = name;
        this.city = city;
        this.userID = userID;
        this.userWriting = false;
        this.unreadMessages = 0;
        this.currentCorversation = new ArrayList<>();
        this.historyConversation = new ArrayList<>();

    }


    public List<ContactMessage> getCurrentCorversation() {
        return currentCorversation;
    }

    public int getUnreadMessages() {
        return unreadMessages;
    }

    public void setUnreadMessages(int unreadMessages) {
        this.unreadMessages = unreadMessages;
    }

    public void setCurrentCorversation(ContactMessage currentCorversation) {
        this.currentCorversation.add(currentCorversation);
    }

    public List<String> getHistoryConversation() {
        return historyConversation;
    }

    public void setHistoryConversation(List<String> historyConversation) {
        this.historyConversation = historyConversation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;

        if (userID != contact.userID) return false;
        if (name != null ? !name.equals(contact.name) : contact.name != null) return false;
        if (city != null ? !city.equals(contact.city) : contact.city != null) return false;
        if (currentCorversation != null ? !currentCorversation.equals(contact.currentCorversation) : contact.currentCorversation != null)
            return false;
        return historyConversation != null ? historyConversation.equals(contact.historyConversation) : contact.historyConversation == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (city != null ? city.hashCode() : 0);
        result = 31 * result + userID;
        result = 31 * result + (currentCorversation != null ? currentCorversation.hashCode() : 0);
        result = 31 * result + (historyConversation != null ? historyConversation.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        if(unreadMessages == 0){
            return  name;
        }
        else{
            return name + " [" + Integer.toString(unreadMessages) + "]";
        }

    }
}
