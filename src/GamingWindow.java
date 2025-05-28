import javax.swing.*;
import java.awt.*;

public class GamingWindow extends JPanel {
    private static final int blockSize = 45;

    public GamingWindow(int rows, int cols) {
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout()); // Center the grid panel

        Resources.loadResources();
        MapType mapType;

        if (rows == 17 && cols == 15) {
            mapType = MapType.SMALL;
        } else if (rows == 19 && cols == 17) {
            mapType = MapType.MEDIUM;
        } else {
            mapType = MapType.LARGE;
        }

        String[] currentBlockMap = Resources.loadMapType(mapType);
        Map.loadMapEntities(currentBlockMap, blockSize);

        JPanel gridPanel = new JPanel(new GridLayout(rows, cols));
        gridPanel.setPreferredSize(new Dimension(cols * blockSize, rows * blockSize));

        buildGridFromMap(currentBlockMap, gridPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(gridPanel, gbc);
    }

    private void buildGridFromMap(String[] currentBlockMap, JPanel gridPanel) {
        for (String row : currentBlockMap) {
            for (char ch : row.toCharArray()) {
                JPanel cell = new JPanel(new BorderLayout());
                cell.setPreferredSize(new Dimension(blockSize, blockSize));
                cell.setBackground(Color.BLACK);

                JLabel iconLabel = new JLabel();
                iconLabel.setHorizontalAlignment(JLabel.CENTER);
                iconLabel.setVerticalAlignment(JLabel.CENTER);

                switch (ch) {
                    case '#' -> iconLabel.setIcon(new ImageIcon(Resources.wallImage));
                    case '\\' -> iconLabel.setIcon(new ImageIcon(Resources.leftPortalImage));
                    case '/' -> iconLabel.setIcon(new ImageIcon(Resources.rightPortalImage));
                    case 'b' -> iconLabel.setIcon(new ImageIcon(Resources.blueGhostImage));
                    case 'o' -> iconLabel.setIcon(new ImageIcon(Resources.orangeGhostImage));
                    case 'p' -> iconLabel.setIcon(new ImageIcon(Resources.pinkGhostImage));
                    case 'r' -> iconLabel.setIcon(new ImageIcon(Resources.redGhostImage));
                    case 'I' -> iconLabel.setIcon(new ImageIcon(Resources.pacmanImage));
                    case ' ' -> {
                        JLabel dot = new JLabel();
                        dot.setOpaque(true);
                        dot.setBackground(Color.WHITE);
                        dot.setPreferredSize(new Dimension(4, 4));
                        JPanel dotPanel = new JPanel();
                        dotPanel.setBackground(Color.BLACK);
                        dotPanel.add(dot);
                        cell.add(dotPanel, BorderLayout.CENTER);
                    }
                }

                cell.add(iconLabel, BorderLayout.CENTER);
                gridPanel.add(cell);
            }
        }
    }
}