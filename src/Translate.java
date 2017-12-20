import java.io.*;
import java.util.Scanner;
import java.util.regex.*;
import com.baidu.translate.demo.TransApi;
import org.apache.commons.lang3.StringEscapeUtils;


class Translate {
    private static String APP_ID = "";            //参看百度翻译api
    private static String SECURITY_KEY = "";      ////参看百度翻译api
    public static void main(String [] args) throws IOException {

        TransApi api = new TransApi(APP_ID, SECURITY_KEY);
        Scanner scanner=new Scanner(System.in);
        System.out.println("请输入appid:");
        APP_ID=scanner.next();
        System.out.println("请输入密 钥:");
        SECURITY_KEY=scanner.next();
        System.out.println("请输入要翻译html文件的绝对路径:");
        String path=scanner.next();
        File file = new File(path);
        try {
                String fo = file.getParent() + "/" + file.getName().replace(".htm", "-translated.htm");
                File fout = new File(fo);
                fout.createNewFile();
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader bis = new InputStreamReader(fis, "Windows-1252");
                int ch;
                String str = new String();
                StringBuffer str2 = new StringBuffer();
                StringBuffer tmp2 = new StringBuffer();
                while ((ch = bis.read()) != -1) {
                    str += (char) ch;
                }

                char[] tmp = str.toCharArray();
                str2.append(tmp);


                String regEx = "<script[^>]*?>[\\s\\S]*?<\\/script>|<style[^>]*?>[\\s\\S]*?<\\/style>|<[^>]+>|\\t|&.{1,5};"; //定义style的正则表达式
                String resRes = "\\\\u[0-9a-zA-Z]{4}+";
                Pattern resp = Pattern.compile(resRes, Pattern.CASE_INSENSITIVE);


                Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(str);


                int i, a, b;
                while (m.find()) {
                    a = m.start();
                    b = m.group().length();


                    for (i = a; i < a + b; i++) {
                        tmp[i] = '1';
                    }


                }
                i = 0;
                StringBuffer translate = new StringBuffer();
                a = 0;
                b = 0;


                i = 0;
                int n = tmp.length, str2i = 0, sumtranslate = 0, sumresult = 0;
                while (i != n - 2) {
                    if (tmp[i] != '1') {
                        a = i;
                        while (tmp[i] != '1') {
                            translate.append(tmp[i]);
                            i++;
                        }


                        Matcher resm = resp.matcher(api.getTransResult(translate.toString(), "en", "zh"));
                        StringBuffer result = new StringBuffer();
                        while (resm.find()) {
                            result.append(StringEscapeUtils.unescapeJava(resm.group()));
                        }


                        str2.replace(i - sumtranslate + sumresult - translate.length(), i - sumtranslate + sumresult, result.toString());
                        sumtranslate += translate.length();
                        sumresult += result.length();


                        translate.delete(0, translate.length());
                    } else i++;
                }

                StringBuffer str3 = new StringBuffer(str2.toString().replace("Windows-1252", "utf-8"));
                FileOutputStream fos = new FileOutputStream(fout);


                fos.write(str3.toString().getBytes("utf-8"));
                System.out.println(file.getName() + "翻译完成");


                fos.close();

                bis.close();
                fis.close();
            }
            catch (Exception e){
            }



    }
}
