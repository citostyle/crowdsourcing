package tuwien.aic.crowdsourcing.mobileworks.task;

public class Location {

	private String country;
	private String city;
	
	public String getCountry() {
		return country;
	}
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}

	@Override
	public String toString() {
		return "Location [country=" + country + ", city=" + city + "]";
	}
}
