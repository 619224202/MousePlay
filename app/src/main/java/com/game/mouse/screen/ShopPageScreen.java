package com.game.mouse.screen;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import com.game.lib9.Engine;
import com.game.lib9.L9Config;
import com.game.lib9.L9EngineLogic;
import com.game.lib9.L9IState;
import com.game.lib9.L9Object;
import com.game.lib9.L9Screen;
import com.game.mouse.context.Config;
import com.game.mouse.context.GameConfigInfo;
import com.game.mouse.context.UserInfo;
import com.game.mouse.gameinfo.GameInfo;
import com.game.mouse.gameinfo.Props;
import com.game.mouse.modle.UserMouse;
import com.game.mouse.modle.service.DataManageService;
import com.game.mouse.modle.service.UserDataManageService;
import com.game.mouse.view.EndGameTipView;
import com.model.base.ParserTbl;
import com.model.control.MyImg;
import com.model.mainServer.MainConfig;
import com.model.mainServer.MainServer;
import com.model.tool.PubToolKit;

public class ShopPageScreen extends L9Screen {
    private Graphics g;

    private MyImg bgImg;

    private MyImg bgbottomImg;

    private MyImg butbg;

    private MyImg but0bg;

    private MyImg butbuy;

    private MyImg butlv;

    private MyImg butstage;

    private MyImg[] propsName;

    private MyImg[] propsImg;

    private MyImg[] propsDesc;

    private MyImg num;

    private MyImg num0;

    private MyImg num00;
    /**
     * 单位
     */
    private MyImg unit;

    private int butbgW, butbgH;

    private int selIndex;

    private boolean isInPay;

    /**
     * 1-扣费道具编码 2-价格 3-购买数量 4-当前数量 5-名称 6-对应保存的编码
     */
    private String[][] props = {
            {"uncheese0", "", "0", "0", "0", "万能奶酪", "0", "恢复心情、喂养使用,购买万能奶酪"},
            {"uncheese1", "", "0", "0", "0", "万能奶酪", "0", "恢复心情、喂养使用,购买万能奶酪"},
            {"uncheese2", "", "0", "0", "0", "万能奶酪", "0", "恢复心情、喂养使用,购买万能奶酪"},
            {"speedcheese", "", "0", "0", "0", "速度奶酪", "1", "控制速度使用,购买速度奶酪"},
            {"propblood", "", "0", "0", "0", "回血胶囊", "2", "购买回血胶囊,恢复血量"},
            {"propprotect", "", "0", "0", "0", "变身头盔", "3", "购买变身头盔,防止机关陷阱伤害"},
            {"propweaponstage", "", "0", "0", "0", "进化武器", "-1", "购买进化武器"},
            {"propweaponlv", "", "0", "0", "0", "升级武器", "-1", "购买升级武器"}};

    /**
     * 购买成功后调用方法
     */
    private String buyAfterMethod = "";

    private EndGameTipView endGameTipView;

    public void Init(int loadCount) {
        switch (loadCount) {
            case 0:
                bgImg = new MyImg("shoppage/bg.png");
                break;
            case 10:
                bgbottomImg = GameInfo.getBottomImg(0);
                butbg = new MyImg("shoppage/butbg.png");
                but0bg = new MyImg("shoppage/but0bg.png");
//			butbgW = butbg.getWidth()/2;
                butbgH = butbg.getHeight() / 2;
                butbgW = butbg.getWidth();
//			butbgH = butbg.getHeight();
                break;
            case 20:
                butbuy = new MyImg("shoppage/butbuy.png");
                butlv = new MyImg("shoppage/butlv.png");
                butstage = new MyImg("shoppage/butstage.png");
                num = new MyImg("shoppage/num.png");
                num0 = new MyImg("shoppage/num0.png");
                num00 = new MyImg("shoppage/num00.png");
                break;
            case 40:
                this.getPropMess();
                break;
            case 60:
                this.createunit();
                break;
            case 80:
                this.getweapon();
            case 100:
                this.initPropsImg();
                break;
        }
    }

    public void getPropMess() {
        for (int i = 0; i < props.length; i++) {
            String[] prop = Props.getIntance().getPricePropByCode(props[i][0]);
            props[i][1] = prop[1];
            props[i][2] = prop[2];
            props[i][3] = prop[3];
            if (!"-1".equals(props[i][6])) {
                props[i][4] = UserDataManageService.getInsatnce()
                        .getPropNumByCode(props[i][6])
                        + "";
            }
        }
    }

