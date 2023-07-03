import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClassCreator {

    public static void main(String[] args) {
        //int n = Integer.parseInt(args[0]);
        int n = 10;
        for(int i =0;i<n;i++){
            String className = "Worker"+(i+1);
            String filePath = "src/" + className + ".java";
            String classCode = "public class " + className + " {\n" +
                    "    public static void main(String[] args) {\n" +
                    "        new Worker(302"+(5+i)+").openServer();\n" +
                    "    }\n" +
                    "}\n";

            File file = new File(filePath);
            file.getParentFile().mkdirs();

            try {
                FileWriter writer = new FileWriter(file);
                writer.write(classCode);
                writer.close();
                System.out.println("Class created successfully: " + file.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
