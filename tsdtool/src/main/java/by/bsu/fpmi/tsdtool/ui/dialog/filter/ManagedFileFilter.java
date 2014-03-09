package by.bsu.fpmi.tsdtool.ui.dialog.filter;

import by.bsu.fpmi.tsdtool.application.Settings;
import by.bsu.fpmi.tsdtool.ui.i18n.MessageUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ManagedFileFilter extends FileFilter {
    private static final String GAP = " ";
    private static final String DESCRIPTION;
    private static final List<String> EXTENSIONS;

    static {
        DESCRIPTION = MessageUtils.getMessage("ui.dialog.filter.managedFileFilter.description");
        String extensions = Settings.getProperty("import.fileFilter.all");
        String[] separatedExtensions = extensions.split(GAP);
        EXTENSIONS = Arrays.asList(separatedExtensions);
    }

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }

        String fileName = file.getName();
        for (String extension : EXTENSIONS) {
            if (StringUtils.endsWithIgnoreCase(fileName, extension)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }
}
