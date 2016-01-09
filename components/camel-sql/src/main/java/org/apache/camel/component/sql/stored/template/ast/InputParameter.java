begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|Expression
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|builder
operator|.
name|ExpressionBuilder
import|;
end_import

begin_class
DECL|class|InputParameter
specifier|public
class|class
name|InputParameter
block|{
DECL|field|name
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
DECL|field|sqlType
specifier|private
specifier|final
name|int
name|sqlType
decl_stmt|;
DECL|field|valueExpression
specifier|private
specifier|final
name|Expression
name|valueExpression
decl_stmt|;
DECL|field|javaType
specifier|private
specifier|final
name|Class
name|javaType
decl_stmt|;
DECL|method|InputParameter (String name, int sqlType, String valueSrcAsStr, Class javaType)
specifier|public
name|InputParameter
parameter_list|(
name|String
name|name
parameter_list|,
name|int
name|sqlType
parameter_list|,
name|String
name|valueSrcAsStr
parameter_list|,
name|Class
name|javaType
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|sqlType
operator|=
name|sqlType
expr_stmt|;
name|this
operator|.
name|javaType
operator|=
name|javaType
expr_stmt|;
name|this
operator|.
name|valueExpression
operator|=
name|parseValueExpression
argument_list|(
name|valueSrcAsStr
argument_list|)
expr_stmt|;
block|}
DECL|method|parseValueExpression (String str)
specifier|private
name|Expression
name|parseValueExpression
parameter_list|(
name|String
name|str
parameter_list|)
block|{
return|return
name|ExpressionBuilder
operator|.
name|simpleExpression
argument_list|(
name|str
argument_list|)
return|;
block|}
DECL|method|getName ()
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
DECL|method|getSqlType ()
specifier|public
name|int
name|getSqlType
parameter_list|()
block|{
return|return
name|sqlType
return|;
block|}
DECL|method|getValueExpression ()
specifier|public
name|Expression
name|getValueExpression
parameter_list|()
block|{
return|return
name|valueExpression
return|;
block|}
DECL|method|getJavaType ()
specifier|public
name|Class
name|getJavaType
parameter_list|()
block|{
return|return
name|javaType
return|;
block|}
block|}
end_class

end_unit

