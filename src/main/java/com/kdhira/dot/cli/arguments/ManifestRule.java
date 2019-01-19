package com.kdhira.dot.cli.arguments;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import com.kdhira.dot.cli.Settings;
import com.kdhira.dot.util.argument.Rule;

/**
 * {@link Rule} for parsing manifest files.
 * @author Kevin Hira
 */
public class ManifestRule implements Rule<Settings> {

    @Override
    public boolean apply(Settings subject, List<String> arguments) {
        if (arguments.size() < 1) {
            return false;
        }
        if (arguments.get(0).equals("--manifest") || arguments.get(0).equals("-m")) {
            if (arguments.size() < 2) {
                throw new RuntimeException(new IllegalArgumentException("--manifest | -m must be followed by file path."));
            }
            File manifestFile = new File(arguments.get(1));
            if (!manifestFile.exists()) {
                throw new RuntimeException(new FileNotFoundException(arguments.get(1) + " does not exist."));
            }
            subject.addManifest(manifestFile);

            arguments.remove(0);
            arguments.remove(0);
            return true;
        }
        return false;
    }

}
