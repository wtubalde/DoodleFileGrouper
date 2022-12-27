
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileGrouper {
  public static void main(String[] args) {
    // Get the path to the folder
    String folderPath = "";

    // Create a map to store the groups of files
    Map<Long, List<File>> fileGroups = new HashMap<>();

    // Get a list of all the files in the folder
    File folder = new File(folderPath);
    File[] files = folder.listFiles();

    // Iterate over the files and group them by timestamp
    for (File file : files) {
      // Get the timestamp of the file
      long timestamp = file.lastModified();

      // Check if there's already a group for this timestamp or a timestamp within a minute
      List<File> group = fileGroups.get(timestamp);
      if (group == null) {
        for (Map.Entry<Long, List<File>> entry : fileGroups.entrySet()) {
          if (Math.abs(entry.getKey() - timestamp) <= 60000) {
            group = entry.getValue();
            break;
          }
        }
        if (group == null) {
          // If there isn't, create a new group
          group = new ArrayList<>();
          fileGroups.put(timestamp, group);
        }
      }

      // Add the file to the group
      group.add(file);
    }

    // Create a new folder for each group of files, named after the timestamp of the group in the "Month-Date-Time" format
    SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-HH-mm");
    for (Map.Entry<Long, List<File>> entry : fileGroups.entrySet()) {
      // Create the new folder
      String newFolderName = dateFormat.format(entry.getKey());
      File newFolder = new File(folder, newFolderName);
      newFolder.mkdir();

      // Move the files in the group to the new folder
      for (File file : entry.getValue()) {
        file.renameTo(new File(newFolder, file.getName()));
      }
    }
  }
}
