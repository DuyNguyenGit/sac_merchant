package com.sacombank.merchants.model.staticmodel;

/**
 * Created by DUY on 7/23/2017.
 */

public class TabModel {

        private int id;
        private String title;
        private int enableDrawableImageId;
        private int disableDrawableImageId;

        public TabModel(int id, String title, int enableDrawableImageId, int disableDrawableImageId) {
            this.id = id;
            this.title = title;
            this.enableDrawableImageId = enableDrawableImageId;
            this.disableDrawableImageId = disableDrawableImageId;
        }

        public int getId() {
            return id;
        }

        public String getTitle() {
            return title;
        }

        public int getEnableDrawableImageId() {
            return enableDrawableImageId;
        }

        public int getDisableDrawableImageId() {
            return disableDrawableImageId;
        }
}