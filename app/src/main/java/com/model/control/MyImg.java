package com.model.control;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.icu.text.DecimalFormat;
import android.text.BoringLayout;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import com.game.mouse.context.Config;
import com.game.mouse.screen.MainMidlet;
import com.model.mainServer.MainServer;

import static android.content.Context.WINDOW_SERVICE;

/**
 * 图片整合：用来保存图片
 *
 * @author Administrator
 */
public class MyImg {
    // 图片保存
    private Image img;
    // 镜像180图片
    private Image imgTrans[];
    // 图片路径
    private String imgPath = "";

    /**
     * 初始是否加载图片,默认加载 优先从hashtable中取图片
     *
     * @param imgName
     * @param bLoad
     * 若设置为false，请把图片放于本地
     */
    public static int httpgetImg = 0;

    public MyImg(String imgPath) {
        this.imgPath = imgPath;
        this.img = ImageManager.getInstance().getImg(imgPath);
        if (img != null) {
            // System.out.println("从hashtable中读取图片。。。。。。。。。。。");
            return;
        }
        loadImg(imgPath);
    }

    public MyImg(String imgKey, int trans) {
        this(imgKey);
        imgTrans = new Image[2];
        if (trans == Sprite.TRANS_MIRROR) {
            imgTrans[0] = Image.createImage(img, 0, 0, img.getWidth(), img
                    .getHeight(), trans);
        }
        if (trans == Sprite.TRANS_MIRROR_ROT180) {
            imgTrans[1] = Image.createImage(img, 0, 0, img.getWidth(), img
                    .getHeight(), trans);
        }
    }

    /**
     * 加载图片
     *
     * @param img
     */
    public MyImg(Image img, int imgX, int imgY, int imgW, int imgH,
                 int transform) {
        this.img = Image.createImage(img, imgX, imgY, imgW, imgH, transform);
    }

    private boolean bHaveLoad;

    public void loadImg(String imgPath) {
        if (bHaveLoad) {
            return;
        }
        if (img == null) {
            this.img = ImageManager.getInstance().getImg(imgPath);
        }
        if (img == null) {
            // System.out.println("需要加载图片。。。。。。");
            img = getLocalImg(imgPath);
            bHaveLoad = true;
            if (img == null) {
//				if (Config.getResType == 1) {
//					img = getSerImageByIntface(imgPath);
//				} else {
//					img = getSerImage(imgPath);
//				}
            }
            if (img != null)
                ImageManager.getInstance().addImg(imgPath, img);
        }
    }

