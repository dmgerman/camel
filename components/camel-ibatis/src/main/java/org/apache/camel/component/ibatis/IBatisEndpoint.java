begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.ibatis
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|ibatis
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
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
name|List
import|;
end_import

begin_import
import|import
name|com
operator|.
name|ibatis
operator|.
name|sqlmap
operator|.
name|client
operator|.
name|SqlMapClient
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
name|Message
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
name|PollingConsumer
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
name|Producer
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
name|impl
operator|.
name|DefaultPollingEndpoint
import|;
end_import

begin_comment
comment|/**  * An<a href="http://activemq.apache.org/camel/ibatis.html>iBatis Endpoint</a>  * for performing SQL operations using an XML mapping file to abstract away the SQL  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|IBatisEndpoint
specifier|public
class|class
name|IBatisEndpoint
extends|extends
name|DefaultPollingEndpoint
block|{
DECL|field|entityName
specifier|private
specifier|final
name|String
name|entityName
decl_stmt|;
DECL|method|IBatisEndpoint (String endpointUri, IBatisComponent component, String entityName)
specifier|public
name|IBatisEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|IBatisComponent
name|component
parameter_list|,
name|String
name|entityName
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|,
name|component
argument_list|)
expr_stmt|;
name|this
operator|.
name|entityName
operator|=
name|entityName
expr_stmt|;
block|}
DECL|method|IBatisEndpoint (String endpointUri, String entityName)
specifier|public
name|IBatisEndpoint
parameter_list|(
name|String
name|endpointUri
parameter_list|,
name|String
name|entityName
parameter_list|)
block|{
name|super
argument_list|(
name|endpointUri
argument_list|)
expr_stmt|;
name|this
operator|.
name|entityName
operator|=
name|entityName
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getComponent ()
specifier|public
name|IBatisComponent
name|getComponent
parameter_list|()
block|{
return|return
operator|(
name|IBatisComponent
operator|)
name|super
operator|.
name|getComponent
argument_list|()
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|IBatisProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|createPollingConsumer ()
specifier|public
name|PollingConsumer
name|createPollingConsumer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|IBatisPollingConsumer
argument_list|(
name|this
argument_list|)
return|;
block|}
comment|/**      * Returns the iBatis SQL client      */
DECL|method|getSqlClient ()
specifier|public
name|SqlMapClient
name|getSqlClient
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|getComponent
argument_list|()
operator|.
name|getSqlMapClient
argument_list|()
return|;
block|}
DECL|method|getEntityName ()
specifier|public
name|String
name|getEntityName
parameter_list|()
block|{
return|return
name|entityName
return|;
block|}
DECL|method|query (Message message)
specifier|public
name|void
name|query
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|IOException
throws|,
name|SQLException
block|{
name|String
name|name
init|=
name|getEntityName
argument_list|()
decl_stmt|;
name|List
name|list
init|=
name|getSqlClient
argument_list|()
operator|.
name|queryForList
argument_list|(
name|name
argument_list|)
decl_stmt|;
name|message
operator|.
name|setBody
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|message
operator|.
name|setHeader
argument_list|(
literal|"org.apache.camel.ibatis.queryName"
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

