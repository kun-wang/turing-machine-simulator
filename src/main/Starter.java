/******************************************************************************
 * Name:        Wang Kun
 * ID:          DG1333031
 * Email:       nju.wangkun@gmail.com
 * Date:        2014-7-23
 ******************************************************************************/
package main;

import ui.MainGUI;

public class Starter
{
	public static void main(String[] args)
    {
        // use this one instead - com.apple.macos.useScreenMenuBar is deprecated
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        MainGUI program = new MainGUI("Turing Machine");
        program.run();
    }
}
