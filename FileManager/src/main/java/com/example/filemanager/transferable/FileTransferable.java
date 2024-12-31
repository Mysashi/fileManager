package com.example.filemanager.transferable;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;

public class FileTransferable implements Transferable {
    private final File file;

    public FileTransferable(File file) {
        this.file = file;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { DataFlavor.javaFileListFlavor };
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DataFlavor.javaFileListFlavor.equals(flavor);
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
        if (!isDataFlavorSupported(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        return java.util.Collections.singletonList(file);
    }
}