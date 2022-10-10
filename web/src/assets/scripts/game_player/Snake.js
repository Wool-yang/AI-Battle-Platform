import { GameObject } from "../GameObject";
import { Cell } from "./Cell";


export class Snake extends GameObject {
    constructor(info, gamemap) {
        super();

        this.id = info.id;          // 蛇的 id
        this.color = info.color;    // 蛇的颜色
        this.gamemap = gamemap;     // gamemap 对象，用来获取 ctx 信息

        this.cells = [new Cell(info.r, info.c)];    // 存放蛇的身体，cells[0]存放蛇头

        this.speed = 5;         // 蛇每秒钟走五个格子

        this.direction = -1;    // -1 表示无指令，0 1 2 3 代表上右下左
        this.status = 'idle';   // idle 表示静止，move 表示正在移动，die 表示已经死亡

        this.next_cell = null;  // 下一步的 cell
        this.dr = [-1, 0, 1, 0];   // 行偏移量
        this.dc = [0, 1, 0, -1];   // 列偏移量

        this.step = 0;      // 当前回合数

        this.eps = 1e-2;    // 判断点移动到终点允许存在的误差

        this.eye_direction = 0;                     // 蛇头的默认方向
        if (this.id == 1) this.eye_direction = 2;   // 

        this.eye_dx = [         // 蛇眼不同方向 x 偏移量
            [-1, 1],
            [1, 1],
            [1, -1],
            [-1, -1]
        ]

        this.eye_dy = [         // 蛇眼不同方向 y 偏移量
            [-1, -1],
            [-1, 1],
            [1, 1],
            [1, -1]
        ]
    }

    start() {

    }

    update() {
        if (this.status === 'move')
            this.update_move();
        this.render();
    }

    // 移动状态下执行 每帧执行 1 次
    update_move() {
        const move_distance = this.speed * this.timedelta / 1000;    // 每两帧移动的距离
        
        // 计算与目标点的距离
        const dx = this.next_cell.x - this.cells[0].x;
        const dy = this.next_cell.y - this.cells[0].y;          
        const distance = Math.sqrt(dx * dx + dy * dy);          
        
        // 如果与目标点的距离小于误差值，则可以视为已移动到目标点
        if (distance < this.eps){
            // 将状态切换为 idle + 等待外部输入状态，停止运动
            this.status = 'idle';
            
            // 用目标点替换掉临时头部
            this.cells[0] = this.next_cell;
            // 清空目标点
            this.next_cell = null;

            // 蛇不变长，去掉蛇尾
            if(!this.check_length_increasing()) {
                this.cells.pop();
            }
        } else {
            // 将帧移动距离在 x, y 上进行投影
            this.cells[0].x += move_distance * dx / distance;
            this.cells[0].y += move_distance * dy / distance;


            if(!this.check_length_increasing()) {
                const k = this.cells.length;
                const tail = this.cells[k - 1];
                const tail_target = this.cells[k - 2];

                const tail_dx = tail_target.x - tail.x;
                const tail_dy = tail_target.y - tail.y;
                
                tail.x += move_distance * tail_dx / distance;
                tail.y += move_distance * tail_dy / distance;
            }
        }
    }

    set_direction(d) {
        this.direction = d;
    }

    // 检测当前回合，蛇的长度是否增加
    check_length_increasing() {
        if (this.step <= 10) return true;
        if (this.step % 3 == 1) return true;
        return false;
    }


    // 将蛇的状态转换为移动下一步
    next_step() {
        const d = this.direction;       // 拿到当前方向状态

        // 通过当前方向状态来计算 next_cell的位置
        this.next_cell = new Cell(this.cells[0].r + this.dr[d], this.cells[0].c + this.dc[d]);

        this.direction = -1;            // 清空用户输入操作方向
        this.status = "move";           // 更改状态，准备好移动
        this.step ++;                   // 增加回合数

        this.eye_direction = d;         // 更改蛇头方向

        // 创建新的头部 Cell
        // 将 k - 1 到 0 索引的 Cell 向后复制一份
        // 新的头部相当于是原来的头部的一个复制
        const k = this.cells.length;
        for (let i = k; i > 0; i -- ) {
            // 这边不序列化反序列化的话会导致最后全部引用第 0 位元素
            this.cells[i] = JSON.parse(JSON.stringify(this.cells[i - 1]));
        }
    }

    // 对当前蛇的每一个 Cell 进行绘制
    render() {
        const L = this.gamemap.L;
        const lengthL = L * 0.8;

        const ctx = this.gamemap.ctx;

        ctx.fillStyle = this.color;
        if (this.status === 'die') {
            ctx.fillStyle = 'white';
        }

        for (const cell of this.cells) {
            // 开启路径
            ctx.beginPath();

            // 绘制圆弧 中点坐标 半径 起始角度 终止角度
            ctx.arc(cell.x * L, cell.y * L, lengthL / 2, 0, Math.PI * 2);
            ctx.fill();
        }


        /*----------------------- 美化部分 -----------------------*/
        
        // 平滑蛇身，每两个 Cell 之间生成一个矩形，并且将蛇的宽度调小一些
        for (let i = 1; i < this.cells.length; i ++ ) {
            const a = this.cells[i], b = this.cells[i - 1];

            // 重合会发生在蛇尾移动时，最后几帧非常重合，所以不需要绘制
            if (Math.abs(a.x - b.x) < this.eps && Math.abs(a.y - b.y) < this.eps) {
                continue;
            }

            // 坐标计算
            if (Math.abs(a.x - b.x) < this.eps) {
                ctx.fillRect((a.x - 0.4) * L, Math.min(a.y, b.y) * L, lengthL, Math.abs(a.y - b.y) * L);
            } else {
                ctx.fillRect(Math.min(a.x, b.x) * L, (a.y - 0.4) * L, Math.abs(a.x - b.x) * L, lengthL);
            }
        }

        // 绘制眼睛
        ctx.fillStyle = 'black';

        for (let i = 0; i < 2; i ++ ) {
            const eye_x = (this.cells[0].x + this.eye_dx[this.eye_direction][i] * 0.15) * L ;
            const eye_y = (this.cells[0].y + this.eye_dy[this.eye_direction][i] * 0.15) * L;
            ctx.beginPath();
            ctx.arc(eye_x, eye_y, L * 0.05, 0, Math.PI * 2);
            ctx.fill();
        }
    }

}