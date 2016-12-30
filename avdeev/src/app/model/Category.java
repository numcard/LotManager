package app.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Category
{
    private final StringProperty category;
    private final IntegerProperty meshokCategory;

    public Category()
    {
        this.category = new SimpleStringProperty("");
        this.meshokCategory = new SimpleIntegerProperty(0);
    }

    public StringProperty getCategory()
    {
        return category;
    }
    public String getName()
    {
        return category.get();
    }
    public void setName(String categoryName)
    {
        category.set(categoryName);
    }
    public int getMeshokCategory()
    {
        return meshokCategory.get();
    }
    public void setMeshokCategory(int meshokCategory)
    {
        this.meshokCategory.set(meshokCategory);
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