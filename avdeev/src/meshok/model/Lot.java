package meshok.model;

import java.util.List;

public class Lot
{
    private long lotNumber;
    private String fixedName;
    private String regularName;
    private String box;
    private int price;
    private int views;
    private int available;
    private String category;
    private String shipping;
    private List<String> images;
    private boolean bestOffer;

    public long getLotNumber()
    {
        return lotNumber;
    }
    public void setLotNumber(long lotNumber)
    {
        this.lotNumber = lotNumber;
    }
    public String getFixedName()
    {
        return fixedName;
    }
    public void setFixedName(String fixedName)
    {
        this.fixedName = fixedName;
    }
    public String getRegularName()
    {
        return regularName;
    }
    public void setRegularName(String regularName)
    {
        this.regularName = regularName;
    }
    public String getBox()
    {
        return box;
    }
    public void setBox(String box)
    {
        this.box = box;
    }
    public int getPrice()
    {
        return price;
    }
    public void setPrice(int price)
    {
        this.price = price;
    }
    public int getViews()
    {
        return views;
    }
    public void setViews(int views)
    {
        this.views = views;
    }
    public int getAvailable()
    {
        return available;
    }
    public void setAvailable(int available)
    {
        this.available = available;
    }
    public String getCategory()
    {
        return category;
    }
    public void setCategory(String category)
    {
        this.category = category;
    }
    public String getShipping()
    {
        return shipping;
    }
    public void setShipping(String shipping)
    {
        this.shipping = shipping;
    }
    public List<String> getImages()
    {
        return images;
    }
    public void setImages(List<String> images)
    {
        this.images = images;
    }
    public boolean isBestOffer()
    {
        return bestOffer;
    }
    public void setBestOffer(boolean bestOffer)
    {
        this.bestOffer = bestOffer;
    }

    @Override
    public String toString()
    {
        return "Lot{" + "lotNumber=" + lotNumber + ", fixedName='" + fixedName + '\'' + ", regularName='" + regularName + '\'' + ", box='" + box + '\'' + ", price=" + price + ", views=" + views + ", available=" + available + ", category='" + category + '\'' + ", shipping='" + shipping + '\'' + ", images=" + images + ", bestOffer=" + bestOffer + '}';
    }
}