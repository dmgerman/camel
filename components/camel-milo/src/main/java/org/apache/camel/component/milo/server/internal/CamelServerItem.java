begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.milo.server.internal
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|milo
operator|.
name|server
operator|.
name|internal
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|CopyOnWriteArraySet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Consumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|core
operator|.
name|AccessLevel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|api
operator|.
name|ServerNodeMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|nodes
operator|.
name|UaObjectNode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|sdk
operator|.
name|server
operator|.
name|nodes
operator|.
name|UaVariableNode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|DataValue
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|DateTime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|LocalizedText
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|NodeId
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|QualifiedName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|StatusCode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|Variant
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|unsigned
operator|.
name|UShort
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
import|import static
name|org
operator|.
name|eclipse
operator|.
name|milo
operator|.
name|opcua
operator|.
name|stack
operator|.
name|core
operator|.
name|types
operator|.
name|builtin
operator|.
name|unsigned
operator|.
name|Unsigned
operator|.
name|ubyte
import|;
end_import

begin_class
DECL|class|CamelServerItem
specifier|public
class|class
name|CamelServerItem
block|{
DECL|field|LOG
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|CamelServerItem
operator|.
name|class
argument_list|)
decl_stmt|;
DECL|field|itemId
specifier|private
specifier|final
name|String
name|itemId
decl_stmt|;
DECL|field|baseNode
specifier|private
specifier|final
name|UaObjectNode
name|baseNode
decl_stmt|;
DECL|field|item
specifier|private
specifier|final
name|UaVariableNode
name|item
decl_stmt|;
DECL|field|listeners
specifier|private
specifier|final
name|Set
argument_list|<
name|Consumer
argument_list|<
name|DataValue
argument_list|>
argument_list|>
name|listeners
init|=
operator|new
name|CopyOnWriteArraySet
argument_list|<>
argument_list|()
decl_stmt|;
DECL|field|value
specifier|private
name|DataValue
name|value
init|=
operator|new
name|DataValue
argument_list|(
name|StatusCode
operator|.
name|BAD
argument_list|)
decl_stmt|;
DECL|method|CamelServerItem (final String itemId, final ServerNodeMap nodeManager, final UShort namespaceIndex, final UaObjectNode baseNode)
specifier|public
name|CamelServerItem
parameter_list|(
specifier|final
name|String
name|itemId
parameter_list|,
specifier|final
name|ServerNodeMap
name|nodeManager
parameter_list|,
specifier|final
name|UShort
name|namespaceIndex
parameter_list|,
specifier|final
name|UaObjectNode
name|baseNode
parameter_list|)
block|{
name|this
operator|.
name|itemId
operator|=
name|itemId
expr_stmt|;
name|this
operator|.
name|baseNode
operator|=
name|baseNode
expr_stmt|;
specifier|final
name|NodeId
name|nodeId
init|=
operator|new
name|NodeId
argument_list|(
name|namespaceIndex
argument_list|,
literal|"items-"
operator|+
name|itemId
argument_list|)
decl_stmt|;
specifier|final
name|QualifiedName
name|qname
init|=
operator|new
name|QualifiedName
argument_list|(
name|namespaceIndex
argument_list|,
name|itemId
argument_list|)
decl_stmt|;
specifier|final
name|LocalizedText
name|displayName
init|=
name|LocalizedText
operator|.
name|english
argument_list|(
name|itemId
argument_list|)
decl_stmt|;
comment|// create variable node
name|this
operator|.
name|item
operator|=
operator|new
name|UaVariableNode
argument_list|(
name|nodeManager
argument_list|,
name|nodeId
argument_list|,
name|qname
argument_list|,
name|displayName
argument_list|)
block|{
annotation|@
name|Override
specifier|public
specifier|synchronized
name|DataValue
name|getValue
parameter_list|()
block|{
return|return
name|getDataValue
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
specifier|synchronized
name|void
name|setValue
parameter_list|(
specifier|final
name|DataValue
name|value
parameter_list|)
block|{
name|setDataValue
argument_list|(
name|value
argument_list|)
expr_stmt|;
block|}
block|}
expr_stmt|;
comment|// item.setDataType();
name|this
operator|.
name|item
operator|.
name|setAccessLevel
argument_list|(
name|ubyte
argument_list|(
name|AccessLevel
operator|.
name|getMask
argument_list|(
name|AccessLevel
operator|.
name|READ_WRITE
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|item
operator|.
name|setUserAccessLevel
argument_list|(
name|ubyte
argument_list|(
name|AccessLevel
operator|.
name|getMask
argument_list|(
name|AccessLevel
operator|.
name|READ_WRITE
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|baseNode
operator|.
name|addComponent
argument_list|(
name|this
operator|.
name|item
argument_list|)
expr_stmt|;
block|}
DECL|method|dispose ()
specifier|public
name|void
name|dispose
parameter_list|()
block|{
name|this
operator|.
name|baseNode
operator|.
name|removeComponent
argument_list|(
name|this
operator|.
name|item
argument_list|)
expr_stmt|;
name|this
operator|.
name|listeners
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
DECL|method|addWriteListener (final Consumer<DataValue> consumer)
specifier|public
name|void
name|addWriteListener
parameter_list|(
specifier|final
name|Consumer
argument_list|<
name|DataValue
argument_list|>
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|listeners
operator|.
name|add
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|removeWriteListener (final Consumer<DataValue> consumer)
specifier|public
name|void
name|removeWriteListener
parameter_list|(
specifier|final
name|Consumer
argument_list|<
name|DataValue
argument_list|>
name|consumer
parameter_list|)
block|{
name|this
operator|.
name|listeners
operator|.
name|remove
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
block|}
DECL|method|setDataValue (final DataValue value)
specifier|protected
name|void
name|setDataValue
parameter_list|(
specifier|final
name|DataValue
name|value
parameter_list|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"setValue -> {}"
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|runThrough
argument_list|(
name|this
operator|.
name|listeners
argument_list|,
name|c
lambda|->
name|c
operator|.
name|accept
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Run through a list, aggregating errors      *<p>      * The consumer is called for each list item, regardless if the consumer did      * through an exception. All exceptions are caught and thrown in one      * RuntimeException. The first exception being wrapped directly while the      * latter ones, if any, are added as suppressed exceptions.      *</p>      *      * @param list the list to run through      * @param consumer the consumer processing list elements      */
DECL|method|runThrough (final Collection<Consumer<T>> list, final Consumer<Consumer<T>> consumer)
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|void
name|runThrough
parameter_list|(
specifier|final
name|Collection
argument_list|<
name|Consumer
argument_list|<
name|T
argument_list|>
argument_list|>
name|list
parameter_list|,
specifier|final
name|Consumer
argument_list|<
name|Consumer
argument_list|<
name|T
argument_list|>
argument_list|>
name|consumer
parameter_list|)
block|{
name|LinkedList
argument_list|<
name|Throwable
argument_list|>
name|errors
init|=
literal|null
decl_stmt|;
for|for
control|(
specifier|final
name|Consumer
argument_list|<
name|T
argument_list|>
name|listener
range|:
name|list
control|)
block|{
try|try
block|{
name|consumer
operator|.
name|accept
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
specifier|final
name|Throwable
name|e
parameter_list|)
block|{
if|if
condition|(
name|errors
operator|==
literal|null
condition|)
block|{
name|errors
operator|=
operator|new
name|LinkedList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
name|errors
operator|.
name|add
argument_list|(
name|e
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|errors
operator|==
literal|null
operator|||
name|errors
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
specifier|final
name|RuntimeException
name|ex
init|=
operator|new
name|RuntimeException
argument_list|(
name|errors
operator|.
name|pollFirst
argument_list|()
argument_list|)
decl_stmt|;
name|errors
operator|.
name|forEach
argument_list|(
name|ex
operator|::
name|addSuppressed
argument_list|)
expr_stmt|;
throw|throw
name|ex
throw|;
block|}
DECL|method|getDataValue ()
specifier|protected
name|DataValue
name|getDataValue
parameter_list|()
block|{
return|return
name|this
operator|.
name|value
return|;
block|}
DECL|method|update (final Object value)
specifier|public
name|void
name|update
parameter_list|(
specifier|final
name|Object
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|instanceof
name|DataValue
condition|)
block|{
name|this
operator|.
name|value
operator|=
operator|(
name|DataValue
operator|)
name|value
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|value
operator|instanceof
name|Variant
condition|)
block|{
name|this
operator|.
name|value
operator|=
operator|new
name|DataValue
argument_list|(
operator|(
name|Variant
operator|)
name|value
argument_list|,
name|StatusCode
operator|.
name|GOOD
argument_list|,
name|DateTime
operator|.
name|now
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|this
operator|.
name|value
operator|=
operator|new
name|DataValue
argument_list|(
operator|new
name|Variant
argument_list|(
name|value
argument_list|)
argument_list|,
name|StatusCode
operator|.
name|GOOD
argument_list|,
name|DateTime
operator|.
name|now
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
DECL|method|toString ()
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"[CamelServerItem - '"
operator|+
name|this
operator|.
name|itemId
operator|+
literal|"']"
return|;
block|}
block|}
end_class

end_unit

