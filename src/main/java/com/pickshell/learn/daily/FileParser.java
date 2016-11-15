package com.pickshell.learn.daily;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class FileParser {

	@Test
	public void parse() throws IOException {
		String path = "C:/Users/UC212310/Desktop/files/收藏/日语词.txt";
		List<String> wordList = FileUtils.readLines(new File(path));
		// 找出重复单词
		Map<String, Integer> map = new HashMap<String, Integer>();
		int row = 0;
		for (String word : wordList) {
			row++;
			if (map.containsKey(word)) {
				System.out.println(word + " : " + row);
				System.out.println(word + " : " + map.get(word));
			} else {
				map.put(word, row);
			}
		}
		// 只保留单词的第一字
		List<String> list = new ArrayList<String>(wordList.size());
		for (int i = 0; i < wordList.size(); i++) {
			list.add(wordList.get(i).substring(0, 1));
		}
		for (int i = 0; i < list.size();) {
			String firstword = list.get(i);
			int j = i + 1;
			while (j < list.size() && firstword.equals(list.get(j))) {
				j++;
			} // j指向下一个词的起始
			List<String> subList = list.subList(j, list.size());
			int index = subList.indexOf(firstword);
			if (index != -1) {

				System.out.println(wordList.get(index + j) + " , " + (index + j));
			}
			i = j;
		}
	}
}
