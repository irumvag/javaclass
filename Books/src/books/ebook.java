/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package books;

/**
 *
 * @author Chairman
 */
public class ebook extends Books{
    private int filesize;
    public void setfilesize(int v)
    {
        this.filesize=v;
    }
    public static void main(String args[])
    {
        ebook obj=new ebook();
        obj.settitle("c++ book");
        obj.setauthor("deepseek");
        obj.setfilesize(200);
        obj.display();
    }
}
