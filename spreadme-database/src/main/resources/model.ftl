package ${ package };

import club.spreadme.database.annotation.Table;

import java.io.Serializable;
import java.util.Date;

@Table(name = "${ tableName }", primarykey = "${ primaryKey }")
public class ${ className } implements Serializable {

    private static final long serialVersionUID = 1L;

<#list fields as field>
    private ${field.fieldType} ${field.fieldName};
</#list>

    public ${ className }(){

    }

<#list fields as field>
    public ${field.fieldType} get${field.fieldName?cap_first}(){
        return this.${field.fieldName};
    }
    public void set${field.fieldName?cap_first}(${field.fieldType} ${field.fieldName}){
        this.${field.fieldName} = ${field.fieldName};
    }
</#list>

}