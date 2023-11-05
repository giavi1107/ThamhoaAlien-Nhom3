
package game;

import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.JPanel;

/**
 *
 * @author
 */
//r


// HÀM CHA CỦA PHI THUYỀN - ALIEN - BULLETS
public class InSpace extends JPanel{
    // VỊ TRÍ - KÍCH THƯỚC - TRẠNG THÁI HIỂN THỊ
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean vis;
    
    public InSpace(int x, int y) {
        this.x = x;
        this.y = y;
        vis = true;
    }
    
    public void setW(int w){
        this.width = w;
    }
    
    public void setH(int h){
        this.height = h;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisible() {
        return vis;
    }

    public void setVisible(Boolean visible) {
        this.vis = visible;
    }

    // TẠO VÙNG HÌNH CHỮ NHẬT
    // sử dụng để kiểm tra va chạm
    // xác định vùng bao quanh một đối tượng trong trò chơi
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
