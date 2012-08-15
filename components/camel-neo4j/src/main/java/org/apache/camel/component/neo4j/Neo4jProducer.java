begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.neo4j
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|neo4j
package|;
end_package

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
name|impl
operator|.
name|DefaultProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|neo4j
operator|.
name|graphdb
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|neo4j
operator|.
name|graphdb
operator|.
name|Relationship
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|data
operator|.
name|neo4j
operator|.
name|core
operator|.
name|GraphDatabase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|data
operator|.
name|neo4j
operator|.
name|support
operator|.
name|Neo4jTemplate
import|;
end_import

begin_class
DECL|class|Neo4jProducer
specifier|public
class|class
name|Neo4jProducer
extends|extends
name|DefaultProducer
block|{
DECL|field|LOGGER
specifier|private
specifier|static
specifier|final
name|Logger
name|LOGGER
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Neo4jProducer
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|template
specifier|private
specifier|final
name|Neo4jTemplate
name|template
decl_stmt|;
DECL|method|Neo4jProducer (Neo4jEndpoint endpoint, GraphDatabase graphDatabase)
specifier|public
name|Neo4jProducer
parameter_list|(
name|Neo4jEndpoint
name|endpoint
parameter_list|,
name|GraphDatabase
name|graphDatabase
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|template
operator|=
operator|new
name|Neo4jTemplate
argument_list|(
name|graphDatabase
argument_list|)
expr_stmt|;
block|}
DECL|method|Neo4jProducer (Neo4jEndpoint endpoint, GraphDatabase graphDatabase, Neo4jTemplate template)
specifier|public
name|Neo4jProducer
parameter_list|(
name|Neo4jEndpoint
name|endpoint
parameter_list|,
name|GraphDatabase
name|graphDatabase
parameter_list|,
name|Neo4jTemplate
name|template
parameter_list|)
block|{
name|super
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
name|this
operator|.
name|template
operator|=
name|template
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|createNode (Object body)
name|Node
name|createNode
parameter_list|(
name|Object
name|body
parameter_list|)
block|{
if|if
condition|(
name|body
operator|==
literal|null
condition|)
block|{
return|return
name|template
operator|.
name|createNode
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|Map
condition|)
block|{
return|return
name|template
operator|.
name|createNode
argument_list|(
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
operator|)
name|body
argument_list|)
return|;
block|}
throw|throw
operator|new
name|Neo4jException
argument_list|(
literal|"Unsupported body type for create node ["
operator|+
name|body
operator|.
name|getClass
argument_list|()
operator|+
literal|"]"
argument_list|)
throw|;
block|}
DECL|method|createRelationship (Object body)
name|Relationship
name|createRelationship
parameter_list|(
name|Object
name|body
parameter_list|)
block|{
if|if
condition|(
name|body
operator|instanceof
name|SpringDataRelationship
condition|)
block|{
name|SpringDataRelationship
argument_list|<
name|?
argument_list|>
name|r
init|=
operator|(
name|SpringDataRelationship
argument_list|<
name|?
argument_list|>
operator|)
name|body
decl_stmt|;
name|Object
name|rr
init|=
name|template
operator|.
name|createRelationshipBetween
argument_list|(
name|r
operator|.
name|getStart
argument_list|()
argument_list|,
name|r
operator|.
name|getEnd
argument_list|()
argument_list|,
name|r
operator|.
name|getRelationshipEntityClass
argument_list|()
argument_list|,
name|r
operator|.
name|getRelationshipType
argument_list|()
argument_list|,
name|r
operator|.
name|isAllowDuplicates
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|(
name|Relationship
operator|)
name|rr
return|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|BasicRelationship
condition|)
block|{
name|BasicRelationship
name|r
init|=
operator|(
name|BasicRelationship
operator|)
name|body
decl_stmt|;
name|Object
name|rr
init|=
name|template
operator|.
name|createRelationshipBetween
argument_list|(
name|r
operator|.
name|getStart
argument_list|()
argument_list|,
name|r
operator|.
name|getEnd
argument_list|()
argument_list|,
name|r
operator|.
name|getRelationshipType
argument_list|()
argument_list|,
name|r
operator|.
name|getProperties
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|(
name|Relationship
operator|)
name|rr
return|;
block|}
throw|throw
operator|new
name|Neo4jException
argument_list|(
literal|"Unsupported body type for create relationship ["
operator|+
name|body
operator|==
literal|null
condition|?
literal|"null"
else|:
name|body
operator|.
name|getClass
argument_list|()
operator|+
literal|"]"
argument_list|)
throw|;
block|}
annotation|@
name|Override
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
name|Object
name|body
init|=
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getBody
argument_list|()
decl_stmt|;
name|Neo4jOperation
name|op
init|=
operator|(
name|Neo4jOperation
operator|)
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|getHeader
argument_list|(
name|Neo4jEndpoint
operator|.
name|HEADER_OPERATION
argument_list|)
decl_stmt|;
if|if
condition|(
name|op
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|Neo4jException
argument_list|(
literal|"No operation specified for exchange "
operator|+
name|exchange
argument_list|)
throw|;
block|}
switch|switch
condition|(
name|op
condition|)
block|{
case|case
name|CREATE_NODE
case|:
name|Node
name|node
init|=
name|createNode
argument_list|(
name|body
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Node created [{}]"
argument_list|,
name|node
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Neo4jEndpoint
operator|.
name|HEADER_NODE_ID
argument_list|,
name|node
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|CREATE_RELATIONSHIP
case|:
name|Relationship
name|r
init|=
name|createRelationship
argument_list|(
name|body
argument_list|)
decl_stmt|;
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Relationship created [{}]"
argument_list|,
name|r
argument_list|)
expr_stmt|;
name|exchange
operator|.
name|getIn
argument_list|()
operator|.
name|setHeader
argument_list|(
name|Neo4jEndpoint
operator|.
name|HEADER_RELATIONSHIP_ID
argument_list|,
name|r
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
break|break;
case|case
name|REMOVE_NODE
case|:
name|removeNode
argument_list|(
name|body
argument_list|)
expr_stmt|;
break|break;
case|case
name|REMOVE_RELATIONSHIP
case|:
name|removeRelationship
argument_list|(
name|body
argument_list|)
expr_stmt|;
break|break;
default|default:
comment|// do nothing here.
block|}
block|}
DECL|method|removeNode (Object body)
name|void
name|removeNode
parameter_list|(
name|Object
name|body
parameter_list|)
block|{
if|if
condition|(
name|body
operator|instanceof
name|Number
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Deleting node by id ["
operator|+
name|body
operator|+
literal|"]"
argument_list|)
expr_stmt|;
name|Node
name|node
init|=
name|template
operator|.
name|getNode
argument_list|(
operator|(
operator|(
name|Number
operator|)
name|body
operator|)
operator|.
name|longValue
argument_list|()
argument_list|)
decl_stmt|;
name|template
operator|.
name|delete
argument_list|(
name|node
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|Node
condition|)
block|{
name|template
operator|.
name|delete
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|Neo4jException
argument_list|(
literal|"Unsupported body type for remove node ["
operator|+
name|body
operator|==
literal|null
condition|?
literal|"null"
else|:
name|body
operator|.
name|getClass
argument_list|()
operator|+
literal|"]"
argument_list|)
throw|;
block|}
block|}
DECL|method|removeRelationship (Object body)
name|void
name|removeRelationship
parameter_list|(
name|Object
name|body
parameter_list|)
block|{
if|if
condition|(
name|body
operator|instanceof
name|Number
condition|)
block|{
name|LOGGER
operator|.
name|debug
argument_list|(
literal|"Deleting relationship by id ["
operator|+
name|body
operator|+
literal|"]"
argument_list|)
expr_stmt|;
name|Relationship
name|r
init|=
name|template
operator|.
name|getRelationship
argument_list|(
operator|(
operator|(
name|Number
operator|)
name|body
operator|)
operator|.
name|longValue
argument_list|()
argument_list|)
decl_stmt|;
name|template
operator|.
name|delete
argument_list|(
name|r
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|Relationship
condition|)
block|{
name|template
operator|.
name|delete
argument_list|(
name|body
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|body
operator|instanceof
name|SpringDataRelationship
condition|)
block|{
name|SpringDataRelationship
argument_list|<
name|?
argument_list|>
name|r
init|=
operator|(
name|SpringDataRelationship
argument_list|<
name|?
argument_list|>
operator|)
name|body
decl_stmt|;
name|template
operator|.
name|deleteRelationshipBetween
argument_list|(
name|r
operator|.
name|getStart
argument_list|()
argument_list|,
name|r
operator|.
name|getEnd
argument_list|()
argument_list|,
name|r
operator|.
name|getRelationshipType
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|Neo4jException
argument_list|(
literal|"Unsupported body type for remove node ["
operator|+
name|body
operator|==
literal|null
condition|?
literal|"null"
else|:
name|body
operator|.
name|getClass
argument_list|()
operator|+
literal|"]"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

