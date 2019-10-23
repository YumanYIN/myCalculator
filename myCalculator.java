import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

class myException extends Exception{
    public myException(){
        super();
    }
    public myException(String message){
        super(message);
    }
}

public class myCalculator extends JFrame {

    private JTextField textField; // Input Text Field
    private String input;       //  result 结果
    private String operator; //operator 操作符

    public myCalculator(){
        input = "";
        operator = "";

        JPanel panel = new JPanel();
        textField = new JTextField(30);
        textField.setEditable(false); //文本框禁止输入
        textField.setHorizontalAlignment(JTextField.LEFT); //水平布局，左边
        textField.setPreferredSize(new Dimension(200, 30)); // 设置该组件的初始大小

        //将textField加入本JFrame中，布局为边界布局，位置为north
        this.add(textField, BorderLayout.NORTH);

        String[] name= {"7","8","9","+","4","5","6","-","1","2","3","*","0","C","=","/"};

        //将这个panel的布局设置为网格布局，有四行四列，行间距和列间距为1
        panel.setLayout(new GridLayout(4,4,1,1));

        //计算器界面布局
        for(int i=0; i<name.length; i++){
            //每个name都是一个Button按键
            JButton button = new JButton(name[i]);

            //设置按钮的时间监听
            button.addActionListener(new myActionListener());

            //将按钮加入到panel中
            panel.add(button);
        }

        //将panel加入到本JFrame中，布局为边界布局，位置为centre
        this.add(panel, BorderLayout.CENTER);
    }

    class myActionListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e){
            int cnt = 0;
            String actionCommand = e.getActionCommand(); //获取按钮上的字符串
            if(actionCommand.equals("+") || actionCommand.equals("-") || actionCommand.equals("*") || actionCommand.equals("/")){

                input += " " + actionCommand + " ";

            }else if(actionCommand.equals("C")){
                input = "";

            }else if(actionCommand.equals("=")){
                try {

                    input += " = " + calculate(input);
                }catch (myException me){
                    if(me.getMessage().equals("The dividend cannot be 0")) //被除数不能为0
                        input = me.getMessage();
                    else
                        input = me.getMessage();
                }
                textField.setText(input);
                input = "";
                cnt = 1;
            }
            else {
                input += actionCommand;     //按下数字
            }

            /**
             * 如果不按“=”按钮cnt一直未0，所以可以保证显示输入的数字和操作键
             */
            if(cnt == 0){
                textField.setText(input);
            }
        }
    }

    /**
     * 计算函数
     * @param input
     * @return
     * @throws myException
     */
    private String calculate(String input) throws myException{
        String[] comput = input.split(" ");
        Stack<Double> stack = new Stack<>();
        double m = Double.parseDouble(comput[0]);
        stack.push(m);

        for (int i=1; i<comput.length; i++){
            if(i%2 == 1){
                if(comput[i].equals("+")){
                    stack.push(Double.parseDouble(comput[i+1]));
                }
                if(comput[i].equals("-")){
                    stack.push(Double.parseDouble(comput[i-1]));
                }
                if(comput[i].equals("*")){  //将前一个数出栈做乘法再入栈
                    Double d = stack.peek();    //取栈顶元素
                    stack.pop();
                    stack.push(d * Double.parseDouble(comput[i+1]));
                }
                if(comput[i].equals("/")){  //将前一个数出栈做乘法再入栈
                    double help = Double.parseDouble(comput[i+1]);
                    if(help == 0){
                        throw new myException("The dividend cannot be 0");
                    }
                    double d = stack.peek();    //取栈顶元素
                    stack.pop();
                    stack.push(d / help);
                }
            }
        }
        double d = 0d;
        while (!stack.isEmpty()){   //求和
            d += stack.peek();
            stack.pop();
        }

        String result = String.valueOf(d);
        return result;
    }

    public static void main(String[] args){
        JFrame f = new myCalculator();
        f.setTitle("Calculator");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setBounds(400, 200, 500, 300);
        f.setVisible(true);
    }

}
