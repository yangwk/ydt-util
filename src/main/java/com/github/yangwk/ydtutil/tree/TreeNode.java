package com.github.yangwk.ydtutil.tree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @see #TreeNode(Object)
 * @author yangwk
 *
 * @param <T>
 */
public class TreeNode<T> {

	private TreeNode<T> parent;

	/**
	 * not be null
	 */
	private List<TreeNode<T>> children;

	private T userObject;
	
	/**
	 * 树结构通用节点
	 * <br>当某节点getParent()==null时，某节点为根节点
	 * @param userObject 该节点包含的数据，排序和判断值往往根据节点数据来操作
	 */
	public TreeNode(T userObject) {
		this.parent = null;
		this.children = new ArrayList<TreeNode<T>>();
		this.userObject = userObject;
	}
	
	public void add(TreeNode<T> newChild) {
        if(newChild != null && newChild.getParent() == this)
            insert(newChild, getChildCount() - 1);
        else
            insert(newChild, getChildCount());
    }
	
	public void insert(TreeNode<T> newChild, int childIndex) {
        if (isNodeAncestor(newChild)) {
            throw new IllegalArgumentException("new child is an ancestor");
        }

        TreeNode<T> oldParent = newChild.getParent();

        if (oldParent != null) {
            oldParent.remove(newChild);
        }
        newChild.setParent(this);
        children.add(childIndex, newChild);
    }
	
	public void remove(TreeNode<T> aChild) {
        if (aChild == null) {
            throw new IllegalArgumentException("argument is null");
        }

        if (!isNodeChild(aChild)) {
            throw new IllegalArgumentException("argument is not a child");
        }
        remove(getIndex(aChild));       // linear search
    }
	
	public int getIndex(TreeNode<T> aChild) {
        if (aChild == null) {
            throw new IllegalArgumentException("argument is null");
        }

        if (!isNodeChild(aChild)) {
            return -1;
        }
        return children.indexOf(aChild);        // linear search
    }
	
	public void remove(int childIndex) {
		TreeNode<T> child = getChildAt(childIndex);
        children.remove(childIndex);
        child.setParent(null);
    }
	
	public void setParent(TreeNode<T> newParent) {
        parent = newParent;
    }
	
	public boolean isNodeChild(TreeNode<T> aNode) {
        boolean retval;

        if (aNode == null) {
            retval = false;
        } else {
            if (getChildCount() == 0) {
                retval = false;
            } else {
                retval = (aNode.getParent() == this);
            }
        }

        return retval;
    }
	
	public boolean isNodeAncestor(TreeNode<T> anotherNode) {
        if (anotherNode == null) {
            return false;
        }

        TreeNode<T> ancestor = this;

        do {
            if (ancestor == anotherNode) {
                return true;
            }
        } while((ancestor = ancestor.getParent()) != null);

        return false;
    }
	
	public int getChildCount() {
		return children.size();
	}
	
	public TreeNode<T> getChildAt(int index) {
		return children.get(index);
	}
	
	public TreeNode<T> getParent() {
		return parent;
	}

	public T getUserObject() {
		return userObject;
	}
	
	/**
	 * 返回此节点上的级数 -- 从根到此节点的距离。如果此节点为根，则返回 0
	 * @author yangwk
	 *
	 * @return
	 */
	public int getLevel() {
        TreeNode<T> ancestor;
        int levels = 0;

        ancestor = this;
        while((ancestor = ancestor.getParent()) != null){
            levels++;
        }

        return levels;
    }
	
	/**
	 * 排序孩子节点
	 * @author yangwk
	 *
	 * @param root 根节点
	 * @param c 比较器
	 */
	public static <T> void sortChildren(TreeNode<T> root, Comparator<? super TreeNode<T>> c) {
		if(root.getChildCount() == 0){
			return ;
		}
		Collections.sort(root.children, c);
		
		for (TreeNode<T> child : root.children) {
			sortChildren(child, c);
		}
	}
	
	
	
}
