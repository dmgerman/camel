begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.aws.xray.decorators
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|aws
operator|.
name|xray
operator|.
name|decorators
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|com
operator|.
name|amazonaws
operator|.
name|xray
operator|.
name|entities
operator|.
name|Entity
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
DECL|class|CqlSegmentDecorator
specifier|public
class|class
name|CqlSegmentDecorator
extends|extends
name|AbstractSegmentDecorator
block|{
DECL|field|CASSANDRA_DB_TYPE
specifier|public
specifier|static
specifier|final
name|String
name|CASSANDRA_DB_TYPE
init|=
literal|"cassandra"
decl_stmt|;
DECL|field|CAMEL_CQL_QUERY
specifier|protected
specifier|static
specifier|final
name|String
name|CAMEL_CQL_QUERY
init|=
literal|"CamelCqlQuery"
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
literal|"cql"
return|;
block|}
annotation|@
name|Override
DECL|method|pre (Entity segment, Exchange exchange, Endpoint endpoint)
specifier|public
name|void
name|pre
parameter_list|(
name|Entity
name|segment
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
name|segment
argument_list|,
name|exchange
argument_list|,
name|endpoint
argument_list|)
expr_stmt|;
name|segment
operator|.
name|putMetadata
argument_list|(
literal|"db.type"
argument_list|,
name|CASSANDRA_DB_TYPE
argument_list|)
expr_stmt|;
name|URI
name|uri
init|=
name|URI
operator|.
name|create
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|uri
operator|.
name|getPath
argument_list|()
operator|!=
literal|null
operator|&&
name|uri
operator|.
name|getPath
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
comment|// Strip leading '/' from path
name|segment
operator|.
name|putMetadata
argument_list|(
literal|"db.instance"
argument_list|,
name|uri
operator|.
name|getPath
argument_list|()
operator|.
name|substring
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Object
name|cql
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|CAMEL_CQL_QUERY
argument_list|)
decl_stmt|;
if|if
condition|(
name|cql
operator|!=
literal|null
condition|)
block|{
name|segment
operator|.
name|putSql
argument_list|(
literal|"db.statement"
argument_list|,
name|cql
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|queryParameters
init|=
name|toQueryParameters
argument_list|(
name|endpoint
operator|.
name|getEndpointUri
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|queryParameters
operator|.
name|containsKey
argument_list|(
literal|"cql"
argument_list|)
condition|)
block|{
name|segment
operator|.
name|putSql
argument_list|(
literal|"db.statement"
argument_list|,
name|queryParameters
operator|.
name|get
argument_list|(
literal|"cql"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

