begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one or more  * contributor license agreements.  See the NOTICE file distributed with  * this work for additional information regarding copyright ownership.  * The ASF licenses this file to You under the Apache License, Version 2.0  * (the "License"); you may not use this file except in compliance with  * the License.  You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
DECL|package|org.apache.camel.component.facebook
package|package
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|facebook
package|;
end_package

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
name|Collections
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
name|apache
operator|.
name|camel
operator|.
name|Consumer
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
name|NoTypeConversionAvailableException
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
name|Producer
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
name|facebook
operator|.
name|config
operator|.
name|FacebookEndpointConfiguration
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
name|facebook
operator|.
name|config
operator|.
name|FacebookNameStyle
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
name|facebook
operator|.
name|data
operator|.
name|FacebookMethodsType
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
name|facebook
operator|.
name|data
operator|.
name|FacebookPropertiesHelper
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
name|support
operator|.
name|DefaultEndpoint
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
name|Metadata
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
name|UriEndpoint
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
name|UriParam
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
name|UriPath
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
name|support
operator|.
name|EndpointHelper
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
import|import static
name|org
operator|.
name|apache
operator|.
name|camel
operator|.
name|component
operator|.
name|facebook
operator|.
name|data
operator|.
name|FacebookMethodsTypeHelper
operator|.
name|convertToGetMethod
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
name|component
operator|.
name|facebook
operator|.
name|data
operator|.
name|FacebookMethodsTypeHelper
operator|.
name|convertToSearchMethod
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
name|component
operator|.
name|facebook
operator|.
name|data
operator|.
name|FacebookMethodsTypeHelper
operator|.
name|getCandidateMethods
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
name|component
operator|.
name|facebook
operator|.
name|data
operator|.
name|FacebookMethodsTypeHelper
operator|.
name|getMissingProperties
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
name|component
operator|.
name|facebook
operator|.
name|data
operator|.
name|FacebookPropertiesHelper
operator|.
name|getEndpointPropertyNames
import|;
end_import

begin_comment
comment|/**  * The Facebook component provides access to all of the Facebook APIs accessible using Facebook4J.  *  * It allows producing messages to retrieve, add, and delete posts, likes, comments, photos, albums, videos, photos,  * checkins, locations, links, etc. It also supports APIs that allow polling for posts, users, checkins, groups, locations, etc.  */
end_comment