    public void getweapon() {
        UserMouse[] userMouses = DataManageService.getInsatnce()
                .getAllUserMouse();
        for (int i = 0; i < userMouses.length; i++) {
            GameConfigInfo.weapon[Integer.parseInt(userMouses[i].getCode())][0] = userMouses[i]
                    .getWeaponStage();
            GameConfigInfo.weapon[Integer.parseInt(userMouses[i].getCode())][1] = userMouses[i]
                    .getWeaponLv();
        }
    }

    /**
     * 实例化单位
     */
    public void createunit() {
        String priceName = MainServer.getInstance().getPriceName();
        if ("元".equals(priceName)) {
            unit = new MyImg("shoppage/unity.png");
        } else if ("元宝".equals(priceName)) {
            unit = new MyImg("shoppage/unityb.png");
        } else if ("TV豆".equals(priceName)) {
            unit = new MyImg("shoppage/unittvd.png");
        } else if ("TV币".equals(priceName)) {
            unit = new MyImg("shoppage/unittvb.png");
        } else if ("代币".equals(priceName)) {
            unit = new MyImg("shoppage/unitdb.png");
        } else if ("i豆".equals(priceName)) {
            unit = new MyImg("shoppage/unitid.png");
        } else if ("沃金币".equals(priceName)) {
            unit = new MyImg("shoppage/unitwjb.png");
        } else if ("游戏币".equals(priceName)) {
            unit = new MyImg("shoppage/unityxb.png");
        } else if ("云币".equals(priceName)) {
            unit = new MyImg("shoppage/unitsby.png");
        }
    }

    public void initPropsImg() {
        propsName = new MyImg[7];
        propsImg = new MyImg[7];
        propsDesc = new MyImg[6];
        for (int i = 0; i < propsName.length; i++) {
            if (i >= 4) {
                propsName[i] = new MyImg("shoppage/prop/" + i + ""
                        + GameConfigInfo.weapon[i - 4][0] + "name.png");
                propsImg[i] = new MyImg("shoppage/prop/" + i + ""
                        + GameConfigInfo.weapon[i - 4][0] + ".png");
            } else {
                propsName[i] = new MyImg("shoppage/prop/" + i + "name.png");
                propsImg[i] = new MyImg("shoppage/prop/" + i + ".png");
                propsDesc[i] = new MyImg("shoppage/prop/" + i + "desc.png");
            }
        }
        propsDesc[4] = new MyImg("shoppage/prop/upstagedesc.png");
        propsDesc[5] = new MyImg("shoppage/prop/uplvdesc.png");
    }

    public void Paint() {
        // TODO Auto-generated method stub
        if (this.g == null) {
            this.g = Engine.FG;
        }
        bgImg.drawImage(g, 0, 0, 0);
        paintprops();
        bgbottomImg
                .drawImage(g, 0, L9Config.SCR_H - bgbottomImg.getHeight(), 0);
        if (isInPay) {
            MainServer.getInstance().paint(g);
        }
        if (endGameTipView != null)
            endGameTipView.paint(g);
    }

