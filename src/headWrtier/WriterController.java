package headWrtier;
/**
 * ===================================================================================================================
 * File: Header Writer
 * Made by: Toby Zhang on Feb. 25, 2017
 * <p>
 * Purpose: Reads a given BSQ file, takes user input for sizes, outputs a new file with the header written
 * Dependencies: none noted.
 * <p>
 * Limitations: input must be a valid BSQ image file, must know exact details of sizes.
 * ====================================================================================================================
 */

//imports

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WriterController implements Initializable {

    //FX GUI hooks
    @FXML
    Pane pane;
    @FXML
    TextField tfPath;
    @FXML
    TextField tfRecLength;
    @FXML
    TextField tfHeadLength;
    @FXML
    TextField tfBandSize;
    @FXML
    Text txtWarning;
    @FXML
    Text txtImgCount;
    @FXML
    ChoiceBox cbImgCount;


    //sets default file paths
    //At school
    File homedir = new File("H:\\var\\gist\\8100\\mod3\\mod3\\");
    //At local
    //File homedir = new File("D:\\Projects\\mod3\\mod3");

    //On program start (sets default testing location and values)
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            tfPath.setText("D:\\Projects\\mod3\\mod3\\mod3data.img");
            tfRecLength.setText("512");
            tfHeadLength.setText("8");
            tfBandSize.setText("512");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void OpenFile() {
        try {
            //opens up the file chooser window
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open img file");
            fileChooser.setInitialDirectory(homedir);
            File input = fileChooser.showOpenDialog(pane.getScene().getWindow());
            //if invalid file selected, warning message appears
            if (input != null) {
                tfPath.setText(String.valueOf(input));
                txtWarning.setVisible(false);
                txtImgCount.setVisible(false);
            } else {
                tfPath.setText("Invalid File Selected");
                txtWarning.setVisible(true);
                txtWarning.setText("WARNING: FILE NOT FOUND");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void CountImages() throws IOException {

        File inputFile = new File(tfPath.getText());
        int recordLength = Integer.parseInt(tfRecLength.getText());
        int headerSize = Integer.parseInt(tfHeadLength.getText());
        int imgSize = Integer.parseInt(tfBandSize.getText());


        DataInputStream d_is = new DataInputStream(new FileInputStream(inputFile));
        int imgCount = 0;

        while (d_is.available() > 0) {
            d_is.skipBytes(recordLength * (headerSize + imgSize));
            //reads the entire image
            imgCount++;
        }

        txtImgCount.setText("Total Images found: " + imgCount);
        txtImgCount.setVisible(true);
        String[] imgNumbers = new String[imgCount];
        for (int i = 0; i < imgCount; i++) {
            imgNumbers[i] = String.valueOf(i + 1);
        }
        cbImgCount.setItems(FXCollections.observableArrayList(imgNumbers));
    }

    public void WriteFile() throws IOException {

        try {
            //Save output file directory choose
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save file");
            fileChooser.setInitialDirectory(homedir);
            File outputPath = fileChooser.showSaveDialog(pane.getScene().getWindow());

            //sets variable values from input
            File inputFile = new File(tfPath.getText());
            int recordLength = Integer.parseInt(tfRecLength.getText());
            int headerSize = Integer.parseInt(tfHeadLength.getText());
            int bandSize = Integer.parseInt(tfBandSize.getText());

            //default header values information
            int sun_magic = 0x59a66a95;    //magic number
            int sun_rows = recordLength;          //num of rows in image
            int sun_cols = bandSize;          //num of cols in image
            int sun_depth = 8;    //pixelbits(1,8 or 24
            int sun_length = sun_rows * sun_cols;//length of data
            int sun_type = 0;              //type of file
            int sun_maptype = 0;       //type of colormap
            int sun_maplength = 0;   //colourmap length bytes)
            //saves all values as array
            int[] sun_values = new int[]{sun_magic, sun_rows, sun_cols, sun_depth, sun_length, sun_type, sun_maptype, sun_maplength};

            //a list of arrays (2d structure)
            //ArrayList<byte[]> headerColumn = new ArrayList<byte[]>();
            //array to store into the list
            //byte[] headerColumnArray = new byte[recordLength];
            //starts datainput stream
            DataInputStream d_is = new DataInputStream(new FileInputStream(inputFile));

            /**
             while (d_is.available() > 0) {
             //fills entire header to array,then stores the array into the list
             for (int i = 0; i < headerSize; i++) {
             d_is.read(headerColumnArray);
             headerColumn.add(headerColumnArray);
             // resets the array
             headerColumnArray = new byte[recordLength];
             }
             //skips to next header location
             d_is.skipBytes(recordLength * bandSize);
             } **/

            //starts data outputstream. closes the input stream and starts it back up to reset the stream position
            DataOutputStream d_os = new DataOutputStream(new FileOutputStream(outputPath));
            //d_is.close();
            //d_is = new DataInputStream(new FileInputStream(inputFile));

            //int imgCount = 0;
            //loops for as long as there is input -- REMOVED ONLY OUTPUT SPECIFIED IMAGE
            //while (d_is.available() > 0) {
            //loops however many columns the header contains
            /**
             for (int i = 0; i < headerSize; i++) {
             //sets array to list at position i
             headerColumnArray = headerColumn.get(i);
             //for each line of the header, reads position of the default values
             //puts the value into a byte array
             byte[] sunBytes = ByteBuffer.allocate(recordLength).putInt(sun_values[i]).array();
             //start replacing from the start of the column, ends at the end of however long the allocated bytes of the value is
             int replaceStart = 0; // inclusive
             int replaceEnd = sunBytes.length; // exclusive

             //creates a 3rd array to replace the allocated bytes with the empty bytes of the original header file
             byte[] toWrite = new byte[headerColumnArray.length - (replaceEnd - replaceStart) + sunBytes.length];
             //copies header array from position 0 to toWrite at position 0 for 0 length (saves all before the starting point of hte replace
             System.arraycopy(headerColumnArray, 0, toWrite, 0, replaceStart);
             //copies sunbytes from position 0 to toWrite at position 0 for the length of sunbytes.length (the repalcement part)
             System.arraycopy(sunBytes, 0, toWrite, replaceStart, sunBytes.length);
             //copies the array from headerColumnarray after the replacement part for however long is left (saves the rest of the unchanged part)
             System.arraycopy(headerColumnArray, replaceEnd, toWrite, replaceStart + sunBytes.length, headerColumnArray.length - replaceEnd);
             //writes the new array to an output file
             d_os.write(toWrite);
             } */
            //creates new img array variable to save the actual image of the files
            byte[] imgArray = new byte[recordLength * bandSize];

            //skips the input stream to the image file
            for (int i = 0; i < sun_values.length; i++) {
                d_os.writeInt(sun_values[i]);
            }
            //skips i number of images plus the header size
            d_is.skipBytes((recordLength * (bandSize + headerSize) * (Integer.parseInt(String.valueOf(cbImgCount.getValue())) - 1) + headerSize * recordLength));
            System.out.print((recordLength * (bandSize + headerSize) + "\n" + (Integer.parseInt(String.valueOf(cbImgCount.getValue())) - 1) + "\n" + headerSize * recordLength));
            //reads the entire image
            d_is.read(imgArray);
            //writes the same image to new file
            d_os.write(imgArray);


            //closes data input stream
            d_is.close();
            d_os.close();
            txtWarning.setVisible(true);
            txtWarning.setText("SUCCESS!");


        } catch (NumberFormatException e) {
            txtWarning.setVisible(true);
            txtWarning.setText("FAILED!");
            e.printStackTrace();

        } catch (IOException e) {
            txtWarning.setVisible(true);
            txtWarning.setText("FAILED!");
            e.printStackTrace();
        }
    }

}
