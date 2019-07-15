package com.kredatus.flockblockers.GameObjects.Resources;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.kredatus.flockblockers.Handlers.UiHandler;
import com.kredatus.flockblockers.Screens.Loader;

public class Rank {
    public String[] rankNames=new String[]{"Copper","Bronze","Silver","Gold","Diamond","Emerald","Sapphire","Amethyst","Ruby","Pearl","Onyx"};

    public int[] expValues=new int[858];
    public int lvl,expGained,remExp;

    public Rank(int lvl, int expGained){
        if (lvl<857) this.lvl=lvl; else lvl=857;

        expValues[0]=100;
        for (int i=1;i<=857;i++){
            expValues[i]*=expValues[i-1]*1.5;
        }
        remExp=expValues[lvl]-expGained;
        this.expGained=expGained;
    }

    public void addExp(int expToAdd){
        if (lvl<857) {
            if (expToAdd < remExp) {//if gaining exp without gaining a lvl
                expGained += expToAdd;
                remExp = expValues[lvl] - expGained;
            } else if (expToAdd - (remExp + expValues[lvl + 1]) >= 0) {//if gaining so much exp that you gain 2 or more levels
                expToAdd -= remExp + expValues[lvl + 1];
                remExp = expValues[lvl + 2] = (expToAdd - (remExp) - expValues[lvl + 1]);
                lvl += 2;
                addExp(expToAdd);//knew recursivity would come in handy lol
                levelUp();
            } else {//if gaining a single level
                expGained = expToAdd - remExp;
                remExp = expValues[lvl++] - expGained;
                levelUp();
            }

        }
    }

    public void levelUp(){
        UiHandler.rankImage = new Image(Loader.ranksList[lvl]);
        if (lvl%78==0){ //if multiple of 78 on level up (level 0 of new rank), change name
            UiHandler.rankLabel.setText(rankNames[(lvl/78)-1]);
        }
    }
}
