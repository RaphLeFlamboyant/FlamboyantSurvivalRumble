package me.flamboyant.survivalrumble.gamecontrollers.classselection;

import java.util.HashMap;

public interface IClassSelectionVisitor {
    void onClassesSelected(HashMap<String, String> playerClasses);
}
