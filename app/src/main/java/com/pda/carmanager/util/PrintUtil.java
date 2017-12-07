package com.pda.carmanager.util;

import android.bluetooth.BluetoothSocket;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.github.promeg.pinyinhelper.Pinyin;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * 蓝牙打印工具类
 */
public class PrintUtil {

    private OutputStreamWriter mWriter = null;
    private OutputStream mOutputStream = null;

    public final static int WIDTH_PIXEL = 360;
    public final static int IMAGE_SIZE = 360;

    /**
     * 初始化Pos实例
     *
     * @param encoding 编码
     * @throws IOException
     */
    public PrintUtil(OutputStream outputStream, String encoding) throws IOException {
        mWriter = new OutputStreamWriter(outputStream, encoding);
        mOutputStream = outputStream;
        initPrinter();
    }

    public void print(byte[] bs) throws IOException {
        mOutputStream.write(bs);
    }

    public void printRawBytes(byte[] bytes) throws IOException {
        mOutputStream.write(bytes);
        mOutputStream.flush();
    }

    /**
     * 初始化打印机
     *
     * @throws IOException
     */
    public void initPrinter() throws IOException {
        mWriter.write(0x1B);
        mWriter.write(0x40);
        mWriter.flush();
    }

    /**
     * 打印换行
     *
     * @return length 需要打印的空行数
     * @throws IOException
     */
    public void printLine(int lineNum) throws IOException {
        for (int i = 0; i < lineNum; i++) {
            mWriter.write("\n");
        }
        mWriter.flush();
    }

    /**
     * 打印换行(只换一行)
     *
     * @throws IOException
     */
    public void printLine() throws IOException {
        printLine(1);
    }

    /**
     * 打印空白(一个Tab的位置，约4个汉字)
     *
     * @param length 需要打印空白的长度,
     * @throws IOException
     */
    public void printTabSpace(int length) throws IOException {
        for (int i = 0; i < length; i++) {
            mWriter.write("\t");
        }
        mWriter.flush();
    }

