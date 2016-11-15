package com.pickshell.learn.audio;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AudioRecord extends JFrame implements ActionListener {
	// 定义所需要的组件
	JPanel jpTitle, jpBoard, jpControl;
	JLabel jlTitle = null;
	JButton captureBtn, stopBtn, playBtn, saveBtn;

	AudioRecordUtils recordUtils = new AudioRecordUtils();

	public AudioRecord() {
		jpTitle = new JPanel();
		jpBoard = new JPanel();
		jpControl = new JPanel();

		// 定义字体
		Font myFont = new Font("华文新魏", Font.BOLD, 30);
		jlTitle = new JLabel("录音机功能的实现");
		jlTitle.setFont(myFont);
		jpTitle.add(jlTitle);

		captureBtn = new JButton("开始录音");
		// 对开始录音按钮进行注册监听
		captureBtn.addActionListener(this);
		captureBtn.setActionCommand("captureBtn");
		// 对停止录音进行注册监听
		stopBtn = new JButton("停止录音");
		stopBtn.addActionListener(this);
		stopBtn.setActionCommand("stopBtn");
		// 对播放录音进行注册监听
		playBtn = new JButton("播放录音");
		playBtn.addActionListener(this);
		playBtn.setActionCommand("playBtn");
		// 对保存录音进行注册监听
		saveBtn = new JButton("保存录音");
		saveBtn.addActionListener(this);
		saveBtn.setActionCommand("saveBtn");

		this.add(jpTitle, BorderLayout.NORTH);
		this.add(jpBoard, BorderLayout.CENTER);
		this.add(jpControl, BorderLayout.SOUTH);
		// jp3.setLayout(null);
		jpControl.setLayout(new GridLayout(1, 4, 10, 10));
		jpControl.add(captureBtn);
		jpControl.add(stopBtn);
		jpControl.add(playBtn);
		jpControl.add(saveBtn);
		// 设置按钮的属性
		captureBtn.setEnabled(true);
		stopBtn.setEnabled(false);
		playBtn.setEnabled(false);
		saveBtn.setEnabled(false);
		// 设置窗口的属性
		this.setSize(400, 300);
		this.setTitle("录音机");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public static void main(String[] args) {
		new AudioRecord();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		if (command.equals("captureBtn")) {
			// 点击开始录音按钮后的动作
			// 停止按钮可以启动
			captureBtn.setEnabled(false);
			stopBtn.setEnabled(true);
			playBtn.setEnabled(false);
			saveBtn.setEnabled(false);

			// 调用录音的方法
			recordUtils.capture();
		} else if (command.equals("stopBtn")) {
			// 点击停止录音按钮的动作
			captureBtn.setEnabled(true);
			stopBtn.setEnabled(false);
			playBtn.setEnabled(true);
			saveBtn.setEnabled(true);
			// 调用停止录音的方法
			recordUtils.stop();

		} else if (command.equals("playBtn")) {
			// 调用播放录音的方法
			recordUtils.play();
		} else if (command.equals("saveBtn")) {
			// 调用保存录音的方法
			recordUtils.save();
		}

	}

}
