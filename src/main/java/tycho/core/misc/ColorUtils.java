package tycho.core.misc;

import javafx.scene.paint.Color;

public class ColorUtils {

    public record RGB(int red, int green, int blue) {

        /**
         * Default constructor
         *
         * @param red The red channel
         * @param green The green channel
         * @param blue The blue channel
         */
        public RGB(int red, int green, int blue) {
            this.red = boundsCheck(red);
            this.green = boundsCheck(green);
            this.blue = boundsCheck(blue);
        }

        /**
         * Checks if the number is above 0 and under 255. And gives the correct number
         *
         * @param i A number, you want to check if it's between 0 and 255
         * @return Returns 0 if 'i' is negative, returns 255 if 'i' is above 255, otherwise just return 'i'
         */
        private int boundsCheck(int i){
            return Math.max(Math.min(i, 255), 0);
        }
    }


    /**
     * Converts a hexadecimal number into a decimal number
     *
     * @param hex A hexadecimal number
     * @return Returns the hexadecimal number as the corresponding decimal number
     */
    private static Integer hexToDecimal(String hex) {
        return Integer.parseInt(hex, 16);
    }

    /**
     * Convert a decimal number into a hexadecimal number
     *
     * @param decimal A normal decimal number
     * @return Returns the decimal number as the corresponding hexadecimal number
     */
    private static String decimalToHex(int decimal) {
        return Integer.toHexString(decimal);
    }

    /**
     * Converts a hex color (aka '#FFFFFF', '#000000', ...) into an RGB object
     *
     * @param hex A hex color with or without the hashtag ('#')
     * @return Returns the hex color as an RGB object
     */
    public static RGB hexToRgb(String hex) {
        if (hex.startsWith("#")) hex = hex.substring(1);
        int red = hexToDecimal(hex.substring(0, 2));
        int green = hexToDecimal(hex.substring(2, 4));
        int blue = hexToDecimal(hex.substring(4, 6));
        return new RGB(red, green, blue);
    }

    /**
     * Converts an RGB object into a hex color
     *
     * @param rgb The RGB object you want to convert into a hex String
     * @param startWithHashtag Whether you want a hashtag '#' at the beginning
     * @return Returns the RGB object as a hex color
     */
    public static String rgbToHex(RGB rgb, boolean startWithHashtag) {
        String hex = startWithHashtag ? "#" : "";

        String red = decimalToHex(rgb.red);
        String green = decimalToHex(rgb.green);
        String blue = decimalToHex(rgb.blue);

        hex += red.length() == 1
                ? "0" + red
                : red;
        hex += green.length() == 1
                ? "0" + green
                : green;
        hex += blue.length() == 1
                ? "0" + blue
                : blue;

        return hex;
    }

    /**
     * Makes the given color a bit darker
     * Note: I know you can't lower the brightness of a simple rgb, but I couldn't come up with a better name
     *
     * @param rgb The RGB object you want to darken
     * @param percentage The percentage of how much you want to darken it
     * @return Returns the darkened RGB object
     */
    public static RGB lowerBrightnessOfColor(RGB rgb, float percentage) {
        return new RGB((int) (rgb.red - (rgb.red * percentage)),
                (int) (rgb.green - (rgb.green * percentage)),
                (int) (rgb.blue - (rgb.blue * percentage)));
    }

    /**
     * Convert a JavaFX Color object to hex format
     *
     * @param color The JavaFX Color object
     * @return Return the Color object as a hex format
     */
    public static String javaFXColorToHex(Color color) {
        return String.format( "#%02X%02X%02X",
                (int)( color.getRed() * 255 ),
                (int)( color.getGreen() * 255 ),
                (int)( color.getBlue() * 255 ) );
    }
}
