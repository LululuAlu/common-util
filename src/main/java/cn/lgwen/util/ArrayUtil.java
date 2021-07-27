package cn.lgwen.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class ArrayUtil {

	private static Object[] EMPTY = new Object[0];
    private static final int CACHE_SIZE = 73;
    private static Object[] sCache = new Object[CACHE_SIZE];

    public static int idealByteArraySize(int need) {
        for (int i = 4; i < 32; i++)
            if (need <= (1 << i) - 12)
                return (1 << i) - 12;

        return need;
    }

    public static int idealBooleanArraySize(int need) {
        return idealByteArraySize(need);
    }

    public static int idealShortArraySize(int need) {
        return idealByteArraySize(need * 2) / 2;
    }

    public static int idealCharArraySize(int need) {
        return idealByteArraySize(need * 2) / 2;
    }

    public static int idealIntArraySize(int need) {
        return idealByteArraySize(need * 4) / 4;
    }

    public static int idealFloatArraySize(int need) {
        return idealByteArraySize(need * 4) / 4;
    }

    public static int idealObjectArraySize(int need) {
        return idealByteArraySize(need * 4) / 4;
    }

    public static int idealLongArraySize(int need) {
        return idealByteArraySize(need * 8) / 8;
    }

    /**
     * Checks if the beginnings of two byte arrays are equal.
     *
     * @param array1 the first byte array
     * @param array2 the second byte array
     * @param length the number of bytes to check
     * @return true if they're equal, false otherwise
     */
    public static boolean equals(byte[] array1, byte[] array2, int length) {
        if (length < 0) {
            throw new IllegalArgumentException();
        }

        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null || array1.length < length || array2.length < length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns an empty array of the specified type.  The intent is that
     * it will return the same empty array every time to avoid reallocation,
     * although this is not guaranteed.
     */
    @SuppressWarnings("unchecked")
	public static <T> T[] emptyArray(Class<T> kind) {
        if (kind == Object.class) {
            return (T[]) EMPTY;
        }

        int bucket = ((System.identityHashCode(kind) / 8) & 0x7FFFFFFF) % CACHE_SIZE;
        Object cache = sCache[bucket];

        if (cache == null || cache.getClass().getComponentType() != kind) {
            cache = Array.newInstance(kind, 0);
            sCache[bucket] = cache;

            // Log.e("cache", "new empty " + kind.getName() + " at " + bucket);
        }

        return (T[]) cache;
    }

    /**
     * Checks that value is present as at least one of the elements of the array.
     * @param array the array to check in
     * @param value the value to check for
     * @return true if the value is present in the array
     */
    public static <T> boolean contains(T[] array, T value) {
        return indexOf(array, value) != -1;
    }


    /**
     * Test if all {@code check} items are contained in {@code array}.
     */
    public static <T> boolean containsAll(T[] array, T[] check) {
        for (T checkItem : check) {
            if (!contains(array, checkItem)) {
                return false;
            }
        }
        return true;
    }

    public static boolean contains(int[] array, int value) {
        for (int element : array) {
            if (element == value) {
                return true;
            }
        }
        return false;
    }

    public static long total(long[] array) {
        long total = 0;
        for (long value : array) {
            total += value;
        }
        return total;
    }

    /**
     * Appends an element to a copy of the array and returns the copy.
     * @param array The original array, or null to represent an empty array.
     * @param element The element to add.
     * @return A new array that contains all of the elements of the original array
     * with the specified element added at the end.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] appendElement(Class<T> kind, T[] array, T element) {
        final T[] result;
        final int end;
        if (array != null) {
            end = array.length;
            result = (T[]) Array.newInstance(kind, end + 1);
            System.arraycopy(array, 0, result, 0, end);
        } else {
            end = 0;
            result = (T[]) Array.newInstance(kind, 1);
        }
        result[end] = element;
        return result;
    }

    /**
     * Removes an element from a copy of the array and returns the copy.
     * If the element is not present, then the original array is returned unmodified.
     * @param array The original array, or null to represent an empty array.
     * @param element The element to remove.
     * @return A new array that contains all of the elements of the original array
     * except the first copy of the specified element removed.  If the specified element
     * was not present, then returns the original array.  Returns null if the result
     * would be an empty array.
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] removeElement(Class<T> kind, T[] array, T element) {
        if (array != null) {
            final int length = array.length;
            for (int i = 0; i < length; i++) {
                if (array[i] == element) {
                    if (length == 1) {
                        return null;
                    }
                    T[] result = (T[]) Array.newInstance(kind, length - 1);
                    System.arraycopy(array, 0, result, 0, i);
                    System.arraycopy(array, i + 1, result, i, length - i - 1);
                    return result;
                }
            }
        }
        return array;
    }

    public static int[] appendInt(int[] cur, int val) {
        if (cur == null) {
            return new int[] { val };
        }
        final int N = cur.length;
        for (int i = 0; i < N; i++) {
            if (cur[i] == val) {
                return cur;
            }
        }
        int[] ret = new int[N + 1];
        System.arraycopy(cur, 0, ret, 0, N);
        ret[N] = val;
        return ret;
    }

    public static int[] removeInt(int[] cur, int val) {
        if (cur == null) {
            return null;
        }
        final int N = cur.length;
        for (int i = 0; i < N; i++) {
            if (cur[i] == val) {
                int[] ret = new int[N - 1];
                if (i > 0) {
                    System.arraycopy(cur, 0, ret, 0, i);
                }
                if (i < (N - 1)) {
                    System.arraycopy(cur, i + 1, ret, i, N - i - 1);
                }
                return ret;
            }
        }
        return cur;
    }
	
	public static int indexOf(long[] array, long target) {
		if(array == null)
			return -1;
		for(int i = 0; i < array.length; ++i) {
			if(array[i] == target)
				return i;
		}
		return -1;
	}
	
	public static int indexOf(int[] array, int target) {
		if(array == null)
			return -1;
		for(int i = 0; i < array.length; ++i) {
			if(array[i] == target)
				return i;
		}
		return -1;
	}
	
	public static int indexOf(String[] array, String target) {
		if(array == null)
			return -1;
		for(int i = 0; i < array.length; ++i) {
			if(array[i] .equals(target))
				return i;
		}
		return -1;
	}
	
	public static<T extends Object> int indexOf(T[] array, T target) {
		if(array == null)
			return -1;
		for(int i = 0; i < array.length; ++i) {
			if(array[i].equals(target))
				return i;
		}
		return -1;
	}
	
	/**
	 * long数组 添加不重复的 long值判断)。重复则忽略
	 * @param array 原始的数组，空数组时为null
	 * @param obj 要插入的对象
	 * @return 新的数组
	 */
	public static long[] addUniqueElement(long[] array, long obj) {
		if(array == null) {
			array = new long[1];
			array[0] = obj;
			return array;
		}
		 // 已经包含，忽略
		if(ArrayUtil.indexOf(array, obj) >= 0)
			return array;
		
		long[] newArray = new long[array.length + 1];
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[newArray.length - 1] = obj;
		return newArray;
	}
	
	public static String[] addUniqueElement(String[] array, String obj) {
		if(array == null) {
			array = new String[1];
			array[0] = obj;
			return array;
		}
		 // 已经包含，忽略
		if(ArrayUtil.indexOf(array, obj) >= 0)
			return array;
		
		String[] newArray = new String[array.length + 1];
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[newArray.length - 1] = obj;
		return newArray;
	}
	
	/**
	 * 对象数组array 添加不重复的 obj (equals()判断)。重复则忽略
	 * @param array 原始的数组，空数组时为null
	 * @param obj 要插入的对象
	 * @return 新的数组
	 */
	@SuppressWarnings("unchecked")
	public static<T extends Object> T[] addUniqueElement(T[] array, T obj) {
		if(array == null) {
			array = (T[])(new Object[1]);
			array[0] = obj;
			return array;
		}
		 // 已经包含，忽略
		if(ArrayUtil.indexOf(array, obj) >= 0)
			return array;
		
		T[] newArray = (T[])(new Object[array.length + 1]);
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[array.length - 1] = obj;
		return newArray;
	}
	
	/**
	 * 删除long数组array中的元素obj。如果对象不存在忽略
	 * @param array 原始的数组，为空是为null
	 * @param obj 要删除的对象
	 * @return 删除对象后的数组，如果数组为空是返回null
	 */
	public static long[] deleteElement(long[] array, long obj) {
		if(array == null)
			return null;
		int idx = ArrayUtil.indexOf(array, obj);
		if(idx < 0) { // 不包含，忽略
			return array;			
		}
		
		// 只有一个元素，直接置为null
		if(array.length == 1)
			return null;
		
		long[] newArray = new long[array.length - 1];
		int newTIdx = -1;
		for(int i = 0; i < array.length; ++i) {
			if(i == idx)
				continue;
			newArray[++newTIdx] = array[i];
		}
		
		return newArray;
	}
	
	public static String[] deleteElement(String[] array, String obj) {
		if(array == null)
			return null;
		int idx = ArrayUtil.indexOf(array, obj);
		if(idx < 0) { // 不包含，忽略
			return array;			
		}
		
		// 只有一个元素，直接置为null
		if(array.length == 1)
			return null;
		
		String[] newArray = new String[array.length - 1];
		int newTIdx = -1;
		for(int i = 0; i < array.length; ++i) {
			if(i == idx)
				continue;
			newArray[++newTIdx] = array[i];
		}
		
		return newArray;
	}
	
	/**
	 * 删除对象数组array中的元素obj。如果对象不存在忽略
	 * @param array 原始的数组，为空是为null
	 * @param obj 要删除的对象
	 * @return 删除对象后的数组，如果数组为空是返回null
	 */
	@SuppressWarnings("unchecked")
	public static<T extends Object> T[] deleteElement(T[] array, T obj) {
		if(array == null)
			return null;
		int idx = ArrayUtil.indexOf(array, obj);
		if(idx < 0) { // 不包含，忽略
			return array;			
		}
		
		// 只有一个元素，直接置为null
		if(array.length == 1)
			return null;
		
		T[] newArray = (T[])(new Object[array.length - 1]);
		int newTIdx = -1;
		for(int i = 0; i < array.length; ++i) {
			if(i == idx)
				continue;
			newArray[++newTIdx] = array[i];
		}
		
		return newArray;
	}
	
	/**
	 * 返回部分数组
	 * @param array 原始数组
	 * @param start 开始的位移，包括
	 * @param limit 返回的部分数组的最大长度
	 * @return 原始数组的部分数组
	 * @throws IndexOutOfBoundsException start 超过原始数组的长度时候抛出
	 */
	public static long[] subArray(long[] array, int start, int limit) 
			throws IndexOutOfBoundsException {
		if(array == null || start < 0 || start >= array.length) {
			throw new IndexOutOfBoundsException("" + start);
		}
		
		int maxIdx = start + limit; // 最大位移，不包括
		if(maxIdx > array.length)
			maxIdx = array.length;
		
		long[] ret = new long[maxIdx - start];
		System.arraycopy(array, start, ret, 0, ret.length);
		return ret;
	}
	
	public static Object[] subArray(Object[] array, int start, int limit)
			throws IndexOutOfBoundsException {
		if (array == null || start < 0 || start >= array.length) {
			throw new IndexOutOfBoundsException("" + start);
		}

		int maxIdx = start + limit; // 最大位移，不包括
		if (maxIdx > array.length)
			maxIdx = array.length;

		Object[] ret = new Object[maxIdx - start];
		System.arraycopy(array, start, ret, 0, ret.length);
		return ret;
	}
	
	/**
	 * 求两个数组的并集, 数据为原先的拷贝，不改变原有的值
	 * @return 数组为空的时候返回 空数组
	 */
	public static long[] union(long[] a1 , long[] a2)throws Exception {
		Set<Long> roleSet = new HashSet<Long>();
		if(a1 != null) {
			for(long a : a1)
				roleSet.add(a);
		}
		
		if(a2 != null) {
			for(long a : a2)
				roleSet.add(a);
		}
		
		if(roleSet.isEmpty())
			return new long[0];
		
		long[] ret = new long[roleSet.size()];
		int i = -1;
		for(long temp : roleSet){
			ret[++i] = temp;
		}
		return ret ;	
	}
	
	/**
	 * 按照顺序排序
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public static int[] order(int[] ids) throws Exception {
		int temp = 0 ;
		for (int mi = 0; mi < ids.length; mi++) {
			for (int mj = mi + 1; mj < ids.length; mj++) {
				if (ids[mi] > ids[mj]) {
					temp = ids[mi];
					ids[mi] = ids[mj];
					ids[mj] = temp;
				}
			}
		}
		return ids;
	}

	/**
	 * 分割list
	 */
	public static <T> List<List<T>> averageAssign(List<T> source,int n){
		List<List<T>> result=new ArrayList<List<T>>();
		int remaider=source.size()%n;  //(先计算出余数)
		int number=source.size()/n;  //然后是商
		int offset=0;//偏移量
		for(int i=0;i<n;i++){
			List<T> value=null;
			if(remaider>0){
				value=source.subList(i*number+offset, (i+1)*number+offset+1);
				remaider--;
				offset++;
			}else{
				value=source.subList(i*number+offset, (i+1)*number+offset);
			}
			result.add(value);
		}
		return result;
	}
}
