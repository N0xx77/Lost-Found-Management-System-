// Source code is decompiled from a .class file using FernFlower decompiler (from Intellij IDEA).
public class App {
   public App() {
   }

   public static void main(String[] args) throws Exception {

      try {
         Class.forName("com.mysql.cj.jdbc.Driver");
         System.out.println("Driver Loaded Successfully!");
      } catch (ClassNotFoundException e) {
         System.out.println("Driver not found! Check your library settings.");
      }

   }
}
