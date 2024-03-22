package com.homeworkfour;

public class DVD {

	// Fields:

	private String title; // Title of this DVD
	private String rating; // Rating of this DVD
	private int runningTime; // Running time of this DVD in minutes

	public DVD(String dvdTitle, String dvdRating, int dvdRunningTime) {
		this.title = dvdTitle;
		this.rating = dvdRating;
		this.runningTime = dvdRunningTime;
	}

	public String getTitle() {

		return title;
	}

	public String getRating() {

		return rating;
	}

	public int getRunningTime() {

		return runningTime;
	}

	public void setTitle(String newTitle) {
		this.title = newTitle;

	}

	public void setRating(String newRating) {
		this.rating = newRating;

	}

	public void setRunningTime(int newRunningTime) {
		this.runningTime = newRunningTime;

	}

	@Override
	public String toString() {

		return title + "," + rating + "," + runningTime + "\n";
	}

	public String getImageFilePath() {
		// Array containing all the paths
	    String[] paths = {
	        "thumbnails/Patriot.jpg",
	        "thumbnails/Mary Poppins.jpg",
	        "thumbnails/How Green Was My Valley.jpg",
	        "thumbnails/A Hollywood Story.jpg",
	        "thumbnails/A tale of two cities.jpg",
	        "thumbnails/1776.jpg"
	    };
	    int randomIndex = (int) (Math.random() * paths.length);
		return paths[randomIndex];
	}

}
