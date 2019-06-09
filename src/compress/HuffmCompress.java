package compress;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.LinkedList;
 
public class HuffmCompress {
	
	private int [] times = new int[256];
	private String [] HuffmCodes=new String[256];
	private LinkedList<HuffmNode> list = new LinkedList<HuffmNode>();
	//统计次数
	
	//初始化
	public HuffmCompress(){
		for (int i = 0; i < HuffmCodes.length; i++) {
			HuffmCodes[i]="";
		}
	}
	
	private void countTimes(String path) throws Exception{
		//构造文件输入流
		FileInputStream fis = new FileInputStream(path);
		//读取文件
		int value=fis.read();
		while(value!=-1){
			times[value]++;
			value=fis.read();
		}
		//关闭流
		fis.close();
	}
	
	//构造哈夫曼树
	private HuffmNode createTree(){
		//将次数作为权值构造森林
		for (int i = 0; i < times.length; i++) {
			if(times[i]!=0){
				HuffmNode node = new HuffmNode(times[i],i);
				//将构造好的节点加入到容器中的正确位置
				list.add(getIndex(node), node);
			}
		}
		
		//将森林（容器中的各个节点）构造成哈夫曼树
		while(list.size()>1) {
			//获取容器中第一个元素（权值最小的节点）
			HuffmNode firstNode =list.removeFirst();
			//获取中新的第一个元素，原来的第一个元素已经被移除了（权值次小的节点）
			HuffmNode secondNode =list.removeFirst();
			//将权值最小的两个节点构造成父节点
			HuffmNode fatherNode =
					new HuffmNode(firstNode.getData()+secondNode.getData(),-1);
			fatherNode.setLeft(firstNode);
			fatherNode.setRight(secondNode);
			//父节点加入到容器中的正确位置
			list.add(getIndex(fatherNode),fatherNode);
		}
		//返回整颗树的根节点
		return list.getFirst();
	}
	//利用前序遍历获取编码表
	private void getHuffmCode(HuffmNode root,String code){
		//往左走，哈夫曼编码加0
		if(root.getLeft()!=null){
			getHuffmCode(root.getLeft(),code+"0");
		}
		//往右走，哈夫曼编码加1
		if(root.getRight()!=null){
			getHuffmCode(root.getRight(),code+"1");
		}
		//如果是叶子节点，返回该叶子节点的哈夫曼编码
		if(root.getLeft()==null && root.getRight()==null){
//			System.out.println(root.getIndex()+"的编码为："+code);
			HuffmCodes[root.getIndex()]=code;
		}
	}
	
	//压缩文件
	public void compress(String path,String destpath) throws Exception{
		
		destpath += ".huf";
		//统计文件中0-255出现的次数
		countTimes(path);
		//构造哈夫曼树，并得到根节点
		HuffmNode root=createTree();
		//得到哈夫曼编码
		getHuffmCode(root, "");
		
		//构建文件输出流
		FileOutputStream fos = new FileOutputStream(destpath);
		FileInputStream fis = new FileInputStream(path);
		
		//读文件，并将对应的哈夫曼编码串接成字符串
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
		
		//统计文件中0-255出现的次数
		countTimes(path);
		//构造哈夫曼树，并得到根节点
		HuffmNode root=createTree();
		//得到哈夫曼编码
		getHuffmCode(root, "");
				
		int sum = 256;
		int sum0 = 0;
		
		FileInputStream fis = new FileInputStream(path);
		
		//读文件，并将对应的哈夫曼编码串接成字符串
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
	
	//插入元素位置的索引
	private int getIndex(HuffmNode node) {
		for (int i = 0; i < list.size(); i++) {
			if(node.getData()<=list.get(i).getData()){
				return i;
			}
		}
       return list.size();
	}
	
	//将字符串转换成整数
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