    /**
     * 自定义打印二维码
     * @param img
     */
    public  void printBitmap2 (Bitmap img) {

//        // 获取这个图片的宽和高
//        float width = img.getWidth();
//        float height = img.getHeight();
//        // 创建操作图片用的matrix对象
//        Matrix matrix = new Matrix();
//        // 计算宽高缩放率
//        float scaleWidth = ((float) 575) / width;
//        float scaleHeight = 1.0f;
//        // 缩放图片动作
//        matrix.postScale(scaleWidth, scaleHeight);
//        Bitmap bitmap = Bitmap.createBitmap(img, 0, 0, (int) width,
//                (int) height, matrix, true);

        try {
//                 条码打印指令
            byte[] PRINT_CODE = new byte[9];
            PRINT_CODE[0] = 0x1d;
            PRINT_CODE[1] = 0x68;
            PRINT_CODE[2] = 120;
            PRINT_CODE[3] = 0x1d;
            PRINT_CODE[4] = 0x48;
            PRINT_CODE[5] = 0x10;
            PRINT_CODE[6] = 0x1d;
            PRINT_CODE[7] = 0x6B;
            PRINT_CODE[8] = 0x02;


//                 打印二维码
            Bitmap bmp = img;

            byte[] data = new byte[]{0x1B, 0x33, 0x00};
            print(data);
            data[0] = (byte) 0x00;
            data[1] = (byte) 0x00;
            data[2] = (byte) 0x00;    //重置参数

            int pixelColor;

            // ESC * m nL nH 点阵图
            byte[] escBmp = new byte[]{0x1B, 0x2A, 0x00, 0x00, 0x00};

            escBmp[2] = (byte) 0x21;

            //nL, nH
            escBmp[3] = (byte) (bmp.getWidth() % 256);
            escBmp[4] = (byte) (bmp.getWidth() / 256);

            // 每行进行打印
            for (int i = 0; i < bmp.getHeight() / 24 + 1; i++) {
                print(escBmp);
                for (int j = 0; j < bmp.getWidth(); j++) {
                    for (int k = 0; k < 24; k++) {
                        if (((i * 24) + k) < bmp.getHeight()) {
                            pixelColor = bmp.getPixel(j, (i * 24) + k);
                            if (pixelColor != -1) {
                                data[k / 8] += (byte) (128 >> (k % 8));
                            }
                        }
                    }

                    print(data);
                    // 重置参数
                    data[0] = (byte) 0x00;
                    data[1] = (byte) 0x00;
                    data[2] = (byte) 0x00;
                }
                //换行
                byte[] byte_send1 = new byte[2];
                byte_send1[0] = 0x0d;
                byte_send1[1] = 0x0a;
                print(byte_send1);
            }

            //换行
            byte[] byte_send2 = new byte[2];
            byte_send2[0] = 0x0d;
            byte_send2[1] = 0x0a;
            //发送测试信息
            print(byte_send2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 绝对打印位置
     *
     * @return
     * @throws IOException
     */
    public byte[] setLocation(int offset) throws IOException {
        byte[] bs = new byte[4];
        bs[0] = 0x1B;
        bs[1] = 0x24;
        bs[2] = (byte) (offset % 256);
        bs[3] = (byte) (offset / 256);
        return bs;
    }

    public byte[] getGbk(String stText) throws IOException {
        byte[] returnText = stText.getBytes("GBK"); // 必须放在try内才可以
        return returnText;
    }

    private int getStringPixLength(String str) {
        int pixLength = 0;
        char c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            if (Pinyin.isChinese(c)) {
                pixLength += 24;
            } else {
                pixLength += 12;
            }
        }
        return pixLength;
    }

    public int getOffset(String str) {
        return WIDTH_PIXEL - getStringPixLength(str);
    }

    /**
     * 打印文字
     *
     * @param text
     * @throws IOException
     */
    public void printText(String text) throws IOException {
        mWriter.write(text);
        mWriter.flush();
    }

    /**
     * 对齐0:左对齐，1：居中，2：右对齐
     */
    public void printAlignment(int alignment) throws IOException {
        mWriter.write(0x1b);
        mWriter.write(0x61);
        mWriter.write(alignment);
    }

    public void printLargeText(String text) throws IOException {

        mWriter.write(0x1b);
        mWriter.write(0x21);
        mWriter.write(48);

        mWriter.write(text);

        mWriter.write(0x1b);
        mWriter.write(0x21);
        mWriter.write(0);

        mWriter.flush();
    }

    public void printTwoColumn(String title, String content) throws IOException {
        int iNum = 0;
        byte[] byteBuffer = new byte[100];
        byte[] tmp;

        tmp = getGbk(title);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = setLocation(getOffset(content));
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = getGbk(content);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);

        print(byteBuffer);
    }

    public void printThreeColumn(String left, String middle, String right) throws IOException {
        int iNum = 0;
        byte[] byteBuffer = new byte[200];
        byte[] tmp = new byte[0];

        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = getGbk(left);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        int pixLength = getStringPixLength(left) % WIDTH_PIXEL;
        if (pixLength > WIDTH_PIXEL / 2 || pixLength == 0) {
            middle = "\n\t\t" + middle;
        }

        tmp = setLocation(192);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = getGbk(middle);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = setLocation(getOffset(right));
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);
        iNum += tmp.length;

        tmp = getGbk(right);
        System.arraycopy(tmp, 0, byteBuffer, iNum, tmp.length);

        print(byteBuffer);
    }

    public void printDashLine() throws IOException {
        printText("--------------------------------");
    }

    public void printBitmap(Bitmap bmp) throws IOException {
        bmp = PrintBitmapUtil.compressPic(bmp);
        byte[] bmpByteArray = PrintBitmapUtil.draw2PxPoint(bmp);
        printRawBytes(bmpByteArray);
    }

