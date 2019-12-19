package com.game.mouse.view.maingame.child;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Object;
import com.game.mouse.context.Config;
import com.game.mouse.gameinfo.GameInfo;
import com.game.mouse.screen.GamePageScreen;
import com.game.mouse.screen.MainMidlet;
import com.game.mouse.view.game.EndGameView;
import com.game.mouse.view.game.GameMainView;
import com.model.control.MyImg;
import com.model.mainServer.MainServer;
import com.model.tool.PubToolKit;

public class EndGamePassDoorView implements EndGameView {
    private GameMainView gameMainView;

    private MyImg bg;

    private MyImg but;

    private MyImg butback;

    private int x;

    private int y;

    private int butW, butH;

    private int selButIndex;

    private String msg = "";

    private Font font;

    public EndGamePassDoorView(GameMainView gameMainView, int initX, int initY) {
        this.gameMainView = gameMainView;
        this.x = initX;
        this.y = initY;
        String priceName = MainServer.getInstance().getPriceName();
        if (Config.isTipMoney) {
            msg = "您未收集满3个奶酪,无法通过当前关卡，是否花费" + gameMainView.props[5][2] + ""
                    + priceName + "直接过关?";
        } else {
            msg = "您未收集满3个奶酪,无法通过当前关卡，是否直接过关?";

        }

        MainServer.mPropType = 10;

        this.init();
        font = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, PubToolKit
                .getFontBig16And24Size());
    }

    public void init() {
        // TODO Auto-generated method stub
        bg = new MyImg("gamepage/endgame/bg.png");
        but = new MyImg("gamepage/endgame/passbut.png");
        butW = but.getWidth();
        butH = but.getHeight() / 2;
        butback = GameInfo.getReviveButImg();
    }

    public void paint(Graphics g) {
        // TODO Auto-generated method stub
        bg.drawImage(g, x, y, g.VCENTER | g.HCENTER);
        PubToolKit.drawString(msg, x - (int) (255 * (MainMidlet.scaleX)), y - (int) (80 * (MainMidlet.scaleY)), bg.getWidth() - 40, font, 0x000000, g);
        but.drawRegion(g, 0, butH, butW, butH, 0, x, y + 50, g.VCENTER | g.HCENTER);
        butback.drawImage(g, x, y + 80, g.VCENTER | g.HCENTER);
    }

    public void update() {
        // TODO Auto-generated method stub

    }

    public void keyPress() {
        int keyCode = Engine.getKeyCode();
        switch (keyCode) {
            case Engine.K_KEY_BACK:
//		case Engine.K_KEY_NUM0:
                keynum0();
                break;
            case Engine.K_KEY_NUM9:
                break;
            case Engine.K_KEY_LEFT:
                selButIndex = selButIndex > 0 ? selButIndex - 1 : 1;
                break;
            case Engine.K_KEY_RIGHT:
                selButIndex = selButIndex < 1 ? selButIndex + 1 : 0;
                break;
            case Engine.K_KEY_FIRE:
                keyfire();
                break;
            default:

        }
    }

    public void keyfire() {
        gameMainView.toBuyPassDoor();
    }

    public void keynum0() {
        gameMainView.mouseNoPassDoor();
    }
}
