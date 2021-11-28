package odd_even_game;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import java.awt.SystemColor;

public class MemberInfoFrame {

   private JFrame frame;

   /**
    * Launch the application.
    */
   public static void main(final String oppID, final int win, final int lose) {
      EventQueue.invokeLater(new Runnable() {
         public void run() {
            try {
               MemberInfoFrame window = new MemberInfoFrame(oppID, win, lose);
               window.frame.setVisible(true);
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      });
   }

   /**
    * Create the application.
    */
   public MemberInfoFrame(String oppID, int win, int lose) {
      initialize(oppID, win, lose);
   }

   /**
    * Initialize the contents of the frame.
    */
   private void initialize(String oppID, int win, int lose) {
      
      frame = new JFrame();
      frame.setTitle(oppID + "님의 회원정보 조회");
      frame.setBounds(100, 100, 445, 310);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().setLayout(null);
      
      JLabel lblNewLabel = new JLabel(oppID + "님");
      lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
      lblNewLabel.setBackground(SystemColor.inactiveCaption);
      lblNewLabel.setFont(new Font("굴림", Font.ITALIC, 39));
      lblNewLabel.setBounds(10, 12, 419, 107);
      frame.getContentPane().add(lblNewLabel);
      
      JLabel lblNewLabel_1 = new JLabel(win + "승");
      lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
      lblNewLabel_1.setBackground(SystemColor.activeCaption);
      lblNewLabel_1.setFont(new Font("굴림", Font.ITALIC, 39));
      lblNewLabel_1.setBounds(10, 131, 200, 142);
      frame.getContentPane().add(lblNewLabel_1);
      
      JLabel lblNewLabel_2 = new JLabel(lose + "패");
      lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
      lblNewLabel_2.setBackground(SystemColor.activeCaption);
      lblNewLabel_2.setFont(new Font("굴림", Font.ITALIC, 39));
      lblNewLabel_2.setBounds(229, 131, 200, 142);
      frame.getContentPane().add(lblNewLabel_2);
   }
}