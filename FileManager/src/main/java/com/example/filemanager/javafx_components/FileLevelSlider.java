package com.example.filemanager.javafx_components;

import com.example.filemanager.handlers.generators.ListViewPanel;
import com.example.filemanager.manager.MyFileVisitor;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class FileLevelSlider {
    public Slider init(File file) {

        Slider slider = new Slider(0,1,1);
        slider.setValue(1);
        slider.setMin(0);
        slider.setMax(10);
        slider.setShowTickMarks(true);
        slider.setBlockIncrement(1);
        slider.setMinorTickCount(1);
        slider.setMajorTickUnit(10);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {

                HashMap<String, Long> hashMapOfSize = MyFileVisitor.getNthMap(MyFileVisitor.counterSize(SidebarNavigation.f, (int)slider.getValue()), 20);
                ArrayList<String> arrayList = new ArrayList<>();
                ListView<String> listView = SidebarNavigation.listView;
                listView.getItems().clear();
                hashMapOfSize.forEach((k,v) -> arrayList.add(k + "  " + MyFileVisitor.convertToHumanMeasure(v)));
                ObservableList<String> observList = FXCollections.observableArrayList(arrayList);
                listView.setItems(observList);
            }
        });
        return slider;
    }
}
