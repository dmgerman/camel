begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.xslt
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|xslt
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|URIResolver
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
name|builder
operator|.
name|xml
operator|.
name|XsltBuilder
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
name|builder
operator|.
name|xml
operator|.
name|XsltUriResolver
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
name|ResourceBasedComponent
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
name|converter
operator|.
name|jaxp
operator|.
name|XmlConverter
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
name|ProcessorEndpoint
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|Resource
import|;
end_import

begin_comment
comment|/**  * An<a href="http://camel.apache.org/xslt.html">XSLT Component</a>  * for performing XSLT transforms of messages  *  * @version $Revision$  */
end_comment

begin_class
DECL|class|XsltComponent
specifier|public
class|class
name|XsltComponent
extends|extends
name|ResourceBasedComponent
block|{
DECL|field|xmlConverter
specifier|private
name|XmlConverter
name|xmlConverter
decl_stmt|;
DECL|method|getXmlConverter ()
specifier|public
name|XmlConverter
name|getXmlConverter
parameter_list|()
block|{
return|return
name|xmlConverter
return|;
block|}
DECL|method|setXmlConverter (XmlConverter xmlConverter)
specifier|public
name|void
name|setXmlConverter
parameter_list|(
name|XmlConverter
name|xmlConverter
parameter_list|)
block|{
name|this
operator|.
name|xmlConverter
operator|=
name|xmlConverter
expr_stmt|;
block|}
DECL|method|createEndpoint (String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|Endpoint
name|createEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|Resource
name|resource
init|=
name|resolveMandatoryResource
argument_list|(
name|remaining
argument_list|)
decl_stmt|;
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
name|this
operator|+
literal|" using schema resource: "
operator|+
name|resource
argument_list|)
expr_stmt|;
block|}
name|XsltBuilder
name|xslt
init|=
name|getCamelContext
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|XsltBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// lets allow the converter to be configured
name|XmlConverter
name|converter
init|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"converter"
argument_list|,
name|XmlConverter
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|converter
operator|==
literal|null
condition|)
block|{
name|converter
operator|=
name|getXmlConverter
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|converter
operator|!=
literal|null
condition|)
block|{
name|xslt
operator|.
name|setConverter
argument_list|(
name|converter
argument_list|)
expr_stmt|;
block|}
name|String
name|transformerFactoryClassName
init|=
name|getAndRemoveParameter
argument_list|(
name|parameters
argument_list|,
literal|"transformerFactoryClass"
argument_list|,
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|TransformerFactory
name|factory
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|transformerFactoryClassName
operator|!=
literal|null
condition|)
block|{
comment|// provide the class loader of this component to work in OSGi environments
name|Class
argument_list|<
name|?
argument_list|>
name|factoryClass
init|=
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
operator|.
name|resolveClass
argument_list|(
name|transformerFactoryClassName
argument_list|,
name|XsltComponent
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|factoryClass
operator|!=
literal|null
condition|)
block|{
name|factory
operator|=
operator|(
name|TransformerFactory
operator|)
name|getCamelContext
argument_list|()
operator|.
name|getInjector
argument_list|()
operator|.
name|newInstance
argument_list|(
name|factoryClass
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Cannot find the TransformerFactoryClass with the class name: "
operator|+
name|transformerFactoryClassName
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|parameters
operator|.
name|get
argument_list|(
literal|"transformerFactory"
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|factory
operator|=
name|resolveAndRemoveReferenceParameter
argument_list|(
name|parameters
argument_list|,
literal|"transformerFactory"
argument_list|,
name|TransformerFactory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|factory
operator|!=
literal|null
condition|)
block|{
name|xslt
operator|.
name|getConverter
argument_list|()
operator|.
name|setTransformerFactory
argument_list|(
name|factory
argument_list|)
expr_stmt|;
block|}
comment|// set resolver before input stream as resolver is used when loading the input stream
name|URIResolver
name|resolver
init|=
operator|new
name|XsltUriResolver
argument_list|(
name|getCamelContext
argument_list|()
operator|.
name|getClassResolver
argument_list|()
argument_list|,
name|remaining
argument_list|)
decl_stmt|;
name|xslt
operator|.
name|setUriResolver
argument_list|(
name|resolver
argument_list|)
expr_stmt|;
name|xslt
operator|.
name|setTransformerInputStream
argument_list|(
name|resource
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|configureXslt
argument_list|(
name|xslt
argument_list|,
name|uri
argument_list|,
name|remaining
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
return|return
operator|new
name|ProcessorEndpoint
argument_list|(
name|uri
argument_list|,
name|this
argument_list|,
name|xslt
argument_list|)
return|;
block|}
DECL|method|configureXslt (XsltBuilder xslt, String uri, String remaining, Map<String, Object> parameters)
specifier|protected
name|void
name|configureXslt
parameter_list|(
name|XsltBuilder
name|xslt
parameter_list|,
name|String
name|uri
parameter_list|,
name|String
name|remaining
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|parameters
parameter_list|)
throws|throws
name|Exception
block|{
name|setProperties
argument_list|(
name|xslt
argument_list|,
name|parameters
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

