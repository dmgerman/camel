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
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|math
operator|.
name|BigDecimal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Calendar
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Property
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|PropertyIterator
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jcr
operator|.
name|PropertyType
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|util
operator|.
name|ObjectHelper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|jackrabbit
operator|.
name|util
operator|.
name|Text
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
name|Session
name|session
init|=
name|openSession
argument_list|()
decl_stmt|;
name|Message
name|message
init|=
name|exchange
operator|.
name|getIn
argument_list|()
decl_stmt|;
name|String
name|operation
init|=
name|determineOperation
argument_list|(
name|message
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
name|JcrConstants
operator|.
name|JCR_INSERT
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|Node
name|base
init|=
name|findOrCreateNode
argument_list|(
name|session
operator|.
name|getRootNode
argument_list|()
argument_list|,
name|getJcrEndpoint
argument_list|()
operator|.
name|getBase
argument_list|()
argument_list|,
literal|""
argument_list|)
decl_stmt|;
name|Node
name|node
init|=
name|findOrCreateNode
argument_list|(
name|base
argument_list|,
name|getNodeName
argument_list|(
name|message
argument_list|)
argument_list|,
name|getNodeType
argument_list|(
name|message
argument_list|)
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
init|=
name|filterComponentHeaders
argument_list|(
name|message
operator|.
name|getHeaders
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|key
range|:
name|headers
operator|.
name|keySet
argument_list|()
control|)
block|{
name|Object
name|header
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|header
operator|!=
literal|null
operator|&&
name|Object
index|[]
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|header
operator|.
name|getClass
argument_list|()
argument_list|)
condition|)
block|{
name|Value
index|[]
name|value
init|=
name|converter
operator|.
name|convertTo
argument_list|(
name|Value
index|[]
operator|.
expr|class
argument_list|,
name|exchange
argument_list|,
name|header
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
else|else
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
name|header
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
block|}
name|node
operator|.
name|addMixin
argument_list|(
literal|"mix:referenceable"
argument_list|)
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
name|getIdentifier
argument_list|()
argument_list|)
expr_stmt|;
name|session
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|JcrConstants
operator|.
name|JCR_GET_BY_ID
operator|.
name|equals
argument_list|(
name|operation
argument_list|)
condition|)
block|{
name|Node
name|node
init|=
name|session
operator|.
name|getNodeByIdentifier
argument_list|(
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getMandatoryBody
argument_list|(
name|String
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|PropertyIterator
name|properties
init|=
name|node
operator|.
name|getProperties
argument_list|()
decl_stmt|;
while|while
condition|(
name|properties
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Property
name|property
init|=
name|properties
operator|.
name|nextProperty
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
init|=
name|classForJCRType
argument_list|(
name|property
argument_list|)
decl_stmt|;
name|Object
name|value
decl_stmt|;
if|if
condition|(
name|property
operator|.
name|isMultiple
argument_list|()
condition|)
block|{
name|value
operator|=
name|converter
operator|.
name|convertTo
argument_list|(
name|aClass
argument_list|,
name|exchange
argument_list|,
name|property
operator|.
name|getValues
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|value
operator|=
name|converter
operator|.
name|convertTo
argument_list|(
name|aClass
argument_list|,
name|exchange
argument_list|,
name|property
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|setHeader
argument_list|(
name|property
operator|.
name|getName
argument_list|()
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Unsupported operation: "
operator|+
name|operation
argument_list|)
throw|;
block|}
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
DECL|method|filterComponentHeaders (Map<String, Object> properties)
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|filterComponentHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|result
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|properties
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|properties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|key
operator|.
name|equals
argument_list|(
name|JcrConstants
operator|.
name|JCR_NODE_NAME
argument_list|)
operator|&&
operator|!
name|key
operator|.
name|equals
argument_list|(
name|JcrConstants
operator|.
name|JCR_OPERATION
argument_list|)
operator|&&
operator|!
name|key
operator|.
name|equals
argument_list|(
name|JcrConstants
operator|.
name|JCR_NODE_TYPE
argument_list|)
condition|)
block|{
name|result
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|result
return|;
block|}
DECL|method|classForJCRType (Property property)
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|classForJCRType
parameter_list|(
name|Property
name|property
parameter_list|)
throws|throws
name|RepositoryException
block|{
switch|switch
condition|(
name|property
operator|.
name|getType
argument_list|()
condition|)
block|{
case|case
name|PropertyType
operator|.
name|STRING
case|:
return|return
name|String
operator|.
name|class
return|;
case|case
name|PropertyType
operator|.
name|BINARY
case|:
return|return
name|InputStream
operator|.
name|class
return|;
case|case
name|PropertyType
operator|.
name|BOOLEAN
case|:
return|return
name|Boolean
operator|.
name|class
return|;
case|case
name|PropertyType
operator|.
name|LONG
case|:
return|return
name|Long
operator|.
name|class
return|;
case|case
name|PropertyType
operator|.
name|DOUBLE
case|:
return|return
name|Double
operator|.
name|class
return|;
case|case
name|PropertyType
operator|.
name|DECIMAL
case|:
return|return
name|BigDecimal
operator|.
name|class
return|;
case|case
name|PropertyType
operator|.
name|DATE
case|:
return|return
name|Calendar
operator|.
name|class
return|;
case|case
name|PropertyType
operator|.
name|NAME
case|:
return|return
name|String
operator|.
name|class
return|;
case|case
name|PropertyType
operator|.
name|PATH
case|:
return|return
name|String
operator|.
name|class
return|;
case|case
name|PropertyType
operator|.
name|REFERENCE
case|:
return|return
name|String
operator|.
name|class
return|;
case|case
name|PropertyType
operator|.
name|WEAKREFERENCE
case|:
return|return
name|String
operator|.
name|class
return|;
case|case
name|PropertyType
operator|.
name|URI
case|:
return|return
name|String
operator|.
name|class
return|;
case|case
name|PropertyType
operator|.
name|UNDEFINED
case|:
return|return
name|String
operator|.
name|class
return|;
default|default:
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"unknown type: "
operator|+
name|property
operator|.
name|getType
argument_list|()
argument_list|)
throw|;
block|}
block|}
DECL|method|determineOperation (Message message)
specifier|private
name|String
name|determineOperation
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|operation
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|JcrConstants
operator|.
name|JCR_OPERATION
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|operation
operator|!=
literal|null
condition|?
name|operation
else|:
name|JcrConstants
operator|.
name|JCR_INSERT
return|;
block|}
DECL|method|getNodeName (Message message)
specifier|private
name|String
name|getNodeName
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|nodeName
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|JcrConstants
operator|.
name|JCR_NODE_NAME
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|nodeName
operator|!=
literal|null
condition|?
name|nodeName
else|:
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getExchangeId
argument_list|()
return|;
block|}
DECL|method|getNodeType (Message message)
specifier|private
name|String
name|getNodeType
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|String
name|nodeType
init|=
name|message
operator|.
name|getHeader
argument_list|(
name|JcrConstants
operator|.
name|JCR_NODE_TYPE
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|nodeType
operator|!=
literal|null
condition|?
name|nodeType
else|:
literal|""
return|;
block|}
DECL|method|findOrCreateNode (Node parent, String path, String nodeType)
specifier|private
name|Node
name|findOrCreateNode
parameter_list|(
name|Node
name|parent
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|nodeType
parameter_list|)
throws|throws
name|RepositoryException
block|{
name|Node
name|result
init|=
name|parent
decl_stmt|;
for|for
control|(
name|String
name|component
range|:
name|path
operator|.
name|split
argument_list|(
literal|"/"
argument_list|)
control|)
block|{
name|component
operator|=
name|Text
operator|.
name|escapeIllegalJcrChars
argument_list|(
name|component
argument_list|)
expr_stmt|;
if|if
condition|(
name|component
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
if|if
condition|(
name|result
operator|.
name|hasNode
argument_list|(
name|component
argument_list|)
condition|)
block|{
name|result
operator|=
name|result
operator|.
name|getNode
argument_list|(
name|component
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|ObjectHelper
operator|.
name|isNotEmpty
argument_list|(
name|nodeType
argument_list|)
condition|)
block|{
name|result
operator|=
name|result
operator|.
name|addNode
argument_list|(
name|component
argument_list|,
name|nodeType
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|result
operator|.
name|addNode
argument_list|(
name|component
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|result
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
if|if
condition|(
name|ObjectHelper
operator|.
name|isEmpty
argument_list|(
name|getJcrEndpoint
argument_list|()
operator|.
name|getWorkspaceName
argument_list|()
argument_list|)
condition|)
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
else|else
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
argument_list|,
name|getJcrEndpoint
argument_list|()
operator|.
name|getWorkspaceName
argument_list|()
argument_list|)
return|;
block|}
block|}
DECL|method|getJcrEndpoint ()
specifier|private
name|JcrEndpoint
name|getJcrEndpoint
parameter_list|()
block|{
return|return
operator|(
name|JcrEndpoint
operator|)
name|getEndpoint
argument_list|()
return|;
block|}
block|}
end_class

end_unit

