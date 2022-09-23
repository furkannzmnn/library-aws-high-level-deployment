package com.example.lib.dto;


public enum CategoryType {
    RESEARCH_HISTORY("Arastirma - Tarih"),
    SCIENCE("Bilim"),
    COMIC("Çizgi Roman"),
    CHILD_AND_YOUTH("Çocuk ve Gençlik"),
    LESSON_TEST_BOOKS("Ders / Sinav Kitaplari"),
    RELIGION("Din Tasavvuf"),
    LITERATURE("Edebiyat"),
    EDUCATION_REFERENCE("Egitim Basvuru"),
    PHILOSOPHY("Felsefe"),
    FOREIGN_LANGUAGES("Foreign Languages"),
    HOBBY("Hobi"),
    MYTH_LEGEND("Mitoloji Efsane"),
    HUMOR("Mizah"),
    PRESTIGE_BOOKS("Prestij Kitaplari"),
    ART_DESIGN("Sanat - Tasarim"),
    AUDIO_BOOKS("Sesli Kitaplar"),
    OTHER("Diğer");

    private final String value;

    CategoryType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
