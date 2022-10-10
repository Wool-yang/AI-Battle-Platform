package com.abp.backend.consumer.utils;

import com.abp.backend.consumer.WebSocketServer;
import com.abp.backend.pojo.Record;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.locks.ReentrantLock;

public class Game extends Thread{
    private final Integer rows;
    private final Integer cols;
    private final Integer inner_walls_count;

    private int g[][];

    private final Player playerA, playerB;  // 维护两名玩家的信息

    // 两名玩家的下一步操作
    private Integer nextStepA = null;
    private Integer nextStepB = null;

    private ReentrantLock lock = new ReentrantLock();   // 加锁解决两个线程同时写同一变量

    private String status = "playing";                  // 记录游戏状态 playing -> finished

    private String winner = "";                         // all: 平局, A: A赢, B: B赢

    private final static int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};

    public Game(Integer rows, Integer cols, Integer inner_walls_count, Integer idA, Integer idB) {
        this.rows = rows;
        this.cols = cols;
        this.inner_walls_count = inner_walls_count;
        this.g = new int[rows][cols];

        this.playerA = new Player(idA, this.rows - 2, 1, new ArrayList<>());
        this.playerB = new Player(idB, 1, this.cols - 2, new ArrayList<>());
        System.out.println("create game" + " " + idA + " " + idB);
    }

    public Player getPlayerA() {
        return this.playerA;
    }

    public Player getPlayerB() {
        return this.playerB;
    }

    public void setNextStepA(Integer nextStepA) {
        lock.lock();
        try {
            this.nextStepA = nextStepA;
        } finally {
            lock.unlock();
        }
    }

    public void setNextStepB(Integer nextStepB) {
        lock.lock();
        try {
            this.nextStepB = nextStepB;
        } finally {
            lock.unlock();
        }
    }

    public int[][] getG() {
        return g;
    }

    private boolean  drawG() {
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                g[i][j] = 0;
            }
        }

        for (int r = 0; r < this.rows; r++) {
            g[r][0] = g[r][this.cols - 1] = 1;
        }

        for (int c = 0; c < this.cols; c++) {
            g[0][c] = g[this.rows - 1][c] = 1;
        }

        Random random = new Random();
        for (int i = 0; i < this.inner_walls_count / 2; i++) {
            for (int j = 0; j < 1000; j++) {
                int r = random.nextInt(this.rows);
                int c = random.nextInt(this.cols);

                if (g[r][c] == 1 || g[this.rows - 1 - r][this.cols - 1 - c] == 1) continue;
                if (r == this.rows - 2 && c == 1 || r == 1 && c == this.cols - 2) continue;

                g[r][c] = g[this.rows - 1 - r][this.cols - 1 - c] = 1;
                break;
            }
        }

        // 检查两个对角连通性会导致 RestTemplate 通信异常，应该是超时了
        return check_connectivity(this.rows - 2, 1, 1, this.cols - 2)/* &&
               check_connectivity(1, 1, this.rows - 2, this.cols - 2)*/;
    }

    private boolean check_connectivity(int sx, int sy, int tx, int ty) {
        if (sx == tx && sy == ty) return true;
        g[sx][sy] = 1;

        for (int i = 0; i < 4; i++) {
            int x = sx + dx[i];
            int y = sy + dy[i];

            if(x >= 0 && x < this.rows && y >= 0 && y < this.cols && g[x][y] == 0)
                if (check_connectivity(x, y, tx, ty)) {
                    g[sx][sy] = 0;          // 恢复现场
                    return true;
                }
        }

        g[sx][sy] = 0;
        return false;
    }

    public void createMap() {
        for (int i = 0; i < 1000; i++) {
            System.out.print(i);
            if (drawG())
                break;
        }
    }

    //等待两名玩家的下一步操作
    private boolean nextStep() {
        try {
            /* 这里睡 200ms 的目的是为了防止 Bot 的输入速度过快
            *  前端每秒最多只能渲染出五格的移动 (speed = 5)
            *  蛇的帧移动距离是根据 speed 来计算的
            *  如果说后端输入速度过快会导致丢失部分操作   */
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < 25; i++ ) {
            try {
                lock.lock();
                try {
                    if (nextStepA != null && nextStepB != null) {
                        playerA.getSteps().add(nextStepA);
                        playerB.getSteps().add(nextStepB);
                        return true;
                    }
                } finally {
                    lock.unlock();
                }
                // 每睡一秒检测一次输入
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private String getMapString() {
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                res.append(g[i][j]);
            }
        }

        return res.toString();
    }
    private void saveToDatabase() {
        Record record = new Record(
                null,
                playerA.getId(),
                playerA.getSx(),
                playerA.getSy(),
                playerB.getId(),
                playerB.getSx(),
                playerB.getSy(),
                playerA.getStepsString(),
                playerB.getStepsString(),
                getMapString(),
                winner,
                new Date()
        );

        WebSocketServer.recordMapper.insert(record);
    }

    private void sendResult() {         // 向两个玩家返回结果
        JSONObject resp = new JSONObject();
        resp.put("event", "result");
        resp.put("winner", winner);

        saveToDatabase();                       // 持久化到数据库

        sendAllMessage(resp.toJSONString());    // 返回结果到前端
    }

    private boolean check_valid(List<Cell> cellsA, List<Cell> cellsB) {
        int n = cellsA.size();

        Cell cell = cellsA.get(n - 1);
        if (g[cell.getX()][cell.getY()] == 1) return false;

        for (int i = 0; i < n - 1; i ++ ) {
            if (cellsA.get(i).getX() == cell.getX() && cellsA.get(i).getY() == cell.getY())
                return false;
        }

        for (int i = 0; i < n - 1; i ++ ) {
            if (cellsB.get(i).getX() == cell.getX() && cellsB.get(i).getY() == cell.getY())
                return false;
        }

        return true;
    }

    private void judge() {              // 判断两名玩家下一步操作是否合法
        List<Cell> cellsA = playerA.getCells();
        List<Cell> cellsB = playerB.getCells();

        boolean validA = check_valid(cellsA, cellsB);
        boolean validB = check_valid(cellsB, cellsA);

        if (!validA || !validB)
            this.status = "finished";

        if (!validA && !validB) {
            winner = "all";
        } else if (!validA) {
            winner = "B";
        } else if (!validB) {
            winner = "A";
        }
    }

    private void sendMove() {           // 向两个玩家发送移动信息
        JSONObject resp = new JSONObject();
        resp.put("event", "move");

        lock.lock();
        try {
            resp.put("a_direction", nextStepA);
            resp.put("b_direction", nextStepB);
            sendAllMessage(resp.toJSONString());
            nextStepA = nextStepB = null;
        } finally {
            lock.unlock();
        }
    }

    private void sendAllMessage(String message) {
        if (WebSocketServer.users.get(playerA.getId()) != null)
            WebSocketServer.users.get(playerA.getId()).sendMessage(message);
        if (WebSocketServer.users.get(playerB.getId()) != null)
            WebSocketServer.users.get(playerB.getId()).sendMessage(message);
    }

    @Override
    public void run() {
        // 两条蛇每三回合增长一格 地图 15 * 16 1000 回合够用
        for (int i = 0; i < 1000; i++) {
            if(nextStep()) {        // 是否获取到了两条蛇的下一步操作
                judge();

                if (status.equals("playing")) {
                    sendMove();
                } else {
                    sendResult();
                    break;
                }

            } else {
                status = "finished";
                lock.lock();
                try {
                    if (nextStepA == null && nextStepB == null) {
                        winner = "all";
                    } else if (nextStepA == null) {
                        winner = "B";
                    } else {
                        winner = "A";
                    }
                } finally {
                    lock.unlock();
                }

                sendResult();
                break;
            }
        }
    }
}
