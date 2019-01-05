/*
 *  Copyright (c) 2019 Wangshuwei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package club.spreadme.database.plugin.paginator;

import club.spreadme.database.metadata.SpeicalType;

import java.io.Serializable;
import java.util.List;

/**
 * @author Wangshuwei
 * @since 2018-6-26
 */
public class Page<T> implements SpeicalType, Serializable {

    private static final long serialVersionUID = 3552956183543557566L;

    private long total;
    private long pages;
    private int pageNum;
    private int pageSize;

    private List<T> datas;

    public Page() {

    }

    public Page(int pageNum, int pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Page(long total, long pages, int pageNum, int pageSize, List<T> datas) {
        this.total = total;
        this.pages = pages;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.datas = datas;
    }

    public Page(long total, int pageNum, int pageSize, List<T> datas) {
        this.total = total;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.datas = datas;
        this.pages = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
    }

    public long getTotal() {
        return total;
    }

    public Page<T> setTotal(long total) {
        this.total = total;
        if (pageSize != 0) {
            this.pages = total % pageSize == 0 ? total / pageSize : total / pageSize + 1;
        }
        return this;
    }

    public long getPages() {
        return pages;
    }

    public Page<T> setPages(long pages) {
        this.pages = pages;
        return this;
    }

    public int getPageNum() {
        return pageNum;
    }

    public Page<T> setPageNum(int pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public int getPageSize() {
        return pageSize;
    }

    public Page<T> setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public List<T> getDatas() {
        return datas;
    }

    public Page<T> setDatas(List<T> datas) {
        this.datas = datas;
        return this;
    }

    @Override
    public String toString() {
        return "Page{" +
                "total=" + total +
                ", pages=" + pages +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", datas=" + datas +
                '}';
    }
}
