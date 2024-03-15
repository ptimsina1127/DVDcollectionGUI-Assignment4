package com.homeworkfour;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DVDCollection {
	private int numdvds;
	private DVD[] dvdArray;
	private String sourceName;
	private boolean modified;

	public DVDCollection() {
		// Initialize data fields
		numdvds = 0;
		dvdArray = new DVD[7];
	}

	public String toString() {
		// Return a string representation of the DVD collection
		String st = "numdvds = " + numdvds + "\n" + "dvdArray.length = " + dvdArray.length + "\n";
		for (int i = 0; i < numdvds; ++i) {
			st = st + "dvdArray[" + i + "] = " + dvdArray[i].getTitle() + "/" + dvdArray[i].getRating() + "/"+ dvdArray[i].getRunningTime() + "\n";
		}

		return st;
	}

	public void addOrModifyDVD(String title, String rating, String runningTime) {
		// Ensure array capacity is sufficient, expand if necessary
		if (numdvds == dvdArray.length) {
			expandArray();
		}

		// Check if the title exists, modify if found; otherwise, add a new DVD
		boolean titleSame = false;
		for (int i = 0; i < numdvds; ++i) {
			if (title.equals(dvdArray[i].getTitle())) {
				dvdArray[i].setRating(rating);
				dvdArray[i].setRunningTime(Integer.parseInt(runningTime));
				titleSame = true;
				modified = true;
			}
		}

		// If title doesn't exist, add a new DVD at the appropriate position
		if (!titleSame) {
			DVD dv = new DVD(title, rating, Integer.parseInt(runningTime));
			int insertIndex = findIndex(title);

			// Shift the array to insert the new DVD
			shiftArrayToInsert(dv, insertIndex);
			numdvds++;
		}
	}

	public void removeDVD(String title) {
		// Remove a DVD by shifting the array
		for (int i = 0; i < numdvds; ++i) {
			if (title.equals(dvdArray[i].getTitle())) {
				System.arraycopy(dvdArray, i + 1, dvdArray, i, numdvds - 1 - i);
				numdvds--;
				modified = true;
				break;
			}
		}
	}
	
	public List<Object> showAll() {
		//Show all DVDs
		List<Object> list = new ArrayList<>();
		for (int i = 0; i < numdvds; ++i) {
				list.add(dvdArray[i]);
			}
		return list;
		}

//	public String showAll() {
//		//Show all DVDs
//		String List = "";
//		for (int i = 0; i < numdvds; ++i) {
//				List+=(dvdArray[i].toString());
//			}
//		return List;
//		}

//	public List showAll() {
//		// Show all DVDs
//		List List = new ArrayList<List>();
//		for (int i = 0; i < numdvds; ++i) {
//			List.add(dvdArray[i].toString());
//		}
//		return List;
//	}

	public String getDVDsByRating(String rating) {
		// Get DVDs by a specified rating
		String matchedList = "";
		for (int i = 0; i < numdvds; ++i) {
			if (rating.equals(dvdArray[i].getRating())) {
				matchedList += (dvdArray[i].toString());
			}
		}
		return matchedList.toString();
	}

	public int getTotalRunningTime() {
		// Get the total running time of all DVDs in the collection
		int totalRunningTime = 0;
		for (int i = 0; i < numdvds; ++i) {
			totalRunningTime += dvdArray[i].getRunningTime();
		}
		return totalRunningTime;
	}

	public boolean loadData(String filename) {
		// Load DVD data from a file
		boolean loaded = true;
		sourceName = filename;
		File file = new File(filename);

		try (Scanner scanner = new Scanner(file)) {
			while (scanner.hasNextLine()) {
				String data = scanner.nextLine();
				String[] parts = data.split(",");
				addOrModifyDVD(parts[0], parts[1], parts[2]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			loaded = false;
		}

		return loaded;
	}

	public void save() {
		// Save DVD data to a file
		String data = "";
		try (FileWriter fileWriter = new FileWriter(sourceName)) {
			for (int i = 0; i < numdvds; ++i) {
				data += dvdArray[i].toString();
			}
			fileWriter.write(data.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Additional private helper methods go here:
	private void expandArray() {
		// Expand the array capacity when needed
		DVD[] newArray = new DVD[dvdArray.length * 2];
		System.arraycopy(dvdArray, 0, newArray, 0, dvdArray.length);
		dvdArray = newArray;
	}

	private void shiftArrayToInsert(DVD dvd, int insertIndex) {
		// Shift the array elements to make space for the new DVD at the specified index
		for (int i = numdvds; i > insertIndex; --i) {
			dvdArray[i] = dvdArray[i - 1];
		}
		dvdArray[insertIndex] = dvd;
	}

	private int findIndex(String title) {
		// Find the index where a new DVD should be inserted based on the title
		for (int i = 0; i < numdvds; ++i) {
			int comparisonResult = dvdArray[i].getTitle().compareToIgnoreCase(title);
			if (comparisonResult > 0) {
				return i;
			}
		}
		return numdvds;
	}
}
