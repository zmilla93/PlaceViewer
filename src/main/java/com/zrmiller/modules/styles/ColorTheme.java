package com.zrmiller.modules.styles;

import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.intellijthemes.*;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMonokaiProIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatNightOwlContrastIJTheme;

/**
 * References to all color themes.
 */
public enum ColorTheme {

    // https://github.com/JFormDesigner/FlatLaf/tree/main/flatlaf-intellij-themes#themes

    //    ARK(new FlatArcIJTheme()),
    ARK_ORANGE(new FlatArcOrangeIJTheme()),
    //    ARK_DARK(new FlatArcDarkIJTheme()),
//    ARK_DARK_ORANGE(new FlatArcDarkOrangeIJTheme()),
    CARBON(new FlatCarbonIJTheme()),
    //    COBALT_2(new FlatCobalt2IJTheme()),
//    CYAN_LIGHT(new FlatCyanLightIJTheme()),
//    DARK_FLAT(new FlatDarkFlatIJTheme()),
    DARK_PURPLE(new FlatDarkPurpleIJTheme()),
//    DRACULA(new FlatDraculaIJTheme()),
    //    GRADIANTO_DARK_FUCHSIA(new FlatGradiantoDarkFuchsiaIJTheme()),
//    GRADIANTO_DEEP_OCEAN(new FlatGradiantoDeepOceanIJTheme()),
//    GRADIANTO_MIDNIGHT_BLUE(new FlatGradiantoMidnightBlueIJTheme()),
//    GRADIANTO_NATURE_GREEN(new FlatGradiantoNatureGreenIJTheme()),
//    GRAY(new FlatGrayIJTheme()),
//    GRUVBOX_DARK_HARD(new FlatGruvboxDarkHardIJTheme()),
//    GRUVBOX_DARK_MEDIUM(new FlatGruvboxDarkMediumIJTheme()),
//    GRUVBOX_DARK_SOFT(new FlatGruvboxDarkSoftIJTheme()),
//    HIBERBEE_DARK(new FlatHiberbeeDarkIJTheme()),
//    HIGH_CONTRAST(new FlatHighContrastIJTheme()),
//    LIGHT_FLAT(new FlatLightFlatIJTheme()),
//    LIGHT_OWL(new FlatLightOwlIJTheme()),
//    LIGHT_OWL_2(new FlatLightOwlContrastIJTheme()),
//    NIGHT_OWL(new FlatNightOwlIJTheme()),
//    NIGHT_OWL_2(new FlatNightOwlContrastIJTheme()),
//    MATERIAL_DESIGN_DARK(new FlatMaterialDesignDarkIJTheme()),
//    MONOCAI(new FlatMonocaiIJTheme()),
//    MONOKAI(new FlatMonokaiProIJTheme()),
    NORD(new FlatNordIJTheme()),
    //    ONE_DARK(new FlatOneDarkIJTheme()),
    SOLARIZED_LIGHT(new FlatSolarizedLightIJTheme()),
    SOLARIZED_DARK(new FlatSolarizedDarkIJTheme()),
    //    SPACEGRAY(new FlatSpacegrayIJTheme()),
    VUESION(new FlatVuesionIJTheme()),
    ;

    public final IntelliJTheme.ThemeLaf lookAndFeel;
    private String cleanName;

    ColorTheme(IntelliJTheme.ThemeLaf lookAndFeel) {
        this.lookAndFeel = lookAndFeel;
    }


    @Override
    public String toString() {
        if (cleanName == null) {
            cleanName = enumToString(name());
        }
        return cleanName;
    }

    private String enumToString(String input) {
        input = input.replaceAll("_", " ");
        input = input.toLowerCase();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (i == 0 || input.charAt(i - 1) == ' ') {
                builder.append(Character.toUpperCase(input.charAt(i)));
            } else {
                builder.append(input.charAt(i));
            }
        }
        return builder.toString();
    }
}
