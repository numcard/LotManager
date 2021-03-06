package app.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class Lot
{
	private final LotName lotName;                          // Название лота
	private final Category category;                        // Категория
	private final ListProperty<String> images;              // Фото
	private final IntegerProperty price;                    // Стоимость
	private final BooleanProperty bestOffer;                // Торг уместен?
	private final StringProperty meshokLink;                // Ссылка на мешок

	public Lot()
	{
		lotName = new LotName();
		category = new Category();
		images = new SimpleListProperty<>(FXCollections.observableArrayList());
		price = new SimpleIntegerProperty();
		bestOffer = new SimpleBooleanProperty(true);
		meshokLink = new SimpleStringProperty("");
	}

	public String getFullName()
	{
		return getName() + " " + lotName.box.get();
	}

	public String getName()
	{
		String part1 = lotName.fixedName.get().trim();
		String part2 = lotName.regularName.get().trim();

		if(part2.length() < 1) return part1;
		else return part1 + LotName.del + part2;
	}

	public StringProperty fixedName()
	{
		return lotName.fixedName;
	}

	public void setFixedName(String fixedName)
	{
		lotName.fixedName.setValue(trim(fixedName));
	}

	public String getFixedName()
	{
		return this.lotName.fixedName.get();
	}

	public void setRegularName(String regularName)
	{
		lotName.regularName.setValue(trim(regularName));
	}

	public String getRegularName()
	{
		return lotName.regularName.get();
	}

	public StringProperty box()
	{
		return lotName.box;
	}

	public void setBox(String box)
	{
		lotName.box.setValue(box);
	}

	public String getBox()
	{
		return lotName.box.get();
	}

	public StringProperty category()
	{
		return category.getCategory();
	}

	public void setCategory(String category)
	{
		this.category.setName(category);
	}

	public String getCategory()
	{
		return category.getName();
	}

	public void setPrice(int price)
	{
		this.price.setValue(price);
	}

	public int getPrice()
	{
		return price.get();
	}

	public void setBestOffer(boolean bestOffer)
	{
		this.bestOffer.setValue(bestOffer);
	}

	public boolean isBestOffer()
	{
		return bestOffer.get();
	}

	@SuppressWarnings("unused")
	public void setImages(ObservableList<String> images)
	{
		this.images.set(images);
	}

	public ObservableList<String> getImages()
	{
		return images.get();
	}

	public String getMeshokLink()
	{
		return meshokLink.get();
	}

	public void setMeshokLink(String meshokLink)
	{
		this.meshokLink.set(meshokLink);
	}

	public class LotName
	{
		private static final String del = ". ";
		private final StringProperty fixedName;   // Первая часть названия лота (обычно уникальная часть)
		private final StringProperty regularName; // Вторая часть названия лота(обычно повторяется)
		private final StringProperty box;         // Третья часть названия лота (номер коробки)

		LotName()
		{
			fixedName = new SimpleStringProperty("");
			regularName = new SimpleStringProperty("");
			box = new SimpleStringProperty("");
		}
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		Lot lot = (Lot) o;

		return getCategory().equals(lot.getCategory()) && getImages().equals(lot.getImages());
	}

	@Override
	public int hashCode()
	{
		int result = getCategory().hashCode();
		result = 31 * result + getImages().hashCode();
		return result;
	}

	@Override
	public String toString()
	{
		return "Lot{" + "lotName=" + lotName + ", category=" + category + ", images=" + images + ", price=" + price + ", bestOffer=" + bestOffer + ", meshokLink=" + meshokLink + '}';
	}

	private String trim(String line)
	{
		String newLine = line.trim();
		if(newLine.endsWith(".")) newLine = newLine.substring(0, newLine.length() - 1);
		return newLine;
	}
}