package com.kredatus.flockblockers.GameObjects.Resources;

import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.kredatus.flockblockers.Handlers.UiHandler;
import com.kredatus.flockblockers.NonGameHandlerScreens.Loader;

public class Rank {
    public String[] rankNames=new String[]{"Copper","Bronze","Silver","Gold","Diamond","Emerald","Sapphire","Amethyst","Ruby","Pearl","Onyx"};

    public int[] expValues=new int[858];
    public int lvl,expGained,remExp;

    public Rank(int lvl, int expGained){
        if (lvl<857) this.lvl=lvl; else this.lvl=857;

        expValues[0]=10;
        for (int i=1;i<=857;i++){
            expValues[i]=expValues[0]+(i*i);
            //System.out.println(expValues[i]);
        }
        remExp=expValues[lvl]-expGained;
        this.expGained=expGained;
    }

    public void addExp(int expToAdd){
        if (lvl<857) {
            if (expToAdd < remExp) {//if gaining exp without gaining a lvl
                System.out.println("0");
                expGained += expToAdd;
                remExp = expValues[lvl] - expGained;
                UiHandler.expBar.setValue(expGained);
            } else if (expToAdd - (remExp + expValues[lvl + 1]) >= 0) {//if gaining so much exp that you gain 2 or more levels
                System.out.println("level up twice pt1");
                expToAdd -= remExp + expValues[lvl + 1];
                remExp = expValues[lvl + 2] = (expToAdd - (remExp) - expValues[lvl + 1]);
                lvl += 2;
                levelUp();
                addExp(expToAdd);//knew recursivity would come in handy lol
                System.out.println("level up twice pt2");

            } else {//if gaining a single level
                System.out.println("level up once");
                expGained = expToAdd - remExp;
                remExp = expValues[lvl++] - expGained;
                levelUp();
            }
        }
    }

    public void levelUp(){
        UiHandler.rankImage.setDrawable(new TextureRegionDrawable(Loader.ranksList[lvl]));
        UiHandler.lvlLabel.setText(lvl%78);
        UiHandler.expBar.setRange(0,expValues[lvl]);
        UiHandler.expBar.setValue(0);
        if (lvl%78==0){ //if multiple of 78 on level up (level 0 of new rank), change name
            UiHandler.rankNameLabel.setText(rankNames[lvl/78]);
        }
    }
}
