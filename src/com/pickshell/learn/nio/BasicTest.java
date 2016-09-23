package com.pickshell.learn.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

import org.junit.Test;

public class BasicTest {
	@Test
	/**
	 * 使用byte buffer读取文件，将每个字节转化为字符输出，所以中文会乱码
	 * 
	 * @throws IOException
	 */
	public void byteBuffer() throws IOException {
		File file = new File("C:\\temp\\read.txt");
		RandomAccessFile afile = new RandomAccessFile(file, "rw");
		FileChannel inChannel = afile.getChannel();
		ByteBuffer buf = ByteBuffer.allocate(128);
		int bytesRead = inChannel.read(buf);
		while (bytesRead != -1) {
			System.out.println("Read:" + bytesRead);
			buf.flip(); // make buffer ready for read
			while (buf.hasRemaining()) {
				// System.out.println(buf.get());
				System.out.print((char) buf.get());
			}
			System.out.println();
			buf.clear();// make buffer ready for writing
			bytesRead = inChannel.read(buf);
		}
		afile.close();
	}

	@Test
	/**
	 * 使用char buffer读取文件
	 * 
	 * @throws IOException
	 */
	public void charBuffer() throws IOException {
		File file = new File("C:\\temp\\read.txt");
		RandomAccessFile afile = new RandomAccessFile(file, "rw");
		FileChannel inChannel = afile.getChannel();
		ByteBuffer buf = ByteBuffer.allocate(128);
		CharBuffer charBuf = CharBuffer.allocate(128);
		int bytesRead = inChannel.read(buf);
		CharsetDecoder cd = StandardCharsets.UTF_8.newDecoder()
				// .onMalformedInput(CodingErrorAction.REPLACE)
				.onUnmappableCharacter(CodingErrorAction.REPLACE);
		cd.reset();
		while (bytesRead != -1) {
			//System.out.println("Read:" + bytesRead);
			buf.flip(); // make buffer ready for read
			CoderResult cr = cd.decode(buf, charBuf, false);
			buf.clear();// make buffer ready for writing
			bytesRead = inChannel.read(buf);
			charBuf.flip();
	        System.out.print(charBuf);
		}
		afile.close();
	}
}
