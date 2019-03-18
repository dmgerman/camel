begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.file
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|file
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashMap
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
name|Processor
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
name|runtimecatalog
operator|.
name|RuntimeCamelCatalog
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
name|spi
operator|.
name|SendDynamicAware
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
name|URISupport
import|;
end_import

begin_class
DECL|class|GenericFileSendDynamicAware
specifier|public
specifier|abstract
class|class
name|GenericFileSendDynamicAware
implements|implements
name|SendDynamicAware
block|{
DECL|field|scheme
specifier|private
name|String
name|scheme
decl_stmt|;
annotation|@
name|Override
DECL|method|setScheme (String scheme)
specifier|public
name|void
name|setScheme
parameter_list|(
name|String
name|scheme
parameter_list|)
block|{
name|this
operator|.
name|scheme
operator|=
name|scheme
expr_stmt|;
block|}
annotation|@
name|Override
DECL|method|getScheme ()
specifier|public
name|String
name|getScheme
parameter_list|()
block|{
return|return
name|scheme
return|;
block|}
annotation|@
name|Override
DECL|method|prepare (Exchange exchange, String uri, String originalUri)
specifier|public
name|DynamicAwareEntry
name|prepare
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|String
name|uri
parameter_list|,
name|String
name|originalUri
parameter_list|)
throws|throws
name|Exception
block|{
name|RuntimeCamelCatalog
name|catalog
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getExtension
argument_list|(
name|RuntimeCamelCatalog
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|properties
init|=
name|catalog
operator|.
name|endpointProperties
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|lenient
init|=
name|catalog
operator|.
name|endpointLenientProperties
argument_list|(
name|uri
argument_list|)
decl_stmt|;
return|return
operator|new
name|DynamicAwareEntry
argument_list|(
name|uri
argument_list|,
name|originalUri
argument_list|,
name|properties
argument_list|,
name|lenient
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|resolveStaticUri (Exchange exchange, DynamicAwareEntry entry)
specifier|public
name|String
name|resolveStaticUri
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|DynamicAwareEntry
name|entry
parameter_list|)
throws|throws
name|Exception
block|{
name|boolean
name|fileName
init|=
name|entry
operator|.
name|getProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"fileName"
argument_list|)
decl_stmt|;
name|boolean
name|tempFileName
init|=
name|entry
operator|.
name|getProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"tempFileName"
argument_list|)
decl_stmt|;
name|boolean
name|idempotentKey
init|=
name|entry
operator|.
name|getProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"idempotentKey"
argument_list|)
decl_stmt|;
name|boolean
name|move
init|=
name|entry
operator|.
name|getProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"move"
argument_list|)
decl_stmt|;
name|boolean
name|moveFailed
init|=
name|entry
operator|.
name|getProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"moveFailed"
argument_list|)
decl_stmt|;
name|boolean
name|preMove
init|=
name|entry
operator|.
name|getProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"preMove"
argument_list|)
decl_stmt|;
name|boolean
name|moveExisting
init|=
name|entry
operator|.
name|getProperties
argument_list|()
operator|.
name|containsKey
argument_list|(
literal|"moveExisting"
argument_list|)
decl_stmt|;
comment|// if any of the above are in use, then they should not be pre evaluated
comment|// and we need to rebuild a new uri with them as-is
if|if
condition|(
name|fileName
operator|||
name|tempFileName
operator|||
name|idempotentKey
operator|||
name|move
operator|||
name|moveFailed
operator|||
name|preMove
operator|||
name|moveExisting
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
init|=
operator|new
name|LinkedHashMap
argument_list|<>
argument_list|(
name|entry
operator|.
name|getProperties
argument_list|()
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|originalParams
init|=
name|URISupport
operator|.
name|parseQuery
argument_list|(
name|entry
operator|.
name|getOriginalUri
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|fileName
condition|)
block|{
name|Object
name|val
init|=
name|originalParams
operator|.
name|get
argument_list|(
literal|"fileName"
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"fileName"
argument_list|,
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|tempFileName
condition|)
block|{
name|Object
name|val
init|=
name|originalParams
operator|.
name|get
argument_list|(
literal|"tempFileName"
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"tempFileName"
argument_list|,
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|idempotentKey
condition|)
block|{
name|Object
name|val
init|=
name|originalParams
operator|.
name|get
argument_list|(
literal|"idempotentKey"
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"idempotentKey"
argument_list|,
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|move
condition|)
block|{
name|Object
name|val
init|=
name|originalParams
operator|.
name|get
argument_list|(
literal|"move"
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"move"
argument_list|,
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|moveFailed
condition|)
block|{
name|Object
name|val
init|=
name|originalParams
operator|.
name|get
argument_list|(
literal|"moveFailed"
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"moveFailed"
argument_list|,
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|preMove
condition|)
block|{
name|Object
name|val
init|=
name|originalParams
operator|.
name|get
argument_list|(
literal|"preMove"
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"preMove"
argument_list|,
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|moveExisting
condition|)
block|{
name|Object
name|val
init|=
name|originalParams
operator|.
name|get
argument_list|(
literal|"moveExisting"
argument_list|)
decl_stmt|;
if|if
condition|(
name|val
operator|!=
literal|null
condition|)
block|{
name|params
operator|.
name|put
argument_list|(
literal|"moveExisting"
argument_list|,
name|val
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|RuntimeCamelCatalog
name|catalog
init|=
name|exchange
operator|.
name|getContext
argument_list|()
operator|.
name|getExtension
argument_list|(
name|RuntimeCamelCatalog
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|catalog
operator|.
name|asEndpointUri
argument_list|(
name|scheme
argument_list|,
name|params
argument_list|,
literal|false
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|entry
operator|.
name|getUri
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
DECL|method|createPreProcessor (Exchange exchange, DynamicAwareEntry entry)
specifier|public
name|Processor
name|createPreProcessor
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|DynamicAwareEntry
name|entry
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
DECL|method|createPostProcessor (Exchange exchange, DynamicAwareEntry entry)
specifier|public
name|Processor
name|createPostProcessor
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
name|DynamicAwareEntry
name|entry
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

