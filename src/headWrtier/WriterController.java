package headWrtier;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class WriterController implements Initializable {

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


    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }


    //At school
    //File homedir = new File("H:/var/gist/8100/mod1/");
    //At local
    File homedir = new File("D:\\Projects");


    public void OpenFile() {
        //opens up the file chooser window
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open img file");
        fileChooser.setInitialDirectory(homedir);
        File input = fileChooser.showOpenDialog(pane.getScene().getWindow());
        //wipes input and output text space
        tfBandSize.setText("");
        tfRecLength.setText("");
        tfHeadLength.setText("");
        //if invalid file selected
        if (input != null) {
            tfPath.setText(String.valueOf(input));
        } else {
            tfPath.setText("Invalid File Selected");
        }
    }

    public void WriteFile() throws IOException {


        //Save output file directory
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.setInitialDirectory(homedir);
        File outputPath = fileChooser.showSaveDialog(pane.getScene().getWindow());


        //sets variable values
        File inputFile = new File(tfPath.getText());
        int recordLength = Integer.parseInt(tfRecLength.getText());
        int headerSize = Integer.parseInt(tfHeadLength.getText());
        int bandSize = Integer.parseInt(tfBandSize.getText());

        //header values information
        int sun_magic = 0x59a66a95;    //magic number
        int sun_rows = recordLength;          //num of rows in image
        int sun_cols = headerSize + bandSize;          //num of cols in image
        int sun_depth = 8;    //pixelbits(1,8 or 24
        int sun_length = sun_rows * sun_cols;//length of data
        int sun_type = 0;              //type of file
        int sun_maptype = 0;       //type of colormap
        int sun_maplength = 0;   //colourmap length bytes)
        int[] sun_values = new int[]{sun_magic, sun_rows, sun_cols, sun_depth, sun_length, sun_type, sun_maptype, sun_maplength};

        int headerNumber = 0;
        ArrayList<byte[]> headerColumn = new ArrayList<byte[]>();


        DataInputStream d_is = new DataInputStream(new FileInputStream(inputFile));

        byte[] headerColumnArray = new byte[recordLength];

        while (d_is.available() > 0) {
            //fills entire header to array


            //System.out.print("C"+headerColumn.size()+"\n");
            for (int i = 0; i < headerSize; i++) {
                d_is.read(headerColumnArray);
                headerColumn.add(headerColumnArray);
                // System.out.print("A"+headerColumn.size()+"\n");
                headerColumnArray = new byte[recordLength];
            }


            //System.out.print("D"+headerFull.size()+"\n");
            d_is.skipBytes(recordLength * bandSize);
        }


        DataOutputStream d_os = new DataOutputStream(new FileOutputStream(outputPath));
        d_is.close();
        d_is = new DataInputStream(new FileInputStream(inputFile));


        while (d_is.available() > 0) {


            for (int i = 0; i < headerSize; i++) {
                headerColumnArray = headerColumn.get(i);
                byte[] sunBytes = ByteBuffer.allocate(recordLength).putInt(sun_values[i]).array();
                int replaceEnd = sunBytes.length;


                byte[] toWrite = new byte[headerColumnArray.length - replaceEnd + sunBytes.length];

                System.arraycopy(headerColumnArray, 0, toWrite, 0, 0);
                System.arraycopy(sunBytes, 0, toWrite, 0, sunBytes.length);
                System.arraycopy(headerColumnArray, replaceEnd, toWrite, sunBytes.length, headerColumnArray.length - replaceEnd);

                d_os.write(toWrite);

            }
            byte[] imgArray = new byte[recordLength * bandSize];
            d_is.read(imgArray);
            d_os.write(imgArray);

        }
        d_is.close();
        d_os.close();
    }

}
