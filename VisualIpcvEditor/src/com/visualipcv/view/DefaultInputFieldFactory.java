package com.visualipcv.view;

import com.visualipcv.core.DataType;
import com.visualipcv.utils.UIHighlight;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class DefaultInputFieldFactory extends InputFieldFactory {
    private static double STD_WIDTH = 180.0;
    private static double STD_HEIGHT = 25.0;
    private static double STD_MARGIN = 10.0;
    private static double STD_TEXT_PADDING = 3.0;

    private void setSize(Region control, double x, double y) {
        control.setPrefWidth(x);
        control.setPrefHeight(y);
        control.setMaxWidth(x);
        control.setMaxHeight(y);
        control.setMinWidth(x);
        control.setMinHeight(y);
    }

    private void setFont(TextInputControl control) {
        control.setFont(new Font(14.0));
    }

    private void setPadding(TextInputControl control) {
        control.setPadding(new Insets(STD_TEXT_PADDING));
    }

    @Override
    public Node create(InputFieldView fieldView, DataType type) {
        if(type == DataType.NUMBER) {
            TextField field = new TextField();
            setSize(field, STD_WIDTH, STD_HEIGHT);
            setFont(field);
            setPadding(field);

            field.setText(fieldView.getValueProperty().get().toString());
            field.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    try {
                        double value = Double.parseDouble(newValue);
                        fieldView.getValueProperty().set(value);
                        UIHighlight.removeHighlight(field);
                    } catch(Exception e) {
                        UIHighlight.highlight(field, Color.RED);
                    }
                }
            });
            return field;
        } else if(type == DataType.STRING) {
            TextField field = new TextField();
            setSize(field, STD_WIDTH, STD_HEIGHT);
            setFont(field);
            setPadding(field);

            field.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    fieldView.getValueProperty().set(field.getText());
                }
            });
            return field;
        } else if(type == DataType.VECTOR2) {
            TextField field1 = new TextField();
            setSize(field1, (STD_WIDTH - 10) / 2, STD_HEIGHT);
            setFont(field1);
            setPadding(field1);

            TextField field2 = new TextField();
            setSize(field2, (STD_WIDTH - 10) / 2, STD_HEIGHT);
            setFont(field2);
            setPadding(field2);
            field2.setLayoutX((STD_WIDTH - 10) / 2 + STD_MARGIN);

            field1.setText(((Double[])fieldView.getValueProperty().get())[0].toString());
            field2.setText(((Double[])fieldView.getValueProperty().get())[1].toString());

            field1.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    try {
                        double value = Double.parseDouble(newValue);
                        Double[] old = (Double[])fieldView.getValueProperty().get();
                        old[0] = value;
                        fieldView.getValueProperty().set(old);
                        UIHighlight.removeHighlight(field1);
                    } catch(Exception e) {
                        UIHighlight.highlight(field1, Color.RED);
                    }
                }
            });

            field2.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    try {
                        double value = Double.parseDouble(newValue);
                        Double[] old = (Double[])fieldView.getValueProperty().get();
                        old[1] = value;
                        fieldView.getValueProperty().set(old);
                        UIHighlight.removeHighlight(field2);
                    } catch(Exception e) {
                        UIHighlight.highlight(field2, Color.RED);
                    }
                }
            });

            Pane pane = new Pane();
            setSize(pane, STD_WIDTH, STD_HEIGHT);
            pane.getChildren().add(field1);
            pane.getChildren().add(field2);
            pane.setManaged(true);
            return pane;
        } else if(type == DataType.VECTOR3) {
            TextField field1 = new TextField();
            setSize(field1, (STD_WIDTH - 20) / 3, STD_HEIGHT);
            setFont(field1);
            setPadding(field1);

            TextField field2 = new TextField();
            setSize(field2, (STD_WIDTH - 20) / 3, STD_HEIGHT);
            setFont(field2);
            setPadding(field2);
            field2.setLayoutX((STD_WIDTH - 20) / 3 + STD_MARGIN);

            TextField field3 = new TextField();
            setSize(field3, (STD_WIDTH - 20) / 3, STD_HEIGHT);
            setFont(field3);
            setPadding(field3);
            field3.setLayoutX((STD_WIDTH - 20) / 3 * 2 + STD_MARGIN * 2);

            field1.setText(((Double[])fieldView.getValueProperty().get())[0].toString());
            field2.setText(((Double[])fieldView.getValueProperty().get())[1].toString());
            field3.setText(((Double[])fieldView.getValueProperty().get())[2].toString());

            field1.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    try {
                        double value = Double.parseDouble(newValue);
                        Double[] old = (Double[])fieldView.getValueProperty().get();
                        old[0] = value;
                        fieldView.getValueProperty().set(old);
                        UIHighlight.removeHighlight(field1);
                    } catch(Exception e) {
                        UIHighlight.highlight(field1, Color.RED);
                    }
                }
            });

            field2.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    try {
                        double value = Double.parseDouble(newValue);
                        Double[] old = (Double[])fieldView.getValueProperty().get();
                        old[1] = value;
                        fieldView.getValueProperty().set(old);
                        UIHighlight.removeHighlight(field2);
                    } catch(Exception e) {
                        UIHighlight.highlight(field2, Color.RED);
                    }
                }
            });

            field3.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    try {
                        double value = Double.parseDouble(newValue);
                        Double[] old = (Double[])fieldView.getValueProperty().get();
                        old[2] = value;
                        fieldView.getValueProperty().set(old);
                        UIHighlight.removeHighlight(field3);
                    } catch(Exception e) {
                        UIHighlight.highlight(field3, Color.RED);
                    }
                }
            });

            Pane pane = new Pane();
            setSize(pane, STD_WIDTH, STD_HEIGHT);
            pane.getChildren().add(field1);
            pane.getChildren().add(field2);
            pane.getChildren().add(field3);
            pane.setManaged(true);
            return pane;
        } else if(type == DataType.VECTOR4) {
            TextField field1 = new TextField();
            setSize(field1, (STD_WIDTH - 30) / 4, STD_HEIGHT);
            setFont(field1);
            setPadding(field1);

            TextField field2 = new TextField();
            setSize(field2, (STD_WIDTH - 30) / 4, STD_HEIGHT);
            setFont(field2);
            setPadding(field2);
            field2.setLayoutX((STD_WIDTH - 30) / 4 + STD_MARGIN);

            TextField field3 = new TextField();
            setSize(field3, (STD_WIDTH - 30) / 4, STD_HEIGHT);
            setFont(field3);
            setPadding(field3);
            field3.setLayoutX((STD_WIDTH - 30) / 4 * 2 + STD_MARGIN * 2);

            TextField field4 = new TextField();
            setSize(field4, (STD_WIDTH - 30) / 4, STD_HEIGHT);
            setFont(field4);
            setPadding(field4);
            field4.setLayoutX((STD_WIDTH - 30) / 4 * 3 + STD_MARGIN * 3);

            field1.setText(((Double[])fieldView.getValueProperty().get())[0].toString());
            field2.setText(((Double[])fieldView.getValueProperty().get())[1].toString());
            field3.setText(((Double[])fieldView.getValueProperty().get())[2].toString());
            field4.setText(((Double[])fieldView.getValueProperty().get())[3].toString());

            field1.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    try {
                        double value = Double.parseDouble(newValue);
                        Double[] old = (Double[])fieldView.getValueProperty().get();
                        old[0] = value;
                        fieldView.getValueProperty().set(old);
                        UIHighlight.removeHighlight(field1);
                    } catch(Exception e) {
                        UIHighlight.highlight(field1, Color.RED);
                    }
                }
            });

            field2.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    try {
                        double value = Double.parseDouble(newValue);
                        Double[] old = (Double[])fieldView.getValueProperty().get();
                        old[1] = value;
                        fieldView.getValueProperty().set(old);
                        UIHighlight.removeHighlight(field2);
                    } catch(Exception e) {
                        UIHighlight.highlight(field2, Color.RED);
                    }
                }
            });

            field3.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    try {
                        double value = Double.parseDouble(newValue);
                        Double[] old = (Double[])fieldView.getValueProperty().get();
                        old[2] = value;
                        fieldView.getValueProperty().set(old);
                        UIHighlight.removeHighlight(field3);
                    } catch(Exception e) {
                        UIHighlight.highlight(field3, Color.RED);
                    }
                }
            });

            field4.textProperty().addListener(new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    try {
                        double value = Double.parseDouble(newValue);
                        Double[] old = (Double[])fieldView.getValueProperty().get();
                        old[3] = value;
                        fieldView.getValueProperty().set(old);
                        UIHighlight.removeHighlight(field4);
                    } catch(Exception e) {
                        UIHighlight.highlight(field4, Color.RED);
                    }
                }
            });

            Pane pane = new Pane();
            setSize(pane, STD_WIDTH, STD_HEIGHT);
            pane.getChildren().add(field1);
            pane.getChildren().add(field2);
            pane.getChildren().add(field3);
            pane.getChildren().add(field4);
            pane.setManaged(true);
            return pane;
        }

        return null;
    }
}
