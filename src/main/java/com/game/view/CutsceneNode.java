package com.game.view;

public class CutsceneNode {
    private final String imagePath; // Đường dẫn ảnh nền (vd: "/images/MainScreen.png")
    private final String speaker;   // Tên người nói (vd: "DẪN CHUYỆN", "VUA ĐÁ")
    private final String text;      // Lời thoại

    public CutsceneNode(String imagePath, String speaker, String text) {
        this.imagePath = imagePath;
        this.speaker = speaker;
        this.text = text;
    }

    public String getImagePath() { return imagePath; }
    public String getSpeaker() { return speaker; }
    public String getText() { return text; }
}