    /**
     * 画道具信息
     */
    public void paintprops() {
        for (int i = 0; i < 3; i++) {
            boolean issel = (i == 0 && selIndex == 0 || i == 1 && selIndex > 0 && selIndex < 8 || i == 2 && selIndex == 8);
            int indexProp = i + (selIndex == 8 ? 6 : selIndex > 0 ? selIndex - 1 : 0);
            if (indexProp >= 6) {
                but0bg.drawRegion(g, 0, issel ? 0 : butbgH, butbgW, butbgH, 0,
                        38 + 180 - 135, 103 + 135 * i + 50 - 10, 0);
                int tmpIndexProp = GameConfigInfo.weapon[indexProp - 6][0] == 0 ? 6
                        : 7;
                PubToolKit.drawString(g, this.num00.getImg(),
                        props[tmpIndexProp][2], ".0123456789", 170 + 235,
                        140 + 135 * i + 50 + 10, 14, 20, 0, 0, props[tmpIndexProp][2]
                                .length(), 0x000000);
                unit.drawImage(g,
                        180 + (props[tmpIndexProp][2].length() - 1) * 7 + 280,
                        140 + 135 * i + 53, 0);
                int att = (GameConfigInfo.weapon[indexProp - 6][2] + GameConfigInfo.weapon[indexProp - 6][1] * 20)
                        * (GameConfigInfo.weapon[indexProp - 6][0] + 1);
                PubToolKit.drawNum(g, att, num0.getImg(), 480, 162 + 135 * i + 80, 0,
                        0, true);
                if (tmpIndexProp == 6) {
                    butstage.drawImage(g, 250 + 35 + 400 - 30, 125 + 132 * i + 57, 0);
                } else {
                    butlv.drawImage(g, 250 + 35 + 400 - 30, 125 + 132 * i + 57, 0);
                }
                int indexImg = i
                        + (selIndex == 8 ? 4 : selIndex > 0 ? selIndex - 3 : 0);
                propsName[indexImg].drawImage(g, 125 + 185, 112 + 135 * i + 45, 0);
                propsImg[indexImg].drawImage(g, 40 + 35 + 185 + 230 - 320, 115 + 135 * i + 35, 0);
            } else {
                butbg.drawRegion(g, 0, issel ? 0 : butbgH, butbgW, butbgH, 0,
                        38 + 180 - 135, 103 + 135 * i + 50 - 10, 0);
                PubToolKit.drawString(g, this.num.getImg(), "X"
                                + props[indexProp][3], "X0123456789", 85, 150 + 135 * i + 45 + 10,
                        27, 31, 0, 0, 2, -1);
                PubToolKit.drawString(g, this.num00.getImg(),
                        props[indexProp][2], ".0123456789", 170 + 235, 140 + 135 * i + 61,
                        14, 20, 0, 0, props[indexProp][2].length(), 0x000000);
                unit.drawImage(g, 180 + (props[indexProp][2].length() - 1) * 7 + 280,
                        140 + 135 * i + 53, 0);
                PubToolKit.drawNum(g, Integer.parseInt(props[indexProp][4]),
                        num0.getImg(), 200 + 280, 162 + 135 * i + 80, 0, 0, true);
                butbuy.drawImage(g, 250 + 435 - 30, 125 + 132 * i + 57, 0);
                int indexImg = indexProp > 2 ? indexProp - 2 : 0;
                propsName[indexImg].drawImage(g, 125 + 185, 112 + 135 * i + 45, 0);
                if (indexProp != 3) {
                    propsImg[indexImg].drawImage(g, 40 + 35 + 185 + 230 - 320, 115 + 135 * i + 35, 0);
                } else {
                    propsImg[indexImg].drawImage(g, 40 + 35 + 185 + 230 + 25 - 320, 115 + 135 * i + 35, 0);
                }
            }
        }
        int selPropIndexDesc = selIndex;
        if (selIndex < 3) {
            selPropIndexDesc = 0;
        } else if (selIndex < 6) {
            selPropIndexDesc = selIndex - 2;
        } else if (selIndex >= 6 && GameConfigInfo.weapon[selIndex - 6][0] == 0) {
            selPropIndexDesc = 4;
        } else if (selIndex >= 6 && GameConfigInfo.weapon[selIndex - 6][0] == 1) {
            selPropIndexDesc = 5;
        }
        propsDesc[selPropIndexDesc].drawImage(g, 410 + 485, 110, 0);
    }

    public void Release() {
        // TODO Auto-generated method stub

    }

    public void Update() {

        for (int i = 0; i < L9EngineLogic.getInstance().objects.size; i++) {
            ((L9Object) L9EngineLogic.getInstance().objects.values[i])
                    .doAllFrame();
        }
        L9EngineLogic.getInstance().flushRemoveList();
        L9EngineLogic.getInstance().flushAddList();
        if (!isInPay) {
            if (endGameTipView != null) {
                endGameTipView.update();
            } else {
                keyPress();
            }
        } else {
            isInPay();
        }
    }

    public boolean isInPay() {
        if (isInPay) {
            int keyCode = Engine.getKeyCode();
            MainServer.getInstance().update(
                    Engine.getKeyCodeByLogicKey(keyCode));
            if (MainServer.getInstance().isPayEnd()) {
                isInPay = false;
                if (MainServer.getInstance().isBuySuccess()) {
                    System.out.println(">>>ShoppageScreen>>>>>>>购买道具成功<<<<<<<<<<");
                    buySucc();
                } else {
                    System.out.println(">>>ShoppageScreen>>>>>>>购买道具失败<<<<<<<<<<");
                }
            }
        }
        return isInPay;
    }

