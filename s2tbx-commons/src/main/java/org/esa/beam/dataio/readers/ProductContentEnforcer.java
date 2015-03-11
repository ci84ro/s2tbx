package org.esa.beam.dataio.readers;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * This class checks products for consistency.
 * A product is considered to be consistent if it has a minimum set of required files (for example,
 * an XML metadata file and a TIFF raster file).
 * The consistency is checked by comparing the name list of files that constitute the product against
 * an expected (minimal) set of patterns (i.e. regular expressions).
 * The patterns are given as regular expressions which are compiled at creation time.
 * Optionally, a set of anti-patterns (i.e. exclusion patterns) may be given. This is especially useful
 * for some products that have similar structure, but they come from different sensors (such an example is
 * given by Deimos-1 and RapidEye L3 products).
 *
 * @author Cosmin Cara
 */
public class ProductContentEnforcer {

    private Pattern[] minimalFilePatternList;
    private Pattern[] notAcceptedFilePatternList;

    /**
     * Factory method that creates an instance of this class from a set of minimal patterns to be respected.
     *
     * @param minimalPatterns   The set of patterns to be checked.
     * @return  A new <code>ProductContentEnforcer</code> instance.
     */
    public static ProductContentEnforcer create(String[] minimalPatterns) {
        return new ProductContentEnforcer(minimalPatterns, null);
    }

    /**
     * Factory method that creates an instance of this class from a set of minimal patterns to be respected,
     * and also a set of exclusion patterns.
     *
     * @param minimalPatterns   The set of patterns to be checked.
     * @param notAcceptedPatterns   The set of exclusion patterns to be checked. Pass <code>null</code> if you
     *                              do not want to have such a check.
     * @return  A new <code>ProductContentEnforcer</code> instance.
     */
    public static ProductContentEnforcer create(String[] minimalPatterns, String[] notAcceptedPatterns) {
        return new ProductContentEnforcer(minimalPatterns, notAcceptedPatterns);
    }

    /**
     * Private constructor
     *
     * @param requiredPatterns  The set of patterns to be checked.
     * @param notAcceptedPatterns   The set of exclusion patterns to be checked. Pass <code>null</code> if you
     *                              do not want to have such a check.
     */
    private ProductContentEnforcer(String[] requiredPatterns, String[] notAcceptedPatterns) {
        if (requiredPatterns != null) {
            minimalFilePatternList = new Pattern[requiredPatterns.length];
            for (int i = 0; i < requiredPatterns.length; i++) {
                minimalFilePatternList[i] = Pattern.compile(requiredPatterns[i], Pattern.CASE_INSENSITIVE);
            }
        }
        if (notAcceptedPatterns != null) {
            notAcceptedFilePatternList = new Pattern[notAcceptedPatterns.length];
            for (int i = 0; i < notAcceptedPatterns.length; i++) {
                notAcceptedFilePatternList[i] = Pattern.compile(notAcceptedPatterns[i], Pattern.CASE_INSENSITIVE);
            }
        }
    }

    /**
     * Checks if the given set of files (representing a product) is consistent with respect
     * to the current instance rules.
     *
     * @param fileNames The files of the product to be checked.
     * @return  <code>true</code> if the product is consistent, <code>false</code> otherwise.
     */
    public boolean isConsistent(String[] fileNames) {
        boolean retFlag = false;
        if (fileNames != null) {
            if (notAcceptedFilePatternList != null) {
                for (Pattern pattern : notAcceptedFilePatternList) {
                    if (Arrays.stream(fileNames).anyMatch(f -> pattern.matcher(f.toLowerCase()).matches()))
                        return false;
                    /*for (String fileName : fileNames) {
                        if (pattern.matcher(fileName.toLowerCase()).matches())
                            return false;
                    }*/
                }
            }
            if (minimalFilePatternList != null && minimalFilePatternList.length > 0) {
                for (Pattern pattern : minimalFilePatternList) {
                    /*for (String fileName : fileNames) {
                        if ((retFlag = pattern.matcher(fileName.toLowerCase()).matches()))
                            break;
                    }*/
                    retFlag = Arrays.stream(fileNames).anyMatch(f -> pattern.matcher(f.toLowerCase()).matches());
                    if (!retFlag)
                        break;
                }
            }
        }
        return retFlag;
    }

    /**
     * Checks if the given set of files (representing a product) is consistent with respect
     * to the current instance rules.
     *
     * @param fileNames The files of the product to be checked.
     * @return  <code>true</code> if the product is consistent, <code>false</code> otherwise.
     */
    public boolean isConsistent(List<String> fileNames) {
        boolean retFlag = false;
        if (fileNames != null) {
            if (notAcceptedFilePatternList != null) {
                for (Pattern pattern : notAcceptedFilePatternList) {
                    /*for (String fileName : fileNames) {
                        if (pattern.matcher(fileName.toLowerCase()).matches())
                            return false;
                    }*/
                    if (fileNames.stream().anyMatch(f -> pattern.matcher(f.toLowerCase()).matches()))
                        return false;
                }
            }
            if (minimalFilePatternList != null && minimalFilePatternList.length > 0) {
                boolean global;
                for (Pattern pattern : minimalFilePatternList) {
                    /*for (String fileName : fileNames) {
                        if ((retFlag = pattern.matcher(fileName.toLowerCase()).matches()))
                            break;
                    }
                    global = retFlag;
                    if (!global)
                        break;*/
                    retFlag = fileNames.stream().anyMatch(f -> pattern.matcher(f.toLowerCase()).matches());
                    if (!retFlag)
                        break;
                }
            }
        }
        return retFlag;
    }
}