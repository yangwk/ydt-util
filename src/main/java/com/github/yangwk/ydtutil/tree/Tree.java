package com.github.yangwk.ydtutil.tree;

import java.util.ArrayList;
import java.util.List;


/**
 * 树结构帮助类
 * @author yangwk
 *
 * @param <T>
 */
public class Tree<T> {
	
	public Tree(){
	}
	
	/**
	 * 返回父节点的所有孩子节点（不仅仅是直接孩子节点）
	 * <br>返回的结果不包括父节点
	 * @author yangwk
	 *
	 * @param parent 父节点
	 * @param query
	 * @return not be null
	 */
	public List<T> getChildrenList(final T parent, final TreeQuery<T> query){
		List<T> result = new ArrayList<T>();
		
		recursiveTreeList(parent, query, result);
		
		return result;
	}
	
	/**
	 * 递归遍历
	 */
	private T recursiveTreeList(T entity, TreeQuery<T> query, List<T> result) {
		T parent = query.queryById(entity);
		List<T> children = query.queryChildrenById(entity);
		// 遍历子节点
		for (T child : children) {
			T t = recursiveTreeList(child, query, result); 
			result.add(t);
		}
		return parent;
	}
	
	/**
	 * 根据某节点获取完整一颗树
	 * <br>层次数据是完整的
	 * @author yangwk
	 *
	 * @param node
	 * @param query
	 * @return 树的根节点
	 */
	public TreeNode<T> getWholeTree(TreeNode<T> node, TreeQuery<TreeNode<T>> query){
		TreeNode<T> root = getRoot(node,query);
		
		return recursiveTreeNode(root, query);
	}
	
	/**
	 * 递归遍历
	 */
	private TreeNode<T> recursiveTreeNode(TreeNode<T> node, TreeQuery<TreeNode<T>> query) {
		TreeNode<T> parent = query.queryById(node);
		List<TreeNode<T>> children = query.queryChildrenById(node);
		// 遍历子节点
		for (TreeNode<T> child : children) {
			TreeNode<T> t = recursiveTreeNode(child, query); 
			parent.add(t);
		}
		return parent;
	}
	
	/**
	 * 根据某节点获取根节点
	 * <br>仅仅定位到根节点，层次数据是不完整的
	 * @author yangwk
	 *
	 * @param node
	 * @param query
	 * @return
	 */
	public TreeNode<T> getRoot(TreeNode<T> node, TreeQuery<TreeNode<T>> query) {
        TreeNode<T> ancestor = node;
        TreeNode<T> previous;

        do {
            previous = ancestor;
            ancestor = query.queryByParentId(ancestor);
        } while (ancestor != null);

        return previous;
    }
	
	/**
	 * 转json格式
	 * @author yangwk
	 *
	 * @param root
	 * @param jsonConvert
	 * @return
	 */
	public String toJson(TreeNode<T> root, JsonConvert<T> jsonConvert) {
		StringBuilder sb = new StringBuilder();
		sb.append("{ \"userObject\": ").append( jsonConvert.convert(root.getUserObject()) );
		//层次
		sb.append(", \"level\": ").append(root.getLevel());
		//父节点
		if(root.getParent() != null){
			sb.append(", \"parent\": ").append( jsonConvert.convert(root.getParent().getUserObject()) );
		}else{
			sb.append(", \"parent\": null");
		}
		//子节点
		if (root.getChildCount() != 0) {
			sb.append(", \"children\": [");
			for (int i=0; i < root.getChildCount(); i++ ) {
				TreeNode<T> node = root.getChildAt(i);
				sb.append( toJson(node, jsonConvert) );
				if(i < root.getChildCount() - 1){	//非最末一个
					sb.append(", ");
				}
			}
			sb.append("]");
		} else {
			sb.append(", \"isLeaf\": true");
		}
		sb.append(" }");
		
		return sb.toString();
	}

	/**
	 * 返回从根到达某节点的路径，此路径中的第一个元素是根，最后一个元素是某节点
	 * @author yangwk
	 *
	 * @param node 某节点
	 * @param query
	 * @return
	 */
	public List<TreeNode<T>> getPath(TreeNode<T> node, TreeQuery<TreeNode<T>> query) {
        return getPathToRoot(node, 0, query);
    }
	
	
	/**
	 * 递归遍历
	 */
	private List<TreeNode<T>> getPathToRoot(TreeNode<T> node, int depth, TreeQuery<TreeNode<T>> query) {
		depth = (depth < 0) ? 0 : depth;
		List<TreeNode<T>> retNodes;

        if(node == null) {
            if(depth == 0)
                return null;
            else{
                retNodes = new ArrayList<TreeNode<T>>();	//depth size
                for(int i=0; i<depth; i++)
                	retNodes.add(null);
            }
        }
        else {
            depth++;
            retNodes = getPathToRoot( query.queryByParentId(node) , depth, query);
            retNodes.set(retNodes.size() - depth, node);
        }
        return retNodes;
    }
	

}