    // 创建本地图片
    private Image getLocalImg(String imgPath) {
        Image localImg = null;
        try {
            System.out.println("加载本地图片>>" + imgPath);
            localImg = Image.createImage("/" + imgPath);
            System.out.println("w="+localImg.getWidth()+",h="+localImg.getHeight());
            if(MainMidlet.scaleX != 1.0 ||  MainMidlet.scaleY != 1.0) {
                Matrix matrix = new Matrix();
                matrix.setScale(MainMidlet.scaleX, MainMidlet.scaleY);

                Bitmap bitmap = Bitmap.createBitmap(localImg.getBitmap(), 0, 0, localImg.getWidth(), localImg.getHeight(), matrix, false);
                localImg = Image.getImage(bitmap);

            }


        } catch (IOException e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
        return localImg;
    }

    // 获得服务器端图片
    public Image getSerImage(String imgName) {
        String URL = getHttpUrl() + imgName;
        System.out.println(URL);
        HttpConnection httpConn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        Image image = null;
        byte[] data = null;
        try {
            httpConn = (HttpConnection) Connector.open(URL);
            int responseCode;
            if ((responseCode = httpConn.getResponseCode()) == HttpConnection.HTTP_OK) {
                is = httpConn.openInputStream();

                int len = (int) httpConn.getLength();
                if (len <= 0) {
                    return null;
                }
                // 第一种方式
                if (MyImg.httpgetImg == 0) {
                    int actual = 0;
                    int bytesread = 0;
                    data = new byte[len];
                    do {
                        actual = is.read(data, bytesread, len - bytesread);
                        bytesread += actual;
                        if (bytesread == len)
                            break;
                    } while (actual != -1);
                    image = Image.createImage(data, 0, data.length);
                    System.out.println("创建" + imgName + "图片完成。。");
                } else {
                    // 第二种方式
                    baos = new ByteArrayOutputStream();
                    int ch = 0;
                    while ((ch = is.read()) != -1) {
                        baos.write(ch);
                    }
                    byte[] imageData = baos.toByteArray();
                    baos.flush();
                    image = Image.createImage(imageData, 0, imageData.length);
                    System.out.println("创建" + imgName + "图片完成。。");
                }
            } else {
                System.out.println("response code:" + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null)
                    baos.close();
                if (is != null)
                    is.close();
                if (httpConn != null)
                    httpConn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return image;
    }

    public Image getSerImageByIntface(String imgName) {
        Image image = null;
        try {
            if (imgName.startsWith("feedpage")) {
                imgName = "fp" + imgName.substring(8, imgName.length());
            } else if (imgName.startsWith("homepage")) {
                imgName = "hp" + imgName.substring(8, imgName.length());
            } else if (imgName.startsWith("helppage")) {
                imgName = "hep" + imgName.substring(8, imgName.length());
            } else if (imgName.startsWith("gamepage")) {
                imgName = "gp" + imgName.substring(8, imgName.length());
            } else if (imgName.startsWith("passpage")) {
                imgName = "pp" + imgName.substring(8, imgName.length());
            } else if (imgName.startsWith("rankpage")) {
                imgName = "rp" + imgName.substring(8, imgName.length());
            } else if (imgName.startsWith("shoppage")) {
                imgName = "sp" + imgName.substring(8, imgName.length());
            } else if (imgName.startsWith("storypage")) {
                imgName = "stp" + imgName.substring(9, imgName.length());
            } else if (imgName.startsWith("sprite")) {
                imgName = "s" + imgName.substring(6, imgName.length());
            }
            imgName = imgName.substring(0, imgName.length() - 4).replace('/',
                    '=');
//			byte[] data = MainServer.getInstance().getRes(imgName);
//			if (data != null && data.length > 0) {
//				image = Image.createImage(data, 0, data.length);
//			}
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }

    // 画整张图片
    public void drawImage(Graphics g, int x, int y, int anctor) {
        // System.out.println("图片名是 "+imgName);
        // loadImg(imgPath);
        if (img != null) {
            g.drawImage(img, (int)(x * MainMidlet.scaleX), (int)(y * MainMidlet.scaleY), anctor);
        }
        // g.drawRegion(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8)
        // g.
    }

    // 画部分图片
    public void drawRegion(Graphics g, int imgX, int imgY, int imgW, int imgH,
                           int translate, int x, int y, int anctor) {
        // loadImg(imgPath);
        if (img != null) {
            if (Sprite.TRANS_MIRROR == translate) {
                // g.drawRegion(imgTrans[0], imgX, imgY, imgW, imgH, 0, x, y,
                // anctor);
                g.drawRegion(img, (int)(imgX * MainMidlet.scaleX), (int)(imgY * MainMidlet.scaleY), (int)(imgW * MainMidlet.scaleX), (int)(imgH * MainMidlet.scaleY), Sprite.TRANS_MIRROR,
                        (int)(x * MainMidlet.scaleX), (int)(y * MainMidlet.scaleY), anctor);
            } else if (Sprite.TRANS_MIRROR_ROT180 == translate) {
                g.drawRegion(imgTrans[1], (int)(imgX * MainMidlet.scaleX), (int)(imgY * MainMidlet.scaleY), (int)(imgW * MainMidlet.scaleX), (int)(imgH * MainMidlet.scaleY), 0, (int)(x * MainMidlet.scaleX), (int)(y * MainMidlet.scaleY),
                        anctor);
            } else {
                g.drawRegion(img, (int)(imgX * MainMidlet.scaleX), (int)(imgY * MainMidlet.scaleY), (int)(imgW * MainMidlet.scaleX), (int)(imgH * MainMidlet.scaleY), 0, (int)(x * MainMidlet.scaleX), (int)(y * MainMidlet.scaleY), anctor);
            }
        }
    }

    int width = 0;
    int height = 0;

    // 获得图片长度
    public int getWidth() {
        if (img == null) {
            return 0;
        }
        if (width == 0) {
            width = (int)(img.getWidth()/MainMidlet.scaleX);
        }

        return width;
    }

    // 获得图片高度
    public int getHeight() {
        if (img == null) {
            return 0;
        }
        if (height == 0) {
            height = (int)(img.getHeight()/MainMidlet.scaleY);
        }
        return height;
    }

    // 获得图片的值
    public String toString() {
        if (img == null) {
            return "null";
        }
        return img.toString();
    }

    // 清除图片
    public void release() {
        img = null;
        System.gc();
    }

    public Image getImg() {
        loadImg(imgPath);
        return img;
    }

    private String getHttpUrl() {
        return Config.resURL;
    }

    public void release(boolean flag) {
        if (flag) {
            ImageManager.getInstance().removeImg(imgPath);
        }
        img = null;
    }


}
