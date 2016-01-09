begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *<p/>  * http://www.apache.org/licenses/LICENSE-2.0  *<p/>  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.sql.stored.template.ast
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|sql
operator|.
name|stored
operator|.
name|template
operator|.
name|ast
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Field
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigInteger
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|Types
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|util
operator|.
name|ReflectionUtils
import|;
end_import

begin_class
DECL|class|ParseHelper
specifier|public
class|class
name|ParseHelper
block|{
DECL|method|parseSqlType (String sqlType)
specifier|public
specifier|static
name|int
name|parseSqlType
parameter_list|(
name|String
name|sqlType
parameter_list|)
block|{
name|Field
name|field
init|=
name|ReflectionUtils
operator|.
name|findField
argument_list|(
name|Types
operator|.
name|class
argument_list|,
name|sqlType
argument_list|)
decl_stmt|;
if|if
condition|(
name|field
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ParseRuntimeException
argument_list|(
literal|"Field "
operator|+
name|sqlType
operator|+
literal|" not found from java.procedureName.Types"
argument_list|)
throw|;
block|}
try|try
block|{
return|return
name|field
operator|.
name|getInt
argument_list|(
name|Types
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IllegalAccessException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ParseRuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
DECL|method|sqlTypeToJavaType (int sqlType, String sqlTypeStr)
specifier|public
specifier|static
name|Class
name|sqlTypeToJavaType
parameter_list|(
name|int
name|sqlType
parameter_list|,
name|String
name|sqlTypeStr
parameter_list|)
block|{
comment|//TODO: as rest of types.
comment|//TODO: add test for each type.
name|Class
name|ret
init|=
literal|null
decl_stmt|;
switch|switch
condition|(
name|sqlType
condition|)
block|{
case|case
name|Types
operator|.
name|INTEGER
case|:
name|ret
operator|=
name|Integer
operator|.
name|class
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|VARCHAR
case|:
name|ret
operator|=
name|String
operator|.
name|class
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|BIGINT
case|:
name|ret
operator|=
name|BigInteger
operator|.
name|class
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|CHAR
case|:
name|ret
operator|=
name|String
operator|.
name|class
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|BOOLEAN
case|:
name|ret
operator|=
name|Boolean
operator|.
name|class
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|DATE
case|:
name|ret
operator|=
name|Date
operator|.
name|class
expr_stmt|;
break|break;
case|case
name|Types
operator|.
name|TIMESTAMP
case|:
name|ret
operator|=
name|Date
operator|.
name|class
expr_stmt|;
break|break;
block|}
if|if
condition|(
name|ret
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ParseRuntimeException
argument_list|(
literal|"Unable to map SQL type "
operator|+
name|sqlTypeStr
operator|+
literal|" to Java type"
argument_list|)
throw|;
block|}
return|return
name|ret
return|;
block|}
block|}
end_class

end_unit

