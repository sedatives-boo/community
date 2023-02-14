package com.nowcoder.community.util;


import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Component
public class SensitiveFilter {


    private class TrieNode{
        public boolean isEnd() {
            return isEnd;
        }

        public void setEnd(boolean end) {
            isEnd = end;
        }

        private boolean isEnd = false;
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public void addSubNode(Character c, TrieNode node){
            subNodes.put(c,node);
        }

        public TrieNode getSubNode(Character c){
            return subNodes.get(c);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    private static String REPLACEMENT = "***";

    public TrieNode rootNode = new TrieNode();
    /**
     *  初始化方法
     *      1.将txt内的读取出来，得到Input Stream is
     *      2.转化成缓冲流 BufferedReader reader
     *      3.构建前缀数
     */
    @PostConstruct
    public void init(){
        try (InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
             BufferedReader reader = new BufferedReader(new InputStreamReader(is));){
            String keyWord;
            while((keyWord=reader.readLine())!=null){
                this.addKeyword(keyWord);
            }
        } catch (Exception e) {
           logger.error("加载敏感词失败"+e.getMessage());
        } finally {
        }
    }

    /**
     *  构建数的过程
     * @param keyWord
     */
    private void addKeyword(String keyWord) {
        TrieNode tempNode = rootNode;
        for(int i = 0;i<keyWord.length();i++){
            char c = keyWord.charAt(i);
            TrieNode subNode = tempNode.getSubNode(c);
            if(subNode==null){
                //如果不存在的话，就初始化一个
                subNode = new TrieNode();
                tempNode.addSubNode(c,subNode);
            }
            tempNode = subNode;
            if(i==keyWord.length()-1){
                tempNode.setEnd(true);
            }
        }
    }

    /**
     *   检索+过滤的方法
     * @param text
     * @return 过滤后的文本
     */
    public String filter(String text){
        if(StringUtils.isBlank(text)){return null;}
        //指针
        TrieNode tempNode = rootNode;
        int begin = 0;
        int position = 0;
        //存结果
        StringBuilder sb = new StringBuilder();
        while(position<text.length()){
            char c = text.charAt(position);
            if(isSymbol(c)){
                if(tempNode==rootNode){
                    sb.append(c);begin++;
                }
                position++;
            }
            //检查下个节点
            tempNode = tempNode.getSubNode(c);
            if(tempNode==null){
                sb.append(text.charAt(begin));
                position = ++begin;
                tempNode = rootNode;
            }else if(tempNode.isEnd){
                sb.append(REPLACEMENT);
                begin = ++position;
                tempNode = rootNode;
            }else{
                position++;
            }
        }
        sb.append(text.substring(begin));
        return sb.toString();
    }

    private boolean isSymbol(char c) {
        return !CharUtils.isAsciiAlphanumeric(c)&&(c<0x2E80||c>0x9FFF);
    }
}
