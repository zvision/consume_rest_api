package com.alaf.consumeRestApi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ConsumeRestApi {

	public static void main(String[] args) {

		ConsumeRestApi con = new ConsumeRestApi();

		String streamOfPosts = con.fetchPosts("https://jsonplaceholder.typicode.com/posts");
		try {
			List<Post> posts = con.jsonStringToObject(streamOfPosts);
			System.out.print(posts.get(0));  // printing out the first record to verify the logic
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	/**
	 * for given adress service read and return all records as a string
	 * @param apiAdress,  api address to access 
	 * @return string, entire data as a string
	 */
	public String fetchPosts(String apiAdress) {

		StringBuffer buf = new StringBuffer();
		try {
			URL url = new URL(apiAdress);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP Error code : " + conn.getResponseCode());
			}
			InputStreamReader in = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(in);
			String output;
			while ((output = br.readLine()) != null) {
				buf.append(output);
				// System.out.println(output);

			}
			conn.disconnect();

		} catch (Exception e) {
			System.out.println("Exception in NetClientGet:- " + e);
		}
		return buf.toString();
	}

	/**
	 * 
	 * @param stream the string containing json formatted data
	 * @return list of Post
	 * @throws JSONException
	 */
	public List<Post> jsonStringToObject(String stream) throws JSONException {

		List<Post> posts = new ArrayList<Post>();

		JSONArray array = new JSONArray(stream);

		for (int i = 0; i < array.length(); i++) {
			JSONObject object = array.getJSONObject(i);
			posts.add(new Post(object.getLong("userId"), object.getLong("id"), object.getString("title"),
					object.getString("body")));
		}
		return posts;
	}

	
	
	
	@AllArgsConstructor
	@NoArgsConstructor
	public @Data class Post {

		private Long userid;
		private Long id;
		private String title;
		private String body;

	}
}