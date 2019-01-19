package com.kdhira.dot.util.yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;

public class YamlReader {

    private List<TypeDescription> typeDescriptions;

    public YamlReader() {
        typeDescriptions = new ArrayList<TypeDescription>();
    }

    public void registerType(Class<?> subclass) {
        typeDescriptions.add(new TypeDescription(subclass, new Tag("!" + subclass.getSimpleName())));
    }

    public <T> List<T> readDocument(String yamlLocation, Class<T> documentRoot) throws FileNotFoundException {
        return readDocument(yamlLocation, documentRoot, documentRoot);
    }

    public <T, U extends T> List<T> readDocument(String yamlLocation, Class<T> returnType, Class<U> rootType) throws FileNotFoundException {
        return readDocument(new File(yamlLocation), returnType, rootType);
    }

    public <T> List<T> readDocument(File yamlLocation, Class<T> documentRoot) throws FileNotFoundException {
        return readDocument(yamlLocation, documentRoot, documentRoot);
    }

    @SuppressWarnings("unchecked")
    public <T, U extends T> List<T> readDocument(File yamlLocation, Class<T> returnType, Class<U> rootType) throws FileNotFoundException {
        Constructor yamlConstructor = new Constructor(rootType);

        for (TypeDescription td : typeDescriptions) {
            yamlConstructor.addTypeDescription(td);
        }

        Yaml yaml = new Yaml(yamlConstructor);
        FileInputStream yamlFile = new FileInputStream(yamlLocation);

        List<T> yamlJobs = new ArrayList<T>();
        for (Object document : yaml.loadAll(yamlFile)) {
            yamlJobs.add((T)document);
        }

        return yamlJobs;
    }

    public <T> T readResource(InputStream resourceStream) throws FileNotFoundException {
        return new Yaml().load(resourceStream);
    }

}
