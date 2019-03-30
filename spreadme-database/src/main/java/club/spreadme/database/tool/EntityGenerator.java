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

package club.spreadme.database.tool;

import club.spreadme.database.core.grammar.Record;
import club.spreadme.database.core.grammar.TableInfo;
import club.spreadme.database.core.tableinfo.TableInfoAcquirer;
import club.spreadme.lang.StringUtil;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.sql.DataSource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EntityGenerator {

    private static final String TEMPLATENAME = "model";
    private static final String TEMPLATE_FIELNAME = "model.ftl";

    private DataSource dataSource;
    private String catalog;
    private String schema;
    private String tableName;
    private String primaryKey;
    private String packageName;
    private String path;

    private EntityGenerator(Builder builder) {
        this.dataSource = builder.dataSource;
        this.catalog = builder.catalog;
        this.schema = builder.schema;
        this.tableName = builder.tableName;
        this.primaryKey = builder.primaryKey;
        this.packageName = builder.packageName;
        this.path = builder.path;
    }

    public static class Builder {
        private DataSource dataSource;
        private String catalog;
        private String schema;
        private String tableName;
        private String primaryKey;
        private String packageName;
        private String path;

        public Builder dataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public Builder catalog(String catalog) {
            this.catalog = catalog;
            return this;
        }

        public Builder schema(String schema) {
            this.schema = schema;
            return this;
        }

        public Builder tableName(String tableName) {
            this.tableName = tableName;
            return this;
        }

        public Builder primaryKey(String primaryKey) {
            this.primaryKey = primaryKey;
            return this;
        }

        public Builder packageName(String packageName) {
            this.packageName = packageName;
            return this;
        }

        public Builder path(String path) {
            this.path = path;
            return this;
        }

        public EntityGenerator build() {
            return new EntityGenerator(this);
        }
    }

    public void generate() throws IOException, TemplateException {
        TableInfoAcquirer tableInfoAcquirer = TableInfoAcquirer.getInstance().use(dataSource);
        List<TableInfo> tableInfos = tableInfoAcquirer.getTableInfo(catalog, schema, tableName, "%");

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);

        InputStream is = EntityGenerator.class.getClassLoader().getResourceAsStream(TEMPLATE_FIELNAME);
        assert is != null;
        String templateContent = new BufferedReader(new InputStreamReader(is))
                .lines()
                .parallel()
                .collect(Collectors.joining(System.lineSeparator()));

        StringTemplateLoader templateLoader = new StringTemplateLoader();
        templateLoader.putTemplate(TEMPLATENAME, templateContent);
        configuration.setTemplateLoader(templateLoader);
        //获取模板
        Template template = configuration.getTemplate(TEMPLATENAME);

        Map<String, Object> datas = new HashMap<>();
        datas.put("package", packageName);
        datas.put("tableName", tableName);
        datas.put("primaryKey", primaryKey);
        datas.put("className", StringUtil.toUpper(tableName.toLowerCase(), 0));

        List<Record> fields = new ArrayList<>();
        tableInfos.forEach(tableInfo -> {
            Record field = new Record();
            field.set("fieldName", tableInfo.getColumn_name());
            field.set("fieldType", tableInfo.getClazz().getSimpleName());
            fields.add(field);
        });

        datas.put("fields", fields);

        template.process(datas, new OutputStreamWriter(
                new FileOutputStream(path + File.separator + StringUtil.toUpper(tableName.toLowerCase(), 0) + ".java")));
    }
}
