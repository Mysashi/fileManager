package com.example.filemanager.handlers;

import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;

import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import javafx.scene.control.ListView;
import javafx.scene.text.Text;

public class DragHandler{
   public void dragOut(ListView<String> listView) {
       JList<String> jlist = new JList<String>((ListModel) listView.getItems());
       System.out.println("HELL YEAH");
       System.out.println(jlist);

   }
}

