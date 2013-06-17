package ru.o2genum.coregame.game;

import android.view.*;
import android.graphics.*;

import ru.o2genum.coregame.framework.*;
import ru.o2genum.coregame.framework.impl.*;
import ru.o2genum.coregame.link.LinkGameScreen;

public class GameActivity extends AndroidGame {
    @Override
    public Screen getStartScreen() {
        return new LinkGameScreen(this); 
    }
}
