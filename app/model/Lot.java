package app.model;

import javafx.beans.property.*;

import javax.xml.bind.annotation.XmlElement;

public class Lot
{
    @XmlElement(name = "lotName") private final LotName lotName;    // Название лота
    private final StringProperty categoryProperty;                  // Категория
    private final StringProperty image1Property;                    // Фотки
    private final StringProperty image2Property;                    // Фотки
    private final StringProperty image3Property;                    // Фотки
    private final StringProperty image4Property;                    // Фотки
    private final IntegerProperty priceProperty;                    // Стоимость
    private final BooleanProperty bestOfferProperty;                // Торг уместен?

    public Lot()
    {
        lotName = new LotName();
        categoryProperty = new SimpleStringProperty("");
        image1Property = new SimpleStringProperty("");
        image2Property = new SimpleStringProperty("");
        image3Property = new SimpleStringProperty("");
        image4Property = new SimpleStringProperty("");
        priceProperty = new SimpleIntegerProperty();
        bestOfferProperty = new SimpleBooleanProperty(true);
    }

    public LotName getLotName()
    {
        return lotName;
    }
    public String getCategory()
    {
        return categoryProperty.get();
    }
    public StringProperty categoryProperty()
    {
        return categoryProperty;
    }
    public void setCategory(String category)
    {
        this.categoryProperty.set(category);
    }
    public int getPrice()
    {
        return priceProperty.get();
    }
    public void setPrice(int price)
    {
        this.priceProperty.set(price);
    }
    public boolean isBestOffer()
    {
        return bestOfferProperty.getValue();
    }
    public void setBestOffer(boolean bestOffer)
    {
        this.bestOfferProperty.set(bestOffer);
    }
    public String getImage1()
    {
        return image1Property.get();
    }
    public void setImage1(String image1)
    {
        this.image1Property.set(image1);
    }
    public String getImage2()
    {
        return image2Property.get();
    }
    public void setImage2(String image2)
    {
        this.image2Property.set(image2);
    }
    public String getImage3()
    {
        return image3Property.get();
    }
    public void setImage3(String image3)
    {
        this.image3Property.set(image3);
    }
    public String getImage4()
    {
        return image4Property.get();
    }
    public void setImage4(String image4)
    {
        this.image4Property.set(image4);
    }

    @Override
    public boolean equals(Object o)
    {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;

        Lot lot = (Lot) o;

        return  getCategory().equals(lot.getCategory()) && getImage1().equals(lot.getImage1())
                && getImage2().equals(lot.getImage2())  && getImage3().equals(lot.getImage3())
                && getImage4().equals(lot.getImage4());
    }

    @Override
    public int hashCode()
    {
        int result = getCategory().hashCode();
        result = 31 * result + getImage1().hashCode();
        result = 31 * result + getImage2().hashCode();
        result = 31 * result + getImage3().hashCode();
        result = 31 * result + getImage4().hashCode();
        return result;
    }
}