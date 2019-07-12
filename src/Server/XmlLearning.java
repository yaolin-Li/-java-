public class XmlLearning {

	public static void main(String[] args) throws Exception {
		//解析.xml文件我们主要用到java中的SAX解析工厂。
		//1、获取解析工厂
		SAXParserFactory factory=SAXParserFactory.newInstance();
		//2、从解析工厂获取解析器
		SAXParser saxParser =factory.newSAXParser();
		//3、编写处理器
		//4、加载文档 Document 注册处理器
		MyHandler handler=new MyHandler();
		//5、解析,""中可以写绝对路径，但是我把文件都放在同一目录下，所以就直接用了
		saxParser.parse(Thread.currentThread().getContextClassLoader()
		.getResourceAsStream("text/web.xml")
		,handler);

        /*
         *这里使用Thread.currentThread().getContextClassLoader()
		 *.getResourceAsStream("text/web.xml")的
         *具体原因是WEB程序，发布到Tomcat里面运行之后首先是执行
         *Tomcat org.apache.catalina.startup.Bootstrap类，这时候的类加载器是
         *ClassLoader.getSystemClassLoader()。如果在后面的WEB程序里使用
         *class.getClassLoader()，可能会导致和当前线程所运行的类加载器不一致。
         *
         *简单来说，使用Thread.currentThread().getContextClassLoader()时为了在
         *获取静态资源的时候，能够使用当前线程的类加载器。不用这个也可以，但是具体使用的是哪
         *个类加载器是开发者需要考虑的问题，毕竟java是多线程的。在这里我就不赘述了，有兴趣
         *的朋友可以去查一查其他的方法（查完回来告诉我一下呗 0 0）。
         *
         *
         *为什么要是用.getResourceAsStream("web.xml")呢？是因为相对路径这个概念
         *在本地eclipse里用着还行，但是到了服务器上就容易出问题。对于一般的项目，
         *写了相对路径之后会从项目的根目录开始往下找，但是到了服务器上，项目的根目录
         *就可能是服务器的某个路径。所以在服务器上想用相对路径，最好是用ClassLoader类的
         *getResource(String name),getResourceAsStream(String name)等方法
         *
         *综上所述，我们这里就使用
         *Thread.currentThread().getContextClassLoader().getResource("")
         *来得到当前的classpath的绝对路径的URI表示法。
         *         
         */
    }

}
class MyHandler extends DefaultHandler{
	@Override
	public void startDocument() throws SAXException {
		System.out.println("开始解析文档");
	}
	
	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		System.out.println("开始解析："+qName);
	}
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		String contents = new String(ch,start,length).trim();
		if(contents.length()>0) {
			System.out.println("内容为："+contents);			
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		System.out.println("解析结束："+qName);
	}
	@Override
	public void endDocument() throws SAXException {
		System.out.println("解析文档结束");
	}
}