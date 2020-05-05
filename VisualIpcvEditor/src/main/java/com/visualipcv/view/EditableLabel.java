package com.visualipcv.view;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.SetChangeListener;
import javafx.css.PseudoClass;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class EditableLabel extends Pane {
    private TextField textField;
    private Label label;

    private boolean editableState = false;

    private final StringProperty text = new SimpleStringProperty("");
    private final BooleanProperty isEditable = new SimpleBooleanProperty(true);

    public EditableLabel() {
        this("");
    }

    public EditableLabel(String text) {
        this.text.setValue(text);

        textField = new TextField();
        label = new Label();

        getChildren().add(textField);
        getChildren().add(label);

        getStyleClass().setAll("editable-label");
        setFocusTraversable(false);
        editableState = false;

        Platform.runLater(this::updateVisibleText);

        getPseudoClassStates().addListener(new SetChangeListener<PseudoClass>() {
            @Override
            public void onChanged(Change<? extends PseudoClass> change) {
                if (change.getSet().contains(PseudoClass.getPseudoClass("editable"))) {
                    if ( !editableState ) {
                        editableState = true;
                        updateVisibleText();
                    }
                } else {
                    if ( editableState ) {
                        editableState = false;
                        updateVisibleText();
                    }
                }
            }
        });

        widthProperty().addListener( observable -> updateVisibleText() );
        textProperty().addListener( observable -> updateVisibleText() );

        setOnMouseClicked(this::onMouseClick);
        setOnKeyPressed(this::onKeyPressed);

        textField.focusedProperty().addListener((new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                onFocusChanged(newValue);
            }
        }));
    }

    public StringProperty textProperty() {
        return text;
    }

    public void setText(String editText) {
        this.text.setValue(editText);
    }

    public String getText() {
        return text.getValue();
    }

    public BooleanProperty editableProperty() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        this.isEditable.setValue(editable);
    }

    public boolean getEditable() {
        return this.isEditable.getValue();
    }

    @Override
    public void layoutChildren() {
        super.layoutChildren();

        label.setLayoutX(getLayoutX());
        label.setLayoutY(getLayoutY());
        label.setPrefWidth(getWidth());
        label.setPrefHeight(getHeight());

        textField.setLayoutX(getLayoutX());
        textField.setLayoutY(getLayoutY());
        textField.setPrefWidth(getWidth());
        textField.setPrefHeight(getHeight());
        textField.setPadding(label.getPadding());
    }

    private void updateVisibleText() {
        String text = getText();
        textField.setText(text);
        label.setText(calculateClipString(text));
        getChildren().clear();

        if(!editableState) {
            getChildren().add(label);
        } else {
            getChildren().add(textField);
        }
    }

    private String calculateClipString(String text) {
        if(text.isEmpty())
            return text;

        double labelWidth = getWidth();

        Text layoutText = new Text(text);
        layoutText.setFont(label.getFont());

        if ( layoutText.getBoundsInLocal().getWidth() < labelWidth ) {
            return text;
        } else {
            layoutText.setText(text + "...");
            while ( layoutText.getBoundsInLocal().getWidth() > labelWidth ) {
                if(text.isEmpty())
                    return "";

                text = text.substring(0, text.length() - 1);
                layoutText.setText(text + "...");
            }

            return text + "...";
        }
    }

    private void onKeyPressed(KeyEvent event) {
        switch(event.getCode()) {
            case ENTER:
                setText(textField.getText());
                exitEditableMode();
                break;
            case ESCAPE:
                exitEditableMode();
                break;
        }
    }

    private void onMouseClick(MouseEvent event) {
        if(event.getClickCount() == 2 && !editableState) {
            enterEditableMode();
        }
    }

    private void onFocusChanged(boolean value) {
        if(!value) {
            setText(textField.getText());
            exitEditableMode();
        }
    }

    public void enterEditableMode() {
        if(!getEditable())
            return;

        if(editableState)
            return;

        getChildren().remove(label);
        getChildren().add(textField);
        pseudoClassStateChanged(PseudoClass.getPseudoClass("editable"), true);
        requestLayout();
    }

    private void exitEditableMode() {
        if(!editableState)
            return;

        getChildren().remove(textField);
        getChildren().add(label);
        pseudoClassStateChanged(PseudoClass.getPseudoClass("editable"), false);
        requestLayout();
    }
}
