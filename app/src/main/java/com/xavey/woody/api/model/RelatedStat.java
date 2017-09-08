package com.xavey.woody.api.model;

/**
 * Created by tinmaungaye on 5/7/15.
 */
public class RelatedStat {
        private String name;
        private String value;
        private String point;

        public RelatedStat(){}

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }
}
