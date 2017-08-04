package com.example.shinji.retrofitexe;

import android.nfc.Tag;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;
import retrofit2.*;

/**
 * Created by shinji on 2017/08/03.
 */

@Generated("org.jsonschema2pojo")
public class QiitaGsonResponse {

    @SerializedName("rendered_body")
    @Expose
    private String renderedBody;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("coediting")
    @Expose
    private Boolean coediting;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("private")
    @Expose
    private Boolean _private;
    @SerializedName("results")
    @Expose
    private List<Object> results = new ArrayList<Object>();
    @Expose
    private List<Tag> tags = new ArrayList<Tag>();
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("url")
    @Expose
    private String url;
    /**
     *
     * @return
     * The renderedBody
     */
    public String getRenderedBody() {
        return renderedBody;
    }

    /**
     *
     * @param renderedBody
     * The rendered_body
     */
    public void setRenderedBody(String renderedBody) {
        this.renderedBody = renderedBody;
    }

    /**
     *
     * @return
     * The body
     */
    public String getBody() {
        return body;
    }

    /**
     *
     * @param body
     * The body
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     *
     * @return
     * The coediting
     */
    public Boolean getCoediting() {
        return coediting;
    }

    /**
     *
     * @param coediting
     * The coediting
     */
    public void setCoediting(Boolean coediting) {
        this.coediting = coediting;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The _private
     */
    public Boolean getPrivate() {
        return _private;
    }

    /**
     *
     * @param _private
     * The private
     */
    public void setPrivate(Boolean _private) {
        this._private = _private;
    }

    /**
     *
     * @return
     * The results
     */
    public List getResults() {
        return results;
    }

    /**
     *
     * @param tags
     * The tags
     */
    public void setResults(ArrayList tags) {
        this.results = results;
    }

    /**
     *
     * @return
     * The title
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     * The title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @return
     * The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
    public void setUrl(String url) {
        this.url = url;
    }


}