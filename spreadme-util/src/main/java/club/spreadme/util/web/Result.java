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

package club.spreadme.util.web;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class Result implements Serializable {

    private static final long serialVersionUID = -4071256089139625383L;

    private Date timestamp;
    private Boolean status;
    private Integer code;
    private String message;
    private Object datas;

    public Result() {

    }

    public Result(Date timestamp, Boolean status, Integer code, String message, Object datas) {
        this.timestamp = timestamp;
        this.status = status;
        this.code = code;
        this.message = message;
        this.datas = datas;
    }

    public Result(Boolean status, Integer code, String message, Object datas) {
        this.timestamp = Calendar.getInstance().getTime();
        this.status = status;
        this.code = code;
        this.message = message;
        this.datas = datas;
    }

    public Result(Boolean status, Integer code, Object datas) {
        this.timestamp = Calendar.getInstance().getTime();
        this.status = status;
        this.code = code;
        this.datas = datas;
    }

    public Result(Boolean status, Integer code, String message) {
        this.timestamp = Calendar.getInstance().getTime();
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDatas() {
        return datas;
    }

    public void setDatas(Object datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return "Result{" +
                "timestamp=" + timestamp +
                ", status=" + status +
                ", code=" + code +
                ", message='" + message + '\'' +
                ", datas=" + datas +
                '}';
    }
}

