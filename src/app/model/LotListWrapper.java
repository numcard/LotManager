package app.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "lots")
public class LotListWrapper
{
	private List<Lot> lots;

	@XmlElement(name = "lot")
	public List<Lot> getLots()
	{
		return lots;
	}

	public void setLots(List<Lot> lots)
	{
		this.lots = lots;
	}
}