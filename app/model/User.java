package app.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class User
{
    private final StringProperty nameProperty;
    private final ObservableList<Lot> lots = FXCollections.observableArrayList();

    public User()
    {
        nameProperty = new SimpleStringProperty("");
    }
    public String getName()
    {
        return nameProperty.get();
    }
    public void setName(String name)
    {
        nameProperty.set(name);
    }
    public ObservableList<Lot> getLots()
    {
        return lots;
    }
    public void setLots(ObservableList<Lot> lots)
    {
        this.lots.clear();
        this.lots.addAll(lots);
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }

        User user = (User) o;

        return getName().equals(user.getName());
    }
    @Override
    public int hashCode()
    {
        return getName().hashCode();
    }
}