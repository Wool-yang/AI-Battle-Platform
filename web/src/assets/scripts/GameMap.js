import { GameObject } from "./GameObject";
import { Wall } from "./Wall";

export class GameMap extends GameObject {
    constructor(ctx, parent) {
        super();
        this.ctx = ctx;         // 画布
        this.parent = parent;   // 画布的父元素

        this.L = 0;             // 地图的格子长度的绝对距离 L

        this.rows = 17;         // 行数
        this.cols = 17;         // 列数

        this.inner_walls_count = 38;
        this.walls = [];
    }

    // 判断两个点之间是否连通 DFS
    check_connetivity(g, sx, sy, tx, ty) {
        if(sx == tx && sy == ty) return true;
        g[sx][sy] = true;

        let dx = [-1, 0, 1, 0], dy = [0, 1, 0, -1];

        for(let i = 0; i < 4; i ++ ) {
            let x = sx + dx[i], y = sy + dy[i];
            if (!g[x][y] && this.check_connetivity(g, x, y, tx, ty))
                return true;
        }
        return false;
    }

    create_walls() {
        // 初始化布尔数组
        const g = [];
        for (let r = 0; r < this.rows; r ++ ) {
            g[r] = [];
            for (let c = 0; c < this.cols; c ++ ) {
                g[r][c]= false;
            }
        }

        // 给地图四周加墙
        for(let r = 0; r < this.rows; r ++ ) {
            g[r][0] = g[r][this.cols - 1] = true;
        }

        for(let c = 0; c < this.rows; c ++ ) {
            g[0][c] = g[this.rows - 1][c] = true;
        }
        
        // 创建随机障碍物
        for (let i = 0; i < this.inner_walls_count / 2; i ++ ) {
            // 随机 1000次 保证单次障碍一定能生成
            for (let j = 0; j < 1000; j ++ ) {
                let r = parseInt(Math.random() * this.rows);
                let c = parseInt(Math.random() * this.cols);

                // 如果随机点已经是障碍物或者随机到玩家生成点则重新随机
                if (g[r][c]) continue;
                if(r == this.rows - 2 && c == 1 || r == 1 && c == this.rows - 2) continue;

                // 对称生成，保证公平性
                g[r][c] = g[c][r] = true;
                break;
            }
        }

        // 玩家之间连通，且另外一个对角也连通，减少地图闭环
        const copy_g1 = JSON.parse(JSON.stringify(g));
        const copy_g2 = JSON.parse(JSON.stringify(g));
        if(!this.check_connetivity(copy_g1, this.rows - 2, 1, 1, this.cols - 2)) return false;
        if(!this.check_connetivity(copy_g2, 1, 1, this.rows - 2, this.cols - 2)) return false;

        // 根据布尔数组生成墙
        for (let r = 0; r < this.rows; r ++ ) {
            for (let c = 0; c < this.cols; c ++ ) {
                if (g[r][c])
                    this.walls.push(new Wall(r, c, this));
            }
        }

        return true;
    }

    start() {
        // 随机 1000 次，确保能生成地图
        for (let i = 0; i < 1000; i ++ ) {
            if (this.create_walls())
                break;
        }
    }

    update_size() {
        // 动态计算格子长度的绝对距离 L
        // this.parent.clientWidth 就是 gamemap 元素的宽度，也就是 PlayGround 的宽度
        // parseInt 取整像素，浮点数会导致生成的地图方块间存在缝隙
        this.L = parseInt(Math.min(this.parent.clientWidth / this.cols, this.parent.clientHeight / this.rows));
        
        // 通过 L 来计算画布的大小
        this.ctx.canvas.width = this.L * this.cols;
        this.ctx.canvas.height = this.L * this.rows;
    }

    update() {
        // 每一帧都更新地图大小
        this.update_size();
        // 进行绘制
        this.render();
    }

    // 将游戏地图进行绘制
    render() {
        const color_even = '#9BDB44', color_odd = '#C5DB44';
        for (let r = 0; r < this.rows; r ++) {
            for (let c = 0; c < this.cols; c ++) {
                if ((r + c) % 2 == 0) {
                    this.ctx.fillStyle = color_even;
                } else {
                    this.ctx.fillStyle = color_odd;
                }
                this.ctx.fillRect(this.L * c, this.L * r, this.L, this.L);
            }
        }
    }
}