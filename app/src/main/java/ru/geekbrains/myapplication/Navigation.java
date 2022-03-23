package ru.geekbrains.myapplication;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Navigation {

    private final FragmentManager fragmentManager;

    public Navigation(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void replaceFragment(int container, Fragment fragment, boolean useBackStack) {
        // Открыть транзакцию
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(container, fragment);
        if (useBackStack) {
            fragmentTransaction.addToBackStack("");
        }
        // Закрыть транзакцию
        fragmentTransaction.commit();
    }

    public void addFragment(int container, Fragment fragment, boolean useBackStack) {
        // Открыть транзакцию
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(container, fragment);
        if (useBackStack) {
            fragmentTransaction.addToBackStack("");
        }
        // Закрыть транзакцию
        fragmentTransaction.commit();
    }
}

