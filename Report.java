package accidentpack;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * @author hananali
 * @version 29 January 2024
 */
public class Report {

	private String state;
	private int severity;
	private String city;
	private double visibility;
	private String weatherCondition;
	private String startTime;
	private String endTime;
	private LocalDateTime dateTime;
	private String dayOrNight;
	private String street;
	private String county;
	private int year;
	private int month;
	private int day;

	public Report(String state, int severity, double visibility, String weatherCondition, String startTime,
			String endTime, String city, String street, String county, int year, int month, int day) {
		this.state = state;
		this.severity = severity;
		this.visibility = visibility;
		this.weatherCondition = weatherCondition;
		this.startTime = startTime;
		this.endTime = endTime;
		this.city = city;
		this.setStreet(street);
		this.county = county;
		this.year = year;
		this.month = month;
		this.day = day;
	}

	public String getCounty() {
		return county;
	}

	public String getState() {
		return state;
	}

	public String getStreet() {
		return street;
	}

	public String getEndTime() {
		return endTime;
	}

	public int getSeverity() {
		return severity;
	}

	public double getVisibility() {
		return visibility;
	}

	public String getWeatherCondition() {
		return weatherCondition;
	}

	public String getStartTime() {
		return startTime;
	}

	public LocalDateTime getDateTime() {
		return dateTime;
	}

	public String getDayOrNight() {
		return dayOrNight;
	}

	public String getCity() {
		return city;
	}

	public int getYear() {
		return year;
	}

	public int getMonth() {
		return month;
	}

	public int getDay() {
		return day;
	}

	/**
	 * Read reports from a CSV file and create an array of Report objects.
	 *
	 * @param csvFile The path to the CSV file.
	 * @return An array of Report objects.
	 * @throws IOException If an I/O error occurs.
	 */
	public static Report[] readReportsFromFile(String csvFile) throws IOException {
		Report[] reports = new Report[0];

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			br.readLine();

			String line;
			int index = 0;

			while ((line = br.readLine()) != null) {
				String[] values = line.split(",");
				if (values.length >= 13) {
					int severity = Integer.parseInt(values[1].trim());
					String startTime = values[2].trim();
					String endTime = values[3].trim();
					String state = values[7].trim();
					double visibility = Double.parseDouble(values[10].trim());
					String weatherCondition = values[11].trim();
					String city = values[5].trim();
					String street = values[4].trim();
					String county = values[6].trim();

					String dateTimeString = values[2].trim();
					int year = Integer.parseInt(dateTimeString.substring(0, 4));
					int month = Integer.parseInt(dateTimeString.substring(5, 7));
					int day = Integer.parseInt(dateTimeString.substring(8, 10));

					LocalDateTime dateTime = parseDateTime(dateTimeString);

					String dayOrNight = values[values.length - 1].trim();

					Report report = new Report(state, severity, visibility, weatherCondition, startTime, endTime, city,
							street, county, year, month, day);
					report.setDateTime(dateTime);
					report.setDayOrNight(dayOrNight);

					if (index == reports.length) {
						reports = Arrays.copyOf(reports, reports.length * 2 + 1);
					}

					reports[index++] = report;
				}
			}

			reports = Arrays.copyOf(reports, index);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return reports;
	}

	/**
	 * Parse date-time string in the format into LocalDateTime.
	 *
	 * @param dateTimeString The date-time string to parse.
	 * @return LocalDateTime object if parsing is successful, or null if an error
	 *         occurs.
	 */
	private static LocalDateTime parseDateTime(String dateTimeString) {
		try {
			int year = Integer.parseInt(dateTimeString.substring(0, 4));
			int month = Integer.parseInt(dateTimeString.substring(5, 7));
			int day = Integer.parseInt(dateTimeString.substring(8, 10));
			int hour = Integer.parseInt(dateTimeString.substring(11, 13));
			int minute = Integer.parseInt(dateTimeString.substring(14, 16));
			int second = Integer.parseInt(dateTimeString.substring(17, 19));

			return LocalDateTime.of(year, month, day, hour, minute, second);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}

	public void setDayOrNight(String dayOrNight) {
		this.dayOrNight = dayOrNight;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setState(String state) {
		this.state = state;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	@Override
	public String toString() {
		return "Report{" + "state '" + state + '\'' + ", visibility " + visibility + ", startTime '" + startTime + '\''
				+ ", endTime '" + endTime + '\'' + ", county '" + county + '\'' + '}';
	}

}