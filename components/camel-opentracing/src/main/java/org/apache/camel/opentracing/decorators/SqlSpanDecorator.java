begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.opentracing.decorators
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|opentracing
operator|.
name|decorators
package|;
end_package

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|Span
import|;
end_import

begin_import
import|import
name|io
operator|.
name|opentracing
operator|.
name|tag
operator|.
name|Tags
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
name|Endpoint
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
name|Exchange
import|;
end_import

begin_class
DECL|class|SqlSpanDecorator
specifier|public
class|class
name|SqlSpanDecorator
extends|extends
name|AbstractSpanDecorator
block|{
DECL|field|CAMEL_SQL_QUERY
specifier|public
specifier|static
specifier|final
name|String
name|CAMEL_SQL_QUERY
init|=
literal|"CamelSqlQuery"
decl_stmt|;
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|String
name|getComponent
parameter_list|()
block|{
return|return
literal|"sql"
return|;
block|}
annotation|@
name|Override
DECL|method|getComponentClassName ()
specifier|public
name|String
name|getComponentClassName
parameter_list|()
block|{
return|return
literal|"org.apache.camel.component.sql.SqlComponent"
return|;
block|}
annotation|@
name|Override
DECL|method|pre (Span span, Exchange exchange, Endpoint endpoint)
specifier|public
name|void
name|pre
parameter_list|(
name|Span
name|span
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Endpoint
name|endpoint
parameter_list|)
block|{
name|super
operator|.
name|pre
argument_list|(
name|span
argument_list|,
name|exchange
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|span
operator|.
name|setTag
argument_list|(
name|Tags
operator|.
name|DB_TYPE
operator|.
name|getKey
argument_list|()
argument_list|,
literal|"sql"
argument_list|)
expr_stmt|;
name|Object
name|sqlquery
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CAMEL_SQL_QUERY
argument_list|)
decl_stmt|;
if|if
condition|(
name|sqlquery
operator|instanceof
name|String
condition|)
block|{
name|span
operator|.
name|setTag
argument_list|(
name|Tags
operator|.
name|DB_STATEMENT
operator|.
name|getKey
argument_list|()
argument_list|,
operator|(
name|String
operator|)
name|sqlquery
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

