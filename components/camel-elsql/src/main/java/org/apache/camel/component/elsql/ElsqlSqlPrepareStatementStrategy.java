begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.elsql
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|elsql
package|;
end_package

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|PreparedStatement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|sql
operator|.
name|SQLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_import
import|import
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
name|SqlPrepareStatementStrategy
import|;
end_import

begin_class
DECL|class|ElsqlSqlPrepareStatementStrategy
specifier|public
class|class
name|ElsqlSqlPrepareStatementStrategy
implements|implements
name|SqlPrepareStatementStrategy
block|{
annotation|@
name|Override
DECL|method|prepareQuery (String query, boolean allowNamedParameters, Exchange exchange)
specifier|public
name|String
name|prepareQuery
parameter_list|(
name|String
name|query
parameter_list|,
name|boolean
name|allowNamedParameters
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
name|query
return|;
block|}
annotation|@
name|Override
DECL|method|createPopulateIterator (String query, String preparedQuery, int expectedParams, Exchange exchange, Object value)
specifier|public
name|Iterator
argument_list|<
name|?
argument_list|>
name|createPopulateIterator
parameter_list|(
name|String
name|query
parameter_list|,
name|String
name|preparedQuery
parameter_list|,
name|int
name|expectedParams
parameter_list|,
name|Exchange
name|exchange
parameter_list|,
name|Object
name|value
parameter_list|)
throws|throws
name|SQLException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|populateStatement (PreparedStatement ps, Iterator<?> iterator, int expectedParams)
specifier|public
name|void
name|populateStatement
parameter_list|(
name|PreparedStatement
name|ps
parameter_list|,
name|Iterator
argument_list|<
name|?
argument_list|>
name|iterator
parameter_list|,
name|int
name|expectedParams
parameter_list|)
throws|throws
name|SQLException
block|{
comment|// noop
block|}
block|}
end_class

end_unit

