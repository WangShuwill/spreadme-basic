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

package club.spreadme.database.springboot;

import club.spreadme.database.dao.ICommonDao;
import org.springframework.beans.factory.FactoryBean;

public class CommonDaoBean<T> implements FactoryBean<T> {

    private Class<T> daoInterfance;
    private ICommonDao commonDao;

    public CommonDaoBean() {

    }

    public CommonDaoBean(Class<T> daoInterfance) {
        this.daoInterfance = daoInterfance;
    }

    @Override
    public T getObject() {
        return commonDao.getDao(daoInterfance);
    }

    @Override
    public Class<?> getObjectType() {
        return this.daoInterfance;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public Class<T> getDaoInterfance() {
        return daoInterfance;
    }

    public void setDaoInterfance(Class<T> daoInterfance) {
        this.daoInterfance = daoInterfance;
    }

    public ICommonDao getCommonDao() {
        return commonDao;
    }

    public void setCommonDao(ICommonDao commonDao) {
        this.commonDao = commonDao;
    }
}
