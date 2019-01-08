package com.kdhira.dot.util.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

public class YamlReader<T> {

    private List<TypeDescription> typeDescriptions;

    public YamlReader() {
        typeDescriptions = new ArrayList<TypeDescription>();
    }
    public void registerType(Class<? extends T> subclass) {
        typeDescriptions.add(new TypeDescription(subclass, new Tag("!" + subclass.getSimpleName())));
    }

    @SuppressWarnings("unchecked")
    public <R> List<T> readDocument(String yamlLocation, Class<? extends T> documentRoot) throws FileNotFoundException {
        Constructor yamlConstructor = new Constructor(documentRoot);

        for (TypeDescription td : typeDescriptions) {
            yamlConstructor.addTypeDescription(td);
        }

        Yaml yaml = new Yaml(yamlConstructor);
        FileInputStream yamlFile = new FileInputStream(new File(yamlLocation));

        List<T> yamlJobs = new ArrayList<T>();
        for (Object document : yaml.loadAll(yamlFile)) {
            yamlJobs.add((T)document);
        }

        return yamlJobs;
    }
}
