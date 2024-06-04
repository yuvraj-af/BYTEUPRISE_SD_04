import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI implements ActionListener {

    private static final JFrame frame = new JFrame();
    private static final JPanel contentPane = new JPanel(new BorderLayout());
    private static final JPanel top = new JPanel(new GridBagLayout());
    private static final JLabel titleLabel = new JLabel("Sudoku Solver");
    private static final JPanel mainPanel = new JPanel();
    private static final JPanel gridPanel = new JPanel(new GridLayout(9, 9));
    private static final JPanel bottomPanel = new JPanel();
    private static final JButton solveButton = new JButton("Solve");
    private final JTextField[][] cells = new JTextField[9][9];
    private static final int[][] puzzleOne = {
            {0, 0, 0, 0, 0, 0, 2, 0, 0},
            {0, 0, 0, 6, 0, 0, 0, 0, 3},
            {0, 7, 4, 0, 8, 0, 0, 0, 0},
            {0, 0, 3, 0, 0, 0, 0, 0, 2},
            {0, 8, 0, 0, 4, 0, 0, 1, 0},
            {6, 0, 0, 0, 0, 0, 5, 0, 0},
            {0, 0, 0, 0, 1, 0, 7, 8, 0},
            {5, 0, 0, 0, 0, 9, 0, 0, 0},
            {0, 0, 6, 0, 0, 0, 0, 0, 0}
    };

    public GUI() {
        initializeFrame();
        initializeContentPane();
        initializeTitle();
        initializeMain();
        initializeBottom();
    }

    private void initializeFrame() {
        frame.setTitle("Sudoku Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 700);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void initializeContentPane() {
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(contentPane);
    }

    private void initializeTitle() {

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        top.setPreferredSize(new Dimension(100, 100));
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        top.add(titleLabel, gbc);

        contentPane.add(top, BorderLayout.NORTH);
    }

    private void initializeMain() {

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new JTextField(3);
                cells[i][j].setHorizontalAlignment(JTextField.CENTER);
                cells[i][j].setFont(new Font("Arial", Font.BOLD, 18));
                cells[i][j].setPreferredSize(new Dimension(0, 50));

                if (puzzleOne[i][j] != 0) {
                    cells[i][j].setText(String.valueOf(puzzleOne[i][j]));
                    cells[i][j].setForeground(Color.BLACK);
                    cells[i][j].setEditable(false);
                } else {
                    cells[i][j].setText("");
                    cells[i][j].setEditable(true);
                }

                gridPanel.add(cells[i][j]);
            }
        }

        mainPanel.add(gridPanel);
        contentPane.add(mainPanel, BorderLayout.CENTER);

    }

    private void initializeBottom() {
        solveButton.setFont(new Font("Arial", Font.BOLD, 18));
        solveButton.addActionListener(this);
        bottomPanel.add(solveButton, BorderLayout.CENTER);
        contentPane.add(bottomPanel, BorderLayout.SOUTH);
    }

    private boolean solveSudoku(int[][] board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (board[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isValid(board, row, col, num)) {
                            board[row][col] = num;
                            if (solveSudoku(board)) {
                                return true;
                            } else {
                                board[row][col] = 0; // Reset and backtrack
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int[][] board, int row, int col, int num) {
        // Check if 'num' is not already placed in the current row, current column, and current 3x3 subgrid.
        for (int x = 0; x < 9; x++) {
            if (board[row][x] == num || board[x][col] == num) {
                return false;
            }
        }
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private void solveAndDisplay() {
        int[][] solvedBoard = new int[9][9];
        for (int i = 0; i < 9; i++) {
            System.arraycopy(puzzleOne[i], 0, solvedBoard[i], 0, 9);
        }

        if (solveSudoku(solvedBoard)) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    if (puzzleOne[i][j] == 0) {
                        cells[i][j].setText(String.valueOf(solvedBoard[i][j]));
                        cells[i][j].setBackground(Color.GREEN);
                        cells[i][j].setOpaque(true);
                    } else {
                        cells[i][j].setBackground(Color.WHITE);
                    }
                }
            }
        } else {
            JOptionPane.showMessageDialog(frame, "No solution exists.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == solveButton) {
            solveAndDisplay();
        }
    }
}
