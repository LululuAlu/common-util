package cn.lgwen.structure;

public class VTrieNode {

	/** 当前字符 */
	private char nodeChar;
	
	/** 值为没有设置的值 */
	private long value = VTrie.UNSET_VALUE;


	private SparseArray<VTrieNode> children = new SparseArray<>();
	
	public VTrieNode() {
		this('\0');
	}
	
	public VTrieNode(char c) {
		this.nodeChar = c;
	}
	
	public VTrieNode(char c, long value) {
		this.nodeChar = c;
		this.value = value;
	}
	
	/**
	 * 插入中间节点
	 * @param chars 字符数组
	 * @param idx 当前操作的字符数组的位移
	 * @param value 叶节点对应的值
	 */
	public void insertChar(char[] chars, int idx, long value) {
		boolean isLeaf = (idx == chars.length - 1);
		char curC = chars[idx];
		VTrieNode childNode = children.get(curC);
		if(isLeaf) {
			if(childNode == null) {
				children.put(curC, new VTrieNode(curC, value));
			} else {
				childNode.setValue(value);
			}
			return;
		}
		
		if(childNode == null) {
			childNode = new VTrieNode(curC);
			children.put(curC, childNode);
		}
		
		childNode.insertChar(chars, idx + 1, value);
	}
	
	public void deleteChar(char[] chars, int idx) {
		boolean isLeaf = (idx == chars.length - 1);
		char curC = chars[idx];
		VTrieNode childNode = children.get(curC);
		if(isLeaf) {
			if(childNode != null) {
				childNode.setValue(VTrie.UNSET_VALUE);
			}
			return;
		}
		
		// 找不到，忽略
		if(childNode == null) {
			return;
		}
		
		childNode.deleteChar(chars, idx + 1);
	}
	
	/**
	 * 寻找下一个节点
	 * @param c 下一个字符
	 * @return
	 */
	public VTrieNode find(char c) {
		return children.get(c);
	}
	
	public void setValue(long value) {
		this.value = value;
	}
	
	public long getValue() {
		return value;
	}
	
	/**
	 * 是否设置有值
	 * @return
	 */
	public boolean isSetValue() {
		return value != VTrie.UNSET_VALUE;
	}
	
	public char getChar() {
		return nodeChar;
	}
}
