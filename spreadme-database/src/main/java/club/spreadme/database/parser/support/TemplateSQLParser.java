package club.spreadme.database.parser.support;

import club.spreadme.database.parser.ExpressionHandler;
import club.spreadme.database.parser.SQLParser;
import club.spreadme.database.parser.grammar.SQLParameter;
import club.spreadme.database.parser.grammar.SQLStatement;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class TemplateSQLParser implements SQLParser {

    private final String sqlTemplateId;
    private final String sqlTemplate;
    private final SQLParameter[] sqlParameters;

    public TemplateSQLParser(String sqlTemplateId, String sqlTemplate, SQLParameter[] sqlParameters) {
        this.sqlTemplateId = sqlTemplateId;
        this.sqlTemplate = sqlTemplate;
        this.sqlParameters = sqlParameters;
    }

    @Override
    public SQLStatement parse() {
        ExpressionHandler expressionHandler = new GenericExpressionHandler();
        GenericTokenParser tokenParser = new GenericTokenParser(expressionHandler);
        String newSqlTemplate = tokenParser.parse(sqlTemplate, ExpressionHandler.PREPAREPLACEHOLDER);

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_23);
        StringTemplateLoader templateLoader = new StringTemplateLoader();
        templateLoader.putTemplate(sqlTemplateId, newSqlTemplate);
        configuration.setTemplateLoader(templateLoader);

        StringWriter writer = new StringWriter();
        Map<String, Object> dataSource = getDataSource(sqlParameters);
        try {
            Template template = configuration.getTemplate(sqlTemplateId);
            template.process(dataSource, writer);
        }
        catch (IOException | TemplateException e) {
            e.printStackTrace();
        }

        String sql = writer.toString();
        return new SQLStatement(sql, null);
    }

    private Map<String, Object> getDataSource(SQLParameter[] sqlParameters) {
        Map<String, Object> dataSource = new HashMap<>(8);
        for (SQLParameter sqlParameter : sqlParameters) {
            dataSource.put(sqlParameter.getName(), sqlParameter.getValue());
        }
        return dataSource;
    }

}
