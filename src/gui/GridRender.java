package gui;

import model.map.BlockType;
import util.Resources;
import javax.swing.*;
import java.awt.*;

public class GridRender {

    public static void renderBlockGrid(JLabel[][] cells, BlockType[][] logicMap) {
        for (int r = 0; r < logicMap.length; r++) {
            for (int c = 0; c < logicMap[0].length; c++) {
                Image image = Resources.getImageForBlockType(logicMap[r][c]);
                cells[r][c].setIcon(image != null ? new ImageIcon(image) : null);
            }
        }
    }

    public static JPanel createCell(JLabel label, int blockSize) {
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(blockSize, blockSize));

        JPanel cell = new JPanel(new BorderLayout());
        cell.setPreferredSize(new Dimension(blockSize, blockSize));
        cell.setBackground(Color.BLACK);
        cell.add(label, BorderLayout.CENTER);
        return cell;
    }
}