begin_class
annotation|@
name|UriEndpoint
argument_list|(
name|firstVersion
operator|=
literal|"2.14.0"
argument_list|,
name|scheme
operator|=
literal|"facebook"
argument_list|,
name|title
operator|=
literal|"Facebook"
argument_list|,
name|syntax
operator|=
literal|"facebook:methodName"
argument_list|,
name|consumerClass
operator|=
name|FacebookConsumer
operator|.
name|class
argument_list|,
name|label
operator|=
literal|"social"
argument_list|)
DECL|class|FacebookEndpoint
specifier|public
class|class
name|FacebookEndpoint
extends|extends
name|DefaultEndpoint
implements|implements
name|FacebookConstants
block|{
DECL|field|nameStyle
specifier|private
name|FacebookNameStyle
name|nameStyle
decl_stmt|;
annotation|@
name|UriPath
argument_list|(
name|name
operator|=
literal|"methodName"
argument_list|,
name|description
operator|=
literal|"What operation to perform"
argument_list|)
annotation|@
name|Metadata
argument_list|(
name|required
operator|=
literal|true
argument_list|)
DECL|field|method
specifier|private
name|String
name|method
decl_stmt|;
DECL|field|methodName
specifier|private
name|FacebookMethodsType
name|methodName
decl_stmt|;
annotation|@
name|UriParam
DECL|field|configuration
specifier|private
name|FacebookEndpointConfiguration
name|configuration
decl_stmt|;
annotation|@
name|UriParam
DECL|field|inBody
specifier|private
name|String
name|inBody
decl_stmt|;
comment|// candidate methods based on method name and endpoint configuration
DECL|field|candidates
specifier|private
name|List
argument_list|<
name|FacebookMethodsType
argument_list|>
name|candidates
decl_stmt|;
DECL|method|FacebookEndpoint (String uri, FacebookComponent facebookComponent, String remaining, FacebookEndpointConfiguration configuration)
specifier|public
name|FacebookEndpoint
parameter_list|(
name|String
name|uri
parameter_list|,
name|FacebookComponent
name|facebookComponent
parameter_list|,
name|String
name|remaining
parameter_list|,
name|FacebookEndpointConfiguration
name|configuration
parameter_list|)
throws|throws
name|NoTypeConversionAvailableException
block|{
name|super
argument_list|(
name|uri
argument_list|,
name|facebookComponent
argument_list|)
expr_stmt|;
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|method
operator|=
name|remaining
expr_stmt|;
block|}
DECL|method|createProducer ()
specifier|public
name|Producer
name|createProducer
parameter_list|()
throws|throws
name|Exception
block|{
return|return
operator|new
name|FacebookProducer
argument_list|(
name|this
argument_list|)
return|;
block|}
DECL|method|createConsumer (Processor processor)
specifier|public
name|Consumer
name|createConsumer
parameter_list|(
name|Processor
name|processor
parameter_list|)
throws|throws
name|Exception
block|{
comment|// make sure inBody is not set for consumers
if|if
condition|(
name|inBody
operator|!=
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Option inBody is not supported for consumer endpoint"
argument_list|)
throw|;
block|}
specifier|final
name|FacebookConsumer
name|consumer
init|=
operator|new
name|FacebookConsumer
argument_list|(
name|this
argument_list|,
name|processor
argument_list|)
decl_stmt|;
comment|// also set consumer.* properties
name|configureConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
return|return
name|consumer
return|;
block|}
DECL|method|isSingleton ()
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
DECL|method|configureProperties (Map<String, Object> options)
specifier|public
name|void
name|configureProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
parameter_list|)
block|{
name|super
operator|.
name|configureProperties
argument_list|(
name|options
argument_list|)
expr_stmt|;
comment|// set configuration properties first
try|try
block|{
if|if
condition|(
name|configuration
operator|==
literal|null
condition|)
block|{
name|configuration
operator|=
operator|new
name|FacebookEndpointConfiguration
argument_list|()
expr_stmt|;
block|}
name|EndpointHelper
operator|.
name|setReferenceProperties
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|configuration
argument_list|,
name|options
argument_list|)
expr_stmt|;
name|EndpointHelper
operator|.
name|setProperties
argument_list|(
name|getCamelContext
argument_list|()
argument_list|,
name|configuration
argument_list|,
name|options
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// extract reading properties
name|FacebookPropertiesHelper
operator|.
name|configureReadingProperties
argument_list|(
name|configuration
argument_list|,
name|options
argument_list|)
expr_stmt|;
comment|// validate configuration
name|configuration
operator|.
name|validate
argument_list|()
expr_stmt|;
comment|// validate and initialize state
name|initState
argument_list|()
expr_stmt|;
block|}
DECL|method|initState ()
specifier|private
name|void
name|initState
parameter_list|()
block|{
comment|// get endpoint property names
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|arguments
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|arguments
operator|.
name|addAll
argument_list|(
name|getEndpointPropertyNames
argument_list|(
name|configuration
argument_list|)
argument_list|)
expr_stmt|;
comment|// add inBody argument for producers
if|if
condition|(
name|inBody
operator|!=
literal|null
condition|)
block|{
name|arguments
operator|.
name|add
argument_list|(
name|inBody
argument_list|)
expr_stmt|;
block|}
specifier|final
name|String
index|[]
name|argNames
init|=
name|arguments
operator|.
name|toArray
argument_list|(
operator|new
name|String
index|[
name|arguments
operator|.
name|size
argument_list|()
index|]
argument_list|)
decl_stmt|;
name|candidates
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|candidates
operator|.
name|addAll
argument_list|(
name|getCandidateMethods
argument_list|(
name|method
argument_list|,
name|argNames
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|candidates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// found an exact name match, allows disambiguation if needed
name|this
operator|.
name|nameStyle
operator|=
name|FacebookNameStyle
operator|.
name|EXACT
expr_stmt|;
block|}
else|else
block|{
comment|// also search for long forms of method name, both get* and search*
comment|// Note that this set will be further sorted by Producers and Consumers
comment|// producers will prefer get* forms, and consumers should prefer search* forms
name|candidates
operator|.
name|addAll
argument_list|(
name|getCandidateMethods
argument_list|(
name|convertToGetMethod
argument_list|(
name|method
argument_list|)
argument_list|,
name|argNames
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|candidates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|this
operator|.
name|nameStyle
operator|=
name|FacebookNameStyle
operator|.
name|GET
expr_stmt|;
block|}
name|int
name|nGetMethods
init|=
name|candidates
operator|.
name|size
argument_list|()
decl_stmt|;
name|candidates
operator|.
name|addAll
argument_list|(
name|getCandidateMethods
argument_list|(
name|convertToSearchMethod
argument_list|(
name|method
argument_list|)
argument_list|,
name|argNames
argument_list|)
argument_list|)
expr_stmt|;
comment|// error if there are no candidates
if|if
condition|(
name|candidates
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"No matching operation for %s, with arguments %s"
argument_list|,
name|method
argument_list|,
name|arguments
argument_list|)
argument_list|)
throw|;
block|}
if|if
condition|(
name|nameStyle
operator|==
literal|null
condition|)
block|{
comment|// no get* methods found
name|nameStyle
operator|=
name|FacebookNameStyle
operator|.
name|SEARCH
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|candidates
operator|.
name|size
argument_list|()
operator|>
name|nGetMethods
condition|)
block|{
comment|// get* and search* methods found
name|nameStyle
operator|=
name|FacebookNameStyle
operator|.
name|GET_AND_SEARCH
expr_stmt|;
block|}
block|}
comment|// log missing/extra properties for debugging
if|if
condition|(
name|log
operator|.
name|isDebugEnabled
argument_list|()
condition|)
block|{
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|missing
init|=
name|getMissingProperties
argument_list|(
name|method
argument_list|,
name|nameStyle
argument_list|,
name|arguments
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|missing
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Method {} could use one or more properties from {}"
argument_list|,
name|method
argument_list|,
name|missing
argument_list|)
expr_stmt|;
block|}
block|}
block|}
DECL|method|getConfiguration ()
specifier|public
name|FacebookEndpointConfiguration
name|getConfiguration
parameter_list|()
block|{
return|return
name|configuration
return|;
block|}
DECL|method|getCandidates ()
specifier|public
name|List
argument_list|<
name|FacebookMethodsType
argument_list|>
name|getCandidates
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|unmodifiableList
argument_list|(
name|candidates
argument_list|)
return|;
block|}
DECL|method|getInBody ()
specifier|public
name|String
name|getInBody
parameter_list|()
block|{
return|return
name|inBody
return|;
block|}
DECL|method|getMethod ()
specifier|public
name|String
name|getMethod
parameter_list|()
block|{
return|return
name|method
return|;
block|}
DECL|method|getNameStyle ()
specifier|public
name|FacebookNameStyle
name|getNameStyle
parameter_list|()
block|{
return|return
name|nameStyle
return|;
block|}
comment|/**      * Sets the name of a parameter to be passed in the exchange In Body      */
DECL|method|setInBody (String inBody)
specifier|public
name|void
name|setInBody
parameter_list|(
name|String
name|inBody
parameter_list|)
block|{
comment|// validate property name
name|ObjectHelper
operator|.
name|notNull
argument_list|(
name|inBody
argument_list|,
literal|"inBody"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|FacebookPropertiesHelper
operator|.
name|getValidEndpointProperties
argument_list|()
operator|.
name|contains
argument_list|(
name|inBody
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unknown property "
operator|+
name|inBody
argument_list|)
throw|;
block|}
name|this
operator|.
name|inBody
operator|=
name|inBody
expr_stmt|;
block|}
comment|/**      * Sets the {@link FacebookEndpointConfiguration} to use      *       * @param configuration the {@link FacebookEndpointConfiguration} to use      */
DECL|method|setConfiguration (FacebookEndpointConfiguration configuration)
specifier|public
name|void
name|setConfiguration
parameter_list|(
name|FacebookEndpointConfiguration
name|configuration
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
block|}
block|}
end_class

end_unit

