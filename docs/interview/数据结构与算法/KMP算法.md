# 一、算法作用

KMP算法是一种**字符串匹配**算法，可以在 O(n+m) 的时间复杂度内实现两个字符串的匹配，通常是在主串中查找模式串

这里以下letcode上面的一个题目为例来学习KMP算法；

# 二、典型例题描述

>#### [28. 实现 strStr()](https://leetcode-cn.com/problems/implement-strstr/)
>
>难度简单1073收藏分享切换为英文接收动态反馈
>
>实现 [strStr()](https://baike.baidu.com/item/strstr/811469) 函数。
>
>给你两个字符串 `haystack` 和 `needle` ，请你在 `haystack` 字符串中找出 `needle` 字符串出现的第一个位置（下标从 0 开始）。如果不存在，则返回 `-1` 。



这里我们把主串记为S，长度记为m；把模式串记为P，长度为记为n

这里以在 "ABCABCABD" 串中查找 "ABCABD" 进行讲解

# 三、简答解法

#### 1. 解题思路

针对这个题目，我们很容易想到的就是暴力匹配

- 从主串的第0个字符开始，扫描接下来的一段字符是否和模式串匹配；
- 如果匹配上，就返回0
- 没匹配上，就匹配主串的第1个字符后面的一段字符是否和模式串匹配；
- 重复以上步骤，一直到主串扫描结束

#### 2.过程示意图

![image-20211017082821362](https://gitee.com/firewolf/allinone/raw/master/images/image-20211017082821362.png)

上图中，绿色表示匹配成功的字符，橙色表示匹配失败的字符，灰色表示不再进行对比的字符

最坏情况下，每一趟的对比，都是在最后一个字符发现匹配失败，比如：在AAAAB中查找AAAB，效果如下：

 <img src="https://gitee.com/firewolf/allinone/raw/master/images/image-20211016172645657.png" alt="image-20211016172645657" style="zoom: 50%;" />

#### 3. 代码

```java
   public int strStr(String haystack, String needle) {
        if (haystack.length() < needle.length()) { //原串长度比匹配串短，不可能匹配
            return -1;
        }
        if (haystack.length() == 0 || needle.length() == 0) { // 空串，直接返回0
            return 0;
        }
        char[] haystackChars = haystack.toCharArray();
        char[] needleChars = needle.toCharArray();

        for (int i = 0; i <= haystackChars.length - needleChars.length; i++) { // 超过 haystackChars.length - needleChars.length的，长度不够，不用遍历
            int j = 0; // 模式串从第一个开始扫描
            int k = i;
            while (j < needleChars.length && needleChars[j] == haystackChars[k]) {//一个一个匹配
                j++;
                k++;
            }
            if (j == needleChars.length) { //所有的字符都相等
                return i;
            }
        }
        return -1;
    }
```

#### 4. 算法分析

代码中使用了两层循环

最坏情况下：外层循环的次数为 m - n ,内层循环次数为 n ；

时间复杂度为O（ (m-n) * n ） 由于通常情况下 m 远大于n ，所以m-n 比较接近于M，于是复杂度大概为 O (mn)（如果代码比较粗糙的写，也是这个复杂度）



# 四、优化思路（KMP核心思想）

#### 1. 优化的可能性

我们很难降低字符串比较的复杂度，也就是暴力破解代码中内层的wile循环（因为比较两个字符串，真的只能逐个比较字符），也即这个O（n）无法降低了；如果比较的趟数能降到足够低，那么总的复杂度也将会下降很多。

#### 2. 对比失败有意义么？

一次失败的匹配，会给我们提供宝贵的信息——如果 S[i : i+len(P)] 与 P 的匹配是在第 r 个位置失败的，那么从 S[i] 开始的 (r-1) 个连续字符，一定与 P 的前 (r-1) 个字符一模一样！比如：第1趟对比的时候，主串的 [0 - 4] 区间的字符和P的[0 - 4]区间的字符一一对应；

#### 3. 跳过不可能的匹配

有些趟字符串比较是有可能会成功的；有些则毫无可能。我们刚刚提到过，优化暴力破解的路线是“尽量减少比较的趟数”，而如果我们跳过那些**绝不可能成功的**字符串比较，则可以让复杂度得到降低

在第0趟比较的时候，到P[5]才失败，那么说明 S[0:5] 等于 P[0:5]，即"ABCAB". 现在我们来考虑：从 S[1]、S[2]、S[3] 开始的匹配尝试，有没有可能成功？
从 S[1] 开始肯定没办法成功，因为 S[1] = P[1] = 'b'，和 P[0] 并不相等。从 S[2] 开始也是没戏的，因为 S[2] = P[2] = 'c'，并不等于P[0]. 但是从 S[3] 开始是有可能成功的——至少按照已知的信息，我们推不出矛盾。

 <img src="https://gitee.com/firewolf/allinone/raw/master/images/image-20211017085236170.png" alt="image-20211017085236170" style="zoom:50%;" />

也就是是，在从S[0]这一趟比较失败的时候，我们应该跳到从S[3]比较这一趟，由于我们能看到，ABCAB中，前面的AB和后面的AB一样，而在S[0]这趟匹配中，各个字符都相等，那么可以直接让P中的[0-1]对应上S[3-4]而不需要比较，也就是说，我们实际上，是从S[5]开始进行的对比，用S[5]之后的字符去对比P[2]之后的字符，整理后如下：

1. 在S[i]为匹配七点到底r个字符失败之后，应该跳转到 P[0-r) 中可以让最大前后缀相等的位置；
2. 跳过最长相等前后缀长度字符的比较；

实现代码如下：





# 五、KMP算法

#### 1. 相关概念

子串：一个字符串中某一段，如：对于abcab来说，子串有：a、ab、abc、abca、abcab、bcab、cab、ab、b

前缀：指的是包含首字母不包含为尾字母的子串，如：对于abcab来说，前缀有：a、ab、abc、abca

后缀：指的是包含尾字母不包含为首字母的子串，如：对于abcab来说，后缀有：bcab、cab、ab、b

最长相等前后缀：前缀和后缀中，相同且长度最想等的字符串，如：对于abcab来说，最长相等前后缀为：ab

next数组：next[i] 表示 P[0] ~ P[i] 这一个子串，使得 **前k个字符**恰等于**后k个字符** 的最大的k. 特别地，k不能取i+1（因为这个子串一共才 i+1 个字符，自己肯定与自己相等，就没有意义了），也就是说，用上次匹配失败的时候主串中的的后k个字符，映射到这次匹配的前k个字符。同时，next[i]也表示下一趟对比的开始位置

#### 2. KMP算法过程

当主串的第i趟比较在P[j]失败之后，移动模式串中的指针到P[0-j]的最大相等前后缀长度后面那个指针(next[j-1])，用P[next[j-1]]后面的字符和 S[i+r]后的字符进行对比即可，如下图所示如下：：

 <img src="https://gitee.com/firewolf/allinone/raw/master/images/image-20211017091251282.png" alt="image-20211017091251282" style="zoom:50%;" />



#### 2. 计算next数组



