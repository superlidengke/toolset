package com.pickshell.learn.audio;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class AudioRecordUtils {
	// 定义录音格式
	private AudioFormat af = null;
	// 定义目标数据行,可以从中读取音频数据,该 TargetDataLine 接口提供从目标数据行的缓冲区读取所捕获数据的方法。
	private TargetDataLine td = null;
	// 定义源数据行,源数据行是可以写入数据的数据行。它充当其混频器的源。应用程序将音频字节写入源数据行，这样可处理字节缓冲并将它们传递给混频器。
	private SourceDataLine sd = null;
	// 定义字节数组输入输出流
	private ByteArrayInputStream bais = null;
	private ByteArrayOutputStream baos = null;
	// 定义音频输入流
	private AudioInputStream ais = null;
	// 定义停止录音的标志，来控制录音线程的运行
	private Boolean stopflag = false;
	// 录制模块
	private Runnable record = new Runnable() {

		@Override
		public void run() {
			// 定义存放录音的字节数组,作为缓冲区
			byte bts[] = new byte[10000];
			baos = new ByteArrayOutputStream();
			try {
				stopflag = false;
				while (stopflag != true) {
					// 当停止录音没按下时，该线程一直执行
					// 从数据行的输入缓冲区读取音频数据。
					// 要读取bts.length长度的字节,cnt 是实际读取的字节数
					int cnt = td.read(bts, 0, bts.length);
					if (cnt > 0) {
						baos.write(bts, 0, cnt);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					// 关闭打开的字节数组流
					if (baos != null) {
						baos.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					td.drain();
					td.close();
				}
			}

		}
	};

	private Runnable play = new Runnable() {

		@Override
		public void run() {

			byte bts[] = new byte[10000];
			try {
				int cnt;
				// 读取数据到缓存数据
				while ((cnt = ais.read(bts, 0, bts.length)) != -1) {
					if (cnt > 0) {
						// 写入缓存数据
						// 将音频数据写入到混频器
						sd.write(bts, 0, cnt);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				sd.drain();
				sd.close();
			}

		}
	};

	// 开始录音
	public void capture() {
		try {
			// af为AudioFormat也就是音频格式
			af = getAudioFormat();
			DataLine.Info info = new DataLine.Info(TargetDataLine.class, af);
			td = (TargetDataLine) (AudioSystem.getLine(info));
			// 打开具有指定格式的行，这样可使行获得所有所需的系统资源并变得可操作。
			td.open(af);
			// 允许某一数据行执行数据 I/O
			td.start();

			Thread t1 = new Thread(this.record);
			t1.start();

		} catch (LineUnavailableException ex) {
			ex.printStackTrace();
			return;
		}
	}

	// 停止录音
	public void stop() {
		stopflag = true;
	}

	// 播放录音
	public void play() {
		// 将baos中的数据转换为字节数据
		byte audioData[] = baos.toByteArray();
		// 转换为输入流
		bais = new ByteArrayInputStream(audioData);
		af = getAudioFormat();
		ais = new AudioInputStream(bais, af, audioData.length / af.getFrameSize());

		try {
			DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, af);
			sd = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
			sd.open(af);
			sd.start();
			// 创建播放进程
			Thread t2 = new Thread(this.play);
			t2.start();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				// 关闭流
				if (ais != null) {
					ais.close();
				}
				if (bais != null) {
					bais.close();
				}
				if (baos != null) {
					baos.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	// 保存录音
	public void save() {
		// 取得录音输入流
		af = getAudioFormat();

		byte audioData[] = baos.toByteArray();
		bais = new ByteArrayInputStream(audioData);
		ais = new AudioInputStream(bais, af, audioData.length / af.getFrameSize());
		// 定义最终保存的文件名
		File file = null;
		// 写入文件
		try {
			// 以当前的时间命名录音的名字
			// 将录音的文件存放到F盘下语音文件夹下
			File filePath = new File("C:/temp");
			if (!filePath.exists()) {// 如果文件不存在，则创建该目录
				filePath.mkdir();
			}
			file = new File(filePath.getPath() + "/" + System.currentTimeMillis() + ".mp3");
			AudioSystem.write(ais, AudioFileFormat.Type.WAVE, file);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭流
			try {

				if (bais != null) {
					bais.close();
				}
				if (ais != null) {
					ais.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 设置AudioFormat的参数
	public AudioFormat getAudioFormat() {
		// 下面注释部分是另外一种音频格式，两者都可以
		AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
		float rate = 8000f;
		int sampleSize = 16;
		boolean bigEndian = true;
		int channels = 1;
		return new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);

	}

	public AudioFormat getAudioFormat2() {
		// 采样率是每秒播放和录制的样本数
		// 采样率8000,11025,16000,22050,44100
		float sampleRate = 16000.0F;
		// sampleSizeInBits表示存储每个声音样本中的位数
		// 8,16
		int sampleSizeInBits = 16;
		// 单声道为1，立体声为2
		int channels = 1;
		// 数字是有符号位，还是无符号位
		boolean signed = true;
		boolean bigEndian = true;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
	}
}
