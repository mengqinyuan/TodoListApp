
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TodoApp extends JFrame {
    private JList<String> taskList;
    private DefaultListModel<String> listModel;
    private JTextField taskNameField;
    private Timer timer;

    // 定义内部类 Task
    private class Task {
        private String name;
        private Date dueDate;
        private String label;

        public Task(String name, String lablel, Date dueDate) {
            this.name = name;
            this.dueDate = dueDate;
            this.label = label;
        }

        public String getName() {
            return name;
        }

        public Date getDueDate() {
            return dueDate;
        }

        public String getLabel() {
            return label;
        }
    }

    public TodoApp() {
        setTitle("Todo List App");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 设置布局
        setLayout(new BorderLayout());

        // 创建帮助按钮
        JButton helpButton = new JButton("Help");
        helpButton.addActionListener(this::showHelpDialog);

        // 创建任务列表
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(taskList);
        add(scrollPane, BorderLayout.CENTER);

        // 创建添加任务面板
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        taskNameField = new JTextField(20);
        JButton addButton = new JButton("Add Task");
        addButton.addActionListener(this::addTask);
        addPanel.add(new JLabel("Task Name:"));
        addPanel.add(taskNameField);
        addPanel.add(addButton);

        // 添加帮助按钮到顶部
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        topPanel.add(helpButton);
        add(topPanel, BorderLayout.NORTH);

        add(addPanel, BorderLayout.SOUTH);

        // 创建关于我标签
        JLabel aboutMeLabel = new JLabel("About Me: https://www.github.com/mengqinyuan");
        add(aboutMeLabel, BorderLayout.NORTH);

        // 初始化计时器
        timer = new Timer();

        // 添加右键菜单
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Delete");
        JMenuItem editItemName = new JMenuItem("Edit Name");
        JMenuItem editItemLabel = new JMenuItem("Edit Label");
        deleteItem.addActionListener(e -> deleteTask());
        editItemName.addActionListener(e -> editTaskName());
        editItemLabel.addActionListener(e -> editLabel());
        popupMenu.add(deleteItem);
        popupMenu.add(editItemName);
        popupMenu.add(editItemLabel);
        taskList.setComponentPopupMenu(popupMenu);

        // 设置选中项高亮
        taskList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void addTask(ActionEvent e) {
        String taskName = taskNameField.getText();
        // 弹出对话框让用户输入完整的截止日期
        String fullDueDateStr = JOptionPane.showInputDialog(this, "Enter the due date (yyyy-MM-dd HH:mm):");
        String label = JOptionPane.showInputDialog(this, "Enter the label:");
        if (fullDueDateStr != null && !fullDueDateStr.trim().isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            try {
                Date dueDate = sdf.parse(fullDueDateStr);
                Task task = new Task(taskName, label, dueDate);
                listModel.addElement(task.getName()+" "+task.getLabel()+" "+sdf.format(dueDate));
                scheduleReminder(task);
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(this, "Invalid date or time format", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void scheduleReminder(Task task) {
        long delay = task.getDueDate().getTime() - System.currentTimeMillis();
        if (delay > 0) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    JOptionPane.showMessageDialog(TodoApp.this, "Reminder: " + task.getName(), "Task Due", JOptionPane.INFORMATION_MESSAGE);
                }
            }, delay);
        }
    }

    private void deleteTask() {
        int selectedIdx = taskList.getSelectedIndex();
        if (selectedIdx != -1) {
            listModel.remove(selectedIdx);
            // give a messagebox
            JOptionPane.showMessageDialog(this, "Task deleted successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void editTaskName() {
        int selectedIdx = taskList.getSelectedIndex();
        if (selectedIdx != -1) {
            String newName = JOptionPane.showInputDialog(this, "Enter new task name:", listModel.getElementAt(selectedIdx));
            if (newName != null && !newName.trim().isEmpty()) {
                listModel.set(selectedIdx, newName);
            }
        }
    }

    private void editLabel() {
        int selectedIdx = taskList.getSelectedIndex();
        if (selectedIdx != -1) {
            String newLabel = JOptionPane.showInputDialog(this, "Enter new label:", listModel.getElementAt(selectedIdx));
            if (newLabel != null && !newLabel.trim().isEmpty()) {
                listModel.set(selectedIdx, newLabel);
            }
        }
    }

    private void showHelpDialog(ActionEvent e) {
        String helpText = "Welcome to the Todo List App!\n\n" +
                "Features:\n" +
                "- Add a task by entering its name and clicking 'Add Task'.\n" +
                "- After adding a task, you will be prompted to enter the due date and time.\n" +
                "- Right-click on a task to edit or delete it.\n" +
                "- The app will remind you when a task is due.\n";
        JOptionPane.showMessageDialog(this, helpText, "Help", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            TodoApp app = new TodoApp();
            app.setVisible(true);
        });
    }
}