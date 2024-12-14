package aoc2024j.day14;

import aoc2024j.day14.Day14.State;
import utils.IO;

import javax.swing.*;
import java.awt.*;


public class JRobot extends JFrame {

    private final RobotPanel robotPanel;
    private final JLabel timeLabel;
    private int simulationTime = 0;

    private static final int cellDimension = 6;

    public JRobot(State state, int time) {
        this.simulationTime = time;
        this.robotPanel = new RobotPanel(state);
        this.timeLabel = new JLabel("Time: " + simulationTime);

        setTitle("Robot Viewer");
        setSize(cellDimension * state.space.width(),
                cellDimension * state.space.height());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        labelPanel.add(timeLabel);
        add(labelPanel, BorderLayout.NORTH);

        add(robotPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    public void repaint() {
        simulationTime++;
        timeLabel.setText("Time: " + simulationTime);
        robotPanel.repaint();
    }

    private static class RobotPanel extends JPanel {
        private final State state;

        public RobotPanel(State state) {
            this.state = state;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawRobots(g);
        }

        private void drawRobots(Graphics g) {
            g.setColor(Color.BLACK);
            for (var robot : state.robots) {
                int x = robot.x * cellDimension;
                int y = robot.y * cellDimension;
                int counter = state.counters[robot.y][robot.x];
                if (counter > 1) {
                    g.setColor(Color.RED);
                } else {
                    g.setColor(Color.GREEN);
                }
                g.fillRect(x, y, cellDimension, cellDimension);
            }
        }
    }

    public static void main(String[] args) {
        var data = IO.getResourceAsList("aoc2024/day14.txt");
        var space = new Day14.Space(101, 103);
        var state = space.parse(data);
        var initialTime = 0;
        state.run(initialTime);
        var viewer = new JRobot(state, initialTime);

        // Simulate the robot movements
        new Timer(1, e -> {
            state.run(1);
            viewer.repaint();
            if (state.treeLike()) {
                ((Timer) e.getSource()).stop();
            }
        }).start();
    }
}