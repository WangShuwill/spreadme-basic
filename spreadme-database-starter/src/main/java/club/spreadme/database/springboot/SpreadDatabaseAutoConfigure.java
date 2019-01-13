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

import club.spreadme.database.annotation.Dao;
import club.spreadme.database.dao.ICommonDao;
import club.spreadme.database.dao.support.CommonDao;
import club.spreadme.database.plugin.Interceptor;
import club.spreadme.database.plugin.paginator.Paginator;
import club.spreadme.database.plugin.paginator.dialect.PaginationDialect;
import club.spreadme.lang.cache.Cache;
import com.alibaba.druid.pool.DruidDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.StringUtils;

import java.sql.SQLException;
import java.util.List;

@Configuration
@ConditionalOnClass(ICommonDao.class)
@EnableConfigurationProperties(SpreadDatabaseProperties.class)
public class SpreadDatabaseAutoConfigure {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpreadDatabaseAutoConfigure.class);

    private SpreadDatabaseProperties properties;

    @Bean
    ICommonDao commonDao() {
        DruidDataSource dataSource = new DruidDataSource();
        // datasource basic infomation
        dataSource.setUrl(properties.getUrl());
        dataSource.setUsername(properties.getUsername());
        dataSource.setPassword(properties.getPassword());
        // datasource extend infomation
        dataSource.setInitialSize(properties.getInitialSize());
        dataSource.setMinIdle(properties.getMinIdle());
        dataSource.setMaxActive(properties.getMaxActive());
        dataSource.setMaxWait(properties.getMaxWait());
        dataSource.setLogAbandoned(properties.isLogAbandoned());
        dataSource.setRemoveAbandoned(properties.isRemoveAbandoned());

        try {
            dataSource.setFilters(properties.getFilters());
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        CommonDao commonDao = CommonDao.getInstance().use(dataSource);
        try {
            for (Class<? extends PaginationDialect> paginationdialect : properties.getPaginationDialects()) {
                commonDao.addInterceptor(new Paginator().addDialect(paginationdialect));
            }
            for (Class<? extends Interceptor> interceptor : properties.getInterceptors()) {
                commonDao.addInterceptor(interceptor.newInstance());
            }
            for (Class<? extends Cache> cache : properties.getCaches()) {
                commonDao.use(cache.newInstance());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return commonDao;
    }

    public static class AutoConfiguredDaoScannerRegister implements BeanFactoryAware, ImportBeanDefinitionRegistrar, ResourceLoaderAware {

        private BeanFactory beanFactory;
        private ResourceLoader resourceLoader;

        @Override
        public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
            this.beanFactory = beanFactory;
        }

        @Override
        public void setResourceLoader(ResourceLoader resourceLoader) {
            this.resourceLoader = resourceLoader;
        }

        @Override
        public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry beanDefinitionRegistry) {
            LOGGER.debug("Searching for daos");
            ClassPathDaoScanner scanner = new ClassPathDaoScanner(beanDefinitionRegistry);
            try {
                if (this.resourceLoader != null) {
                    scanner.setResourceLoader(resourceLoader);
                }

                List<String> packages = AutoConfigurationPackages.get(this.beanFactory);
                if (LOGGER.isDebugEnabled()) {
                    packages.forEach(pkg -> LOGGER.debug("Using auto-config base package {}", pkg));
                }

                scanner.setAnnotationClass(Dao.class);
                scanner.registerFilters();
                scanner.doScan(StringUtils.toStringArray(packages));

            }
            catch (IllegalStateException ex) {
                LOGGER.debug("Could not determine auto-config package, automatic mapper scanning disabled.", ex);
            }
        }
    }

    @Configuration
    @Import(AutoConfiguredDaoScannerRegister.class)
    public static class DaoScannerRegisterNotFoundConfiguration implements InitializingBean {

        @Override
        public void afterPropertiesSet() {
            LOGGER.debug("No {} found.", CommonDaoBean.class.getName());
        }
    }

    @Autowired
    public void setProperties(SpreadDatabaseProperties properties) {
        this.properties = properties;
    }
}
