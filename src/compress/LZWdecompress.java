package compress;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class LZWdecompress {
	
	private ArrayList<Integer> list = new ArrayList<Integer>();// ��߰�λ
	private int count = 0;// �±�
	private ArrayList<Integer> numlist = new ArrayList<>();// �����
	HashMap<String, Integer> hm = new HashMap<>();// �����
	HashMap<Integer, String> hm1 = new HashMap<>();// �����
	private String cw = "";
	private String pw = "";
	private String p = "";
	private String c = "";
	private int bianma = 256;

	public void decompress(String path, String path1) throws IOException {
		// ��ȡѹ���ļ�
		InputStream is = new FileInputStream(path);
		byte[] buffer = new byte[is.available()];
		is.read(buffer);
		is.close();// �ر���
//		String str = new String(buffer);
		// System.out.println(str);
		// ���߰�λ  �Ѹ߰�λ����ʾ�����ַ���List��
		for (int i = 0; i < buffer.length; i += 2) {
			int a = buffer[i];
			list.add(a);// �߰�λ����list�б���
		}
		
		for (int i = 1; i < buffer.length; i += 2) {// ���Ͱ�λ
			// System.out.println(list.get(count)+"---");
			if (buffer[i] == -1 && buffer[i - 1] == -1) {
				
				numlist.add(65535);
			} else {
				// System.out.println(i);
				if (list.get(count) > 0) {// ����Ͱ�λ��Ӧ�ĸ߰�λΪ1
					if (buffer[i] < 0) {
						int a = buffer[i] + 256 + 256 * list.get(count);
						// buffer[i]+=256+256*list.get(count);
						numlist.add(a);// ����numlist��

					} else {
						int a = buffer[i] + 256 * (list.get(count));
						// System.out.println(buffer[i]+" "+a + "+++");
						numlist.add(a);// ����numlist��

					}

				} else {// �߰�λΪ0
					// System.out.println(buffer[i]);
					numlist.add((int) buffer[i]);// ����numlist��
				}
				count++;
			}

		}
		// System.out.println(list.size()+" "+count+" "+numlist.size()+"�Ƚϴ�С"+"
		// "+buffer.length);
		// for(int i=0;i<numlist.size();i++){
		// System.out.println(numlist.get(i)+"p");
		// }
		/**
		 * ��0-255λ�ַ�����
		 */
		for (int i = 0; i < 256; i++) {
			char ch = (char) i;
			String st = ch + "";
			hm.put(st, i);
			hm1.put(i, st);
		}

		/**
		 * ����numlist�����е�Ԫ�ؿ�ʼ���±��룬����ļ�
		 */
		// ���������
		OutputStream os = new FileOutputStream(path1);
		// ����numlist
		for (int i = 0; i < numlist.size(); i++) {

			int n = numlist.get(i);
			if (hm.containsValue(n) == true) {// ���������д���
				cw = hm1.get(n);
				// System.out.println(cw+"*");
				if (pw != "") {
					os.write(cw.getBytes("gbk"));

					p = pw;
					c = cw.charAt(0) + "";// c=cw�ĵ�һ��
					// System.out.println(c+"&");
					hm.put(p + c, bianma);
					hm1.put(bianma, p + c);
					bianma++;
				} else {
					os.write(cw.getBytes("gbk"));// ��һ��
				}
			} else {// ������в�����
				p = pw;
				// System.out.println(pw+"-=");

				c = pw.charAt(0) + "";// c=pw�ĵ�һ��
				hm.put(p + c, bianma);
				hm1.put(bianma, p + c);
				bianma++;

				os.write((p + c).getBytes("gbk"));
				cw = p + c;

			}
			pw = cw;
			// System.out.println(bianma);
			// System.out.println(cw+"==");
			if (i == 65535) {
				System.out.println("����2");
				hm.clear();
				hm1.clear();
				for (int j = 0; j < 256; j++) {
					char ch = (char) j;
					String st = ch + "";
					hm.put(st, j);
					hm1.put(j, st);
				}
				bianma = 0;
				pw = "";

			}
		}
		// System.out.println(hm1.toString());
		os.close();
	}

}
