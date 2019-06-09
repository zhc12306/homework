package compress;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.LinkedList;
 
public class HuffmCompress {
	
	private int [] times = new int[256];
	private String [] HuffmCodes=new String[256];
	private LinkedList<HuffmNode> list = new LinkedList<HuffmNode>();
	//ͳ�ƴ���
	
	//��ʼ��
	public HuffmCompress(){
		for (int i = 0; i < HuffmCodes.length; i++) {
			HuffmCodes[i]="";
		}
	}
	
	private void countTimes(String path) throws Exception{
		//�����ļ�������
		FileInputStream fis = new FileInputStream(path);
		//��ȡ�ļ�
		int value=fis.read();
		while(value!=-1){
			times[value]++;
			value=fis.read();
		}
		//�ر���
		fis.close();
	}
	
	//�����������
	private HuffmNode createTree(){
		//��������ΪȨֵ����ɭ��
		for (int i = 0; i < times.length; i++) {
			if(times[i]!=0){
				HuffmNode node = new HuffmNode(times[i],i);
				//������õĽڵ���뵽�����е���ȷλ��
				list.add(getIndex(node), node);
			}
		}
		
		//��ɭ�֣������еĸ����ڵ㣩����ɹ�������
		while(list.size()>1) {
			//��ȡ�����е�һ��Ԫ�أ�Ȩֵ��С�Ľڵ㣩
			HuffmNode firstNode =list.removeFirst();
			//��ȡ���µĵ�һ��Ԫ�أ�ԭ���ĵ�һ��Ԫ���Ѿ����Ƴ��ˣ�Ȩֵ��С�Ľڵ㣩
			HuffmNode secondNode =list.removeFirst();
			//��Ȩֵ��С�������ڵ㹹��ɸ��ڵ�
			HuffmNode fatherNode =
					new HuffmNode(firstNode.getData()+secondNode.getData(),-1);
			fatherNode.setLeft(firstNode);
			fatherNode.setRight(secondNode);
			//���ڵ���뵽�����е���ȷλ��
			list.add(getIndex(fatherNode),fatherNode);
		}
		//�����������ĸ��ڵ�
		return list.getFirst();
	}
	//����ǰ�������ȡ�����
	private void getHuffmCode(HuffmNode root,String code){
		//�����ߣ������������0
		if(root.getLeft()!=null){
			getHuffmCode(root.getLeft(),code+"0");
		}
		//�����ߣ������������1
		if(root.getRight()!=null){
			getHuffmCode(root.getRight(),code+"1");
		}
		//�����Ҷ�ӽڵ㣬���ظ�Ҷ�ӽڵ�Ĺ���������
		if(root.getLeft()==null && root.getRight()==null){
//			System.out.println(root.getIndex()+"�ı���Ϊ��"+code);
			HuffmCodes[root.getIndex()]=code;
		}
	}
	
	//ѹ���ļ�
	public void compress(String path,String destpath) throws Exception{
		
		destpath += ".huf";
		//ͳ���ļ���0-255���ֵĴ���
		countTimes(path);
		//����������������õ����ڵ�
		HuffmNode root=createTree();
		//�õ�����������
		getHuffmCode(root, "");
		
		//�����ļ������
		FileOutputStream fos = new FileOutputStream(destpath);
		FileInputStream fis = new FileInputStream(path);
		
		//���ļ���������Ӧ�Ĺ��������봮�ӳ��ַ���
		int value=fis.read();
		String str = "";
		while(value!=-1){
			str+=HuffmCodes[value];
//			System.out.println((char)value+":"+str);
			value=fis.read();
		}
		fis.close();
		
		for ( int i = 0 ; i < HuffmCodes.length ; i++ ) {
			fos.write(HuffmCodes[i].length());
		}
		
		String sc = "";
		for (int i = 0 ; i < HuffmCodes.length;i++) {
			if (HuffmCodes[i].length()>0) {
				sc += HuffmCodes[i];
			}
		}
		String s="";
		while(sc.length()>8){
			s=sc.substring(0, 8);
			int b=changeStringToInt(s);
//			System.out.println(c);
			fos.write(b);
			fos.flush();
			sc=sc.substring(8);
		}
		
		int last1=8-sc.length();
		for (int i = 0; i <last1; i++) {
			sc+="0";
		}
		s=sc.substring(0, 8);
		int d=changeStringToInt(s);
		fos.write(d);
		
		while(str.length()>=8){
			s=str.substring(0, 8);
			int b=changeStringToInt(s);
			fos.write(b);
			fos.flush();
			str=str.substring(8);
		}
		
		int last2=8-str.length();
		for (int i = 0; i <last2; i++) {
			str+="0";
		}
		s=str.substring(0, 8);
		int d1=changeStringToInt(s);
		fos.write(d1);
		
		fos.write(last2);
		fos.flush();
		fos.close();
	}
	
	public Double calculate(String path) throws Exception{
		
		//ͳ���ļ���0-255���ֵĴ���
		countTimes(path);
		//����������������õ����ڵ�
		HuffmNode root=createTree();
		//�õ�����������
		getHuffmCode(root, "");
				
		int sum = 256;
		int sum0 = 0;
		
		FileInputStream fis = new FileInputStream(path);
		
		//���ļ���������Ӧ�Ĺ��������봮�ӳ��ַ���
		int value=fis.read();
		String str = "";
		while(value!=-1){
			str+=HuffmCodes[value];
//			System.out.println((char)value+":"+str);
			value=fis.read();
			sum0++;
		}
		fis.close();
//		System.out.println(str);
		sum += str.length()/8+1;
		
		String sc = "";
		for (int i = 0 ; i < HuffmCodes.length;i++) {
			if (HuffmCodes[i].length()>0) {
				sc += HuffmCodes[i];
			}
		}
		sum += sc.length()/8;
		if (sc.length()%8!=0) {
			sum++;
		}
		sum++;
		double rate = (sum0-sum)*100.0/sum0;
//		System.out.println(sum0);
//		System.out.println(sum);
		BigDecimal b = new BigDecimal(Double.toString(rate));
//        return b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
//		System.out.println(rate);
		return new Double(b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue());
	}
	
	//����Ԫ��λ�õ�����
	private int getIndex(HuffmNode node) {
		for (int i = 0; i < list.size(); i++) {
			if(node.getData()<=list.get(i).getData()){
				return i;
			}
		}
       return list.size();
	}
	
	//���ַ���ת��������
	private int changeStringToInt(String s){
		int v1=(s.charAt(0)-48)*128;
		int v2=(s.charAt(1)-48)*64;
		int v3=(s.charAt(2)-48)*32;
		int v4=(s.charAt(3)-48)*16;
		int v5=(s.charAt(4)-48)*8;
		int v6=(s.charAt(5)-48)*4;
		int v7=(s.charAt(6)-48)*2;
		int v8=(s.charAt(7)-48)*1;
		return v1+v2+v3+v4+v5+v6+v7+v8;
			
	}	
	
}
