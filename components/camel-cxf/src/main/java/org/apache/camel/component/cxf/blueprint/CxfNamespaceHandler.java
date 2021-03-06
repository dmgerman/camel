begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.cxf.blueprint
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|cxf
operator|.
name|blueprint
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|aries
operator|.
name|blueprint
operator|.
name|ParserContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|BaseNamespaceHandler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|reflect
operator|.
name|ComponentMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|service
operator|.
name|blueprint
operator|.
name|reflect
operator|.
name|Metadata
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

begin_class
DECL|class|CxfNamespaceHandler
specifier|public
class|class
name|CxfNamespaceHandler
extends|extends
name|BaseNamespaceHandler
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
name|CxfNamespaceHandler
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Override
DECL|method|getSchemaLocation (String s)
specifier|public
name|URL
name|getSchemaLocation
parameter_list|(
name|String
name|s
parameter_list|)
block|{
if|if
condition|(
literal|"http://camel.apache.org/schema/blueprint/cxf"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"schema/blueprint/camel-cxf.xsd"
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|findCoreSchemaLocation
argument_list|(
name|s
argument_list|)
return|;
block|}
annotation|@
name|Override
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"rawtypes"
block|}
argument_list|)
DECL|method|getManagedClasses ()
specifier|public
name|Set
argument_list|<
name|Class
argument_list|>
name|getManagedClasses
parameter_list|()
block|{
return|return
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|CxfNamespaceHandler
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|parse (Element element, ParserContext context)
specifier|public
name|Metadata
name|parse
parameter_list|(
name|Element
name|element
parameter_list|,
name|ParserContext
name|context
parameter_list|)
block|{
name|Metadata
name|answer
init|=
literal|null
decl_stmt|;
name|String
name|s
init|=
name|element
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"cxfEndpoint"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"parsing the cxfEndpoint element"
argument_list|)
expr_stmt|;
name|answer
operator|=
operator|new
name|EndpointDefinitionParser
argument_list|()
operator|.
name|parse
argument_list|(
name|element
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|"rsClient"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"parsing the rsClient element"
argument_list|)
expr_stmt|;
name|answer
operator|=
operator|new
name|RsClientDefinitionParser
argument_list|()
operator|.
name|parse
argument_list|(
name|element
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|"rsServer"
operator|.
name|equals
argument_list|(
name|s
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|debug
argument_list|(
literal|"parsing the rsServer element"
argument_list|)
expr_stmt|;
name|answer
operator|=
operator|new
name|RsServerDefinitionParser
argument_list|()
operator|.
name|parse
argument_list|(
name|element
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
annotation|@
name|Override
DECL|method|decorate (Node node, ComponentMetadata componentMetadata, ParserContext parserContext)
specifier|public
name|ComponentMetadata
name|decorate
parameter_list|(
name|Node
name|node
parameter_list|,
name|ComponentMetadata
name|componentMetadata
parameter_list|,
name|ParserContext
name|parserContext
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

