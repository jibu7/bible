-- Create tables with schema that matches Room's expectations

-- Books table
CREATE TABLE "books" (
    "bookId" INTEGER,
    "name" TEXT NOT NULL,
    "testament" TEXT NOT NULL,
    "bookOrder" INTEGER NOT NULL,
    PRIMARY KEY("bookId" AUTOINCREMENT)
);

-- Chapters table
CREATE TABLE "chapters" (
    "chapterId" INTEGER,
    "bookId" INTEGER NOT NULL,
    "chapterNumber" INTEGER NOT NULL,
    UNIQUE("bookId","chapterNumber"),
    PRIMARY KEY("chapterId" AUTOINCREMENT),
    FOREIGN KEY("bookId") REFERENCES "books"("bookId") ON DELETE CASCADE
);

-- Create indices for chapters table
CREATE INDEX "index_chapters_bookId" ON "chapters"("bookId");
CREATE UNIQUE INDEX "index_chapters_bookId_chapterNumber" ON "chapters"("bookId", "chapterNumber");

-- Headings table
CREATE TABLE "headings" (
    "headingId" INTEGER NOT NULL,
    "chapterId" INTEGER NOT NULL,
    "headingOrder" INTEGER NOT NULL,
    "headingText" TEXT NOT NULL,
    PRIMARY KEY("headingId" AUTOINCREMENT),
    FOREIGN KEY("chapterId") REFERENCES "chapters"("chapterId") ON DELETE CASCADE
);

-- Create indices for headings table
CREATE INDEX "index_headings_chapterId" ON "headings"("chapterId");
CREATE UNIQUE INDEX "index_headings_chapterId_headingOrder" ON "headings"("chapterId", "headingOrder");

-- Languages table
CREATE TABLE "languages" (
    "languageId" INTEGER NOT NULL,
    "languageCode" TEXT NOT NULL,
    "languageName" TEXT NOT NULL,
    PRIMARY KEY("languageId" AUTOINCREMENT)
);

-- Create unique index for languages table
CREATE UNIQUE INDEX "index_languages_languageCode" ON "languages"("languageCode");

-- Verses table
CREATE TABLE "verses" (
    "verseId" INTEGER NOT NULL,
    "chapterId" INTEGER NOT NULL,
    "verseNumber" INTEGER NOT NULL,
    PRIMARY KEY("verseId" AUTOINCREMENT),
    FOREIGN KEY("chapterId") REFERENCES "chapters"("chapterId") ON DELETE CASCADE
);

-- Create indices for verses table
CREATE INDEX "index_verses_chapterId" ON "verses"("chapterId");
CREATE UNIQUE INDEX "index_verses_chapterId_verseNumber" ON "verses"("chapterId", "verseNumber");

-- Translations table
CREATE TABLE "translations" (
    "translationId" INTEGER NOT NULL,
    "verseId" INTEGER NOT NULL,
    "languageId" INTEGER NOT NULL,
    "languageCode" TEXT NOT NULL,
    "verseText" TEXT NOT NULL,
    PRIMARY KEY("translationId" AUTOINCREMENT),
    FOREIGN KEY("languageId") REFERENCES "languages"("languageId") ON DELETE RESTRICT,
    FOREIGN KEY("verseId") REFERENCES "verses"("verseId") ON DELETE CASCADE
);

-- Create indices for translations table
CREATE INDEX "index_translations_verseId" ON "translations"("verseId");
CREATE INDEX "index_translations_languageId" ON "translations"("languageId");
CREATE UNIQUE INDEX "index_translations_verseId_languageId" ON "translations"("verseId", "languageId"); 