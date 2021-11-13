package net.minecraft.client.gui;

import net.minecraft.client.Minecraft;

public class GuiOculusEggOnOff{
    public static boolean actiiv = false;
    public static String activ_text="OculusEgg Predictor: OFF";

    public static GuiButton activ;



    public static boolean block_switch = true;
    public static String block_switch_text = "OculusEgg Predictor will SWITCH";

    public static GuiButton EggSwitch;

    public static void activToFalse()
    {
        actiiv=false;
        activ_text="OculusEgg Predictor: OFF";
        EggSwitch.visible=false;
    }
    public static void activToTrue()
    {
        actiiv=true;
        activ_text="OculusEgg Predictor: ON";
        EggSwitch.visible=true;
    }
    public static void switchToFalse()
    {
        block_switch=false;
        block_switch_text="OculusEgg Predictor will TRAIN";
    }
    public static void switchToTrue()
    {
        block_switch=true;
        block_switch_text="OculusEgg Predictor will SWITCH";
    }
}

