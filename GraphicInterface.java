import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphicInterface extends JFrame {
    private Drawing canvas;
    private JButton resetBtn;
    private JTextArea logArea;
    private ButtonListener bl;

    // Culori din codul tău original
    private final Color COL_RED = new Color(34, 108, 159);
    private final Color COL_BG = new Color(236, 240, 241);
    private final Font FONT_BTN = new Font("Segoe UI", Font.BOLD, 13);

    public GraphicInterface() {
        super("Divide et Impera - Convex Hull"); // Titlu actualizat
        this.setLayout(new BorderLayout());

        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(COL_BG);
        this.setContentPane(mainContainer);
        bl = new ButtonListener();

        // --- Side Panel ---
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(Color.WHITE);
        sidePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        sidePanel.setPreferredSize(new Dimension(240, 600));

        JLabel title = new JLabel("Divide et Impera");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidePanel.add(title);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JLabel instr = new JLabel("Click on the canvas to add points");
        instr.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        instr.setMaximumSize(new Dimension(200, 80));
        instr.setAlignmentX(Component.LEFT_ALIGNMENT);
        JPanel instrPanel = new JPanel();
        instrPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0)); // FlowLayout aliniat Stânga
        instrPanel.setBackground(Color.WHITE); // Se asigură că fundalul se potrivește
        instrPanel.setMaximumSize(new Dimension(200, 80));
        instrPanel.add(instr);

        sidePanel.add(instrPanel);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));

        resetBtn = createButton("Reset Canvas", COL_RED);
        sidePanel.add(resetBtn);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // --- Logger ---
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        JScrollPane scroll = new JScrollPane(logArea);
        scroll.setPreferredSize(new Dimension(200, 300));
        sidePanel.add(new JLabel("Event Log:"));
        sidePanel.add(scroll);

        // --- Canvas ---
        canvas = new Drawing(logArea);
        JPanel canvasWrap = new JPanel(new GridBagLayout());
        canvasWrap.setBackground(COL_BG);
        canvasWrap.add(canvas);

        this.add(sidePanel, BorderLayout.WEST);
        this.add(canvasWrap, BorderLayout.CENTER);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(900, 700);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private JButton createButton(String txt, Color bg) {
        JButton b = new JButton(txt);
        b.setAlignmentX(Component.CENTER_ALIGNMENT);
        b.setMaximumSize(new Dimension(200, 35));
        b.setFont(FONT_BTN);
        b.setBackground(bg);
        b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.addActionListener(bl);
        return b;
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == resetBtn) {
                canvas.reset(); // Această metodă va curăța totul în cascadă
                logArea.append("---------------------\n");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GraphicInterface::new);
    }
}