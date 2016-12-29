package app.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Category
{
    private final StringProperty nameProperty;

    public Category()
    {
        this.nameProperty = new SimpleStringProperty("");
    }

    public String getName()
    {
        return nameProperty.get();
    }
    public void setName(String categoryName)
    {
        nameProperty.set(categoryName);
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;

        Category category = (Category) o;

        return getName().equals(category.getName());
    }
    @Override
    public int hashCode()
    {
        return getName().hashCode();
    }
}