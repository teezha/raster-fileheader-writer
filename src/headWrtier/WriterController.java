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
    File homedir = new File(System.getProperty("user.home"));


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
        int[] sun_values = new int[]{sun_magic, sun_rows,sun_cols,sun_depth,sun_length,sun_type,sun_maptype,sun_maplength};

        int headerNumber = 0;
        ArrayList<ArrayList<byte[]>> headerFull = new ArrayList<ArrayList<byte[]>>();
        ArrayList<byte[]> headerColumn = new ArrayList<byte[]>();


        byte[] headerColumnArray = new byte[recordLength];

        DataInputStream d_is = new DataInputStream(new FileInputStream(inputFile));

        while (d_is.available() > 0) {
            //fills entire header to array
            headerColumn.clear();
            for (int i = 0; i < headerSize; i++) {
                d_is.read(headerColumnArray);
                headerColumn.add(headerColumnArray);
                headerColumnArray = null;
                d_is.skipBytes(recordLength * bandSize);
            }
            headerFull.add(headerColumn);
            headerNumber++;
        }
        headerColumn.clear();


        for (int i = 0; i < headerNumber;i++) {
            headerColumn = headerFull.get(i);
            for (int k =0; k < headerSize; k++)
            {
                byte[] sunBytes = ByteBuffer.allocate(recordLength).putInt(sun_values[k]).array();
                int replaceEnd = sunBytes.length;
                headerColumnArray = headerColumn.get(k);

                byte[] toWrite = new byte[headerColumnArray.length-replaceEnd+sunBytes.length];

                System.arraycopy(headerColumnArray,0,toWrite,0,0);
                System.arraycopy(sunBytes,0,toWrite,0,sunBytes.length);
                System.arraycopy(headerColumnArray,replaceEnd,toWrite,sunBytes.length, headerColumnArray.length-replaceEnd);

                headerColumn.set(k, toWrite);

            }
            headerFull.set(i, headerColumn);
        }

        DataOutputStream d_os = new DataOutputStream(new FileOutputStream(outputPath));
        d_is.reset();


        while (d_is.available() > 0) {

            for (int i = 0; i < headerNumber; i++) {
                headerColumn = headerFull.get(i);
                for (int k =0; k < headerSize; k++)                {


                }
            }

        }


    }

}
