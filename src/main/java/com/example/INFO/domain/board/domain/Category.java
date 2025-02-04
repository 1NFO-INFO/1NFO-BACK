package com.example.INFO.domain.board.domain;

public enum Category {
    NEIGHBORHOOD("내 주변"),
    SELF_TIP("자취 팁 공유"),
    MONEY_MANAGEMENT("돈 관리"),
    HOUSING_CONCERN("주거 고민"),
    HEALTH_FITNESS("건강/운동"),
    OTHER("기타");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}