package loginfrom;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class LoginForm extends JFrame implements ActionListener {

    JPanel panel;
    JLabel lblname;
    JLabel lblpassword;
    JLabel lblmess;
    JTextField txtname;
    JPasswordField txtpassword;
    JButton btlogin;
    JPanel panelreg;
    JLabel lblnamereg;
    JLabel lblpasswordreg;
    JLabel lblmessreg;
    JTextField txtnamereg;
    JPasswordField txtpasswordreg;
    JButton btsubmit;

    LoginForm() {

        setDefaultCloseOperation(0);
        setUndecorated(true);
       // setLocationRelativeTo(true);
        
        setTitle("Diaz Cervera Brian Noe/Login and Regitred");
        setSize(660, 260);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Container content = getContentPane();
        JDesktopPane des = new JDesktopPane();

        //Login form
        JInternalFrame flog = new JInternalFrame();
        flog.setSize(300, 200);
        flog.setLocation(10, 2);
        
        flog.setTitle("Login ");
        lblname = new JLabel("User name:");
        lblpassword = new JLabel("Password:");
        lblmess = new JLabel("");
        btlogin = new JButton("Login");
        btlogin.addActionListener(this);
        txtname = new JTextField(20);
        txtpassword = new JPasswordField(20);
        panel = new JPanel();
        panel.add(lblname);
        panel.add(txtname);
        panel.add(lblpassword);
        panel.add(txtpassword);
        panel.add(btlogin);
        panel.add(lblmess);
        flog.add(panel);
        flog.setVisible(true);

        //Registration form
        JInternalFrame freg = new JInternalFrame();
        freg.setSize(300, 200);
        freg.setLocation(315, 2);
        freg.setTitle("Registration");
        lblnamereg = new JLabel("User Name:");
        lblpasswordreg = new JLabel("Password:");
        lblmessreg = new JLabel("");
        btsubmit = new JButton("Submit");
        btsubmit.addActionListener(this);
        btlogin.addActionListener(this);
        txtnamereg = new JTextField(20);
        txtpasswordreg = new JPasswordField(20);
        txtpasswordreg.addKeyListener(new KeyList());
        panelreg = new JPanel();
        panelreg.add(lblnamereg);
        panelreg.add(txtnamereg);
        panelreg.add(lblpasswordreg);
        panelreg.add(txtpasswordreg);
        panelreg.add(btsubmit);
        panelreg.add(lblmessreg);
        freg.add(panelreg);
        freg.setVisible(true);
        des.add(flog);
        des.add(freg);
        content.add(des, BorderLayout.CENTER);
        setVisible(true);
        txtname.requestFocus();

    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == btsubmit) {
            String uname = txtnamereg.getText();
            String passw = new String(txtpasswordreg.getPassword());
            if (!checkBlank(uname, passw, lblnamereg, lblpasswordreg)) {
                if (!checkExist("accounts.txt", uname)) {
                    passw = new String(encrypt(passw));
                    String accinfo = uname + "-" + passw;
                    saveToFile("accounts.txt", accinfo);
                }
            }

        } else if (e.getSource() == btlogin) {
            String uname = txtname.getText();
            String passw = new String(txtpassword.getPassword());
            if (!checkBlank(uname, passw, lblname, lblpassword)) {
                validateUser("accounts.txt", uname, passw);
            }
        }

    }

    public class KeyList extends KeyAdapter {

        public void keyPressed(KeyEvent ke) {
            String passw = new String(txtpasswordreg.getPassword());
            String mess = checkStrength(passw);
            showMess(mess + " password", lblpasswordreg);
        }

    }

    public boolean checkBlank(String name, String passw, JLabel namemess, JLabel passwmess) {
        boolean hasBlank = false;
        if (name.length() < 1) {
            showMess("User name is required.", namemess);
            hasBlank = true;
        }
        if (passw.length() < 1) {
            showMess("Password is required.", passwmess);
            hasBlank = true;
        }
        return hasBlank;

    }

    public void showMess(String mess, JLabel lbl) {
        lbl.setText(mess);
        lbl.setForeground(Color.RED);
    }

    public String checkStrength(String passw) {
        Pattern pat = Pattern.compile("([0-9][aA-zZ]|[aA-zZ][0-9])");
        Matcher mat = pat.matcher(passw);
        if (mat.find()) {
            if (passw.length() >= 8) {
                return "Strong";
            } else {
                return "Medium";
            }
        } else {
            return "Weak";
        }

    }

    public void reset(JLabel lblname, JLabel lblpassw) {
        lblname.setText("User Name:");
        lblname.setForeground(Color.BLACK);
        lblpassw.setText("Password:");
        lblpassw.setForeground(Color.BLACK);
    }

    public void validateUser(String filename, String name, String password) {
        FileReader fr;
        BufferedReader br;
        boolean valid = false;
        String accinfo;
        try {

            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            while ((accinfo = br.readLine()) != null) {

                if (check(accinfo, name, password)) {
                    valid = true;
                    JOptionPane.showMessageDialog(null, "Accediste");
                    System.exit(0);
                    break;
                    
                }

            }

            if (!valid) {
                showMess("Invalid login", lblmess);
            }
            reset(lblname, lblpassword);

            br.close();
            fr.close();
        } catch (Exception ie) {
            System.out.println("Error!");
        }

    }

    public boolean check(String accinfo, String name, String passw) {
        String[] info = accinfo.split("-");
        String uname = info[0];
        String pass = new String(decrypt(info[1]));
        if (uname.equals(name) && pass.equals(passw)) {
            return true;
        } else {
            return false;
        }

    }

    public boolean checkExist(String filename, String name) {
        FileReader fr;
        BufferedReader br;
        String accinfo;
        boolean exist = false;
        try {

            fr = new FileReader(filename);
            br = new BufferedReader(fr);
            while ((accinfo = br.readLine()) != null) {

                if (check(accinfo, name)) {
                    showMess("The account already exists.", lblmessreg);
                    exist = true;
                    break;
                }

            }

            br.close();
            fr.close();
        } catch (Exception ie) {
            System.out.println("Error!");
        }
        return exist;
    }

    public boolean check(String accinfo, String name) {
        String[] info = accinfo.split("-");
        String uname = info[0];
        if (uname.equals(name)) {
            return true;
        } else {
            return false;
        }

    }

    public void saveToFile(String filename, String text) {
        try {
            FileWriter fw = new FileWriter(filename, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(text);
            bw.newLine();
            bw.flush();
            bw.close();
            showMess("The account is created.", lblmessreg);
            reset(lblnamereg, lblpasswordreg);
        } catch (IOException ie) {
            System.out.println("Error in writing to file...");
        }
    }

    public byte[] encrypt(String passw) {
        byte[] sb = passw.getBytes();
        int i;
        for (i = 0; i < sb.length; i++) {
            sb[i] = (byte) (sb[i] + 5);
        }

        return (sb);
    }

    public byte[] decrypt(String passw) {

        byte[] sb = passw.getBytes();
        int i;
        for (i = 0; i < sb.length; i++) {
            sb[i] = (byte) (sb[i] - 5);
        }

        return (sb);
    }

}
