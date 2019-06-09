package compress;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Main {
	
	public Main() {
		
	}
	public static void main (String [] args) throws IOException{
		
		JFrame main = new JFrame("��ӭʹ��ѹ�����");
		main.setBounds(0, 0, 600, 600);
		main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		main.setBackground(Color.BLACK);
		
		JTextField t1 = new JTextField("����Դ��ַ");
		t1.setBounds(10, 10, 560, 100);
		t1.setBackground(Color.white);
//		t1.setEditable(false);
		main.add(t1);
		
		JTextField t2 = new JTextField("����Ŀ�ĵ�ַ");
		t2.setBounds(10, 120, 560, 100);
		t2.setBackground(Color.white);
//		t1.setEditable(false);
		main.add(t2);
		
		JButton one = new JButton("׼��ѹ��");
		one.setBounds(200, 240, 180,50);
		one.setBackground(Color.LIGHT_GRAY);
		one.setBorder(BorderFactory.createRaisedBevelBorder());
		one.setFont(new java.awt.Font("�����п�", 3, 30));
		main.add(one);
		
		JButton two = new JButton("׼����ѹ");
		two.setBounds(200, 240, 180,50);
		two.setBackground(Color.LIGHT_GRAY);
		two.setBorder(BorderFactory.createRaisedBevelBorder());
		two.setFont(new java.awt.Font("�����п�", 3, 30));
		main.add(two);
		two.setVisible(false);
		
		JLabel jl1 = new JLabel("������ѹ��");
		jl1.setBounds(20,310,200,50);
		jl1.setBackground(Color.LIGHT_GRAY);
		jl1.setFont(new java.awt.Font("�����п�",3,20));
		main.add(jl1);
		
		JButton jb1 = new JButton("ѹ��");
		jb1.setBounds(240, 310, 100,50);
		jb1.setBackground(Color.LIGHT_GRAY);
		jb1.setBorder(BorderFactory.createRaisedBevelBorder());
		jb1.setFont(new java.awt.Font("�����п�", 3, 30));
		main.add(jb1);
		
		JLabel jl11 = new JLabel("�ҵ�ѹ���ȸ�!ѡ��!");
		jl11.setBounds(370,310,200,50);
		jl11.setBackground(Color.LIGHT_GRAY);
		jl11.setFont(new java.awt.Font("�����п�",3,20));
		jl11.setVisible(false);
		main.add(jl11);
		
		JLabel jl2 = new JLabel("LZWѹ��");
		jl2.setBounds(20,370,200,50);
		jl2.setBackground(Color.LIGHT_GRAY);
		jl2.setFont(new java.awt.Font("�����п�",3,20));
		main.add(jl2);
		
		JButton jb2 = new JButton("ѹ��");
		jb2.setBounds(240, 370, 100,50);
		jb2.setBackground(Color.LIGHT_GRAY);
		jb2.setBorder(BorderFactory.createRaisedBevelBorder());
		jb2.setFont(new java.awt.Font("�����п�", 3, 30));
		main.add(jb2);
		
		JLabel jl21 = new JLabel("�ҵ�ѹ���ȸ�!ѡ��!");
		jl21.setBounds(370,370,200,50);
		jl21.setBackground(Color.LIGHT_GRAY);
		jl21.setFont(new java.awt.Font("�����п�",3,20));
		jl21.setVisible(false);
		main.add(jl21);
		
		JButton jb3 = new JButton("��ʵ��������ѹ�ļ���");
		jb3.setBounds(90, 450, 400,50);
		jb3.setBackground(Color.LIGHT_GRAY);
		jb3.setBorder(BorderFactory.createRaisedBevelBorder());
		jb3.setFont(new java.awt.Font("�����п�", 3, 30));
		main.add(jb3);
		
		one.addActionListener(new ActionListener(){
//			@Override
			public void actionPerformed(ActionEvent e) {
				jb3.setVisible(false);
				String path = t1.getText();
				String destpath = t2.getText();
				HuffmCompress huffmc = new HuffmCompress();
				LZWcompress lzwc = new LZWcompress();
				Double rate1 = new Double(0);
				Double rate2 = new Double(0);
				boolean b = true;
				try {
					rate1 = huffmc.calculate(path);
					rate2 = lzwc.calculate(path);
				} catch (Exception e1) {
					b = false;
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if (b) {
					jl1.setText("������ѹ����" + rate1.toString()+"%");
					jl2.setText("LZWѹ����" + rate2.toString()+"%");
					jl11.setVisible(rate1>=rate2);
					jl21.setVisible(rate1<rate2);
					
					jb1.addActionListener(new ActionListener(){
//						@Override
						public void actionPerformed(ActionEvent e) {
							boolean b = true;
							try {
								huffmc.compress(path, destpath);
							} catch (Exception e1) {
								b = false;
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (b) {
								jl11.setText("ѹ����ɣ�");
								jl11.setVisible(true);
								jl21.setVisible(false);
							}
							else {
								WrongFrame wj = new WrongFrame();
								wj.setVisible(true);
							}
						}
					});
					
					jb2.addActionListener(new ActionListener(){
//						@Override
						public void actionPerformed(ActionEvent e) {
							boolean b = true;
							try {
								lzwc.compress(path, destpath);
							} catch (Exception e1) {
								b = false;
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if (b) {
								jl21.setText("ѹ����ɣ�");
								jl11.setVisible(false);
								jl21.setVisible(true);
							}
							else {
								WrongFrame wj = new WrongFrame();
								wj.setVisible(true);
							}
						}
					});
				}
				else {
					WrongFrame wj = new WrongFrame();
					wj.setVisible(true);
				}
			}
		});
		
		jb3.addActionListener(new ActionListener() {
		
			public void actionPerformed(ActionEvent e) {
				one.setVisible(false);
				two.setVisible(true);
				jl1.setText("��������ѹ");
				jb1.setText("��ѹ");
				jl11.setText("ѡ�ҽ�ѹ��");
				jl2.setText("LZW��ѹ");
				jb2.setText("��ѹ");
				jl21.setText("ѡ�ҽ�ѹ��");
				jl1.setVisible(false);
				jb1.setVisible(false);
				jl11.setVisible(false);
				jl2.setVisible(false);
				jb2.setVisible(false);
				jl21.setVisible(false);
				jb3.setVisible(false);
				
				two.addActionListener(new ActionListener(){
//					@Override
					public void actionPerformed(ActionEvent e1) {
						String path = t1.getText();
						String destpath = t2.getText();
						String method = path.substring(path.length()-4, path.length());
						if ( method.equals(".huf") ) {
							jl1.setVisible(true);
							jb1.setVisible(true);
							jl2.setVisible(true);
							jb2.setVisible(true);
							jl11.setVisible(true);
							jl21.setVisible(false);
							HuffmDecompress dehuffmc = new HuffmDecompress();
							
							jb1.addActionListener(new ActionListener(){
//								@Override
								public void actionPerformed(ActionEvent e) {
									boolean b = true;
									try {
										dehuffmc.decompress(path, destpath);
									} catch (Exception e1) {
										b = false;
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									if (b) {
										jl11.setText("��ѹ��ɣ������գ�");
									}
									else {
										WrongFrame wj = new WrongFrame();
										wj.setVisible(true);
									}
								}
							});
						}
						else if (method.equals(".lzw")) {
							jl1.setVisible(true);
							jb1.setVisible(true);
							jl2.setVisible(true);
							jb2.setVisible(true);
							jl21.setVisible(true);
							jl11.setVisible(false);
							LZWdecompress delzwc = new LZWdecompress();
							
							jb2.addActionListener(new ActionListener(){
//								@Override
								public void actionPerformed(ActionEvent e) {
									boolean b = true;
									try {
										delzwc.decompress(path, destpath);
									} catch (Exception e1) {
										b = false;
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									if (b) {
										jl21.setText("��ѹ��ɣ������գ�");
									}
									else {
										WrongFrame wj = new WrongFrame();
										wj.setVisible(true);
									}
								}
							});
						}
						else {
							WrongFrame wj = new WrongFrame();
							wj.setVisible(true);
						}
					}
				});
			}
		});
		main.setLayout(null);
		main.setVisible(true);
	}
//	boolean b = up.UpdateStudent(num, name, change);
//	if(b){
//		JOptionPane.showMessageDialog(frame, "���³ɹ�","��ʾ",JOptionPane.INFORMATION_MESSAGE);
//	} 
//	else {
//		JOptionPane.showMessageDialog(frame, "����ʧ��","��ʾ",JOptionPane.INFORMATION_MESSAGE);
//	}

}

class WrongFrame extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel jl1 = new JLabel("��������");
	JLabel jl2 = new JLabel("�����أ�");
	
	public WrongFrame() {
		setLayout( null );
		getContentPane().add( jl1 );
		jl1.setBounds(100,20,200,50);
		jl1.setBackground(Color.LIGHT_GRAY);
		jl1.setFont(new java.awt.Font("�����п�",3,40));
		getContentPane().add( jl2 );
		jl2.setBounds(100,80,200,50);
		jl2.setBackground(Color.LIGHT_GRAY);
//		jl2.setBorder(BorderFactory.createRaisedBevelBorder());
		jl2.setFont(new java.awt.Font("�����п�",3,40));
		setBounds(100, 240, 400,200);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setVisible(true);
	}
}
