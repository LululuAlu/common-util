package cn.lgwen.structure;

/**
 * 字符串统一编码为UTF8
 * VTrie的读是线程安全的，写不是线程安全
 *
 */
public class VTrie {

	/** 没有设置的值 */
	public static final long UNSET_VALUE = Long.MIN_VALUE;
	
	private VTrieNode rootNode = new VTrieNode();
	
	/**
	 * 添加新的词和对应的值，非线程安全
	 * @param word
	 * @param value
	 */
	public void addWord(String word, long value) {
		if(word == null || word.isEmpty())
			return;
		char[] chars = word.toCharArray();
		rootNode.insertChar(chars, 0, value);
	}
	
	public void deleteWord(String word) {
		if(word == null || word.isEmpty())
			return;
		char[] chars = word.toCharArray();
		rootNode.deleteChar(chars, 0);
	}
	
	public VTrieNode getRootNode() {
		return rootNode;
	}
	
	public long getValue(String word) {
		VTrieNode node = getVNode(word);
		return node == null ? UNSET_VALUE : node.getValue();
	}
	
	public VTrieNode getVNode(String word) {
		if(word == null || word.isEmpty())
			return null;
		char[] chars = word.toCharArray();
		VTrieNode curNode = rootNode;
		for(int i = 0; i < chars.length; ++i) {
			curNode = curNode.find(chars[i]);
			if(curNode == null)
				return null;
		}
		
		return curNode;
	}
	
	/**
	 * 至少包含一个词的时候返回True
	 * @param text 输入的文本，UTF8编码
	 * @return true表示包含trie中至少一个词
	 */
	public boolean containsAtLeastOneWord(String text) {
		if(text == null || text.isEmpty())
			return false;
		char[] chars = text.toCharArray();
		int charLen = chars.length;
		
		// 当前开始的位移
		int curBeginIdx = 0;
		// 当前操作的位移
		int curOpIdx = curBeginIdx;
		// 当前操作的节点
		VTrieNode curOpNode = null;
		while(curBeginIdx < charLen) {
			// 当前查找的字符
			char curC = chars[curOpIdx]; 
			if(curOpIdx == curBeginIdx) {
				curOpNode = rootNode.find(curC);
			} else {
				curOpNode = curOpNode.find(curC);
			}
			
			if(curOpNode != null) {
				if(curOpNode.isSetValue())
					return true;
				++curOpIdx;
				// 当前操作位移没有越界，继续下一个查找
				if(curOpIdx < charLen)
					continue;
				// 否则重新设置开始字符
			}
			
			// 重新设置开始字符
			++curBeginIdx;
			curOpIdx = curBeginIdx;
		}
		
		return false;
	}
}