    /*************************************************************************
     * 假设一个360*360的图片，分辨率设为24, 共分15行打印 每一行,是一个 360 * 24 的点阵,y轴有24个点,存储在3个byte里面。
     * 即每个byte存储8个像素点信息。因为只有黑白两色，所以对应为1的位是黑色，对应为0的位是白色
     **************************************************************************/
//    private byte[] draw2PxPoint(Bitmap bmp) {
//        //先设置一个足够大的size，最后在用数组拷贝复制到一个精确大小的byte数组中
//        int size = bmp.getWidth() * bmp.getHeight() / 8 + 1000;
//        byte[] tmp = new byte[size];
//        int k = 0;
//        // 设置行距为0
//        tmp[k++] = 0x1B;
//        tmp[k++] = 0x33;
//        tmp[k++] = 0x00;
//        // 居中打印
//        tmp[k++] = 0x1B;
//        tmp[k++] = 0x61;
//        tmp[k++] = 1;
//        for (int j = 0; j < bmp.getHeight() / 24f; j++) {
//            tmp[k++] = 0x1B;
//            tmp[k++] = 0x2A;// 0x1B 2A 表示图片打印指令
//            tmp[k++] = 33; // m=33时，选择24点密度打印
//            tmp[k++] = (byte) (bmp.getWidth() % 256); // nL
//            tmp[k++] = (byte) (bmp.getWidth() / 256); // nH
//            for (int i = 0; i < bmp.getWidth(); i++) {
//                for (int m = 0; m < 3; m++) {
//                    for (int n = 0; n < 8; n++) {
//                        byte b = px2Byte(i, j * 24 + m * 8 + n, bmp);
//                        tmp[k] += tmp[k] + b;
//                    }
//                    k++;
//                }
//            }
//            tmp[k++] = 10;// 换行
//        }
//        // 恢复默认行距
//        tmp[k++] = 0x1B;
//        tmp[k++] = 0x32;
//
//        byte[] result = new byte[k];
//        System.arraycopy(tmp, 0, result, 0, k);
//        return result;
//    }
/*************************************************************************
 * 假设一个240*240的图片，分辨率设为24, 共分10行打印
 * 每一行,是一个 240*24 的点阵, 每一列有24个点,存储在3个byte里面。
 * 每个byte存储8个像素点信息。因为只有黑白两色，所以对应为1的位是黑色，对应为0的位是白色
 **************************************************************************/

    /**
     * 把一张Bitmap图片转化为打印机可以打印的字节流
     *
     * @param bmp
     * @return
     */
    public static byte[] draw2PxPoint(Bitmap bmp) {
        //用来存储转换后的 bitmap 数据。为什么要再加1000，这是为了应对当图片高度无法
        //整除24时的情况。比如bitmap 分辨率为 240 * 250，占用 7500 byte，
        //但是实际上要存储11行数据，每一行需要 24 * 240 / 8 =720byte 的空间。再加上一些指令存储的开销，
        //所以多申请 1000byte 的空间是稳妥的，不然运行时会抛出数组访问越界的异常。
        int size = bmp.getWidth() * bmp.getHeight() / 8 + 1000;
        byte[] data = new byte[size];
        int k = 0;
        //设置行距为0的指令
        data[k++] = 0x1B;
        data[k++] = 0x33;
        data[k++] = 0x00;
        // 逐行打印
        for (int j = 0; j < bmp.getHeight() / 24f; j++) {
            //打印图片的指令
            data[k++] = 0x1B;
            data[k++] = 0x2A;
            data[k++] = 33;
            data[k++] = (byte) (bmp.getWidth() % 256); //nL
            data[k++] = (byte) (bmp.getWidth() / 256); //nH
            //对于每一行，逐列打印
            for (int i = 0; i < bmp.getWidth(); i++) {
                //每一列24个像素点，分为3个字节存储
                for (int m = 0; m < 3; m++) {
                    //每个字节表示8个像素点，0表示白色，1表示黑色
                    for (int n = 0; n < 8; n++) {
                        byte b = px2Byte(i, j * 24 + m * 8 + n, bmp);
                        data[k] += data[k] + b;
                    }
                    k++;
                }
            }
            data[k++] = 10;//换行
        }
        return data;
    }

//    /**
//     * 图片二值化，黑色是1，白色是0
//     *
//     * @param x   横坐标
//     * @param y   纵坐标
//     * @param bit 位图
//     * @return
//     */
//    private byte px2Byte(int x, int y, Bitmap bit) {
//        if (x < bit.getWidth() && y < bit.getHeight()) {
//            byte b;
//            int pixel = bit.getPixel(x, y);
//            int red = (pixel & 0x00ff0000) >> 16; // 取高两位
//            int green = (pixel & 0x0000ff00) >> 8; // 取中两位
//            int blue = pixel & 0x000000ff; // 取低两位
//            int gray = RGB2Gray(red, green, blue);
//            if (gray < 128) {
//                b = 1;
//            } else {
//                b = 0;
//            }
//            return b;
//        }
//        return 0;
//    }
//
//    /**
//     * 图片灰度的转化
//     */
//    private int RGB2Gray(int r, int g, int b) {
//        int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b); // 灰度转化公式
//        return gray;
//    }

    /**
     * 灰度图片黑白化，黑色是1，白色是0
     *
     * @param x   横坐标
     * @param y   纵坐标
     * @param bit 位图
     * @return
     */
    public static byte px2Byte(int x, int y, Bitmap bit) {
        if (x < bit.getWidth() && y < bit.getHeight()) {
            byte b;
            int pixel = bit.getPixel(x, y);
            int red = (pixel & 0x00ff0000) >> 16; // 取高两位
            int green = (pixel & 0x0000ff00) >> 8; // 取中两位
            int blue = pixel & 0x000000ff; // 取低两位
            int gray = RGB2Gray(red, green, blue);
            if (gray < 128) {
                b = 1;
            } else {
                b = 0;
            }
            return b;
        }
        return 0;
    }

