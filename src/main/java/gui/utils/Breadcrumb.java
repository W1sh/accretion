package gui.utils;

import gui.controllers.SceneMediator;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Breadcrumb {

    private String name;
    private List<Node> nodes;

    public Breadcrumb(String name, Boolean disabled) {
        this.name = name;
        buildNodes(disabled);
    }

    private void buildNodes(Boolean disabled){
        this.nodes = new ArrayList<>();
        String separator = "/home/bruno/IdeaProjects/accretion/src/main/resources/gui/assets/arrow.png";
        Button button = new Button(this.name);
        button.setOnMouseClicked(event -> SceneMediator.getInstance().activateScene(this.name));
        button.setDisable(disabled);
        ImageView imageView = new ImageView(new Image(new File(separator).toURI().toString()));
        imageView.setFitHeight(20);
        imageView.setFitWidth(20);
        nodes.add(button);
        nodes.add(imageView);
    }

    public void setDisabled(Boolean disabled){
        this.nodes.get(0).setDisable(disabled);
    }

    public String getName() {
        return name;
    }

    public List<Node> getNodes() {
        return nodes;
    }
}
