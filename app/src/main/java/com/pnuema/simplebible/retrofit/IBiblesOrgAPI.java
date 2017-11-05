package com.pnuema.simplebible.retrofit;

import com.pnuema.simplebible.data.bibles.org.Books;
import com.pnuema.simplebible.data.bibles.org.Chapters;
import com.pnuema.simplebible.data.bibles.org.Verses;
import com.pnuema.simplebible.data.bibles.org.Versions;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface IBiblesOrgAPI {
    @GET("versions.js")
    Call<Versions> getVersions();

    @GET("versions/{version}/books.js?include_chapters=false")
    Call<Books> getBooks(@Path("version") String version);

    @GET("books/{book}/chapters.js")
    Call<Chapters> getChapters(@Path("book") String book);

    @GET("chapters/{version}:{book}.{chapter}/verses.js")
    Call<Verses> getVerses(@Path("version") String version, @Path("book") String book, @Path("chapter") String chapter);
}