    public void keyPress() {
        int keyCode = Engine.getKeyCode();
        switch (keyCode) {
            case Engine.K_KEY_LEFT:
                System.out.println("K_KEY_LEFT");
                this.keyleft();
                break;
            case Engine.K_KEY_UP:
                System.out.println("K_KEY_UP");
                this.keyup();
                break;
            case Engine.K_KEY_DOWN:
                System.out.println("K_KEY_DOWN");
                this.keydown();
                break;
            case Engine.K_KEY_RIGHT:
                System.out.println("K_KEY_RIGHT");
                this.keyright();
                break;
            case Engine.K_KEY_FIRE:
                System.out.println("K_KEY_FIRE");
                this.keyfire();
                break;
            case Engine.K_KEY_BACK:
//		case Engine.K_KEY_NUM0:
                System.out.println("K_KEY_BACK");
                this.keynum0();
                break;
        }

    }

    public void buySucc() {
        if ("buyProp".equals(buyAfterMethod)) {
            this.buyProp();
        } else if ("buyUpStage".equals(buyAfterMethod)) {
            this.buyUpStage();
        } else if ("buyUpLv".equals(buyAfterMethod)) {
            this.buyUpLv();
        }
    }

    public boolean isDrawJalousieStateBegin() {
        return false;
    }

    public boolean isDrawJalousieStateEnd() {
        return false;
    }

    public void dialogLogic(int selectOPtion) {
        // TODO Auto-generated method stub

    }

    public void keyleft() {
    }

    public void keyright() {
    }

    public void keyup() {
        selIndex = selIndex > 0 ? selIndex - 1 : 0;
    }

    public void keydown() {
        selIndex = selIndex < 8 ? selIndex + 1 : 8;
    }

    public void keyfire() {
        if (selIndex < 6) {
            MainServer.mPropType = selIndex + 2;
            this.toBuyProp();
        } else if (GameConfigInfo.weapon[selIndex - 6][0] == 0) {
            MainServer.mPropType = 8;
            this.toBuyUpStage();
        } else if (selIndex >= 6 && GameConfigInfo.weapon[selIndex - 6][0] == 1) {
            MainServer.mPropType = 9;
            this.toBuyUpLv();
        }
        Engine.resetKey();
    }

    /**
     * 去购买道具
     */
    public void toBuyProp() {
        isInPay = true;
        buyAfterMethod = "buyProp";
        switch (Config.regionType) {
            case MainConfig.Type_GDDX:
            case MainConfig.Type_SXDX:
                MainServer.getInstance().buyProp(props[selIndex][1],
                        props[selIndex][2], props[selIndex][5], false,
                        Integer.parseInt(props[selIndex][3]), props[selIndex][7],
                        false, false, "/propimg/" + props[selIndex][6] + ".png");
                break;
            default:
                MainServer.getInstance().buyProp(props[selIndex][1],
                        props[selIndex][2], props[selIndex][5], false,
                        Integer.parseInt(props[selIndex][3]), props[selIndex][7],
                        false, false);
        }
    }

    /**
     * 去进化
     */
    public void toBuyUpStage() {
        UserMouse userMouse = DataManageService.getInsatnce()
                .getUserMouseByCode((selIndex - 6) + "");
        if (userMouse.isIsbuy()) {
            if (userMouse.getWeaponStage() < 1) {
                isInPay = true;
                buyAfterMethod = "buyUpStage";
                switch (Config.regionType) {
                    case MainConfig.Type_GDDX:
                    case MainConfig.Type_SXDX:
                        MainServer.getInstance()
                                .buyProp(
                                        props[6][1],
                                        props[6][2],
                                        props[6][5],
                                        false,
                                        -1,
                                        props[6][7],
                                        false,
                                        false,
                                        "/propimg/"
                                                + (Integer.parseInt(userMouse
                                                .getCode()) + 7) + ".png");
                        break;
                    default:
                        MainServer.getInstance().buyProp(props[6][1], props[6][2],
                                props[6][5], false, -1, props[6][7], false, false);
                }

                //我写的逻辑
//				if(MainMidlet.Area.equals("SBY")){
//					IptvPayShiBoYun.getInstance().pay(selIndex + 2);
//				}

            } else {
                String msg = "武器已经进化到最高阶段，不能进行进化！";
                this.tip(msg);
            }
        } else {
            String msg = userMouse.getName() + "专属武器，角色未开通不能进化！";
            this.tip(msg);
        }
    }

