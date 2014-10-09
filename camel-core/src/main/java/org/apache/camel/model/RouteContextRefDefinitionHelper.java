begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|Iterator
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
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Marshaller
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
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
name|model
operator|.
name|language
operator|.
name|NamespaceAwareExpression
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
name|CamelContextHelper
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

begin_comment
comment|/**  * Helper for {@link RouteContextRefDefinition}.  */
end_comment

begin_class
DECL|class|RouteContextRefDefinitionHelper
specifier|public
specifier|final
class|class
name|RouteContextRefDefinitionHelper
block|{
DECL|field|jaxbContext
specifier|private
specifier|static
name|JAXBContext
name|jaxbContext
decl_stmt|;
DECL|method|RouteContextRefDefinitionHelper ()
specifier|private
name|RouteContextRefDefinitionHelper
parameter_list|()
block|{     }
comment|/**      * Lookup the routes from the {@link RouteContextRefDefinition}.      *<p/>      * This implementation must be used to lookup the routes as it performs a deep clone of the routes      * as a {@link RouteContextRefDefinition} can be re-used with multiple {@link ModelCamelContext} and each      * context should have their own instances of the routes. This is to ensure no side-effects and sharing      * of instances between the contexts. For example such as property placeholders may be context specific      * so the routes should not use placeholders from another {@link ModelCamelContext}.      *      * @param camelContext the CamelContext      * @param ref          the id of the {@link RouteContextRefDefinition} to lookup and get the routes.      * @return the routes.      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
DECL|method|lookupRoutes (ModelCamelContext camelContext, String ref)
specifier|public
specifier|static
specifier|synchronized
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|lookupRoutes
parameter_list|(
name|ModelCamelContext
name|camelContext
parameter_list|,
name|String
name|ref
parameter_list|)
block|{
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|camelContext
argument_list|,
literal|"camelContext"
argument_list|)
expr_stmt|;
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|ref
argument_list|,
literal|"ref"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|answer
init|=
name|CamelContextHelper
operator|.
name|lookup
argument_list|(
name|camelContext
argument_list|,
name|ref
argument_list|,
name|List
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot find RouteContext with id "
operator|+
name|ref
argument_list|)
throw|;
block|}
comment|// must clone the route definitions as they can be reused with multiple CamelContexts
comment|// and they would need their own instances of the definitions to not have side effects among
comment|// the CamelContext - for example property placeholder resolutions etc.
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|clones
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteDefinition
argument_list|>
argument_list|(
name|answer
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|JAXBContext
name|jaxb
init|=
name|getOrCreateJAXBContext
argument_list|(
name|camelContext
argument_list|)
decl_stmt|;
for|for
control|(
name|RouteDefinition
name|def
range|:
name|answer
control|)
block|{
name|RouteDefinition
name|clone
init|=
name|cloneRouteDefinition
argument_list|(
name|jaxb
argument_list|,
name|def
argument_list|)
decl_stmt|;
if|if
condition|(
name|clone
operator|!=
literal|null
condition|)
block|{
name|clones
operator|.
name|add
argument_list|(
name|clone
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ObjectHelper
operator|.
name|wrapRuntimeCamelException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
name|clones
return|;
block|}
DECL|method|getOrCreateJAXBContext (final ModelCamelContext camelContext)
specifier|private
specifier|static
specifier|synchronized
name|JAXBContext
name|getOrCreateJAXBContext
parameter_list|(
specifier|final
name|ModelCamelContext
name|camelContext
parameter_list|)
throws|throws
name|JAXBException
block|{
if|if
condition|(
name|jaxbContext
operator|==
literal|null
condition|)
block|{
name|jaxbContext
operator|=
name|camelContext
operator|.
name|getModelJAXBContextFactory
argument_list|()
operator|.
name|newJAXBContext
argument_list|()
expr_stmt|;
block|}
return|return
name|jaxbContext
return|;
block|}
DECL|method|cloneRouteDefinition (JAXBContext jaxbContext, RouteDefinition def)
specifier|private
specifier|static
name|RouteDefinition
name|cloneRouteDefinition
parameter_list|(
name|JAXBContext
name|jaxbContext
parameter_list|,
name|RouteDefinition
name|def
parameter_list|)
throws|throws
name|JAXBException
block|{
name|Marshaller
name|marshal
init|=
name|jaxbContext
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|marshal
operator|.
name|marshal
argument_list|(
name|def
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|ByteArrayInputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|bos
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|Unmarshaller
name|unmarshaller
init|=
name|jaxbContext
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|Object
name|clone
init|=
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|bis
argument_list|)
decl_stmt|;
if|if
condition|(
name|clone
operator|!=
literal|null
operator|&&
name|clone
operator|instanceof
name|RouteDefinition
condition|)
block|{
name|RouteDefinition
name|def2
init|=
operator|(
name|RouteDefinition
operator|)
name|clone
decl_stmt|;
comment|// need to clone the namespaces also as they are not JAXB marshalled (as they are transient)
name|Iterator
argument_list|<
name|ExpressionNode
argument_list|>
name|it
init|=
name|ProcessorDefinitionHelper
operator|.
name|filterTypeInOutputs
argument_list|(
name|def
operator|.
name|getOutputs
argument_list|()
argument_list|,
name|ExpressionNode
operator|.
name|class
argument_list|)
decl_stmt|;
name|Iterator
argument_list|<
name|ExpressionNode
argument_list|>
name|it2
init|=
name|ProcessorDefinitionHelper
operator|.
name|filterTypeInOutputs
argument_list|(
name|def2
operator|.
name|getOutputs
argument_list|()
argument_list|,
name|ExpressionNode
operator|.
name|class
argument_list|)
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
operator|&&
name|it2
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ExpressionNode
name|node
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|ExpressionNode
name|node2
init|=
name|it2
operator|.
name|next
argument_list|()
decl_stmt|;
name|NamespaceAwareExpression
name|name
init|=
literal|null
decl_stmt|;
name|NamespaceAwareExpression
name|name2
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|node
operator|.
name|getExpression
argument_list|()
operator|instanceof
name|NamespaceAwareExpression
condition|)
block|{
name|name
operator|=
operator|(
name|NamespaceAwareExpression
operator|)
name|node
operator|.
name|getExpression
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|node2
operator|.
name|getExpression
argument_list|()
operator|instanceof
name|NamespaceAwareExpression
condition|)
block|{
name|name2
operator|=
operator|(
name|NamespaceAwareExpression
operator|)
name|node2
operator|.
name|getExpression
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|!=
literal|null
operator|&&
name|name2
operator|!=
literal|null
operator|&&
name|name
operator|.
name|getNamespaces
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
name|name
operator|.
name|getNamespaces
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|map
operator|.
name|putAll
argument_list|(
name|name
operator|.
name|getNamespaces
argument_list|()
argument_list|)
expr_stmt|;
name|name2
operator|.
name|setNamespaces
argument_list|(
name|map
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|def2
return|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

