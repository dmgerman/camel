begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.cdi
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|ParameterizedType
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Optional
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|event
operator|.
name|Event
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Any
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Default
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|InjectionException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Instance
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|Typed
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|enterprise
operator|.
name|inject
operator|.
name|spi
operator|.
name|InjectionPoint
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
name|CamelContext
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
name|ConsumerTemplate
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
name|FluentProducerTemplate
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
name|ProducerTemplate
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
name|component
operator|.
name|mock
operator|.
name|MockEndpoint
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|CdiEventEndpoint
operator|.
name|eventEndpointUri
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|CdiSpiHelper
operator|.
name|isAnnotationType
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|cdi
operator|.
name|DefaultLiteral
operator|.
name|DEFAULT
import|;
end_import

begin_class
DECL|class|CdiCamelFactory
specifier|final
class|class
name|CdiCamelFactory
block|{
annotation|@
name|Produces
DECL|method|typeConverter (InjectionPoint ip, @Any Instance<CamelContext> instance, CdiCamelExtension extension)
specifier|private
specifier|static
name|TypeConverter
name|typeConverter
parameter_list|(
name|InjectionPoint
name|ip
parameter_list|,
annotation|@
name|Any
name|Instance
argument_list|<
name|CamelContext
argument_list|>
name|instance
parameter_list|,
name|CdiCamelExtension
name|extension
parameter_list|)
block|{
return|return
name|selectContext
argument_list|(
name|ip
argument_list|,
name|instance
argument_list|,
name|extension
argument_list|)
operator|.
name|getTypeConverter
argument_list|()
return|;
block|}
annotation|@
name|Produces
comment|// Qualifiers are dynamically added in CdiCamelExtension
DECL|method|consumerTemplate (InjectionPoint ip, @Any Instance<CamelContext> instance, CdiCamelExtension extension)
specifier|private
specifier|static
name|ConsumerTemplate
name|consumerTemplate
parameter_list|(
name|InjectionPoint
name|ip
parameter_list|,
annotation|@
name|Any
name|Instance
argument_list|<
name|CamelContext
argument_list|>
name|instance
parameter_list|,
name|CdiCamelExtension
name|extension
parameter_list|)
block|{
return|return
name|selectContext
argument_list|(
name|ip
argument_list|,
name|instance
argument_list|,
name|extension
argument_list|)
operator|.
name|createConsumerTemplate
argument_list|()
return|;
block|}
annotation|@
name|Produces
annotation|@
name|Default
annotation|@
name|Uri
argument_list|(
literal|""
argument_list|)
comment|// Qualifiers are dynamically added in CdiCamelExtension
DECL|method|producerTemplate (InjectionPoint ip, @Any Instance<CamelContext> instance, CdiCamelExtension extension)
specifier|private
specifier|static
name|ProducerTemplate
name|producerTemplate
parameter_list|(
name|InjectionPoint
name|ip
parameter_list|,
annotation|@
name|Any
name|Instance
argument_list|<
name|CamelContext
argument_list|>
name|instance
parameter_list|,
name|CdiCamelExtension
name|extension
parameter_list|)
block|{
return|return
name|getQualifierByType
argument_list|(
name|ip
argument_list|,
name|Uri
operator|.
name|class
argument_list|)
operator|.
name|map
argument_list|(
name|uri
lambda|->
name|producerTemplateFromUri
argument_list|(
name|ip
argument_list|,
name|instance
argument_list|,
name|extension
argument_list|,
name|uri
argument_list|)
argument_list|)
operator|.
name|orElseGet
argument_list|(
parameter_list|()
lambda|->
name|defaultProducerTemplate
argument_list|(
name|ip
argument_list|,
name|instance
argument_list|,
name|extension
argument_list|)
argument_list|)
return|;
block|}
DECL|method|producerTemplateFromUri (InjectionPoint ip, @Any Instance<CamelContext> instance, CdiCamelExtension extension, Uri uri)
specifier|private
specifier|static
name|ProducerTemplate
name|producerTemplateFromUri
parameter_list|(
name|InjectionPoint
name|ip
parameter_list|,
annotation|@
name|Any
name|Instance
argument_list|<
name|CamelContext
argument_list|>
name|instance
parameter_list|,
name|CdiCamelExtension
name|extension
parameter_list|,
name|Uri
name|uri
parameter_list|)
block|{
try|try
block|{
name|CamelContext
name|context
init|=
name|selectContext
argument_list|(
name|ip
argument_list|,
name|instance
argument_list|,
name|extension
argument_list|)
decl_stmt|;
name|ProducerTemplate
name|producerTemplate
init|=
name|context
operator|.
name|createProducerTemplate
argument_list|()
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
operator|.
name|value
argument_list|()
argument_list|,
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|producerTemplate
operator|.
name|setDefaultEndpoint
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|producerTemplate
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|cause
parameter_list|)
block|{
throw|throw
operator|new
name|InjectionException
argument_list|(
literal|"Error injecting producer template annotated with "
operator|+
name|uri
operator|+
literal|" into "
operator|+
name|ip
argument_list|,
name|cause
argument_list|)
throw|;
block|}
block|}
DECL|method|defaultProducerTemplate (InjectionPoint ip, @Any Instance<CamelContext> instance, CdiCamelExtension extension)
specifier|private
specifier|static
name|ProducerTemplate
name|defaultProducerTemplate
parameter_list|(
name|InjectionPoint
name|ip
parameter_list|,
annotation|@
name|Any
name|Instance
argument_list|<
name|CamelContext
argument_list|>
name|instance
parameter_list|,
name|CdiCamelExtension
name|extension
parameter_list|)
block|{
try|try
block|{
name|CamelContext
name|context
init|=
name|selectContext
argument_list|(
name|ip
argument_list|,
name|instance
argument_list|,
name|extension
argument_list|)
decl_stmt|;
return|return
name|context
operator|.
name|createProducerTemplate
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|cause
parameter_list|)
block|{
throw|throw
operator|new
name|InjectionException
argument_list|(
literal|"Error injecting producer template into "
operator|+
name|ip
argument_list|,
name|cause
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Produces
annotation|@
name|Default
annotation|@
name|Uri
argument_list|(
literal|""
argument_list|)
comment|// Qualifiers are dynamically added in CdiCamelExtension
DECL|method|fluentProducerTemplate (InjectionPoint ip, @Any Instance<CamelContext> instance, CdiCamelExtension extension)
specifier|private
specifier|static
name|FluentProducerTemplate
name|fluentProducerTemplate
parameter_list|(
name|InjectionPoint
name|ip
parameter_list|,
annotation|@
name|Any
name|Instance
argument_list|<
name|CamelContext
argument_list|>
name|instance
parameter_list|,
name|CdiCamelExtension
name|extension
parameter_list|)
block|{
return|return
name|getQualifierByType
argument_list|(
name|ip
argument_list|,
name|Uri
operator|.
name|class
argument_list|)
operator|.
name|map
argument_list|(
name|uri
lambda|->
name|fluentProducerTemplateFromUri
argument_list|(
name|ip
argument_list|,
name|instance
argument_list|,
name|extension
argument_list|,
name|uri
argument_list|)
argument_list|)
operator|.
name|orElseGet
argument_list|(
parameter_list|()
lambda|->
name|defaultFluentProducerTemplate
argument_list|(
name|ip
argument_list|,
name|instance
argument_list|,
name|extension
argument_list|)
argument_list|)
return|;
block|}
DECL|method|fluentProducerTemplateFromUri (InjectionPoint ip, @Any Instance<CamelContext> instance, CdiCamelExtension extension, Uri uri)
specifier|private
specifier|static
name|FluentProducerTemplate
name|fluentProducerTemplateFromUri
parameter_list|(
name|InjectionPoint
name|ip
parameter_list|,
annotation|@
name|Any
name|Instance
argument_list|<
name|CamelContext
argument_list|>
name|instance
parameter_list|,
name|CdiCamelExtension
name|extension
parameter_list|,
name|Uri
name|uri
parameter_list|)
block|{
try|try
block|{
name|CamelContext
name|context
init|=
name|selectContext
argument_list|(
name|ip
argument_list|,
name|instance
argument_list|,
name|extension
argument_list|)
decl_stmt|;
name|FluentProducerTemplate
name|producerTemplate
init|=
name|context
operator|.
name|createFluentProducerTemplate
argument_list|()
decl_stmt|;
name|Endpoint
name|endpoint
init|=
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
operator|.
name|value
argument_list|()
argument_list|,
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|producerTemplate
operator|.
name|setDefaultEndpoint
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
return|return
name|producerTemplate
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|cause
parameter_list|)
block|{
throw|throw
operator|new
name|InjectionException
argument_list|(
literal|"Error injecting fluent producer template annotated with "
operator|+
name|uri
operator|+
literal|" into "
operator|+
name|ip
argument_list|,
name|cause
argument_list|)
throw|;
block|}
block|}
DECL|method|defaultFluentProducerTemplate (InjectionPoint ip, @Any Instance<CamelContext> instance, CdiCamelExtension extension)
specifier|private
specifier|static
name|FluentProducerTemplate
name|defaultFluentProducerTemplate
parameter_list|(
name|InjectionPoint
name|ip
parameter_list|,
annotation|@
name|Any
name|Instance
argument_list|<
name|CamelContext
argument_list|>
name|instance
parameter_list|,
name|CdiCamelExtension
name|extension
parameter_list|)
block|{
try|try
block|{
name|CamelContext
name|context
init|=
name|selectContext
argument_list|(
name|ip
argument_list|,
name|instance
argument_list|,
name|extension
argument_list|)
decl_stmt|;
return|return
name|context
operator|.
name|createFluentProducerTemplate
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|cause
parameter_list|)
block|{
throw|throw
operator|new
name|InjectionException
argument_list|(
literal|"Error injecting fluent producer template into "
operator|+
name|ip
argument_list|,
name|cause
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Produces
annotation|@
name|Typed
argument_list|(
name|MockEndpoint
operator|.
name|class
argument_list|)
comment|// Qualifiers are dynamically added in CdiCamelExtension
DECL|method|mockEndpointFromMember (InjectionPoint ip, @Any Instance<CamelContext> instance, CdiCamelExtension extension)
specifier|private
specifier|static
name|MockEndpoint
name|mockEndpointFromMember
parameter_list|(
name|InjectionPoint
name|ip
parameter_list|,
annotation|@
name|Any
name|Instance
argument_list|<
name|CamelContext
argument_list|>
name|instance
parameter_list|,
name|CdiCamelExtension
name|extension
parameter_list|)
block|{
name|String
name|uri
init|=
literal|"mock:"
operator|+
name|ip
operator|.
name|getMember
argument_list|()
operator|.
name|getName
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|selectContext
argument_list|(
name|ip
argument_list|,
name|instance
argument_list|,
name|extension
argument_list|)
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|cause
parameter_list|)
block|{
throw|throw
operator|new
name|InjectionException
argument_list|(
literal|"Error injecting mock endpoint into "
operator|+
name|ip
argument_list|,
name|cause
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Uri
argument_list|(
literal|""
argument_list|)
annotation|@
name|Produces
annotation|@
name|Typed
argument_list|(
name|MockEndpoint
operator|.
name|class
argument_list|)
comment|// Qualifiers are dynamically added in CdiCamelExtension
DECL|method|mockEndpointFromUri (InjectionPoint ip, @Any Instance<CamelContext> instance, CdiCamelExtension extension)
specifier|private
specifier|static
name|MockEndpoint
name|mockEndpointFromUri
parameter_list|(
name|InjectionPoint
name|ip
parameter_list|,
annotation|@
name|Any
name|Instance
argument_list|<
name|CamelContext
argument_list|>
name|instance
parameter_list|,
name|CdiCamelExtension
name|extension
parameter_list|)
block|{
name|Uri
name|uri
init|=
name|getQualifierByType
argument_list|(
name|ip
argument_list|,
name|Uri
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
try|try
block|{
name|CamelContext
name|context
init|=
name|selectContext
argument_list|(
name|ip
argument_list|,
name|instance
argument_list|,
name|extension
argument_list|)
decl_stmt|;
return|return
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
operator|.
name|value
argument_list|()
argument_list|,
name|MockEndpoint
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|cause
parameter_list|)
block|{
throw|throw
operator|new
name|InjectionException
argument_list|(
literal|"Error injecting mock endpoint annotated with "
operator|+
name|uri
operator|+
literal|" into "
operator|+
name|ip
argument_list|,
name|cause
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Uri
argument_list|(
literal|""
argument_list|)
annotation|@
name|Produces
comment|// Qualifiers are dynamically added in CdiCamelExtension
DECL|method|endpoint (InjectionPoint ip, @Any Instance<CamelContext> instance, CdiCamelExtension extension)
specifier|private
specifier|static
name|Endpoint
name|endpoint
parameter_list|(
name|InjectionPoint
name|ip
parameter_list|,
annotation|@
name|Any
name|Instance
argument_list|<
name|CamelContext
argument_list|>
name|instance
parameter_list|,
name|CdiCamelExtension
name|extension
parameter_list|)
block|{
name|Uri
name|uri
init|=
name|getQualifierByType
argument_list|(
name|ip
argument_list|,
name|Uri
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
try|try
block|{
name|CamelContext
name|context
init|=
name|selectContext
argument_list|(
name|ip
argument_list|,
name|instance
argument_list|,
name|extension
argument_list|)
decl_stmt|;
return|return
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
operator|.
name|value
argument_list|()
argument_list|,
name|Endpoint
operator|.
name|class
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|cause
parameter_list|)
block|{
throw|throw
operator|new
name|InjectionException
argument_list|(
literal|"Error injecting endpoint annotated with "
operator|+
name|uri
operator|+
literal|" into "
operator|+
name|ip
argument_list|,
name|cause
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Produces
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|// Qualifiers are dynamically added in CdiCamelExtension
DECL|method|cdiEventEndpoint (InjectionPoint ip, @Any Instance<CamelContext> instance, CdiCamelExtension extension, @Any Event<Object> event)
specifier|private
specifier|static
parameter_list|<
name|T
parameter_list|>
name|CdiEventEndpoint
argument_list|<
name|T
argument_list|>
name|cdiEventEndpoint
parameter_list|(
name|InjectionPoint
name|ip
parameter_list|,
annotation|@
name|Any
name|Instance
argument_list|<
name|CamelContext
argument_list|>
name|instance
parameter_list|,
name|CdiCamelExtension
name|extension
parameter_list|,
annotation|@
name|Any
name|Event
argument_list|<
name|Object
argument_list|>
name|event
parameter_list|)
throws|throws
name|Exception
block|{
name|CamelContext
name|context
init|=
name|selectContext
argument_list|(
name|ip
argument_list|,
name|instance
argument_list|,
name|extension
argument_list|)
decl_stmt|;
name|Type
name|type
init|=
name|Object
operator|.
name|class
decl_stmt|;
if|if
condition|(
name|ip
operator|.
name|getType
argument_list|()
operator|instanceof
name|ParameterizedType
condition|)
block|{
name|type
operator|=
operator|(
operator|(
name|ParameterizedType
operator|)
name|ip
operator|.
name|getType
argument_list|()
operator|)
operator|.
name|getActualTypeArguments
argument_list|()
index|[
literal|0
index|]
expr_stmt|;
block|}
name|String
name|uri
init|=
name|eventEndpointUri
argument_list|(
name|type
argument_list|,
name|ip
operator|.
name|getQualifiers
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|.
name|hasEndpoint
argument_list|(
name|uri
argument_list|)
operator|==
literal|null
condition|)
block|{
name|context
operator|.
name|addEndpoint
argument_list|(
name|uri
argument_list|,
name|extension
operator|.
name|getEventEndpoint
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|context
operator|.
name|getEndpoint
argument_list|(
name|uri
argument_list|,
name|CdiEventEndpoint
operator|.
name|class
argument_list|)
return|;
block|}
DECL|method|selectContext (InjectionPoint ip, Instance<T> instance, CdiCamelExtension extension)
specifier|private
specifier|static
parameter_list|<
name|T
extends|extends
name|CamelContext
parameter_list|>
name|T
name|selectContext
parameter_list|(
name|InjectionPoint
name|ip
parameter_list|,
name|Instance
argument_list|<
name|T
argument_list|>
name|instance
parameter_list|,
name|CdiCamelExtension
name|extension
parameter_list|)
block|{
name|Collection
argument_list|<
name|Annotation
argument_list|>
name|qualifiers
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|ip
operator|.
name|getQualifiers
argument_list|()
argument_list|)
decl_stmt|;
name|qualifiers
operator|.
name|retainAll
argument_list|(
name|extension
operator|.
name|getContextQualifiers
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|qualifiers
operator|.
name|isEmpty
argument_list|()
operator|&&
operator|!
name|instance
operator|.
name|select
argument_list|(
name|DEFAULT
argument_list|)
operator|.
name|isUnsatisfied
argument_list|()
condition|)
block|{
return|return
name|instance
operator|.
name|select
argument_list|(
name|DEFAULT
argument_list|)
operator|.
name|get
argument_list|()
return|;
block|}
return|return
name|instance
operator|.
name|select
argument_list|(
name|qualifiers
operator|.
name|toArray
argument_list|(
operator|new
name|Annotation
index|[
literal|0
index|]
argument_list|)
argument_list|)
operator|.
name|get
argument_list|()
return|;
block|}
DECL|method|getQualifierByType (InjectionPoint ip, Class<T> type)
specifier|private
specifier|static
parameter_list|<
name|T
extends|extends
name|Annotation
parameter_list|>
name|Optional
argument_list|<
name|T
argument_list|>
name|getQualifierByType
parameter_list|(
name|InjectionPoint
name|ip
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|ip
operator|.
name|getQualifiers
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|isAnnotationType
argument_list|(
name|type
argument_list|)
argument_list|)
operator|.
name|findAny
argument_list|()
operator|.
name|map
argument_list|(
name|type
operator|::
name|cast
argument_list|)
return|;
block|}
block|}
end_class

end_unit

