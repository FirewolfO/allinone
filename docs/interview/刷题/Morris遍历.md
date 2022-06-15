#### 前序遍历

```java
public List<Integer> preorderTraversal(TreeNode root){
    List<Integer> res = new ArrayList<>();
    TreeNode cur = root;
    while(cur != null){
        if(cur.left == null){
            res.add(cur.val);
            cur = cur.right;
        }else{
            TreeNode mostRight = cur.left;
            while(mostRight.right != null && mostRight.right != cur){
                mostRight.right = right;
            }
            if(mostRight.right == null){
                mostRight.right = cur;// 链接指针
                res.add(cur.val);
                cur = cur.left;
            }else{
                mostRight.right = null;
                cur = cur.right;
            }
        }
    }
    return res;
}
```



#### 中序遍历

```java
public List<Integer> inorderTraversal(TreeNode root){
    List<Integer> res = new ArrayList<>();
    TreeNode cur = root;
    while(cur != null){
        if(cur.left == null){
            res.add(cur.val);
            cur = cur.right;
        }else{
            TreeNode mostRight = cur.left;
            while(mostRight.right != null && mostRight.right != cur){
                mostRight = mostRight.right;
            }
            if(mostRight.right == null){
                mostRight.right = cur;
                cur = cur.left;
            }else{
                res.add(cur.val);
                mostRight.right = null;
                cur = cur.right;
            }
        }
    }
    return res;
}
```



#### 后序遍历

```java
public List<Integer> postorderTraversal(TreeNode root){
    List<Integer> res = new ArrayList<>();
    TreeNode cur = root;
    while(cur != null){
        if(cur.left == null){
            cur = cur.right;
        }else{
            TreeNode mostRight = cur.left;
            while(mostRight.right != null && mostRight.right != cur){
                mostRight = mostRight.right;
            }
            if(mostRight.right == null){
                mostRight.right = cur;
                cur = cur.left;
            }else{
                ////恢复树结构
                mostRight.right = null; 
                //左下角的全部遍历完了， cur.left 这个点到最右那个节点的数据
				traversalEdge(cur.left);
                cur = cur.right;//跳转到右子树
            }
        }
    }
    traversalEdge(root);
    return res;
}
private void traversalEdge(TreeNode root,List<Integer> res){
    int index = res.size();
    while(root != null){
        res.add(index,root.val);
        root = root.right;
    }
}
```

