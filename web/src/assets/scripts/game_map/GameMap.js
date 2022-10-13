import { GameObject } from "../GameObject";
import { Snake } from "../game_player/Snake";
import { Wall } from "./Wall";

export class GameMap extends GameObject {
    constructor(ctx, parent, store) {
        super();
        this.ctx = ctx;         // 画布
        this.parent = parent;   // 画布的父元素
        this.store = store;

        this.L = 0;             // 地图的格子长度的绝对距离 L

        this.rows = 17;         // 行数
        this.cols = 18;         // 列数

        // this.inner_walls_count = 38;

        this.walls = [];

        this.snakes = [
            new Snake({id: 0, color: '#FF3030', r: this.rows - 2, c: 1}, this),
            new Snake({id: 1, color: '#00B2EE', r: 1, c: this.cols - 2}, this)
        ]
    }

    create_walls() {        
        const g = this.store.state.pk.game_map;

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
        this.create_walls();
        this.add_listening_events();
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

        // 检测所有蛇是否准备好进入下一回合
        if (this.check_snake_ready()) {
            this.next_step();
        }

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

    /*----------------------- 蛇的逻辑部分 -----------------------*/

    // 判断两条蛇是否都准备好下一回合
    check_snake_ready() {
        for (const snake of this.snakes) {
            if (snake.status !== 'idle') return false;
            if (snake.direction === -1) return false;
        }
        
        return true;
    }

    // 让蛇进入下一回合
    next_step() {   
        for (const snake of this.snakes) {
            snake.next_step();
        }
    }
    
    // 绑定获取用户信息的时间
    add_listening_events() {
        if (this.store.state.record.is_record) {
            let k = 0;

            const [snake0, snake1] = this.snakes;
            const a_steps = this.store.state.record.a_steps;
            const b_steps = this.store.state.record.b_steps;
            const winner = this.store.state.record.record_winner;

            const interval_id = setInterval(() => {
                // 最后一步不能渲染移动，需要修改蛇的颜色
                if (k >= a_steps.length - 1) {
                    if (winner === "all" || winner === "B") {
                        snake0.status = "die";
                    } 
                    if (winner === "all" || winner === "A") {
                        snake1.status = "die";
                    }
                    // 停止定时任务
                    clearInterval(interval_id);
                } else {
                    snake0.set_direction(parseInt(a_steps[k]));
                    snake1.set_direction(parseInt(b_steps[k]));
                    k ++;
                }
            }, 300)         // 200ms 蛇可以渲染完一步，这边 300ms 输入一次
        } else {
            // 需要先将 canvas 聚焦
            this.ctx.canvas.focus();

            this.ctx.canvas.addEventListener("keydown", e => {
                let d = -1;

                if (e.key === 'w') d = 0;
                else if (e.key === 'd') d = 1;
                else if (e.key === 's') d = 2;
                else if (e.key === 'a') d = 3;

                if (d >= 0) {
                    this.store.state.pk.socket.send(JSON.stringify({
                        event: "move",
                        direction: d
                    }));
                }
                
            });
        }
    }
}