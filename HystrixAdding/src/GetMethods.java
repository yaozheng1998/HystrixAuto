import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Pattern;

/**
 * @Author YZ
 * @Date 28/06/2018
 */
public class GetMethods {
    /**
     * 得到controller文件中的所有方法名
     * @param f
     * @return
     * @throws IOException
     */
    public List<String> getMethodsFromOne(File f) throws IOException {
        List<String> result=new ArrayList<>();
        RandomAccessFile raf=new RandomAccessFile(f,"rw");
        String line=null;
        String lastLine=null;

        while((line=raf.readLine())!=null){
            lastLine=line;
            if(Pattern.matches(".*RequestMethod.*",line)){
                String methodLine=raf.readLine();
                if(!methodLine.contains("@HystrixCommand")) {
                    List<String> splits = splitMethodLine(methodLine);
                    String methodName = splits.get(2);
                    result.add(methodName);
                }
            }
        }
        System.out.println(result);
        return result;
    }

    /**
     * 已知controller文件名称和方法名称，返回修改过的代码片段
     * @param controllerFile
     * @param methodName
     * @return
     */
    public String getMethodContent(File controllerFile, String methodName){
        String content="";
        Stack s=new Stack();
        RandomAccessFile raf= null;
        try {
            raf = new RandomAccessFile(controllerFile,"rw");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line=null;
        String lastLine=null;

        int o=0;
        try {
            while((line=raf.readLine())!=null){
                lastLine=line;
                if(Pattern.matches(".*RequestMethod.*",line)){
                    content+=line;
                    content+='\n';

                    String methodLine=raf.readLine();
                    if(!methodLine.contains("@HystrixCommand")) {
                        List<String> splits = splitMethodLine(methodLine);
                        String method = splits.get(2);
                        if (method.equals(methodName)){
                            s.push("{");
                            content+=methodLine;
                            content+='\n';

                            while(!s.isEmpty()){
                                line=raf.readLine();
                                if (!line.equals("")) {

                                    if (line.contains("{")) {
                                        s.push('{');
                                    }
                                    if (line.contains("}") && !s.isEmpty()) {
                                        s.pop();
                                    }
                                    content += line;
                                    content += '\n';
                                }
                            }
                            o=1;
                        }
                    }
                }
//                break;
                if (o==1){
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(content);
        return content;
    }
    /**
     * 得到方法中的片段
     * @param methodLine
     * @return
     */
    public static List<String> splitMethodLine(String methodLine){
        List<String> result=new ArrayList<>();
        int space=0;
        for(int i=0;i<methodLine.length();i++){
            if(result.size()==0 && methodLine.charAt(i)==' '){
                space++;
            }
            if(methodLine.charAt(i)!=' '){
                String news=methodLine.charAt(i)+"";
                while(i<methodLine.length()-1 && methodLine.charAt(++i)!=' ' && methodLine.charAt(i)!='(' && methodLine.charAt(i)!=')'){
                    news+=methodLine.charAt(i);
                }
                result.add(news);
            }
        }
        result.add(space+"");
        return result;
    }
    public static void main(String[]args) throws IOException {
        GetMethods getMethods=new GetMethods();
        getMethods.getMethodContent(new File("TestController"),"add");
    }
}
