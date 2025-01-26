package com.example.filemanager.utils;
import javafx.geometry.Insets;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;

public class ResizableScrollPane extends ScrollPane {

    private double mouseX;
    private double mouseY;
    private boolean dragging;
    private boolean isResizing = false;
    private int resizeBorderWidth = 10; // Width of resize border


    public ResizableScrollPane() {
        super();

        // Apply a border around the content so we can have a resizable edge
        setBorder(Border.EMPTY);
        this.setPadding(new Insets(resizeBorderWidth));

        // Add event filters for resizing
        addEventFilter(MouseEvent.MOUSE_MOVED, this::handleMouseMoved);
        addEventFilter(MouseEvent.MOUSE_PRESSED, this::handleMousePressed);
        addEventFilter(MouseEvent.MOUSE_DRAGGED, this::handleMouseDragged);
        addEventFilter(MouseEvent.MOUSE_RELEASED, this::handleMouseReleased);
    }

    private void handleMouseMoved(MouseEvent event) {
        if (isInResizeRegion(event.getX(), event.getY())) {
            setCursor(javafx.scene.Cursor.NW_RESIZE);
            this.isResizing = true;
        } else {
            setCursor(javafx.scene.Cursor.DEFAULT);
            this.isResizing = false;
        }
    }

    private void handleMousePressed(MouseEvent event) {
        if (isResizing) {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
            dragging = true;
            event.consume();
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        if (dragging) {
            double deltaX = event.getSceneX() - mouseX;
            double deltaY = event.getSceneY() - mouseY;

            double newWidth = getPrefWidth();
            double newHeight = getPrefHeight();

            // Resize only if dragging vertically or horizontally
            if (Math.abs(deltaY) > Math.abs(deltaX)) {
                newHeight = getPrefHeight() - deltaY;  // Invert deltaY for up/down behavior

                // Ensure height doesn't get too small
                if (newHeight < 20) {
                    newHeight = 20;
                }
                setPrefHeight(newHeight);

            }
            else if(Math.abs(deltaX) > Math.abs(deltaY)){

                newWidth = getPrefWidth() + deltaX;
                // Ensure width and height doesn't get too small
                if(newWidth < 20) {
                    newWidth = 20;
                }

                setPrefWidth(newWidth);
            }


            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
            event.consume();
        }
    }


    private void handleMouseReleased(MouseEvent event) {
        dragging = false;
        event.consume();
    }

    private boolean isInResizeRegion(double x, double y) {
        double width = getWidth();
        double height = getHeight();
        return (x >= (width - (resizeBorderWidth)) && y >= (height - (resizeBorderWidth)) ||
                x < resizeBorderWidth || y < resizeBorderWidth);
    }

}
