package crl.game;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import javax.net.ssl.HttpsURLConnection;

public class GameVersion {
	
	public static final GameVersion
		currentVersion = new GameVersion("0.8.3", 2024, 4, 22);
	
	
	public GameVersion(String code, int year, int month, int day) {
		this.code = code;
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		c.set(year, month, day);
		this.date = c.getTime();
	}

	@Override
	public boolean equals(Object o) {
		return ((GameVersion)o).getCode().equals(code);
	}

	private String code;
	private Date date;

	public String getCode() {
		return code;
	}

	private DateFormat sdf = DateFormat.getDateInstance(DateFormat.MEDIUM);

	public String getFormattedDate() {
		return sdf.format(date);
	}


	public static GameVersion getWebLatestVersion() {
		String uri = "http://slashie.net/cvrl/latestVersion.txt";
		String str = httpsGet(uri);
		if (str == null) {
			return null;
		}
		String[] info = str.split(",");
		try {
			return new GameVersion(
				info[0],
				Integer.parseInt(info[1]),	// year
				Integer.parseInt(info[2]),	// month
				Integer.parseInt(info[3]));	// day of month
		} catch (NumberFormatException e) {
			System.out.println("Invalid content for latest version URL: " + str);
			return null;
		}
	}
	
	private static final String httpsGet(String targetURI) {
		HttpsURLConnection conn = null;
		try {
			URI uri = new URI(targetURI);
			URL url = uri.toURL();
			conn = (HttpsURLConnection)url.openConnection();
			InputStream is = conn.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			StringBuilder response = new StringBuilder();
			String line;
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			rd.close();
			return response.toString();
		} catch (URISyntaxException urise) {
			urise.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}
	}
	
}
