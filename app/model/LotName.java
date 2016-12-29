package app.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class LotName
{
    private static final String del = ". ";
    private final StringProperty fixedNameProperty;   // Первая часть названия лота (обычно уникальная часть)
    private final StringProperty regularNameProperty; // Вторая часть названия лота(обычно повторяется)
    private final StringProperty boxProperty;         // Третья часть названия лота (номер коробки)

    LotName()
    {
        fixedNameProperty = new SimpleStringProperty("");
        regularNameProperty = new SimpleStringProperty("");
        boxProperty = new SimpleStringProperty("");
    }

    public String getFixedName()
    {
        return fixedNameProperty.get();
    }
    public StringProperty fixedNameProperty()
    {
        return fixedNameProperty;
    }
    public void setFixedName(String fixedName)
    {
        this.fixedNameProperty.set(fixedName.trim());
    }
    public String getRegularName()
    {
        return regularNameProperty.get();
    }
    public void setRegularName(String regularName)
    {
        this.regularNameProperty.set(regularName.trim());
    }
    public String getBox()
    {
        return boxProperty.get();
    }
    public StringProperty boxProperty()
    {
        return boxProperty;
    }
    public void setBox(String box)
    {
        this.boxProperty.set(box.trim());
    }
    public String getFullName()
    {
        return getFixedName() + del + getRegularName();
    }
}
