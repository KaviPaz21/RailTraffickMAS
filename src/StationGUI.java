import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class StationGUI extends JFrame {
    private static StationGUI instance; // Singleton instance
    private JTable platformTable;
    private JTable trainTable;
    private DefaultTableModel platformTableModel;
    private DefaultTableModel trainTableModel;
    private Map<String, String> trainLocationMap; // Map to store the location of each train

    // Private constructor to prevent direct instantiation
    private StationGUI() {
        setTitle("Rail Traffic Control System");
        setLayout(new GridLayout(2, 1));

        // Initialize tables for platform and train data
        platformTableModel = new DefaultTableModel(new Object[]{"Station", "Platform", "Status", "Train"}, 0);
        platformTable = new JTable(platformTableModel);
        platformTable.setFillsViewportHeight(true);

        // Increase font size for table content
        platformTable.setFont(new Font("Serif", Font.PLAIN, 18)); // Content font size
        platformTable.setRowHeight(25); // Row height to match font size

        // Customize table header font
        platformTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 20)); // Header font size

        JScrollPane platformScrollPane = new JScrollPane(platformTable);


        trainTableModel = new DefaultTableModel(new Object[]{"Train", "Last Allocated Station", "Direction"}, 0);
        trainTable = new JTable(trainTableModel);
        trainTable.setFillsViewportHeight(true);

        // Increase font size for table content
        trainTable.setFont(new Font("Serif", Font.PLAIN, 16)); // Content font size
        trainTable.setRowHeight(25); // Row height to match font size

        // Customize table header font
        trainTable.getTableHeader().setFont(new Font("Serif", Font.BOLD, 18)); // Header font size


        JScrollPane trainScrollPane = new JScrollPane(trainTable);

        // Add scroll panes to the frame
        add(platformScrollPane);
        add(trainScrollPane);

        trainLocationMap = new HashMap<>();

        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Public method to get the singleton instance of StationGUI
    public static StationGUI getInstance() {
        if (instance == null) {
            instance = new StationGUI();
        }
        return instance;
    }

    // Method to update platform status (free or occupied)
    public void updatePlatformStatus(String stationName, int platform, boolean isOccupied, String trainName) {
        // Update the platform table model
        boolean platformExists = false;
        for (int i = 0; i < platformTableModel.getRowCount(); i++) {
            if (platformTableModel.getValueAt(i, 0).equals(stationName) &&
                    Integer.parseInt(platformTableModel.getValueAt(i, 1).toString()) == platform) {
                platformTableModel.setValueAt(isOccupied ? "Occupied" : "Free", i, 2);
                platformTableModel.setValueAt(isOccupied ? trainName : "", i, 3);
                platformTable.setRowSelectionInterval(i, i);
                platformExists = true;
                break;
            }
        }

        // If the platform does not exist, add a new row
        if (!platformExists) {
            platformTableModel.addRow(new Object[]{stationName, platform, isOccupied ? "Occupied" : "Free", isOccupied ? trainName : ""});
        }

        // Set the background color based on occupancy
        for (int i = 0; i < platformTableModel.getRowCount(); i++) {
            if (platformTableModel.getValueAt(i, 2).equals("Occupied")) {
                platformTable.setRowSelectionInterval(i, i);
                platformTable.setSelectionBackground(Color.RED); // Red for occupied
            } else {
                platformTable.setRowSelectionInterval(i, i);
                platformTable.setSelectionBackground(Color.GREEN); // Green for free
            }
        }
    }

    // Method to update the train's location and direction
    public void updateTrainLocation(String trainName, String currentStation, String direction) {
        boolean trainExists = false;
        for (int i = 0; i < trainTableModel.getRowCount(); i++) {
            if (trainTableModel.getValueAt(i, 0).equals(trainName)) {
                trainTableModel.setValueAt(currentStation, i, 1);
                trainTableModel.setValueAt(direction, i, 2);
                trainExists = true;
                break;
            }
        }

        if (!trainExists) {
            trainTableModel.addRow(new Object[]{trainName, currentStation, direction});
        }
    }
}
