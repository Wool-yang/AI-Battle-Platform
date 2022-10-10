package com.abp.backend.consumer.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private Integer id;

    // 起点坐标
    private Integer sx;
    private Integer sy;

    // 历史操作方向
    private List<Integer> steps;

    private boolean check_tail_increasing(int steps) {      // 检验当前回合蛇的长度是否增加
        if (steps <= 10) return true;
        if (steps % 3 == 1) return true;
        return false;
    }

    public List<Cell> getCells() {
        List<Cell> res = new ArrayList<>();

        int[] dx = {-1, 0, 1, 0}, dy = {0, 1, 0, -1};
        int step = 0;

        int x = sx, y = sy;
        res.add(new Cell(x, y));

        for (int d : steps) {
            x += dx[d];
            y += dy[d];
            res.add(new Cell(x, y));

            if(!check_tail_increasing(++step)) {
                res.remove(0);
            }
        }

        return res;
    }

    public String getStepsString() {
        StringBuilder res = new StringBuilder();

        for (int d : steps) {
            res.append(d);
        }

        return res.toString();
    }
}
