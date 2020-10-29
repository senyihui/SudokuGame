# Sudoku

ref: https://www.youtube.com/watch?v=qH9mWpYMtYU

## UserInterface

* `IUserInterfaceContract`：前端与后端的交互接口
  * `EventListener`：处理每次输入时的接口
  * `View`：更新前端以及消息提示的接口
* `SudokuTextField`：继承了JavaFX中的`TextField`接口
  * x与y属性：用户界面坐标，与`Coordinates`类形成键值对存入HashMap
  * 覆写了replaceText和replaceSelection两个方法，使输入只能为1-9的数字
* `UserInterfaceImpl`：前端核心代码
  * 继承`IUserInterfaceContract.View`和`EventHandler<KeyEvent>`接口

## ProblemDomain

* `Coordinate`：后端数组“坐标”
* `SudokuGame`
  * `gameState`
    1. NEW
    2. ACTIVE
    3. COMPLETE
  * `gridState`
  * `isOrigin`: 数独中数字是否能够被更改，保证写入Storage之后仍然能保持数独中非用户输入的数字无法更改
* `IStorage`：数据储存接口

## Sudoku Algorithm

### `GameLogic.isSudokuValid(char[][] board)`函数

*功能：判断数独是否合法*

#### 笨办法：行、列、子方格

```java
public boolean isValidSudoku(char[][] board) {
    this.board = board;
    return isRowVaild() && isColVaild() && isSquareVaild();
}

public boolean isRowVaild() {
    for (int xIndex = 0; xIndex < 9; xIndex++) {
        Set<Character> rowSet = new HashSet<>();
        for (int yIndex = 0; yIndex < 9; yIndex++) {
            if (board[xIndex][yIndex] == '.') continue;
            if (rowSet.contains(board[xIndex][yIndex])) {
                return false;
            } else {
                rowSet.add(board[xIndex][yIndex]);
            }
        }
    }
    return true;
}

public boolean isColVaild() {
    for (int yIndex = 0; yIndex < 9; yIndex++) {
        Set<Character> colSet = new HashSet<>();
        for (int xIndex = 0; xIndex < 9; xIndex++) {
            if (board[xIndex][yIndex] == '.') continue;
            if (colSet.contains(board[xIndex][yIndex])) {
                return false;
            } else {
                colSet.add(board[xIndex][yIndex]);
            }
        }
    }
    return true;
}

public boolean isSquareVaild() {
    int delta = 3;
    for (int x = 0; x < 3; x++) {
        for (int y = 0; y < 3; y++) {
            Set<Character> squareSet = new HashSet<>();
            for (int xIndex = x * delta; xIndex < (x + 1) * delta; xIndex++) {
                for (int yIndex = y * delta; yIndex < (y + 1) * delta; yIndex++) {
                    if (board[xIndex][yIndex] == '.') continue;
                    if (squareSet.contains(board[xIndex][yIndex])) {
                        return false;
                    } else {
                        squareSet.add(board[xIndex][yIndex]);
                    }
                }
            }
        }
    }
    return true;
}
```

#### 一次迭代

来自LeetCode官方题解：https://leetcode-cn.com/problems/valid-sudoku/solution/you-xiao-de-shu-du-by-leetcode/

```java
class Solution {
  public boolean isValidSudoku(char[][] board) {
    // init data
    HashMap<Integer, Integer> [] rows = new HashMap[9];
    HashMap<Integer, Integer> [] columns = new HashMap[9];
    HashMap<Integer, Integer> [] boxes = new HashMap[9];
    for (int i = 0; i < 9; i++) {
      rows[i] = new HashMap<Integer, Integer>();
      columns[i] = new HashMap<Integer, Integer>();
      boxes[i] = new HashMap<Integer, Integer>();
    }

    // validate a board
    for (int i = 0; i < 9; i++) {
      for (int j = 0; j < 9; j++) {
        char num = board[i][j];
        if (num != '.') {
          int n = (int)num;
          int box_index = (i / 3 ) * 3 + j / 3;

          // keep the current cell value
          rows[i].put(n, rows[i].getOrDefault(n, 0) + 1);
          columns[j].put(n, columns[j].getOrDefault(n, 0) + 1);
          boxes[box_index].put(n, boxes[box_index].getOrDefault(n, 0) + 1);

          // check if this value has been already seen before
          if (rows[i].get(n) > 1 || columns[j].get(n) > 1 || boxes[box_index].get(n) > 1)
            return false;
        }
      }
    }

    return true;
  }
}
```

#### 位运算(最终采用)

作者：sharonou
链接：https://leetcode-cn.com/problems/valid-sudoku/solution/javawei-yun-suan-1ms-100-li-jie-fang-ge-suo-yin-by/
来源：力扣（LeetCode）

```java
class Solution {
    private final int N = 9;
    public boolean isValidSudoku(char[][] board) {
        int[] rows = new int[N]; //行的位运算数组
        int[] cols = new int[N]; //列的位运算数组
        int[] boxes = new int[N]; //方格的位运算数组
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (board[i][j] == '.')
                    continue;
                int tmp = board[i][j] - '0';
                int boxIndex = i / 3 * 3 + j / 3;
                if ((rows[i] >> tmp & 1) == 1 //rows[i] >> tmp & 1取出第i行的tmp数字，看是否已填，如果等于1，代表已填
                   || (cols[j] >> tmp & 1) == 1 //cols[j] >> tmp & 1取出第j列的tmp数字，看是否已填，如果等于1，代表已填
                   || (boxes[boxIndex] >> tmp & 1) == 1) //boxes[boxIndex] >> tmp & 1取出第boxIndex个方格的tmp数字，看是否已填，如果等于1，代表已填
                    return false;
                rows[i] = rows[i] | (1 << tmp); //将tmp数字加入到第i行的位运算数组
                cols[j] = cols[j] | (1 << tmp); //将tmp数字加入到第j列的位运算数组
                boxes[boxIndex] = boxes[boxIndex] | (1 << tmp); //将tmp数字加入到第boxIndex个方格的位运算数组
            }
        }
        return true;
    }
}
```

### `GameGenerator.getSolvedGame()`函数

*功能：生成 一个9 \* 9的数独*

预设一个完整且成立的数独，设置一个0~9的随机数，分别在原有数组的基础上向后推移相应的随机数，生成一个新的数独。例如，随机数为2，则原来为1的空格变为3，原来为2的空格变为4......以此类推。

## Constants

* `GameState`
* `Messages`

## TODO List

* 一个更加正式的数独生成算法
* 一键显示答案
* 一键更换题目
* 填错提示