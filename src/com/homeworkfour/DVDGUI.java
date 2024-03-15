package com.homeworkfour;

import java.awt.BorderLayout;
import java.awt.Component;
import java.security.AllPermission;
import java.util.List;

import javax.swing.*;

/**
 * This class is an implementation of DVDUserInterface that uses JOptionPane to
 * display the menu of command choices.
 */

public class DVDGUI implements DVDUserInterface {

	private DVDCollection dvdlist;

	public DVDGUI(DVDCollection dl) {
		dvdlist = dl;
	}

	public void openFile() {

		boolean loaded = false;

		while (!loaded) {
			String filename = JOptionPane.showInputDialog("Enter file name: ");

			if (filename == null) {
				// User clicked on cancel, handled it accordingly
				return;
			}

			loaded = dvdlist.loadData(filename);

			if (!loaded) {
				JOptionPane.showMessageDialog(null, "Error: Double-check the file name.");
			}
		}

		processCommands();
	}

	public void processCommands() {

		String[] commands = { "Show All","Add/Modify DVD", "Remove DVD", "Get DVDs By Rating", "Get Total Running Time",
				"Exit and Save" };

		int choice;

		do {
			choice = JOptionPane.showOptionDialog(null, "Select a command", "DVD Collection",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, commands,
					commands[commands.length - 1]);

			switch (choice) {
			case 0:
				doShowAll();
				break;
			case 1:
				doAddOrModifyDVD();
				break;
			case 2:
				doRemoveDVD();
				break;
			case 3:
				doGetDVDsByRating();
				break;
			case 4:
				doGetTotalRunningTime();
				break;
			case 5:
				doSave();
				break;
			default: // do nothing
			}

		} while (choice != commands.length - 1);
		System.exit(0);
	}

	private void doShowAll() {
		
		List all = dvdlist.showAll();
		DefaultListModel<Object> listModel = new DefaultListModel<>();
		for (Object i:all) {
			listModel.addElement((DVD)i);
		}
		System.out.println(all.get(5));
//		System.out.println("Is this a Joke");
//		List showAll = dvdlist.showAll();
//		System.out.println(showAll.toString());
		String text = "The List of Movies";
		JLabel label = new JLabel(text);
		
	    JList<Object> list = new JList<>(listModel);
	    //System.out.println(listModel.toString());
		JScrollPane scrollPane = new JScrollPane(list);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);
        
        JOptionPane.showMessageDialog(null, panel, "DVD Collection", JOptionPane.INFORMATION_MESSAGE);
    }

	private void doAddOrModifyDVD() {

		// Request the title
		String title = JOptionPane.showInputDialog("Enter title");
		if (title == null) {
			return; // dialog was cancelled
		}
		title = title.toUpperCase();

		// Request the rating
		String rating = JOptionPane.showInputDialog("Enter rating for " + title);
		if (rating == null) {
			return; // dialog was cancelled
		}
		rating = rating.toUpperCase();

		// Request the running time
		String time = JOptionPane.showInputDialog("Enter running time for " + title);
		if (time == null) {
		}

		// Add or modify the DVD (assuming the rating and time are valid
		dvdlist.addOrModifyDVD(title, rating, time);

		// Display current collection to the console for debugging
		System.out.println("Adding/Modifying: " + title + "," + rating + "," + time);
		System.out.println(dvdlist);

	}

	private void doRemoveDVD() {

		// Request the title
		String title = JOptionPane.showInputDialog("Enter title");
		if (title == null) {
			return; // dialog was cancelled
		}
		title = title.toUpperCase();

		// Remove the matching DVD if found
		dvdlist.removeDVD(title);

		// Display current collection to the console for debugging
		System.out.println("Removing: " + title);
		System.out.println(dvdlist);

	}

	private void doGetDVDsByRating() {

		// Request the rating
		String rating = JOptionPane.showInputDialog("Enter rating");
		if (rating == null) {
			return; // dialog was cancelled
		}
		rating = rating.toUpperCase();

		String results = dvdlist.getDVDsByRating(rating);
		System.out.println("DVDs with rating " + rating);
		System.out.println(results);

	}

	private void doGetTotalRunningTime() {

		int total = dvdlist.getTotalRunningTime();
		System.out.println("Total Running Time of DVDs: ");
		System.out.println(total);

	}

	private void doSave() {

		dvdlist.save();

	}

}
