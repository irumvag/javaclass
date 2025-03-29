/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package books;

/**
 *
 * @author Chairman
 */
public class Books {

    private String title;
    private String author;
    public void settitle(String title){
        this.title=title;
    }
    public void setauthor(String author){
        this.author=author;
    }
    public String gettitle(){
    return this.title;
    }
    public String getauthor(){
    return this.author;
    }
    public void display()
    {
        System.out.println("The title is"+title+" the author: "+author);
    }
    public static void main(String[] args) {
        
        // TODO code application logic here
    }
    
}
