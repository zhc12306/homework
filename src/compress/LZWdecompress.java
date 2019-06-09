package compress;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class LZWdecompress {
	
	private ArrayList<Integer> list = new ArrayList<Integer>();// 存高八位
	private int count = 0;// 下标
	private ArrayList<Integer> numlist = new ArrayList<>();// 存编码
	HashMap<String, Integer> hm = new HashMap<>();// 编码表
	HashMap<Integer, String> hm1 = new HashMap<>();// 编码表
	private String cw = "";
	private String pw = "";
	private String p = "";
	private String c = "";
	private int bianma = 256;

	public void decompress(String path, String path1) throws IOException {
		// 读取压缩文件
		InputStream is = new FileInputStream(path);
		byte[] buffer = new byte[is.available()];
		is.read(buffer);
		is.close();// 关闭流
//		String str = new String(buffer);
		// System.out.println(str);
		// 读高八位  把高八位所表示的数字放入List中
		for (int i = 0; i < buffer.length; i += 2) {
			int a = buffer[i];
			list.add(a);// 高八位存入list列表中
		}
		
		for (int i = 1; i < buffer.length; i += 2) {// 读低八位
			// System.out.println(list.get(count)+"---");
			if (buffer[i] == -1 && buffer[i - 1] == -1) {
				
				numlist.add(65535);
			} else {
				// System.out.println(i);
				if (list.get(count) > 0) {// 如果低八位对应的高八位为1
					if (buffer[i] < 0) {
						int a = buffer[i] + 256 + 256 * list.get(count);
						// buffer[i]+=256+256*list.get(count);
						numlist.add(a);// 存入numlist中

					} else {
						int a = buffer[i] + 256 * (list.get(count));
						// System.out.println(buffer[i]+" "+a + "+++");
						numlist.add(a);// 存入numlist中

					}

				} else {// 高八位为0
					// System.out.println(buffer[i]);
					numlist.add((int) buffer[i]);// 存入numlist中
				}
				count++;
			}

		}
		// System.out.println(list.size()+" "+count+" "+numlist.size()+"比较大小"+"
		// "+buffer.length);
		// for(int i=0;i<numlist.size();i++){
		// System.out.println(numlist.get(i)+"p");
		// }
		/**
		 * 把0-255位字符编码
		 */
		for (int i = 0; i < 256; i++) {
			char ch = (char) i;
			String st = ch + "";
			hm.put(st, i);
			hm1.put(i, st);
		}

		/**
		 * 根据numlist队列中的元素开始重新编码，输出文件
		 */
		// 创建输出流
		OutputStream os = new FileOutputStream(path1);
		// 遍历numlist
		for (int i = 0; i < numlist.size(); i++) {

			int n = numlist.get(i);
			if (hm.containsValue(n) == true) {// 如果编码表中存在
				cw = hm1.get(n);
				// System.out.println(cw+"*");
				if (pw != "") {
					os.write(cw.getBytes("gbk"));

					p = pw;
					c = cw.charAt(0) + "";// c=cw的第一个
					// System.out.println(c+"&");
					hm.put(p + c, bianma);
					hm1.put(bianma, p + c);
					bianma++;
				} else {
					os.write(cw.getBytes("gbk"));// 第一个
				}
			} else {// 编码表中不存在
				p = pw;
				// System.out.println(pw+"-=");

				c = pw.charAt(0) + "";// c=pw的第一个
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
				System.out.println("重置2");
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
