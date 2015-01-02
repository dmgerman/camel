begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.model.rest
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|model
operator|.
name|rest
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|annotation
operator|.
name|XmlAccessType
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
name|annotation
operator|.
name|XmlAccessorType
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
name|annotation
operator|.
name|XmlAttribute
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
name|annotation
operator|.
name|XmlElementRef
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
name|annotation
operator|.
name|XmlRootElement
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
name|model
operator|.
name|OptionalIdentifiedDefinition
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
name|RouteDefinition
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
name|ToDefinition
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
name|Label
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
name|camel
operator|.
name|util
operator|.
name|URISupport
import|;
end_import

begin_comment
comment|/**  * Represents an XML&lt;rest/&gt; element  */
end_comment

begin_class
annotation|@
name|Label
argument_list|(
literal|"rest"
argument_list|)
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"rest"
argument_list|)
annotation|@
name|XmlAccessorType
argument_list|(
name|XmlAccessType
operator|.
name|FIELD
argument_list|)
DECL|class|RestDefinition
specifier|public
class|class
name|RestDefinition
extends|extends
name|OptionalIdentifiedDefinition
argument_list|<
name|RestDefinition
argument_list|>
block|{
annotation|@
name|XmlAttribute
DECL|field|path
specifier|private
name|String
name|path
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|consumes
specifier|private
name|String
name|consumes
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|produces
specifier|private
name|String
name|produces
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|bindingMode
specifier|private
name|RestBindingMode
name|bindingMode
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|skipBindingOnErrorCode
specifier|private
name|Boolean
name|skipBindingOnErrorCode
decl_stmt|;
annotation|@
name|XmlAttribute
DECL|field|enableCORS
specifier|private
name|Boolean
name|enableCORS
decl_stmt|;
annotation|@
name|XmlElementRef
DECL|field|verbs
specifier|private
name|List
argument_list|<
name|VerbDefinition
argument_list|>
name|verbs
init|=
operator|new
name|ArrayList
argument_list|<
name|VerbDefinition
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Override
DECL|method|getLabel ()
specifier|public
name|String
name|getLabel
parameter_list|()
block|{
return|return
literal|"rest"
return|;
block|}
DECL|method|getPath ()
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
DECL|method|setPath (String path)
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
DECL|method|getConsumes ()
specifier|public
name|String
name|getConsumes
parameter_list|()
block|{
return|return
name|consumes
return|;
block|}
DECL|method|setConsumes (String consumes)
specifier|public
name|void
name|setConsumes
parameter_list|(
name|String
name|consumes
parameter_list|)
block|{
name|this
operator|.
name|consumes
operator|=
name|consumes
expr_stmt|;
block|}
DECL|method|getProduces ()
specifier|public
name|String
name|getProduces
parameter_list|()
block|{
return|return
name|produces
return|;
block|}
DECL|method|setProduces (String produces)
specifier|public
name|void
name|setProduces
parameter_list|(
name|String
name|produces
parameter_list|)
block|{
name|this
operator|.
name|produces
operator|=
name|produces
expr_stmt|;
block|}
DECL|method|getBindingMode ()
specifier|public
name|RestBindingMode
name|getBindingMode
parameter_list|()
block|{
return|return
name|bindingMode
return|;
block|}
DECL|method|setBindingMode (RestBindingMode bindingMode)
specifier|public
name|void
name|setBindingMode
parameter_list|(
name|RestBindingMode
name|bindingMode
parameter_list|)
block|{
name|this
operator|.
name|bindingMode
operator|=
name|bindingMode
expr_stmt|;
block|}
DECL|method|getVerbs ()
specifier|public
name|List
argument_list|<
name|VerbDefinition
argument_list|>
name|getVerbs
parameter_list|()
block|{
return|return
name|verbs
return|;
block|}
DECL|method|setVerbs (List<VerbDefinition> verbs)
specifier|public
name|void
name|setVerbs
parameter_list|(
name|List
argument_list|<
name|VerbDefinition
argument_list|>
name|verbs
parameter_list|)
block|{
name|this
operator|.
name|verbs
operator|=
name|verbs
expr_stmt|;
block|}
DECL|method|getSkipBindingOnErrorCode ()
specifier|public
name|Boolean
name|getSkipBindingOnErrorCode
parameter_list|()
block|{
return|return
name|skipBindingOnErrorCode
return|;
block|}
DECL|method|setSkipBindingOnErrorCode (Boolean skipBindingOnErrorCode)
specifier|public
name|void
name|setSkipBindingOnErrorCode
parameter_list|(
name|Boolean
name|skipBindingOnErrorCode
parameter_list|)
block|{
name|this
operator|.
name|skipBindingOnErrorCode
operator|=
name|skipBindingOnErrorCode
expr_stmt|;
block|}
DECL|method|getEnableCORS ()
specifier|public
name|Boolean
name|getEnableCORS
parameter_list|()
block|{
return|return
name|enableCORS
return|;
block|}
DECL|method|setEnableCORS (Boolean enableCORS)
specifier|public
name|void
name|setEnableCORS
parameter_list|(
name|Boolean
name|enableCORS
parameter_list|)
block|{
name|this
operator|.
name|enableCORS
operator|=
name|enableCORS
expr_stmt|;
block|}
comment|// Fluent API
comment|//-------------------------------------------------------------------------
comment|/**      * To set the base path of this REST service      */
DECL|method|path (String path)
specifier|public
name|RestDefinition
name|path
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|setPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|get ()
specifier|public
name|RestDefinition
name|get
parameter_list|()
block|{
return|return
name|addVerb
argument_list|(
literal|"get"
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|get (String uri)
specifier|public
name|RestDefinition
name|get
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|addVerb
argument_list|(
literal|"get"
argument_list|,
name|uri
argument_list|)
return|;
block|}
DECL|method|post ()
specifier|public
name|RestDefinition
name|post
parameter_list|()
block|{
return|return
name|addVerb
argument_list|(
literal|"post"
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|post (String uri)
specifier|public
name|RestDefinition
name|post
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|addVerb
argument_list|(
literal|"post"
argument_list|,
name|uri
argument_list|)
return|;
block|}
DECL|method|put ()
specifier|public
name|RestDefinition
name|put
parameter_list|()
block|{
return|return
name|addVerb
argument_list|(
literal|"put"
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|put (String uri)
specifier|public
name|RestDefinition
name|put
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|addVerb
argument_list|(
literal|"put"
argument_list|,
name|uri
argument_list|)
return|;
block|}
DECL|method|delete ()
specifier|public
name|RestDefinition
name|delete
parameter_list|()
block|{
return|return
name|addVerb
argument_list|(
literal|"delete"
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|delete (String uri)
specifier|public
name|RestDefinition
name|delete
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|addVerb
argument_list|(
literal|"delete"
argument_list|,
name|uri
argument_list|)
return|;
block|}
DECL|method|head ()
specifier|public
name|RestDefinition
name|head
parameter_list|()
block|{
return|return
name|addVerb
argument_list|(
literal|"head"
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|head (String uri)
specifier|public
name|RestDefinition
name|head
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
return|return
name|addVerb
argument_list|(
literal|"head"
argument_list|,
name|uri
argument_list|)
return|;
block|}
DECL|method|verb (String verb)
specifier|public
name|RestDefinition
name|verb
parameter_list|(
name|String
name|verb
parameter_list|)
block|{
return|return
name|addVerb
argument_list|(
name|verb
argument_list|,
literal|null
argument_list|)
return|;
block|}
DECL|method|verb (String verb, String uri)
specifier|public
name|RestDefinition
name|verb
parameter_list|(
name|String
name|verb
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
return|return
name|addVerb
argument_list|(
name|verb
argument_list|,
name|uri
argument_list|)
return|;
block|}
annotation|@
name|Override
DECL|method|id (String id)
specifier|public
name|RestDefinition
name|id
parameter_list|(
name|String
name|id
parameter_list|)
block|{
if|if
condition|(
name|getVerbs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|super
operator|.
name|id
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// add on last verb as that is how the Java DSL works
name|VerbDefinition
name|verb
init|=
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|verb
operator|.
name|id
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|description (String text)
specifier|public
name|RestDefinition
name|description
parameter_list|(
name|String
name|text
parameter_list|)
block|{
if|if
condition|(
name|getVerbs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|super
operator|.
name|description
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// add on last verb as that is how the Java DSL works
name|VerbDefinition
name|verb
init|=
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|verb
operator|.
name|description
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
annotation|@
name|Override
DECL|method|description (String id, String text, String lang)
specifier|public
name|RestDefinition
name|description
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|text
parameter_list|,
name|String
name|lang
parameter_list|)
block|{
if|if
condition|(
name|getVerbs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|super
operator|.
name|description
argument_list|(
name|id
argument_list|,
name|text
argument_list|,
name|lang
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// add on last verb as that is how the Java DSL works
name|VerbDefinition
name|verb
init|=
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|verb
operator|.
name|description
argument_list|(
name|id
argument_list|,
name|text
argument_list|,
name|lang
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|consumes (String mediaType)
specifier|public
name|RestDefinition
name|consumes
parameter_list|(
name|String
name|mediaType
parameter_list|)
block|{
if|if
condition|(
name|getVerbs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|consumes
operator|=
name|mediaType
expr_stmt|;
block|}
else|else
block|{
comment|// add on last verb as that is how the Java DSL works
name|VerbDefinition
name|verb
init|=
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|verb
operator|.
name|setConsumes
argument_list|(
name|mediaType
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|produces (String mediaType)
specifier|public
name|RestDefinition
name|produces
parameter_list|(
name|String
name|mediaType
parameter_list|)
block|{
if|if
condition|(
name|getVerbs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|produces
operator|=
name|mediaType
expr_stmt|;
block|}
else|else
block|{
comment|// add on last verb as that is how the Java DSL works
name|VerbDefinition
name|verb
init|=
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|verb
operator|.
name|setProduces
argument_list|(
name|mediaType
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|type (Class<?> classType)
specifier|public
name|RestDefinition
name|type
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|classType
parameter_list|)
block|{
comment|// add to last verb
if|if
condition|(
name|getVerbs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Must add verb first, such as get/post/delete"
argument_list|)
throw|;
block|}
name|VerbDefinition
name|verb
init|=
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|verb
operator|.
name|setType
argument_list|(
name|classType
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|typeList (Class<?> classType)
specifier|public
name|RestDefinition
name|typeList
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|classType
parameter_list|)
block|{
comment|// add to last verb
if|if
condition|(
name|getVerbs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Must add verb first, such as get/post/delete"
argument_list|)
throw|;
block|}
name|VerbDefinition
name|verb
init|=
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
comment|// list should end with [] to indicate array
name|verb
operator|.
name|setType
argument_list|(
name|classType
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|"[]"
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|outType (Class<?> classType)
specifier|public
name|RestDefinition
name|outType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|classType
parameter_list|)
block|{
comment|// add to last verb
if|if
condition|(
name|getVerbs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Must add verb first, such as get/post/delete"
argument_list|)
throw|;
block|}
name|VerbDefinition
name|verb
init|=
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|verb
operator|.
name|setOutType
argument_list|(
name|classType
operator|.
name|getCanonicalName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|outTypeList (Class<?> classType)
specifier|public
name|RestDefinition
name|outTypeList
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|classType
parameter_list|)
block|{
comment|// add to last verb
if|if
condition|(
name|getVerbs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Must add verb first, such as get/post/delete"
argument_list|)
throw|;
block|}
name|VerbDefinition
name|verb
init|=
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
comment|// list should end with [] to indicate array
name|verb
operator|.
name|setOutType
argument_list|(
name|classType
operator|.
name|getCanonicalName
argument_list|()
operator|+
literal|"[]"
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|bindingMode (RestBindingMode mode)
specifier|public
name|RestDefinition
name|bindingMode
parameter_list|(
name|RestBindingMode
name|mode
parameter_list|)
block|{
if|if
condition|(
name|getVerbs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|bindingMode
operator|=
name|mode
expr_stmt|;
block|}
else|else
block|{
comment|// add on last verb as that is how the Java DSL works
name|VerbDefinition
name|verb
init|=
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|verb
operator|.
name|setBindingMode
argument_list|(
name|mode
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|skipBindingOnErrorCode (boolean skipBindingOnErrorCode)
specifier|public
name|RestDefinition
name|skipBindingOnErrorCode
parameter_list|(
name|boolean
name|skipBindingOnErrorCode
parameter_list|)
block|{
if|if
condition|(
name|getVerbs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|skipBindingOnErrorCode
operator|=
name|skipBindingOnErrorCode
expr_stmt|;
block|}
else|else
block|{
comment|// add on last verb as that is how the Java DSL works
name|VerbDefinition
name|verb
init|=
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|verb
operator|.
name|setSkipBindingOnErrorCode
argument_list|(
name|skipBindingOnErrorCode
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
DECL|method|enableCORS (boolean enableCORS)
specifier|public
name|RestDefinition
name|enableCORS
parameter_list|(
name|boolean
name|enableCORS
parameter_list|)
block|{
if|if
condition|(
name|getVerbs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|enableCORS
operator|=
name|enableCORS
expr_stmt|;
block|}
else|else
block|{
comment|// add on last verb as that is how the Java DSL works
name|VerbDefinition
name|verb
init|=
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|verb
operator|.
name|setEnableCORS
argument_list|(
name|enableCORS
argument_list|)
expr_stmt|;
block|}
return|return
name|this
return|;
block|}
comment|/**      * Routes directly to the given endpoint.      *<p/>      * If you need additional routing capabilities, then use {@link #route()} instead.      *      * @param uri the uri of the endpoint      * @return this builder      */
DECL|method|to (String uri)
specifier|public
name|RestDefinition
name|to
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
comment|// add to last verb
if|if
condition|(
name|getVerbs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Must add verb first, such as get/post/delete"
argument_list|)
throw|;
block|}
name|ToDefinition
name|to
init|=
operator|new
name|ToDefinition
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|VerbDefinition
name|verb
init|=
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|verb
operator|.
name|setTo
argument_list|(
name|to
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
DECL|method|route ()
specifier|public
name|RouteDefinition
name|route
parameter_list|()
block|{
comment|// add to last verb
if|if
condition|(
name|getVerbs
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Must add verb first, such as get/post/delete"
argument_list|)
throw|;
block|}
comment|// link them together so we can navigate using Java DSL
name|RouteDefinition
name|route
init|=
operator|new
name|RouteDefinition
argument_list|()
decl_stmt|;
name|route
operator|.
name|setRestDefinition
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|VerbDefinition
name|verb
init|=
name|getVerbs
argument_list|()
operator|.
name|get
argument_list|(
name|getVerbs
argument_list|()
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
name|verb
operator|.
name|setRoute
argument_list|(
name|route
argument_list|)
expr_stmt|;
return|return
name|route
return|;
block|}
comment|// Implementation
comment|//-------------------------------------------------------------------------
DECL|method|addVerb (String verb, String uri)
specifier|private
name|RestDefinition
name|addVerb
parameter_list|(
name|String
name|verb
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|VerbDefinition
name|answer
decl_stmt|;
if|if
condition|(
literal|"get"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
name|answer
operator|=
operator|new
name|GetVerbDefinition
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"post"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
name|answer
operator|=
operator|new
name|PostVerbDefinition
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"delete"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
name|answer
operator|=
operator|new
name|DeleteVerbDefinition
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"head"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
name|answer
operator|=
operator|new
name|HeadVerbDefinition
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
literal|"put"
operator|.
name|equals
argument_list|(
name|verb
argument_list|)
condition|)
block|{
name|answer
operator|=
operator|new
name|PutVerbDefinition
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|VerbDefinition
argument_list|()
expr_stmt|;
name|answer
operator|.
name|setMethod
argument_list|(
name|verb
argument_list|)
expr_stmt|;
block|}
name|answer
operator|.
name|setRest
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|answer
operator|.
name|setUri
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|getVerbs
argument_list|()
operator|.
name|add
argument_list|(
name|answer
argument_list|)
expr_stmt|;
return|return
name|this
return|;
block|}
comment|/**      * Transforms this REST definition into a list of {@link org.apache.camel.model.RouteDefinition} which      * Camel routing engine can add and run. This allows us to define REST services using this      * REST DSL and turn those into regular Camel routes.      */
DECL|method|asRouteDefinition (CamelContext camelContext)
specifier|public
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|asRouteDefinition
parameter_list|(
name|CamelContext
name|camelContext
parameter_list|)
block|{
name|List
argument_list|<
name|RouteDefinition
argument_list|>
name|answer
init|=
operator|new
name|ArrayList
argument_list|<
name|RouteDefinition
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|VerbDefinition
name|verb
range|:
name|getVerbs
argument_list|()
control|)
block|{
comment|// either the verb has a singular to or a embedded route
name|RouteDefinition
name|route
init|=
name|verb
operator|.
name|getRoute
argument_list|()
decl_stmt|;
if|if
condition|(
name|route
operator|==
literal|null
condition|)
block|{
comment|// it was a singular to, so add a new route and add the singular
comment|// to as output to this route
name|route
operator|=
operator|new
name|RouteDefinition
argument_list|()
expr_stmt|;
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|add
argument_list|(
name|verb
operator|.
name|getTo
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// add the binding
name|RestBindingDefinition
name|binding
init|=
operator|new
name|RestBindingDefinition
argument_list|()
decl_stmt|;
name|binding
operator|.
name|setType
argument_list|(
name|verb
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|binding
operator|.
name|setOutType
argument_list|(
name|verb
operator|.
name|getOutType
argument_list|()
argument_list|)
expr_stmt|;
comment|// verb takes precedence over configuration on rest
if|if
condition|(
name|verb
operator|.
name|getConsumes
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|binding
operator|.
name|setConsumes
argument_list|(
name|verb
operator|.
name|getConsumes
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|binding
operator|.
name|setConsumes
argument_list|(
name|getConsumes
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|verb
operator|.
name|getProduces
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|binding
operator|.
name|setProduces
argument_list|(
name|verb
operator|.
name|getProduces
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|binding
operator|.
name|setProduces
argument_list|(
name|getProduces
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|verb
operator|.
name|getBindingMode
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|binding
operator|.
name|setBindingMode
argument_list|(
name|verb
operator|.
name|getBindingMode
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|binding
operator|.
name|setBindingMode
argument_list|(
name|getBindingMode
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|verb
operator|.
name|getSkipBindingOnErrorCode
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|binding
operator|.
name|setSkipBindingOnErrorCode
argument_list|(
name|verb
operator|.
name|getSkipBindingOnErrorCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|binding
operator|.
name|setSkipBindingOnErrorCode
argument_list|(
name|getSkipBindingOnErrorCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|verb
operator|.
name|getEnableCORS
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|binding
operator|.
name|setEnableCORS
argument_list|(
name|verb
operator|.
name|getEnableCORS
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|binding
operator|.
name|setEnableCORS
argument_list|(
name|getEnableCORS
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|route
operator|.
name|getOutputs
argument_list|()
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|binding
argument_list|)
expr_stmt|;
comment|// create the from endpoint uri which is using the rest component
name|String
name|from
init|=
literal|"rest:"
operator|+
name|verb
operator|.
name|asVerb
argument_list|()
operator|+
literal|":"
operator|+
name|buildUri
argument_list|(
name|verb
argument_list|)
decl_stmt|;
comment|// append options
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// verb takes precedence over configuration on rest
if|if
condition|(
name|verb
operator|.
name|getConsumes
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|options
operator|.
name|put
argument_list|(
literal|"consumes"
argument_list|,
name|verb
operator|.
name|getConsumes
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getConsumes
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|options
operator|.
name|put
argument_list|(
literal|"consumes"
argument_list|,
name|getConsumes
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|verb
operator|.
name|getProduces
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|options
operator|.
name|put
argument_list|(
literal|"produces"
argument_list|,
name|verb
operator|.
name|getProduces
argument_list|()
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|getProduces
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|options
operator|.
name|put
argument_list|(
literal|"produces"
argument_list|,
name|getProduces
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// append optional type binding information
name|String
name|inType
init|=
name|binding
operator|.
name|getType
argument_list|()
decl_stmt|;
if|if
condition|(
name|inType
operator|!=
literal|null
condition|)
block|{
name|options
operator|.
name|put
argument_list|(
literal|"inType"
argument_list|,
name|inType
argument_list|)
expr_stmt|;
block|}
name|String
name|outType
init|=
name|binding
operator|.
name|getOutType
argument_list|()
decl_stmt|;
if|if
condition|(
name|outType
operator|!=
literal|null
condition|)
block|{
name|options
operator|.
name|put
argument_list|(
literal|"outType"
argument_list|,
name|outType
argument_list|)
expr_stmt|;
block|}
comment|// if no route id has been set, then use the verb id as route id
if|if
condition|(
operator|!
name|route
operator|.
name|hasCustomIdAssigned
argument_list|()
condition|)
block|{
comment|// use id of verb as route id
name|String
name|id
init|=
name|verb
operator|.
name|getId
argument_list|()
decl_stmt|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|)
block|{
name|route
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|routeId
init|=
name|route
operator|.
name|idOrCreate
argument_list|(
name|camelContext
operator|.
name|getNodeIdFactory
argument_list|()
argument_list|)
decl_stmt|;
name|options
operator|.
name|put
argument_list|(
literal|"routeId"
argument_list|,
name|routeId
argument_list|)
expr_stmt|;
comment|// include optional description, which we favor from 1) to/route description 2) verb description 3) rest description
comment|// this allows end users to define general descriptions and override then per to/route or verb
name|String
name|description
init|=
name|verb
operator|.
name|getTo
argument_list|()
operator|!=
literal|null
condition|?
name|verb
operator|.
name|getTo
argument_list|()
operator|.
name|getDescriptionText
argument_list|()
else|:
name|route
operator|.
name|getDescriptionText
argument_list|()
decl_stmt|;
if|if
condition|(
name|description
operator|==
literal|null
condition|)
block|{
name|description
operator|=
name|verb
operator|.
name|getDescriptionText
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|description
operator|==
literal|null
condition|)
block|{
name|description
operator|=
name|getDescriptionText
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|description
operator|!=
literal|null
condition|)
block|{
name|options
operator|.
name|put
argument_list|(
literal|"description"
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|options
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|String
name|query
decl_stmt|;
try|try
block|{
name|query
operator|=
name|URISupport
operator|.
name|createQueryString
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
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
name|from
operator|=
name|from
operator|+
literal|"?"
operator|+
name|query
expr_stmt|;
block|}
comment|// the route should be from this rest endpoint
name|route
operator|.
name|fromRest
argument_list|(
name|from
argument_list|)
expr_stmt|;
name|route
operator|.
name|setRestDefinition
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|answer
operator|.
name|add
argument_list|(
name|route
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
DECL|method|buildUri (VerbDefinition verb)
specifier|private
name|String
name|buildUri
parameter_list|(
name|VerbDefinition
name|verb
parameter_list|)
block|{
if|if
condition|(
name|path
operator|!=
literal|null
operator|&&
name|verb
operator|.
name|getUri
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|path
operator|+
literal|":"
operator|+
name|verb
operator|.
name|getUri
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|path
operator|!=
literal|null
condition|)
block|{
return|return
name|path
return|;
block|}
elseif|else
if|if
condition|(
name|verb
operator|.
name|getUri
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
name|verb
operator|.
name|getUri
argument_list|()
return|;
block|}
else|else
block|{
return|return
literal|""
return|;
block|}
block|}
block|}
end_class

end_unit

