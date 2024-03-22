package com.homeworkfour;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.File;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

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

		String[] commands = { "Show All", "Add/Modify DVD", "Remove DVD", "Get DVDs By Rating",
				"Get Total Running Time", "Exit and Save" };

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
// //This Should work for Without the thumbnail. Thumbnail is incomplete should be completed In Future. 
//	private void doShowAll() {
//
//		List<Object> all = dvdlist.showAll();
//		DefaultListModel<Object> listModel = new DefaultListModel<>();
//		for (Object i : all) {
//			listModel.addElement((DVD) i);
//		}
////		String text = "The List of Movies";
////		JLabel label = new JLabel();
//		JList<Object> list = new JList<>(listModel);
//		JScrollPane scrollPane = new JScrollPane(list);
//		JPanel panel = new JPanel(new BorderLayout());
//		panel.add(scrollPane, BorderLayout.CENTER);
//		JOptionPane.showMessageDialog(null, panel, "DVD Collection", JOptionPane.INFORMATION_MESSAGE);
//	}
	
	private void doShowAll() {
		// Load all DVDs
		List<Object> all = dvdlist.showAll();

		// Create a panel to hold the thumbnail and the list
		JPanel panel = new JPanel(new BorderLayout());

		// Add thumbnail image
		JLabel thumbnailLabel = new JLabel();
		thumbnailLabel.setPreferredSize(new Dimension(100, 300)); // Set preferred size for the thumbnail
		panel.add(thumbnailLabel, BorderLayout.NORTH);

		// Create a list model and populate it with DVD objects
		DefaultListModel<Object> listModel = new DefaultListModel<>();
		for (Object i : all) {
			listModel.addElement((DVD) i);
		}

		// Create a JList with the populated list model
		JList<Object> list = new JList<>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		// Add a list selection listener to the JList
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					// Get the selected DVD
					DVD selectedDVD = (DVD) list.getSelectedValue();
					if (selectedDVD != null) {
						// Refresh the thumbnail image with the selected DVD's image
						refreshThumbnail(selectedDVD.getImageFilePath(), thumbnailLabel);
					}
				}
			}
		});

		// Add the JList to a scroll pane and then add the scroll pane to the panel
		JScrollPane scrollPane = new JScrollPane(list);
		panel.add(scrollPane, BorderLayout.CENTER);

		// Show the panel in a JOptionPane dialog
		JOptionPane.showMessageDialog(null, panel, "DVD Collection", JOptionPane.INFORMATION_MESSAGE);
	}

	private void refreshThumbnail(String imagePath, JLabel thumbnailLabel) {
		try {
			// Read the image file and create a BufferedImage
			File file = new File(imagePath);
			BufferedImage img = ImageIO.read(file);

			// Scale the image to fit the thumbnail label
			Image scaledImg = img.getScaledInstance(thumbnailLabel.getWidth(), thumbnailLabel.getHeight(),
					Image.SCALE_SMOOTH);

			// Set the scaled image as the icon for the thumbnail label
			thumbnailLabel.setIcon(new ImageIcon(scaledImg));
		} catch (IOException ex) {
			// Handle exceptions
			ex.printStackTrace();
		}
	}
	
	private void doAddOrModifyDVD() {
		while (true) {
			// Create an array to store the titles of DVDs
			List<Object> allDVDs = dvdlist.showAll();
			String[] titles = new String[allDVDs.size() + 1]; // Add 1 for the "Add New DVD" option
			for (int i = 0; i < allDVDs.size(); i++) {
				DVD dvd = (DVD) allDVDs.get(i);
				titles[i] = dvd.getTitle();
			}
			titles[titles.length - 1] = "Add New DVD";

			// Create a label to display the message
			JLabel messageLabel = new JLabel("Select a DVD title to modify or choose 'Add New DVD' to add a new DVD.");

			// Display a dialog with a combo box containing the list of DVD titles
			JComboBox<String> comboBox = new JComboBox<>(titles);
			comboBox.setEditable(true); // Allow user to enter a new title if desired

			// Create a panel to hold the message label and combo box
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(messageLabel, BorderLayout.NORTH);
			panel.add(comboBox, BorderLayout.CENTER);

			// Show the dialog
			JOptionPane.showMessageDialog(null, panel, "Select DVD", JOptionPane.QUESTION_MESSAGE);

			// Retrieve the selected title
			String selectedTitle = (String) comboBox.getSelectedItem();
			if (selectedTitle == null || selectedTitle.trim().isEmpty()) {
				return; // dialog was cancelled
			}
			selectedTitle = selectedTitle.toUpperCase();

			if (selectedTitle.equals("Add New DVD")) {
				// Prompt for the title of the new DVD
				selectedTitle = JOptionPane.showInputDialog(null, "Enter title for the new DVD", "New DVD Title",
						JOptionPane.QUESTION_MESSAGE);
				if (selectedTitle == null || selectedTitle.trim().isEmpty()) {
					return; // dialog was cancelled
				}
				selectedTitle = selectedTitle.toUpperCase();
			} else {
				// Prompt for the new rating
				String newRating = JOptionPane.showInputDialog(null, "Enter new rating for " + selectedTitle,
						"New DVD Rating", JOptionPane.QUESTION_MESSAGE);
				if (newRating == null || newRating.trim().isEmpty()) {
					return; // dialog was cancelled
				}
				newRating = newRating.toUpperCase();

				// Prompt for the new running time
				String newTime = JOptionPane.showInputDialog(null, "Enter new running time for " + selectedTitle,
						"New DVD Running Time", JOptionPane.QUESTION_MESSAGE);
				if (newTime == null || newTime.trim().isEmpty()) {
					return; // dialog was cancelled
				}

				// Modify the existing DVD
				dvdlist.addOrModifyDVD(selectedTitle, newRating, newTime);
			}

			// Display the updated collection For Debugging
			System.out.println("Adding/Modifying: " + selectedTitle);
			System.out.println(dvdlist);

			// Exit the loop if the operation was successful
			break;
		}
	}

	private void doRemoveDVD() {
		// Get the list of DVDs
		List<Object> all = dvdlist.showAll();
		DefaultListModel<Object> listModel = new DefaultListModel<>();

		for (Object i : all) {
			listModel.addElement((DVD) i);
		}

		// Create a label to display the message
		JLabel messageLabel = new JLabel("Select a DVD title to Delete or [X] to Cancel");

		// Create a panel to hold the message label and combo box
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(messageLabel, BorderLayout.NORTH);

		// Create a JList to display the DVDs
		JList<Object> list = new JList<>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Allow only single selection

		// Show the list of DVDs in a JOptionPane dialog
		// JOptionPane.showMessageDialog(null, new JScrollPane(list), "Select DVD to
		// Remove", JOptionPane.PLAIN_MESSAGE);
		panel.add(new JScrollPane(list));
		JOptionPane.showMessageDialog(null, panel, "Select DVD to Remove", JOptionPane.PLAIN_MESSAGE);

		// Get the selected DVD
		DVD selectedDVD = (DVD) list.getSelectedValue();

		if (selectedDVD != null) {
			// Remove the selected DVD
			dvdlist.removeDVD(selectedDVD.getTitle());

			// Display a message indicating the DVD has been removed
			JOptionPane.showMessageDialog(null, "DVD removed: " + selectedDVD.getTitle(), "DVD Removed",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void doGetDVDsByRating() {

		// Request the rating
		String rating = JOptionPane.showInputDialog("Enter rating");
		if (rating == null) {
			return; // dialog was cancelled
		}
		rating = rating.toUpperCase();

		List<Object> results = dvdlist.getDVDsByRating(rating);
		// Console for Testing Purpose
		System.out.println("DVDs with rating " + rating);
		System.out.println(results);

		// Display the DVDs with the specified rating in a GUI window

		DefaultListModel<Object> listModel = new DefaultListModel<>();
		for (Object i : results) {
			listModel.addElement((DVD) i);
		}

		JLabel label = new JLabel("DVDs with rating " + rating + ":");
		JList<Object> list = new JList<>(listModel);
		JScrollPane scrollPane = new JScrollPane(list);
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(scrollPane, BorderLayout.CENTER);
		JOptionPane.showMessageDialog(null, panel, "DVDs By Rating", JOptionPane.INFORMATION_MESSAGE);
	}

	private void doGetTotalRunningTime() {

		int total = dvdlist.getTotalRunningTime();
		System.out.println("Testing in Console, Total Running Time of DVDs: ");
		System.out.println(total);
		JOptionPane.showMessageDialog(null, "Total Running Time of DVDs in Minutes : " + total);

	}

	private void doSave() {
		if (dvdlist.isModified()) {
			dvdlist.save();
			JOptionPane.showMessageDialog(null, "Saved Successfully...");
		} else {
			JOptionPane.showMessageDialog(null, "No Changes to Save. Click OK to Exit");
		}
	}

}