    /**
     * 图片灰度的转化
     */
    private static int RGB2Gray(int r, int g, int b) {
        int gray = (int) (0.29900 * r + 0.58700 * g + 0.11400 * b);  //灰度转化公式
        return gray;
    }
    /**
     * 对图片进行压缩(不去除透明度)
     * @param bitmapOrg
     */
    public static Bitmap compressBitmap(Bitmap bitmapOrg) {
        // 加载需要操作的图片，这里是一张图片
//        Bitmap bitmapOrg = BitmapFactory.decodeResource(getResources(),R.drawable.alipay);
        // 获取这个图片的宽和高
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        // 定义预转换成的图片的宽度和高度
        int newWidth = 360;
        int newHeight = 360;
        // 计算缩放率，新尺寸除原始尺寸
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0, width,height, matrix, true);
        // 将上面创建的Bitmap转换成Drawable对象，使得其可以使用在ImageView, ImageButton中
//        BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
        return resizedBitmap;
    }
    /**
     * 对图片进行压缩（去除透明度）
     *
     * @param bitmapOrg
     */
    private Bitmap compressPic(Bitmap bitmapOrg) {
        // 获取这个图片的宽和高
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        // 定义预转换成的图片的宽度和高度
        int newWidth = IMAGE_SIZE;
        int newHeight = IMAGE_SIZE;
        Bitmap targetBmp = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
        Canvas targetCanvas = new Canvas(targetBmp);
        targetCanvas.drawColor(0xffffffff);
        targetCanvas.drawBitmap(bitmapOrg, new Rect(0, 0, width, height), new Rect(0, 0, newWidth, newHeight), null);
        return targetBmp;
    }

    public static void printTest(BluetoothSocket bluetoothSocket, Bitmap bitmap,String address,String StopNum,String carNum,String stopTime
    ,String Arrears ,String ArrearsAddress,String ArrearsStopNum,String ArrearsStart,String ArrearsEnd) {

        try {
            PrintUtil pUtil = new PrintUtil(bluetoothSocket.getOutputStream(), "GBK");
            // 店铺名 居中 放大
            pUtil.printAlignment(1);
            pUtil.printLargeText("泊讯停车|临街车位缴费小票");
            pUtil.printLine(3);

            pUtil.printText("本次停车信息");
            pUtil.printLine(1);
            pUtil.printAlignment(0);
            pUtil.printTwoColumn("停车街道:", address);
            pUtil.printLine(1);

            pUtil.printTwoColumn("车位编号:", StopNum);
            pUtil.printLine(1);

            pUtil.printTwoColumn("车牌号:",carNum);
            pUtil.printLine(1);

            pUtil.printTwoColumn("停车时刻:",stopTime);
            pUtil.printLine(1);

            // 分隔线
            pUtil.printDashLine();
            pUtil.printLine(1);
            pUtil.printDashLine();
            pUtil.printLine(2);

            //打印商品列表
            pUtil.printText("欠费记录");
            pUtil.printTwoColumn("欠费记录:","￥"+Arrears);
            pUtil.printLine(1);
            pUtil.printTwoColumn("停车街道:", ArrearsAddress);
            pUtil.printLine(1);

            pUtil.printTwoColumn("车位编号:", ArrearsStopNum);
            pUtil.printLine(1);

            pUtil.printTwoColumn("停车时段:", ArrearsStart);
            pUtil.printLine();

            pUtil.printAlignment(2);
            pUtil.printText("至"+ArrearsEnd);
            pUtil.printLine(1);
            pUtil.printAlignment(0);
            pUtil.printTwoColumn("欠费金额:", Arrears+"元");


            // 分隔线
            pUtil.printDashLine();
            pUtil.printLine(1);
            pUtil.printDashLine();
            pUtil.printLine(2);

            pUtil.printAlignment(1);
            pUtil.printText("您离开时可用支付宝或微信");
            pUtil.printLine();
            pUtil.printAlignment(1);
            pUtil.printText("扫描下方二维码自主缴费");
            pUtil.printLine(2);
            pUtil.printAlignment(1);
            pUtil.printBitmap(bitmap);

            pUtil.printLine(2);

        } catch (IOException e) {

        }
    }
}