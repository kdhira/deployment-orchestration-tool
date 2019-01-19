package com.kdhira.dot.cli.arguments;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.kdhira.dot.cli.Settings;
import com.kdhira.dot.util.argument.Rule;

/**
 * {@link Rule} for parsing resource files.
 * @author Kevin Hira
 */
public class ResourceRule implements Rule<Settings> {

    @Override
    public boolean apply(Settings subject, List<String> arguments) {
        if (arguments.size() < 1) {
            return false;
        }
        if (arguments.get(0).equals("--resource") || arguments.get(0).equals("-r")) {
            if (arguments.size() < 2) {
                throw new RuntimeException(new IllegalArgumentException("--resource | -r must be followed by file path."));
            }
            File resourceFile = new File(arguments.get(1));
            if (!resourceFile.exists()) {
                throw new RuntimeException(new FileNotFoundException(arguments.get(1) + " does not exist."));
            }
            subject.addResource(resourceFile);

            arguments.remove(0);
            arguments.remove(0);
            return true;
        }
        return false;
    }

}
