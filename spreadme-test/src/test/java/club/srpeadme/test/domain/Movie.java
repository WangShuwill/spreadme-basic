/*
 *  Copyright (c) 2019 Wangshuwei
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package club.srpeadme.test.domain;

import club.spreadme.database.annotation.Table;

import java.io.Serializable;
import java.util.Date;

@Table(name = "movies", primarykey = "id")
public class Movie implements Serializable {

    private static final long serialVersionUID = -1694434253161314015L;

    private String id;
    private String type;
    private String primaryTitle;
    private String originalTitle;
    private String isAdult;
    private Integer startYear;
    private Integer endYear;
    private Integer runtimeMinutes;
    private String genres;
    private Date operateDate;
    private Double rating;
    private Long votes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrimaryTitle() {
        return primaryTitle;
    }

    public void setPrimaryTitle(String primaryTitle) {
        this.primaryTitle = primaryTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getIsAdult() {
        return isAdult;
    }

    public void setIsAdult(String isAdult) {
        this.isAdult = isAdult;
    }

    public Integer getStartYear() {
        return startYear;
    }

    public void setStartYear(Integer startYear) {
        this.startYear = startYear;
    }

    public Integer getEndYear() {
        return endYear;
    }

    public void setEndYear(Integer endYear) {
        this.endYear = endYear;
    }

    public Integer getRuntimeMinutes() {
        return runtimeMinutes;
    }

    public void setRuntimeMinutes(Integer runtimeMinutes) {
        this.runtimeMinutes = runtimeMinutes;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Long getVotes() {
        return votes;
    }

    public void setVotes(Long votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "Movie [id=" + id + ", type=" + type + ", primaryTitle=" + primaryTitle + ", originalTitle="
                + originalTitle + ", isAdult=" + isAdult + ", startYear=" + startYear + ", endYear=" + endYear
                + ", runtimeMinutes=" + runtimeMinutes + ", genres=" + genres + ", operateDate=" + operateDate
                + ", rating=" + rating + ", votes=" + votes + "]";
    }

}
