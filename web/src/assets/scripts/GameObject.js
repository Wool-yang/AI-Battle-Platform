// 储存所有游戏物体
const GAME_OBJECTS = [];

export class GameObject {
    constructor() {
        GAME_OBJECTS.push(this);
        this.timedelta = 0;             // 当前帧与上一帧的时间间隔
        this.has_called_first = false;  // 游戏对象是否初始化执行start
    }

    // 只在创建时执行一次
    start() {

    }

    // 每一帧执行一次，除了第一帧之外
    update() {

    }

    // 删除前执行
    on_distroy() {

    }

    // 删除对象
    distroy() {
        this.on_distroy();

        for (let i in GAME_OBJECTS) {
            const obj = GAME_OBJECTS[i];
            if (obj === this) {
                GAME_OBJECTS.splice(i);
                break;
            }
        }
    }
}

let last_timestap; // 上一次执行时间
const step = timestap => {
    for (let obj of GAME_OBJECTS) {
        if (!obj.has_called_first) {
            obj.has_called_first = true;
            obj.start(); 
        } else {
            // 计算时间差
            obj.timedelta = timestap- last_timestap;
            // 执行 update
            obj.update();
        }
    }
    last_timestap = timestap;
    
    // 重复调用
    requestAnimationFrame(step);
}

// 在下一帧浏览器刷新时调用回调函数
requestAnimationFrame(step);