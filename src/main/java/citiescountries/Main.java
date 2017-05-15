package citiescountries;

import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import com.google.gson.Gson;

public class Main {
	public static void main(String[] args) throws IOException, NumberFormatException {
		Iterable<CSVRecord> countryList = readCSV("resources/country_by_code.csv");
		Map<String, String> countryMap = new HashMap<>();
		// Reading CVS country codes into a Map
		for (CSVRecord csvRecord : countryList) {
			countryMap.put(csvRecord.get(1), csvRecord.get(0));
		}
		// Reading and processing cities (distinct)
		long before = System.currentTimeMillis();
		Iterable<CSVRecord> citiesList = readCSV("resources/worldcitiespop_filtered.csv");
		Set<City> cities = new HashSet<City>();
		int cnt = 0;
		for (CSVRecord csvRecord : citiesList) {
			cities.add(new City(
				csvRecord.get(0),
				csvRecord.get(1),
				countryMap.get(csvRecord.get(0)),
				0,
				Double.parseDouble(csvRecord.get(3)),
				Double.parseDouble(csvRecord.get(4))
			));
			cnt++;
		}
		// Saving file
		/*
		String str = new Gson().toJson(cities);
		try(PrintWriter out = new PrintWriter( "resources/output.json" )){
		    out.println(str);
		}
		*/
		long after = System.currentTimeMillis();
		System.out.println("Elapsed time processing file " + (after-before)/1000);
		System.out.println("Number of records " + cnt);
		System.out.println("Number of distinct records " + cities.size());
	}
	
	public static Iterable<CSVRecord> readCSV(String filename) throws IOException {
		return CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(new FileReader(filename));
	}
	
}
	
class City {
	String country;
	String city;
	String countryName;
	long population;
	double latitude;
	double longitude;
	
	public City(String country, String city, String countryName, long population, double latitude, double longitude) {
		this.country = country;
		this.city = city;
		this.population = population;
		this.latitude = latitude;
		this.longitude = longitude;
		this.countryName = countryName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (int) (population ^ (population >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof City)) {
			return false;
		}
		City other = (City) obj;
		if (city == null) {
			if (other.city != null) {
				return false;
			}
		} else if (!city.equals(other.city)) {
			return false;
		}
		if (country == null) {
			if (other.country != null) {
				return false;
			}
		} else if (!country.equals(other.country)) {
			return false;
		}
		if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude)) {
			return false;
		}
		if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude)) {
			return false;
		}
		if (population != other.population) {
			return false;
		}
		return true;
	}
}