    /**
     * 去升级
     */
    public void toBuyUpLv() {
        UserMouse userMouse = DataManageService.getInsatnce()
                .getUserMouseByCode((selIndex - 6) + "");
        if (userMouse.isIsbuy()) {
            if (userMouse.getWeaponLv() < 10) {
                isInPay = true;
                buyAfterMethod = "buyUpLv";
                switch (Config.regionType) {
                    case MainConfig.Type_GDDX:
                    case MainConfig.Type_SXDX:
                        MainServer.getInstance()
                                .buyProp(
                                        props[7][1],
                                        props[7][2],
                                        props[7][5],
                                        false,
                                        -1,
                                        props[7][7],
                                        false,
                                        false,
                                        "/propimg/"
                                                + (Integer.parseInt(userMouse
                                                .getCode()) + 7) + ".png");
                        break;
                    default:
                        MainServer.getInstance().buyProp(props[7][1], props[7][2],
                                props[7][5], false, -1, props[7][7], false, false);
                }

                //我写的逻辑
//				if(MainMidlet.Area.equals("SBY")){
//					IptvPayShiBoYun.getInstance().pay(selIndex + 2);
//				}

            } else {
                String msg = "武器已经升级到最高阶段，不能进行升级！";
                this.tip(msg);
            }
        } else {
            String msg = userMouse.getName() + "专属武器，角色未开通不能升级！";
            this.tip(msg);
        }
    }

    public void buyProp() {
        props[selIndex][4] = (Integer.parseInt(props[selIndex][4]) + Integer
                .parseInt(props[selIndex][3]))
                + "";
        for (int i = 0; i < props.length; i++) {
            if (props[i][6] == props[selIndex][6]) {
                props[i][4] = props[selIndex][4];
            }
        }
        new Thread() {
            public void run() {
                UserDataManageService.getInsatnce().updateMouseProp(
                        props[selIndex][6],
                        Integer.parseInt(props[selIndex][4]), true);
            }
        }.start();
    }

    /**
     * 进化武器成功
     */
    public void buyUpStage() {
        UserMouse userMouse = DataManageService.getInsatnce()
                .getUserMouseByCode((selIndex - 6) + "");
        if (userMouse.getWeaponStage() < 1) {
            userMouse.setWeaponStage(userMouse.getWeaponStage() + 1);
            GameConfigInfo.weapon[(selIndex - 6)][0] = userMouse
                    .getWeaponStage();
            int indexImg = (selIndex - 6) + 4;
            propsName[indexImg] = new MyImg("shoppage/prop/" + indexImg
                    + "1name.png");
            propsImg[indexImg] = new MyImg("shoppage/prop/" + indexImg
                    + "1.png");
            final UserMouse saveUserMouse = userMouse;
            new Thread() {
                public void run() {
                    UserDataManageService.getInsatnce().updateMouse(
                            saveUserMouse);
                    UserInfo.score += 50;
                    MainServer.getInstance().updateScore(UserInfo.score);
                }
            }.start();
        }
    }

    /**
     * 升级武器成功
     */
    public void buyUpLv() {
        UserMouse userMouse = DataManageService.getInsatnce().getUserMouseByCode((selIndex - 6) + "");
        if (userMouse.getWeaponLv() < 10) {
            userMouse.setWeaponLv(userMouse.getWeaponLv() + 1);
            GameConfigInfo.weapon[(selIndex - 6)][1] = userMouse.getWeaponLv();
            final UserMouse saveUserMouse = userMouse;
            new Thread() {
                public void run() {
                    UserDataManageService.getInsatnce().updateMouse(
                            saveUserMouse);
                }
            }.start();
        }
    }

    public void keynum0() {
        HomePageScreen homePageScreen = new HomePageScreen(2);
        MainMidlet.engine.changeState(homePageScreen);
    }

    public void closeTip() {
        endGameTipView = null;
    }

    public void tip(String msg) {
        endGameTipView = new EndGameTipView(this, msg, L9Config.SCR_W / 2,
                L9Config.SCR_H / 2 - 15);
    }

    public void savaData() {
        UserDataManageService.getInsatnce().mySaveGameData();
    }

    public void paySuccess() {
        System.out.println(" ---- paySuccess() ---- ");
        if (selIndex < 6) {
            //我写的逻辑
            if (MainConfig.Area == MainConfig.Type_SBY) {
                MainServer.getInstance().buyProp(props[selIndex][1],
                        props[selIndex][2], props[selIndex][5], false,
                        Integer.parseInt(props[selIndex][3]), props[selIndex][7],
                        false, false);
            }
        } else if (GameConfigInfo.weapon[selIndex - 6][0] == 0) {
            MainServer.getInstance().buyProp(props[6][1], props[6][2],
                    props[6][5], false, -1, props[6][7], false, false);
        } else if (selIndex >= 6 && GameConfigInfo.weapon[selIndex - 6][0] == 1) {
            MainServer.getInstance().buyProp(props[7][1], props[7][2],
                    props[7][5], false, -1, props[7][7], false, false);
        }

        savaData();
    }
}
