package com.example.gross.gallery.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PixabayResponse {
    @SerializedName("hits")
    @Expose
    private List<Hit> hits = null;
    @SerializedName("total")
    @Expose
    private Integer total;

    public List<Hit> getHits() {
        return hits;
    }

    public void setHits(List<Hit> hits) {
        this.hits = hits;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    //----------------------
    public class Hit {
        @SerializedName("previewURL")
        @Expose
        private String previewURL;
        @SerializedName("webformatURL")
        @Expose
        private String webformatURL;


        public String getPreviewURL() {
            return previewURL;
        }

        public void setPreviewURL(String previewURL) {
            this.previewURL = previewURL;
        }

        public String getWebformatURL() {
            return webformatURL;
        }

        public void setWebformatURL(String webformatURL) {
            this.webformatURL = webformatURL;
        }

        public String getTitle() {
            char[] preview = this.previewURL.toCharArray();
            StringBuilder titleBuilder = new StringBuilder();
            for (int i = preview.length - 1; i > 0 && preview[i] != '/'; i--) {
                titleBuilder.append(preview[i]);
            }
            return titleBuilder.reverse().toString();
        }
    }
}


