begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.jcr
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|jcr
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|RepositoryException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|Value
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
name|TypeConverter
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
name|DefaultProducer
import|;
end_import

begin_class
DECL|class|JcrProducer
specifier|public
class|class
name|JcrProducer
extends|extends
name|DefaultProducer
block|{
DECL|method|JcrProducer (JcrEndpoint jcrEndpoint)
specifier|public
name|JcrProducer
parameter_list|(
name|JcrEndpoint
name|jcrEndpoint
parameter_list|)
throws|throws
name|RepositoryException
block|{
name|super
argument_list|(
name|jcrEndpoint
argument_list|)
expr_stmt|;
block|}
DECL|method|process (Exchange exchange)
specifier|public
name|void
name|process
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
throws|throws
name|Exception
block|{
name|Session
name|session
init|=
name|openSession
argument_list|()
decl_stmt|;
try|try
block|{
name|Node
name|base
init|=
name|getBaseNode
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|Node
name|node
init|=
name|base
operator|.
name|addNode
argument_list|(
name|getNodeName
argument_list|(
name|exchange
argument_list|)
argument_list|)
decl_stmt|;
name|TypeConverter
name|converter
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getTypeConverter
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|exchange
operator|.
name|getProperties
argument_list|()
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Value
name|value
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Value
operator|.
name|class
argument_list|,
name|exchange
argument_list|,
name|exchange
operator|.
name|getProperty
argument_list|(
name|key
argument_list|)
argument_list|)
decl_stmt|;
name|node
operator|.
name|setProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|node
operator|.
name|addMixin
argument_list|(
literal|"mix:referenceable"
argument_list|)
expr_stmt|;
name|session
operator|.
name|save
argument_list|()
expr_stmt|;
name|exchange
operator|.
name|getOut
argument_list|()
operator|.
name|setBody
argument_list|(
name|node
operator|.
name|getUUID
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|session
operator|!=
literal|null
operator|&&
name|session
operator|.
name|isLive
argument_list|()
condition|)
block|{
name|session
operator|.
name|logout
argument_list|()
expr_stmt|;
block|}
block|}
block|}
DECL|method|getNodeName (Exchange exchange)
specifier|private
name|String
name|getNodeName
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getProperty
argument_list|(
name|JcrConstants
operator|.
name|JCR_NODE_NAME
argument_list|)
operator|!=
literal|null
condition|)
block|{
return|return
name|exchange
operator|.
name|getProperty
argument_list|(
name|JcrConstants
operator|.
name|JCR_NODE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
return|;
block|}
return|return
name|exchange
operator|.
name|getExchangeId
argument_list|()
return|;
block|}
DECL|method|getBaseNode (Session session)
specifier|private
name|Node
name|getBaseNode
parameter_list|(
name|Session
name|session
parameter_list|)
throws|throws
name|Exception
block|{
name|Node
name|baseNode
init|=
name|session
operator|.
name|getRootNode
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|node
range|:
name|getJcrEndpoint
argument_list|()
operator|.
name|getBase
argument_list|()
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
control|)
block|{
name|baseNode
operator|=
name|baseNode
operator|.
name|addNode
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
return|return
name|baseNode
return|;
block|}
DECL|method|openSession ()
specifier|protected
name|Session
name|openSession
parameter_list|()
throws|throws
name|RepositoryException
block|{
return|return
name|getJcrEndpoint
argument_list|()
operator|.
name|getRepository
argument_list|()
operator|.
name|login
argument_list|(
name|getJcrEndpoint
argument_list|()
operator|.
name|getCredentials
argument_list|()
argument_list|)
return|;
block|}
DECL|method|getJcrEndpoint ()
specifier|private
name|JcrEndpoint
name|getJcrEndpoint
parameter_list|()
block|{
name|JcrEndpoint
name|endpoint
init|=
operator|(
name|JcrEndpoint
operator|)
name|getEndpoint
argument_list|()
decl_stmt|;
return|return
name|endpoint
return|;
block|}
block|}
end_class

end_unit

